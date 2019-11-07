package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.GoodsDao;
import com.lanme.miaosha.model.MiaoshaOrder;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.model.OrderInfo;
import com.lanme.miaosha.prefix.MiaoshaKey;
import com.lanme.miaosha.redis.RedisService;
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

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存、下订单、写入秒杀订单
        boolean success =goodService.reduceStock(goods);
        if(success){
            return orderService.createOrder(user,goods);
        }else{
               setGoodsOver(goods.getId());
            return null;
        }

    }


    public long getMiaoshaResult(Long id, long goodsId) {
        MiaoshaOrder orderInfo = orderService.getMiaoshaOrderByUserIdGoodsId(id,goodsId);
        if(orderInfo != null){
            return orderInfo.getOrderId();
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                //库存已经没了
                return -1;
            }else{
                //表示还没入库，继续等待结果
                return 0;
            }
        }
    }


    private void setGoodsOver(long goodId){
        redisService.set(MiaoshaKey.isGoodsOver,""+goodId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }
}
