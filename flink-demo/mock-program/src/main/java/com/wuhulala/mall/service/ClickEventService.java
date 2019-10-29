package com.wuhulala.mall.service;

import com.wuhulala.mall.dao.GoodsDao;
import com.wuhulala.mall.dao.UserInfoDao;
import com.wuhulala.mall.dto.Event;
import com.wuhulala.mall.entity.Goods;
import com.wuhulala.mall.entity.UserInfo;
import com.wuhulala.mall.support.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author wuhulala<br>
 * @date 2019/10/29<br>
 * @since v1.0<br>
 */
@Slf4j
@Service
public class ClickEventService implements InitializingBean {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private KafkaSender kafkaSender;


    private Random goodsRandom = new Random();
    private Random userRandom = new Random();
    private Random eventRandom = new Random();

    @Value("goods.click.event.topic:goods_click_event")
    private String topic;

    private List<Goods> goods;

    private List<UserInfo> userInfoList;

    @Override
    public void afterPropertiesSet() throws Exception {
        goods = goodsDao.findAll();
        userInfoList = userInfoDao.findAll();
    }

    @Scheduled(cron = "* 0/5 * * * *")
    public void initEvent() {

        log.info("init click event Start ");
        int number = eventRandom.nextInt(1000);
        for (int i = 0; i < number; i++) {
            Event event = generateEvent();
            kafkaSender.sendEvent(event, topic);
        }
        log.info("generate {} event Ended!", number);
    }


    private Event generateEvent() {
        Event event = new Event();
        event.setDate(new Date());
        int goodsLength = goods.size();
        int userListLength = userInfoList.size();
        Goods good = goods.get(goodsRandom.nextInt(goodsLength));
        UserInfo userInfo = userInfoList.get(userRandom.nextInt(userListLength));
        event.setGoodsId(good.getId());
        event.setGoodsName(good.getName());
        event.setUserId(userInfo.getId());
        event.setUserName(userInfo.getName());
        return event;
    }
}
