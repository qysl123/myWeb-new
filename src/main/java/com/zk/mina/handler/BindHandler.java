package com.zk.mina.handler;

import com.zk.mina.constants.CIMConstant;
import com.zk.mina.modle.Message;
import com.zk.mina.modle.ReplyBody;
import com.zk.mina.modle.SentBody;
import com.zk.mina.push.SystemMessagePusher;
import com.zk.mina.session.CIMSession;
import com.zk.mina.session.DefaultSessionManager;
import com.zk.mina.utils.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by Ken on 2017/1/20.
 */
public class BindHandler implements CIMRequestHandler {
    private final Logger logger = LoggerFactory.getLogger(BindHandler.class);

    public ReplyBody process(CIMSession newSession, SentBody message) {
        DefaultSessionManager sessionManager = ((DefaultSessionManager) ContextHolder.getBean("CIMSessionManager"));

        ReplyBody reply = new ReplyBody();
        reply.setCode(CIMConstant.ReturnCode.CODE_200);
        try {
            String account = message.get("account");
            newSession.setGid(UUID.randomUUID().toString().replaceAll("-", ""));
            newSession.setAccount(account);
            newSession.setDeviceId(message.get("deviceId"));
            newSession.setHost(InetAddress.getLocalHost().getHostAddress());
            newSession.setChannel(message.get("channel"));
            newSession.setDeviceModel(message.get("device"));
            newSession.setClientVersion(message.get("version"));
            newSession.setSystemVersion(message.get("osVersion"));
            newSession.setBindTime(System.currentTimeMillis());
            newSession.setPackageName(message.get("packageName"));
            /**
             * 由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接
             */
            CIMSession oldSession = sessionManager.get(account);

            //如果是账号已经在另一台终端登录。则让另一个终端下线
            if (oldSession != null) {
                if (oldSession.fromOtherDevice(newSession)) {
                    sendForceOfflineMessage(oldSession, account);
                }
                if (oldSession.equals(newSession)) {
                    reply.put("sid", oldSession.getGid());
                    return reply;
                }
                oldSession.removeAttribute(CIMConstant.SESSION_KEY);
                oldSession.closeNow();
            }

            //第一次设置心跳时间为登录时间
            newSession.setBindTime(System.currentTimeMillis());
            newSession.setHeartbeat(System.currentTimeMillis());

            sessionManager.add(account, newSession);
        } catch (Exception e) {
            reply.setCode(CIMConstant.ReturnCode.CODE_500);
            e.printStackTrace();
        }
        logger.debug("bind :account:" + message.get("account") + "-----------------------------" + reply.getCode());
        reply.put("sid", newSession.getGid());
        return reply;
    }


    private void sendForceOfflineMessage(CIMSession oldSession, String account) {
        Message msg = new Message();
        msg.setType(CIMConstant.MessageType.TYPE_999);//强行下线消息类型
        msg.setReceiver(account);

        if (oldSession.isLocalhost() && oldSession.isConnected()) {
            oldSession.write(msg);
            oldSession.closeOnFlush();
        }
        if (!oldSession.isLocalhost()) {
            ContextHolder.getBean(SystemMessagePusher.class).push(msg);
        }
        oldSession.removeAttribute(CIMConstant.SESSION_KEY);
    }
}
