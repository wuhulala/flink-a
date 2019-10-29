package com.wuhulala.mall.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuhulala.mall.entity.UserInfo;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 用户生成器
 *
 * @author wuhulala<br>
 * @date 2019/10/23<br>
 * @since v1.0<br>
 */
public class UserGeneratorUtils {

    private static String[] FIRST_NAME_ARR = {"张", "朱", "谢", "杨", "周",
            "孙", "许", "李", "王", "陈",
            "何", "孙", "马", "刘", "杜",
            "师", "石", "高", "张", "诸葛",
            "薛", "黄", "宋", "夏", "胡", "夏侯", "曹", "龙"};
    private static String[] SECOND_NAME_ARR = {"园", "样", "综", "辉", "叶", "儿", "子", "金", "茹",
            "成", "枭", "笑", "彤", "林", "龙", "名", "胜", "钰",
            "和", "明", "霓", "杰", "云", "支", "茶", "燕", "阳",
            "过", "修", "禾", "朗", "聪", "格", "新", "霞", "屋",
            "元", "树", "曼", "丁", "哈", "丽", "一", "占", "春",
            "水", "流", "文", "年", "桑", "晁", "则", "昊", "瑞",
            "天", "若", "飞", "函", "轩", "兆", "琼", "莫", "让",
            "熊", "军", "浅", "杰", "项", "蓉", "静", "康", "建",
            "笨", "博", "坡", "坚", "艺", "迪"};

    public static UserInfo createUser() {

        UserInfo userInfo = new UserInfo();
        userInfo.setName(getName());
        userInfo.setSex(Math.random() > 0.5 ? "1" : "2");
        userInfo.setBirth(getBirth());
        userInfo.setEmail("");
        userInfo.setAddress(getAddress());
        return userInfo;
    }

    private static Integer getBirth() {
        double tmp = Math.random();

        if (tmp >= 0 && tmp < 0.05) { // 60 +
            return (1959 - random.nextInt(40)) * 10000 + (random.nextInt(12) + 1) * 100 + random.nextInt(29) + 1;

        } else if (tmp >= 0.05 && tmp < 0.3) { // 40 -60
            return (1979 - random.nextInt(20)) * 10000 + (random.nextInt(12) + 1) * 100 + random.nextInt(29 ) + 1;

        } else if (tmp >= 0.3 && tmp < 0.7) { // 21 -40 岁
            return (1999 - random.nextInt(20)) * 10000 + (random.nextInt(12) + 1) * 100 + random.nextInt(29) + 1;
        } else if (tmp >= 0.7 && tmp <= 1.0) { // 20 岁左右的
            return (2019 - random.nextInt(20)) * 10000 + (random.nextInt(12) + 1) * 100 + random.nextInt(29) + 1;
        }
        return 0;
    }

    public static String getAddress() {
        JSONObject tmp = (JSONObject) getRandomByList(addressObject);
        if (tmp == null) {
            return "";
        }
        StringBuilder address = new StringBuilder(tmp.getString("name"));
        JSONArray tmpSub = tmp.getJSONArray("sub");

        while (tmpSub != null) {
            tmp = (JSONObject) getRandomByList(tmpSub);
            if (tmp == null) {
                break;
            }
            address.append("-").append(tmp.getString("name"));
            tmpSub = tmp.getJSONArray("sub");
        }
        return address.toString();
    }


    public static JSONArray addressObject;

    static {
        try {
            addressObject = JSON.parseArray(IOUtils.readLines(Thread.currentThread().getContextClassLoader().getResourceAsStream("address.json"), "UTF-8").get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Random random = new Random();

    public static <T> T getRandomByArr(T[] arr) {
        int length = arr.length;
        return arr[random.nextInt(length)];
    }

    public static <T> T getRandomByList(List<T> list) {
        int length = list.size();
        if (length == 0) {
            return null;
        }
        return list.get(random.nextInt(length));
    }


    public static String getName() {
        String first = getRandomByArr(FIRST_NAME_ARR), second;

        if (Math.random() > 0.3) {
            second = getRandomByArr(SECOND_NAME_ARR) + getRandomByArr(SECOND_NAME_ARR);
        } else {
            second = getRandomByArr(SECOND_NAME_ARR);
        }
        return first + second;
    }

}
