package com.zk.mina.session;

import com.zk.mina.constants.CIMConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ken on 2017/1/20.
 */
public class DefaultSessionManager implements SessionManager {

    private static HashMap<String, CIMSession> sessions = new HashMap<>();
    private static final AtomicInteger connectionsCounter = new AtomicInteger(0);

    /**
     *
     */
    public void add(String account, CIMSession session) {
        if (session != null) {
            session.setAttribute(CIMConstant.SESSION_KEY, account);
            sessions.put(account, session);
            connectionsCounter.incrementAndGet();
        }

    }


    public CIMSession get(String account) {
        return sessions.get(account);
    }


    public List<CIMSession> queryAll() {
        List<CIMSession> list = new ArrayList<>();
        list.addAll(sessions.values());
        return list;
    }

    public void remove(CIMSession session) {
        sessions.remove(session.getAttribute(CIMConstant.SESSION_KEY));
    }


    public void remove(String account) {
        sessions.remove(account);
    }


    public boolean containsCIMSession(String account) {
        return sessions.containsKey(account);
    }


    public String getAccount(CIMSession ios) {
        if (ios.getAttribute(CIMConstant.SESSION_KEY) == null) {
            for (String key : sessions.keySet()) {
                if (sessions.get(key).equals(ios) || Objects.equals(sessions.get(key).getGid(), ios.getGid())) {
                    return key;
                }
            }
        } else {
            return ios.getAttribute(CIMConstant.SESSION_KEY).toString();
        }

        return null;
    }


    @Override
    public void update(CIMSession session) {
        sessions.put(session.getAccount(), session);
    }


    @Override
    public void setState(String account, int state) {
        sessions.get(account).setStatus(state);
    }
}
