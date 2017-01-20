package com.zk.mina.handler;

import com.zk.mina.constants.CIMConstant;
import com.zk.mina.modle.ReplyBody;
import com.zk.mina.modle.SentBody;
import com.zk.mina.session.CIMSession;
import com.zk.mina.session.DefaultSessionManager;
import com.zk.mina.utils.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ken on 2017/1/20.
 */
public class SessionClosedHandler implements CIMRequestHandler {

    protected final Logger logger = LoggerFactory.getLogger(SessionClosedHandler.class);

    public ReplyBody process(CIMSession ios, SentBody message) {
        Object account =ios.getAttribute(CIMConstant.SESSION_KEY);
        if(account == null){
            return null;
        }
        DefaultSessionManager sessionManager  =  ((DefaultSessionManager) ContextHolder.getBean("CIMSessionManager"));

        ios.removeAttribute(CIMConstant.SESSION_KEY);
        ios.closeNow();
        sessionManager.remove(account.toString());
        return null;
    }
}
