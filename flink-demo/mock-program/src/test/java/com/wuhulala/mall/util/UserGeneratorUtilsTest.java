package com.wuhulala.mall.util;

import com.alibaba.fastjson.JSON;
import com.wuhulala.mall.entity.UserInfo;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * @author wuhulala<br>
 * @date 2019/10/23<br>
 * @since v1.0<br>
 */
public class UserGeneratorUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(UserGeneratorUtilsTest.class);

    @org.junit.Test
    public void createUser() {
        for (int i = 0; i < 10000; i++) {
            UserInfo userInfo = UserGeneratorUtils.createUser();
            logger.debug("batch#{} created user is {}!", i, JSON.toJSONString(userInfo));
            Assert.assertNotNull(userInfo);
        }

    }
}