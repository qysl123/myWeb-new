package com.zk.jdbc.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.jdbc.Jdbc2Template;
import com.zk.jdbc.query.QuerySQL;
import com.zk.jdbc.query.WhereBuild;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class Query extends AbstractOperation implements QuerySQL {
    public static final String SELECT = "select ";
    public static final String FROM = " from ";
    private String[] columns;
    private WhereBuild whereBuild;

    public Query(DruidDataSource dataSource, String tableName, Class clazz, String... columns) {
        this.columns = columns;
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.clazz = clazz;
        this.tableName = tableName;
        this.whereBuild = new WhereBuild();
    }

    public static Query query(String tableName, DruidDataSource dataSource, Class clazz) {
        return new Query(dataSource, tableName, clazz, new String[0]);
    }

    public static Query query(Class clazz, String tableName, DruidDataSource dataSource, String... columns) {
        return new Query(dataSource, tableName, clazz, columns);
    }

    public Query where(String expression, Object... values) {
        this.whereBuild.where(expression, values);
        return this;
    }

    public Query order(String expression) {
        this.whereBuild.order(expression);
        return this;
    }

    public <T> List<T> page(int limit, int page) {
        return this.jdbc2Template.findSQL(this.sqlPrefix().append(this.whereBuild.page(limit, page).buildSuffix()).toString(), this.clazz, this.whereBuild.whereValues.toArray());
    }

    public <T> List<T> all() {
        return this.jdbc2Template.findSQL(this.sqlPrefix().append(this.whereBuild.buildSuffix()).toString(), this.clazz, this.whereBuild.whereValues.toArray());
    }

    public <T> List<T> limit(int size) {
        return this.jdbc2Template.findSQL(this.sqlPrefix().append(this.whereBuild.limit(size).buildSuffix()).toString(), this.clazz, this.whereBuild.whereValues.toArray());
    }

    public <T> T first() {
        return this.jdbc2Template.findOne(this.sqlPrefix().append(this.whereBuild.limit(1).buildSuffix()).toString(), this.clazz, this.whereBuild.whereValues.toArray());
    }

    public <T> T last() {
        return this.jdbc2Template.findOne(this.sqlPrefix().append(this.whereBuild.order("id desc").limit(1).buildSuffix()).toString(), this.clazz, this.whereBuild.whereValues.toArray());
    }

    public <T> T find(Object id) {
        return this.jdbc2Template.findOne(this.sqlPrefix().append(this.whereBuild.where("id=?", new Object[]{id}).buildSuffix()).toString(), this.clazz, this.whereBuild.whereValues.toArray());
    }

    public Long count() {
        return (Long)this.jdbc2Template.jdbcTemplate().queryForObject(this.sqlPrefixCount().append(this.whereBuild.buildSuffix()).toString(), Long.class, this.whereBuild.whereValues.toArray());
    }

    public Map findOfMap() {
        return this.jdbc2Template.jdbcTemplate().queryForMap(this.sqlPrefix().append(this.whereBuild.buildSuffix()).toString(), this.whereBuild.whereValues.toArray());
    }

    public List<Map<String, Object>> pageOfMap(int limit, int page) {
        return this.jdbc2Template.jdbcTemplate().queryForList(this.sqlPrefix().append(this.whereBuild.page(limit, page).buildSuffix()).toString(), this.whereBuild.whereValues.toArray());
    }

    private StringBuilder sqlPrefixCount() {
        StringBuilder count = new StringBuilder("select ");
        count.append(" count(1) ");
        count.append(" from ");
        count.append(this.tableName());
        return count;
    }

    protected StringBuilder sqlPrefix() {
        StringBuilder prefix = new StringBuilder("select ");
        prefix.append(arrayToString(this.columns));
        prefix.append(" from ");
        prefix.append(this.tableName());
        return prefix;
    }
}
