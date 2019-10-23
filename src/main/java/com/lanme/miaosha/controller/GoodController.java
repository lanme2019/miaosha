package com.lanme.miaosha.controller;

import com.alibaba.fastjson.JSON;
import com.lanme.miaosha.service.GoodService;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

}
