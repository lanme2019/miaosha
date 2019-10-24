package com.lanme.miaosha.controller;

import com.alibaba.fastjson.JSON;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.service.GoodService;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/to_list")
    public String toGoodlist(Model model){
        List<GoodsVo> goodsVo = goodService.listGoodsVo();
        model.addAttribute("goodsList",JSON.toJSON(goodsVo));
        return "goods_list";
    }

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
