package com.zk.jdbc.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.jdbc.Jdbc2Template;
import com.zk.jdbc.query.WhereBuild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class Update extends AbstractOperation {
    public static final String UPDATE = "update ";
    public static final String SET = " set ";
    private static Logger log = LoggerFactory.getLogger("Update");
    private WhereBuild whereBuild;
    private Map<String, Object> incMap;

    public Update where(String expression, Object... values) {
        this.whereBuild.where(expression, values);
        return this;
    }

    public Update(Class clazz, String tableName, DruidDataSource dataSource) {
        this.clazz = clazz;
        this.jdbc2Template = new Jdbc2Template(dataSource);
        this.tableName = tableName;
        this.incMap = new HashMap();
        this.whereBuild = new WhereBuild();
    }

    public static Update update(Class clazz, String tableName, DruidDataSource dataSource) {
        return new Update(clazz, tableName, dataSource);
    }

    public Update inc(String column, Object value) {
        this.incMap.put(column, value);
        return this;
    }

    public int set() {
        return this.incMap.size() == 0?0:this.jdbc2Template.update(this.sqlPrefix() + this.incSql().toString() + this.whereBuild.buildSuffix(), this.whereBuild.whereValues.toArray());
    }

    public int set(String column, Object value) {
        this.whereBuild.whereValues.addFirst(value);
        StringBuilder updateColumn = new StringBuilder();
        updateColumn.append(column).append(" = ").append("?");
        if(this.incSql().length() > 0) {
            updateColumn.append(",").append(this.incSql());
        }

        return this.jdbc2Template.update(this.sqlPrefix() + updateColumn.toString() + this.whereBuild.buildSuffix(), this.whereBuild.whereValues.toArray());
    }

    public int set(Map<String, Object> map) {
        LinkedList<Object> setValues = new LinkedList();
        StringJoiner stringJoiner = new StringJoiner(" = ?, ", "", " = ?");
        map.forEach((key, value) -> {
            stringJoiner.add(key);
            setValues.addLast(value);
        });
        StringBuilder column = (new StringBuilder()).append(stringJoiner);
        if(this.incSql().length() > 0) {
            column.append(",").append(this.incSql());
        }

        this.whereBuild.whereValues.forEach((v) -> {
            setValues.addLast(v);
        });
        return this.jdbc2Template.update(this.sqlPrefix().append(column).append(this.whereBuild.buildSuffix()).toString(), setValues.toArray());
    }

    protected StringBuilder sqlPrefix() {
        StringBuilder stringBuilder = new StringBuilder("update ");
        stringBuilder.append(this.tableName());
        stringBuilder.append(" set ");
        return stringBuilder;
    }

    private StringBuilder incSql() {
        StringBuilder updateColumn = new StringBuilder();
        if(this.incMap.size() > 0) {
            this.incMap.forEach((k, v) -> {
                updateColumn.append(k + " = ").append(k);
                updateColumn.append(" + ").append(v).append(" ");
                updateColumn.append(",");
            });
            int len = updateColumn.length();
            updateColumn.delete(len - 1, len);
        }

        return updateColumn;
    }
}
