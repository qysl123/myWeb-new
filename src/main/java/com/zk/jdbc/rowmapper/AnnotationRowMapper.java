package com.zk.jdbc.rowmapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class AnnotationRowMapper<T> implements RowMapper<T> {
    private Class<T> mappedClass;
    private Map<String, Class<?>> mappedColumns;
    private Map<String, String> mappedFields;

    public AnnotationRowMapper(Class<T> mappedClass) {
        this.initialize(mappedClass);
    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap();
        this.mappedColumns = new HashMap();
        List<Field> fields = new ArrayList(Arrays.asList(mappedClass.getDeclaredFields()));
        Class clzzz = mappedClass;

        while(clzzz != null) {
            clzzz = clzzz.getSuperclass();
            if(clzzz.getName().equals("java.lang.Object")) {
                break;
            }

            fields.addAll(Arrays.asList(clzzz.getDeclaredFields()));
        }

        Iterator var4 = fields.iterator();

        while(var4.hasNext()) {
            Field field = (Field)var4.next();
            Class<?> clazz = null;
            String fileName = null;
            String columnName = null;
            if(field.isAnnotationPresent(Column.class)) {
                Column column = (Column)field.getAnnotation(Column.class);
                columnName = column.name();
            } else {
                columnName = field.getName();
            }

            fileName = field.getName();
            if(field.isAnnotationPresent(Enumerated.class)) {
                Enumerated enumerated = (Enumerated)field.getAnnotation(Enumerated.class);
                if(enumerated.value().toString().equals("STRING")) {
                    clazz = String.class;
                } else {
                    clazz = Integer.class;
                }
            } else {
                clazz = field.getType();
            }

            if(null != clazz && null != columnName) {
                this.mappedColumns.put(columnName, clazz);
                this.mappedFields.put(columnName, fileName);
            }
        }

    }

    protected Object getColumnValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, requiredType);
    }

    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

        for(int index = 1; index <= columnCount; ++index) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            if(this.mappedFields.get(column) != null) {
                Object value = this.getColumnValue(rs, index, (Class)this.mappedColumns.get(column));
                bw.setPropertyValue((String)this.mappedFields.get(column), value);
            }
        }

        return mappedObject;
    }
}
