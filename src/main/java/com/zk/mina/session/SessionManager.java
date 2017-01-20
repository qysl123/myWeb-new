package com.zk.mina.session;

import java.util.List;

/**
 * Created by Ken on 2017/1/20.
 */
interface SessionManager {
    /**
     * 添加新的session
     */
    void add(String account, CIMSession session);

    /**
     * 更新session
     */
    void update(CIMSession session);

    /**
     * @param account 客户端session的 key 一般可用 用户账号来对应session
     */
    CIMSession get(String account);

    /**
     * 获取所有session
     */
    List<CIMSession> queryAll();


    /**
     * 删除session
     */
    void remove(String account);

    /**
     * 设置session失效
     */
    void setState(String account, int state);
}
