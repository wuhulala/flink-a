package com.wuhulala.mall.dao;

import com.wuhulala.mall.util.GoodsGeneratorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wuhulala<br>
 * @date 2019/10/29<br>
 * @since v1.0<br>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsDaoTest {

    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void initGoods(){
        goodsDao.saveAll(GoodsGeneratorUtils.readComputerGoods());
    }

}