package com.wuhulala.mall.service;

import com.wuhulala.mall.dao.UserInfoDao;
import com.wuhulala.mall.entity.UserInfo;
import com.wuhulala.mall.util.UserGeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author wuhulala<br>
 * @date 2019/10/23<br>
 * @since v1.0<br>
 */
@Slf4j
@Service
public class UserInfoService {

    private final UserInfoDao userInfoDao;

    @Autowired
    public UserInfoService(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    @Scheduled(cron = "* 0/5 * * * *")
    public void initPeople() {

        log.info("init People Start ");

        Random random = new Random();
        int number = random.nextInt(100);
        List<UserInfo> userInfos = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            UserInfo userInfo = UserGeneratorUtils.createUser();
            userInfos.add(userInfo);
        }

        for (int i = 0; i <= number / 50; i++) {
            userInfoDao.saveAll(userInfos.stream().skip(i * 50).limit(50).collect(Collectors.toList()));
        }

        log.info("init {} People Ended!", number);
    }

}
