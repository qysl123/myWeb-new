package com.zk.mina.handler;

import com.alibaba.fastjson.JSONObject;
import com.zk.mina.constants.CIMConstant;
import com.zk.mina.modle.ReplyBody;
import com.zk.mina.modle.SentBody;
import com.zk.mina.session.CIMSession;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by Ken on 2017/1/20.
 */
public class CIMIoHandler extends IoHandlerAdapter {

    protected final Logger logger = LoggerFactory.getLogger(CIMIoHandler.class);
    private final static String CIMSESSION_CLOSED_HANDLER_KEY = "client_cimsession_closed";
    private HashMap<String, CIMRequestHandler> handlers = new HashMap<>();


    public void sessionCreated(IoSession session) throws Exception {
        logger.debug("sessionCreated()... from " + session.getRemoteAddress());
    }

    public void sessionOpened(IoSession session) throws Exception {

    }

    public void messageReceived(IoSession ios, Object message)
            throws Exception {
        /**
         * flex 客户端安全策略请求，需要返回特定报文
         */
        if (CIMConstant.FLEX_POLICY_REQUEST.equals(message)) {
            ios.write(CIMConstant.FLEX_POLICY_RESPONSE);
            return;
        }

        if (!(message instanceof SentBody)) {
            return;
        }
        CIMSession cimSession = new CIMSession(ios);
        ReplyBody reply = new ReplyBody();
        SentBody body = (SentBody) message;
        String key = body.getKey();

        CIMRequestHandler handler = handlers.get(key);
        if (handler == null) {
            reply.setCode(CIMConstant.ReturnCode.CODE_405);
            reply.setCode("KEY [" + key + "] 服务端未定义");
        } else {
            reply = handler.process(cimSession, body);
        }

        if (reply != null) {
            reply.setKey(key);
            cimSession.write(reply);
            logger.info("-----------------------process done. reply: " + reply.toString());
        }
    }

    /**
     */
    public void sessionClosed(IoSession ios) throws Exception {
        logger.debug(JSONObject.toJSONString(ios.getRemoteAddress()));
        CIMSession cimSession = new CIMSession(ios);
        CIMRequestHandler handler = handlers.get(CIMSESSION_CLOSED_HANDLER_KEY);
        handler.process(cimSession, null);
    }

    /**
     */
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.debug("sessionIdle()... from " + session.getRemoteAddress());
    }

    /**
     */
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        logger.error("通信失败!", cause);
        cause.printStackTrace();
    }

    /**
     */
    public void messageSent(IoSession session, Object message) throws Exception {
    }

    public HashMap<String, CIMRequestHandler> getHandlers() {
        return handlers;
    }


    public void setHandlers(HashMap<String, CIMRequestHandler> handlers) {
        this.handlers = handlers;
    }
}
