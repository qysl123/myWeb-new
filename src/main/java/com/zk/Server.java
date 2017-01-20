package com.zk;


import com.zk.redis.ArticleVote;
import com.zk.redis.SellGoods;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collections;

public class Server {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ArticleVote articleVote = (ArticleVote) applicationContext.getBean("articleVote");
//        articleVote.articlesVote("article:124", "userVote");
//        articleVote.postArticle("user3", "title tt 1", "link ll 1");
//        articleVote.addRemoveGroups(124, Collections.singletonList("programing"), new ArrayList<String>());
//        articleVote.getArticles(1, "score:");
//        articleVote.getGroupArticles("programing", 1);
        SellGoods sellGoods = (SellGoods) applicationContext.getBean("sellGoods");
        sellGoods.listItem();
    }
}
