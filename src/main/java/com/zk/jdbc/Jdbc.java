package com.zk.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.api.spring.SpringApplicationContext;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class Jdbc {
    public Jdbc() {
    }

    public static JdbcOperation build() {
        return new JdbcOperation((DruidDataSource) SpringApplicationContext.getBean("dataSource"));
    }

    public static JdbcOperation build(DruidDataSource dataSource) {
        return new JdbcOperation(dataSource);
    }

    public static JdbcOperation build(String tableName) {
        return new JdbcOperation((DruidDataSource)SpringApplicationContext.getBean("dataSource"), tableName);
    }

    public static JdbcOperation build(DruidDataSource dataSource, String tableName) {
        return new JdbcOperation(dataSource, tableName);
    }
}
