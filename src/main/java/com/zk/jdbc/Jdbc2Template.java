package com.zk.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.jdbc.rowmapper.AnnotationRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongkun on 2017/6/30.
 */
public class Jdbc2Template {
    private static Logger log = LoggerFactory.getLogger(Jdbc2Template.class);
    private DruidDataSource dataSource;

    public Jdbc2Template(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long insert(Object obj, String tableName) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(obj);
        return Long.valueOf(this.simpleJdbcInsert(tableName).executeAndReturnKey(parameters).longValue());
    }

    public int[] insertBatch(List list, String tableName) {
        SqlParameterSource[] sqlParameterSources = new BeanPropertySqlParameterSource[list.size()];

        for(int i = 0; i < list.size(); ++i) {
            sqlParameterSources[i] = new BeanPropertySqlParameterSource(list.get(i));
        }

        return this.simpleJdbcInsert(tableName).executeBatch(sqlParameterSources);
    }

    public <T> List<T> findSQL(String sql, Class clazz) {
        return this.namedParameterJdbcTemplate().query(sql, new AnnotationRowMapper(clazz));
    }

    public <T> List<T> findSQL(String sql, Class clazz, HashMap hashMap) {
        return this.namedParameterJdbcTemplate().query(sql, hashMap, new AnnotationRowMapper(clazz));
    }

    public <T> List<T> findSQL(String sql, Class clazz, Object... params) {
        if(log.isInfoEnabled()) {
            log.info("findSQL__:" + sql + "params:" + Arrays.toString(params));
        }

        return this.jdbcTemplate().query(sql, params, new AnnotationRowMapper(clazz));
    }

    public <T> T findOne(String sql, Class clazz) {
        if(log.isInfoEnabled()) {
            log.info("findSQL__:" + sql);
        }

        return this.jdbcTemplate().queryForObject(sql, (RowMapper<T>) new AnnotationRowMapper(clazz));
    }

    public <T> T findOne(String sql, Class clazz, Object... params) {
        try {
            if(log.isInfoEnabled()) {
                log.info("findOne__:" + sql + "params:" + Arrays.toString(params));
            }

            return this.jdbcTemplate().queryForObject(sql, params, (RowMapper<T>) new AnnotationRowMapper(clazz));
        } catch (IncorrectResultSizeDataAccessException var5) {
            log.error("no result");
            return null;
        }
    }

    public <T> T findOne(String sql, Class clazz, HashMap map) {
        return this.namedParameterJdbcTemplate().queryForObject(sql, (Map<String, ?>) map, (RowMapper<T>) new AnnotationRowMapper(clazz));
    }

    public int update(String sql) {
        return this.jdbcTemplate().update(sql);
    }

    public int update(String sql, Object... params) {
        log.info(sql);
        return this.jdbcTemplate().update(sql, params);
    }

    public int update(String sql, HashMap map) {
        return this.namedParameterJdbcTemplate().update(sql, map);
    }

    public int delete(String sql) {
        return this.jdbcTemplate().update(sql);
    }

    public int delete(String sql, Object... params) {
        log.info("delete --" + sql);
        return this.jdbcTemplate().update(sql, params);
    }

    public int delete(String sql, HashMap map) {
        return this.namedParameterJdbcTemplate().update(sql, map);
    }

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(this.dataSource);
    }

    private SimpleJdbcInsert simpleJdbcInsert(String tableName) {
        return (new SimpleJdbcInsert(this.dataSource)).withTableName(tableName).usingGeneratedKeyColumns(new String[]{"id"});
    }

    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(this.dataSource);
    }
}
