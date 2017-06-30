package com.zk.jdbc.query;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongkun on 2017/6/30.
 */
public interface QuerySQL {
    <T> List<T> page(int var1, int var2);

    <T> List<T> all();

    <T> List<T> limit(int var1);

    <T> T first();

    <T> T last();

    <T> T find(Object var1);

    Long count();

    Map findOfMap();

    List<Map<String, Object>> pageOfMap(int var1, int var2);
}
