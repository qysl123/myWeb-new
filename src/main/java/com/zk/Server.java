package com.zk;


import com.zk.redis.ArticleVote;
import com.zk.redis.SellGoods;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class Server {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ArticleVote articleVote = (ArticleVote) applicationContext.getBean("articleVote");
//        articleVote.articlesVote("article:124", "userVote");
//        articleVote.postArticle("user3", "title tt 1", "link ll 1");
//        articleVote.addRemoveGroups(124, Collections.singletonList("programing"), new ArrayList<String>());
//        articleVote.getArticles(1, "score:");
//        articleVote.getGroupArticles("programing", 1);
//        SellGoods sellGoods = (SellGoods) applicationContext.getBean("sellGoods");
//        sellGoods.listItem();
        System.out.println(URLEncoder.encode("{\"userId\":\"16874943\",\"phone\":\"\",\"email\":\"a@a.com\",\"allonym\":\"xx\",\"realName\":\"\",\"nickname\":\"xx\",\"idnumber\":\"\",\"sex\":\"FEMALE\",\"status\":\"ACTIVE\",\"qq\":\"12345\",\"avatar\":\"http://tb.himg.baidu.com/sys/portrait/item/d765697435323679613cb0\"}"));
    }
}
