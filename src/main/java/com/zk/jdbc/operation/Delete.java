package com.zk.jdbc.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.jdbc.Jdbc2Template;
import com.zk.jdbc.query.WhereBuild;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class Delete extends AbstractOperation {
    public static final String DELETE = "delete ";
    public static final String FROM = " from ";
    private WhereBuild whereBuild;

    public Delete where(String expression, Object... values) {
        this.whereBuild.where(expression, values);
        return this;
    }

    public Delete(Class clazz, String tableName, DruidDataSource dataSource) {
        this.clazz = clazz;
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.tableName = tableName;
        this.whereBuild = new WhereBuild();
    }

    public static Delete delete(Class clazz, String tableName, DruidDataSource dataSource) {
        return new Delete(clazz, tableName, dataSource);
    }

    public int delete() {
        return this.whereBuild.whereValues != null && this.whereBuild.whereValues.size() != 0?this.jdbc2Template.delete(this.sqlPrefix().append(this.whereBuild.buildSuffix()).toString(), this.whereBuild.whereValues.toArray()):0;
    }

    public int delete(Object id) {
        this.whereBuild.where("id = ? ", new Object[]{id});
        return this.delete();
    }

    protected StringBuilder sqlPrefix() {
        StringBuilder stringBuilder = new StringBuilder("delete ");
        stringBuilder.append(" from ");
        stringBuilder.append(this.tableName());
        return stringBuilder;
    }
}
