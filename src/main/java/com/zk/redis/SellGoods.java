package com.zk.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 出售商品
 * Created by Ken on 2016/11/30.
 */
@Service("sellGoods")
public class SellGoods extends RedisSupport {

    public void listItem(final int itemId, int sellerId, final int price) {
        final String inventory = "inventory:" + sellerId;
        final String item = itemId + "." + sellerId;
        final Long end = System.currentTimeMillis() + 5000;
        getRedisTemplate2().executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                while (System.currentTimeMillis() < end) {
                    try {
                        operations.watch(inventory);
                        if (operations.boundSetOps(inventory).isMember(itemId)) {
                            operations.unwatch();
                            return null;
                        }

                        operations.multi();
                        operations.boundZSetOps("market:").add(item, price);
                        operations.boundSetOps(inventory).remove(itemId);
                        operations.exec();
                    } catch (Exception e) {
                        continue;
                    }
                    operations.unwatch();
                    return null;
                }
                return null;
            }
        });

    }
}
