package com.wuhulala.mall.util;

import com.wuhulala.mall.entity.Goods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author wuhulala<br>
 * @date 2019/10/29<br>
 * @since v1.0<br>
 */
public class GoodsGeneratorUtils {


    public static void main(String[] args) {
        readGoods();
    }

    public static List<Goods> readGoods() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdcomiphone_page1.html");
        Document document = null;
        try {
            document = Jsoup.parse(is, "UTF-8", "https://search.jd.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = document.body().children().first().children().select(".gl-item");
        ListIterator<Element> listIterator = elements.listIterator();
        List<Goods> goodsList = new ArrayList<>();
        while (listIterator.hasNext()) {
            Goods goods = new Goods();
            Element element = listIterator.next();
//            System.out.println(element);
            String name = element.select(".p-name").select("em").text();
            String price = element.select(".p-price").select("i").text();
            goods.setPrice(Double.valueOf(price));
            goods.setName(name);
            goods.setType("手机");
            goodsList.add(goods);
        }
        return goodsList;
    }

    public static List<Goods> readComputerGoods() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdcomcomputer_page1.html");
        Document document = null;
        try {
            document = Jsoup.parse(is, "UTF-8", "https://search.jd.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = document.body().children().first().children().select(".gl-item");
        ListIterator<Element> listIterator = elements.listIterator();
        List<Goods> goodsList = new ArrayList<>();
        while (listIterator.hasNext()) {
            Goods goods = new Goods();
            Element element = listIterator.next();
//            System.out.println(element);
            String name = element.select(".p-name").select("em").text();
            String price = element.select(".p-price").select("i").text();
            goods.setPrice(Double.valueOf(price));
            goods.setName(name);
            goods.setType("电脑");
            goodsList.add(goods);
        }
        return goodsList;
    }
}
