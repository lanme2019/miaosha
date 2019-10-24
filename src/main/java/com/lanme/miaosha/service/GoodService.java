package com.lanme.miaosha.service;

import com.lanme.miaosha.dao.GoodsDao;
import com.lanme.miaosha.model.Goods;
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
}
