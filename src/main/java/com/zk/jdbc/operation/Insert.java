package com.zk.jdbc.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.jdbc.Jdbc2Template;

import java.util.List;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class Insert extends AbstractOperation {
    public Insert(DruidDataSource dataSource, Object obj, String tableName) {
        this.clazz = obj.getClass();
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.tableName = tableName;
    }

    public static Long insert(DruidDataSource dataSource, Object obj, String tableName) {
        return (new Insert(dataSource, obj, tableName))._insert(obj);
    }

    public static int[] insertBatch(DruidDataSource dataSource, List list, String tableName) {
        return list != null && list.size() != 0?(new Insert(dataSource, list.get(0), tableName))._insertBatch(list):null;
    }

    public Long _insert(Object obj) {
        return this.jdbc2Template.insert(obj, this.tableName());
    }

    public int[] _insertBatch(List list) {
        return this.jdbc2Template.insertBatch(list, this.tableName());
    }

    protected StringBuilder sqlPrefix() {
        return null;
    }
}
