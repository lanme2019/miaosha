package com.lanme.miaosha.controller;

import com.alibaba.fastjson.JSON;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.prefix.GoodsKey;
import com.lanme.miaosha.redis.RedisService;
import com.lanme.miaosha.service.GoodService;
import com.lanme.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/23 16:17
 */

@Controller
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    GoodService goodService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;



    // qps:88/s  500*5
    @RequestMapping("/to_list")
    public String toGoodlist(Model model){
        List<GoodsVo> goodsVo = goodService.listGoodsVo();
        model.addAttribute("goodsList",JSON.toJSON(goodsVo));
        System.out.println("you");
        return "goods_list";
    }

//    // qps:725/s  500*5
//    @RequestMapping(value = "to_list",produces = "text/html")
//    @ResponseBody
//    public String toGoodlist(Model model,  HttpServletRequest request, HttpServletResponse response) throws IOException {
////        if(user == null){
////            response.sendRedirect("/login/to_login");
////            return null;
////        }
//
////        model.addAttribute("user",user);
//        //先尝试从缓存中取
//        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
//        if(!StringUtils.isEmpty(html)){
//            return html;
//        }
//        //取不到，则手动渲染，再保存到redis
//        List<GoodsVo> goodsVoList = goodService.listGoodsVo();
//        model.addAttribute("goodsList",goodsVoList);
//        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
//        if (!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsList, "", html);
//        }
//        return html;
//    }


    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId){

        GoodsVo goods = goodService.getGoodVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt) {
            //秒杀还未开始
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);
        }else if(now < endAt){
            miaoshaStatus = 1;
            remainSeconds = 0;
        }else {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }

        model.addAttribute("user",user);
        model.addAttribute("goods",goods);
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";
    }

}
