package com.lanme.miaosha.controller;

import com.lanme.miaosha.common.CodeMsg;
import com.lanme.miaosha.common.Result;
import com.lanme.miaosha.model.MiaoshaMessage;
import com.lanme.miaosha.model.MiaoshaOrder;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.model.OrderInfo;
import com.lanme.miaosha.prefix.GoodsKey;
import com.lanme.miaosha.rabbitmq.MQSender;
import com.lanme.miaosha.redis.RedisService;
import com.lanme.miaosha.service.GoodService;
import com.lanme.miaosha.service.MiaoshaService;
import com.lanme.miaosha.service.OrderService;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/24 16:54
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController  implements InitializingBean {

    @Autowired
    GoodService goodService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    private Map<Long,Boolean> localOverMap = new HashMap<>();


    // qps:64/s
    @RequestMapping("/do_miaosha1")
    public String list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId){

        //判断是否有库存
        GoodsVo goods = goodService.getGoodVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //开始秒杀
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        model.addAttribute("goods", goods);
        model.addAttribute("orderInfo",orderInfo);
        return "order_detail";
    }

    @RequestMapping(value="/do_miaosha", method= RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model,MiaoshaUser user,
                                     @RequestParam("goodsId")long goodsId) {

        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //内存标记，减少不必要的redis的访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //1.预减库存进行优化
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);

        if(stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }


        //2.判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //3.进入消息队列
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(message);
        return Result.success(0);
    }

    @RequestMapping(value="/result", method= RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {

        model.addAttribute("user",user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

       long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);

    }


    // 预加载 库存到redis
    @Override
    public void afterPropertiesSet() throws Exception {

        List<GoodsVo> goodList = goodService.listGoodsVo();
        if(goodList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodList) {
            localOverMap.put(goodsVo.getId(),false);
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
        }
    }
}



