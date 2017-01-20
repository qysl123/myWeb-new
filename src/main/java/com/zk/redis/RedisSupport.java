package com.zk.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 *
 * Created by Ken on 2016/11/29.
 */
public class RedisSupport {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate2;

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, Object> getRedisTemplate2() {
        return redisTemplate2;
    }

    public void setRedisTemplate2(RedisTemplate<String, Object> redisTemplate2) {
        this.redisTemplate2 = redisTemplate2;
    }
}
