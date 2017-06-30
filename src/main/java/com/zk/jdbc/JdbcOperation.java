package com.zk.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.jdbc.operation.Delete;
import com.zk.jdbc.operation.Insert;
import com.zk.jdbc.operation.Query;
import com.zk.jdbc.operation.Update;

import java.util.List;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class JdbcOperation {
    private DruidDataSource defaultDataSource;
    private String tableName;

    public JdbcOperation(DruidDataSource dataSource) {
        this.defaultDataSource = dataSource;
    }

    public JdbcOperation(DruidDataSource dataSource, String tableName) {
        this.defaultDataSource = dataSource;
        this.tableName = tableName;
    }

    public Jdbc2Template SQL() {
        return new Jdbc2Template(this.defaultDataSource);
    }

    public Long insert(Object object) {
        return Insert.insert(this.defaultDataSource, object, this.tableName);
    }

    public int[] insertBatch(List list) {
        return Insert.insertBatch(this.defaultDataSource, list, this.tableName);
    }

    public Query query(Class clazz, String... columns) {
        return Query.query(clazz, this.tableName, this.defaultDataSource, columns);
    }

    public Delete delete(Class clazz) {
        return Delete.delete(clazz, this.tableName, this.defaultDataSource);
    }

    public Update update(Class clazz) {
        return Update.update(clazz, this.tableName, this.defaultDataSource);
    }
}
