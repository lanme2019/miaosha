package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.OrderDao;
import com.lanme.miaosha.model.MiaoshaOrder;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.model.OrderInfo;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/24 17:40
 */

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;


    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, long goodsId) {
        return orderDao.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
    }

    @Transactional
    public OrderInfo creatStock(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setUserId(user.getId());
        orderInfo.setStatus(1);
        //先建立普通订单
        long orderId = orderDao.insert(orderInfo);
        //在建立秒杀订单
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setGoodsId(goods.getId());

        orderDao.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;

    }
}
