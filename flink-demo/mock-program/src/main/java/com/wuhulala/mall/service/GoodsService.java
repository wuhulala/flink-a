package com.wuhulala.mall.service;

import com.wuhulala.mall.dao.GoodsDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuhulala<br>
 * @date 2019/10/23<br>
 * @since v1.0<br>
 */
@Slf4j
@Service
public class GoodsService {

    private final GoodsDao goodsDao;

    @Autowired
    public GoodsService(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }



}
