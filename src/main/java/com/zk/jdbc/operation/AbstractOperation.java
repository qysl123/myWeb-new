package com.zk.jdbc.operation;

import com.zk.jdbc.Jdbc2Template;

import javax.persistence.Table;
import java.util.StringJoiner;

/**
 * Created by zhongkun on 2017/6/30.
 */
public abstract class AbstractOperation {
    public static final String space = " ";
    protected Jdbc2Template jdbc2Template;
    protected Class clazz;
    protected String tableName;

    public AbstractOperation() {
    }

    public String tableName() {
        if(this.tableName != null) {
            return this.tableName;
        } else if(this.clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table)this.clazz.getAnnotation(Table.class);
            return table.name();
        } else {
            return this.clazz.getSimpleName();
        }
    }

    protected abstract StringBuilder sqlPrefix();

    protected static String arrayToString(String[] a) {
        if(a != null && a.length != 0) {
            StringJoiner stringJoiner = new StringJoiner(",");
            String[] var2 = a;
            int var3 = a.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String str = var2[var4];
                stringJoiner.add(str);
            }

            return stringJoiner.toString();
        } else {
            return " * ";
        }
    }
}
