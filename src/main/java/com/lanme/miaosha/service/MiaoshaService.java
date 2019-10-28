package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.GoodsDao;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.model.OrderInfo;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/24 17:40
 */

@Service
public class MiaoshaService {

    @Autowired
    GoodService goodService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        goodService.reduceStock(goods);

        return orderService.creatStock(user,goods);

    }
}
