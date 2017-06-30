package com.zk.jdbc.query;

import java.util.LinkedList;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class WhereBuild {
    public static final String space = " ";
    private LinkedList<String> whereExpressions = new LinkedList();
    public LinkedList<Object> whereValues = new LinkedList();
    private StringBuilder orderExpressions = null;
    private StringBuilder limitExpressions = null;
    private StringBuilder pageExpressions = null;

    public WhereBuild() {
    }

    public WhereBuild where(String expression, Object... value) {
        this.whereExpressions.add(expression);
        Object[] var3 = value;
        int var4 = value.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Object v = var3[var5];
            this.whereValues.addLast(v);
        }

        return this;
    }

    public WhereBuild order(String expression) {
        this.orderExpressions = (new StringBuilder("order by")).append(" ").append(expression);
        return this;
    }

    public WhereBuild page(int limit, int page) {
        this.pageExpressions = (new StringBuilder("limit")).append(" ").append((page - 1) * limit).append(",").append(limit);
        return this;
    }

    public WhereBuild limit(int size) {
        this.limitExpressions = (new StringBuilder("limit")).append(" ").append(size);
        return this;
    }

    public StringBuilder buildSuffix() {
        StringBuilder sql = new StringBuilder();
        if(this.whereExpressions != null && this.whereExpressions.size() > 0) {
            sql.append(" ").append("where").append(" ");
            this.whereExpressions.forEach((v) -> {
                sql.append(v).append(" ").append("and").append(" ");
            });
            sql.delete(sql.length() - 4, sql.length());
        }

        if(this.orderExpressions != null && this.orderExpressions.length() > 0) {
            sql.append(" ").append(this.orderExpressions);
        }

        if(this.limitExpressions != null && this.limitExpressions.length() > 0) {
            sql.append(" ").append(this.limitExpressions);
        }

        if(this.pageExpressions != null && this.pageExpressions.length() > 0) {
            sql.append(" ").append(this.pageExpressions);
        }

        return sql;
    }

    public static enum Up {
        OR(" or "),
        AND(" and "),
        EMPTY(" ");

        private String value;

        private Up(String value) {
            this.value = value;
        }
    }
}
