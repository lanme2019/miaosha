package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.GoodsDao;
import com.lanme.miaosha.model.Goods;
import com.lanme.miaosha.model.MiaoshaGoods;
import com.lanme.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/23 18:11
 */

@Service
public class GoodService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int i = goodsDao.reduceStock(g);
        return i>0;
    }
}
