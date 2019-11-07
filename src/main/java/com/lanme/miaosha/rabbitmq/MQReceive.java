package com.lanme.miaosha.rabbitmq;

import com.lanme.miaosha.common.CodeMsg;
import com.lanme.miaosha.model.MiaoshaMessage;
import com.lanme.miaosha.model.MiaoshaOrder;
import com.lanme.miaosha.model.MiaoshaUser;
import com.lanme.miaosha.model.OrderInfo;
import com.lanme.miaosha.redis.RedisService;
import com.lanme.miaosha.service.GoodService;
import com.lanme.miaosha.service.MiaoshaService;
import com.lanme.miaosha.service.OrderService;
import com.lanme.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/11/5 16:50
 */
@Service
public class MQReceive {

    @Autowired
    GoodService goodService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    private static Logger logger = LoggerFactory.getLogger(MQReceive.class);

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        logger.info("receive message"+message);
    }

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void miaoshaReceive(String message) {
        MiaoshaMessage msg = redisService.stringToBean(message,MiaoshaMessage.class);
        logger.info("receive message"+message);
        long goodsId = msg.getGoodsId();
        MiaoshaUser user = msg.getUser();

        //判断是否有库存
        GoodsVo goods = goodService.getGoodVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }

        //判断是否已经秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return ;
        }
        //开始秒杀
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);

    }

}
