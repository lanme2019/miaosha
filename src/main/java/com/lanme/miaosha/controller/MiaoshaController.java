package com.lanme.miaosha.controller;

import com.alibaba.fastjson.JSON;
import com.lanme.miaosha.common.CodeMsg;
import com.lanme.miaosha.common.Result;
import com.lanme.miaosha.model.MiaoshaOrder;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.model.OrderInfo;
import com.lanme.miaosha.service.GoodService;
import com.lanme.miaosha.service.MiaoshaService;
import com.lanme.miaosha.service.OrderService;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/24 16:54
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodService goodService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;



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
    public Result<OrderInfo> miaosha(Model model,MiaoshaUser user,
                                     @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
            GoodsVo goods = goodService.getGoodVoByGoodsId(goodsId);//10个商品，req1 req2
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);
    }
}



