package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.OrderDao;
import com.lanme.miaosha.model.MiaoshaOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
