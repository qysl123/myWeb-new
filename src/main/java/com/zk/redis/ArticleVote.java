package com.zk.redis;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 投票
 * Created by Ken on 2016/11/29.
 */
@Service("articleVote")
public class ArticleVote extends RedisSupport {
    public static Long ONE_WEEK_IN_SECONDS = 7 * 86400L;
    public static int VOTE_SCORE = 432;
    public static int ARTICLES_PER_PAGE = 25;

    public void articlesVote(String articles, String user, String type) {
        String id = articles.split(":")[1];
        if (getRedisTemplate2().opsForSet().add("voted:" + id, user) != 0) {
            getRedisTemplate2().opsForZSet().incrementScore("score:", articles, VOTE_SCORE);
            getRedisTemplate2().opsForHash().increment(articles, "votes", 1);
        }
    }

    public void postArticle(String user, String title, String link) {
        Long id = getRedisTemplate2().opsForValue().increment("article:", 1L);

        String voted = "voted:" + id;
        getRedisTemplate2().opsForSet().add(voted, user);
        getRedisTemplate2().expire(voted, ONE_WEEK_IN_SECONDS, TimeUnit.SECONDS);

        String article = "article:" + id;
        Long now = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("link", link);
        map.put("poster", user);
        map.put("time", now);
        map.put("votes", 1);
        getRedisTemplate2().opsForHash().putAll(article, map);

        getRedisTemplate2().opsForZSet().add("score:", article, now + VOTE_SCORE);
    }

    public void getArticles(int page, String key) {
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = 3;

        Set<Object> set = getRedisTemplate2().opsForZSet().reverseRange(key, start, end);
        for (Object o : set) {
            String articlesId = (String) o;
            Map<Object, Object> map = getRedisTemplate2().opsForHash().entries(articlesId);
            System.out.println(map);
        }
    }

    public void addRemoveGroups(int articleId, List<String> addGroupList, List<String> removeGroupList) {
        String article = "article:" + articleId;
        for (String add : addGroupList)
            getRedisTemplate2().opsForSet().add("group:" + add, article);
        for (String remove : removeGroupList)
            getRedisTemplate2().opsForSet().remove("group:" + remove, article);
    }

    public void getGroupArticles(String group, int page) {
        String key = "score:" + group;
        if (!getRedisTemplate2().hasKey(key)) {
            getRedisTemplate2().opsForZSet().intersectAndStore("score:", "group:" + group, key);
        }

        getArticles(1, key);
    }
}
