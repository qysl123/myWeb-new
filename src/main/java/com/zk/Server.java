package com.zk;


import com.zk.redis.ArticleVote;
import com.zk.redis.SellGoods;
import com.zk.utils.HttpUtil;
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
        System.out.println(URLEncoder.encode("{\"sex\": \"1\"}"));
        System.out.println(URLDecoder.decode("%22channel%22:%22%22,%22clientVersion%22:%222.0.0%22,%22guid%22:%22676398CE264D2D128BD74EE4D0135F9D%22,%22mobileType%22:%22vivo%20X9%22,%22os%22:%22android%22,%22phone%22:%2218101290501%22"));
    }
}
