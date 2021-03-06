package com.zk.mina.session;

import java.util.List;

/**
 * 集群 session管理实现示例， 各位可以自行实现 AbstractSessionManager接口来实现自己的 session管理
 * <p/>
 * 服务器集群时 须要将CIMSession 信息存入数据库或者nosql 等 第三方存储空间中，便于所有服务器都可以访问
 */
public class ClusterSessionManager implements SessionManager {


    public void addSession(String account, CIMSession session) {
        /**
         * 下面 将session 存入数据库
         */
    }


    public CIMSession get(String account) {
        //这里查询数据库
         /*CIMSession session = database.getSession(account);
         session.setIoSession(ContextHolder.getBean(CIMNioSocketAcceptor.class).getManagedSessions().get(session.getNid()));
         return session;*/
        return null;
    }


    @Override
    public List<CIMSession> queryAll() {
        /*//这里查询数据库
   	 return database.getSessions();*/
        return null;
    }


    @Override
    public void remove(String account) {
        //database.removeSession(account);*/
    }


    @Override
    public void setState(String account, int state) {
    }

    @Override
    public void update(CIMSession session) {
    }

    @Override
    public void add(String account, CIMSession session) {
    }

}
