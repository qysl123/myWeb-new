package com.zk.mina.handler;

import com.zk.mina.constants.CIMConstant;
import com.zk.mina.modle.Message;
import com.zk.mina.modle.ReplyBody;
import com.zk.mina.modle.SentBody;
import com.zk.mina.session.CIMSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ken on 2017/1/20.
 */
public class PushOfflineMessageHandler implements CIMRequestHandler {

    protected final Logger logger = LoggerFactory.getLogger(PushOfflineMessageHandler.class);

    public ReplyBody process(CIMSession ios, SentBody message) {
        ReplyBody reply = new ReplyBody();
        reply.setCode(CIMConstant.ReturnCode.CODE_200);
        try {
            String account = message.get("account");
            //获取到存储的离线消息
            //List<Message> list = messageService.queryOffLineMessages(account);
            List<Message> list = new ArrayList<>();
            for (Message m : list) {
                ios.write(m);
            }
        } catch (Exception e) {
            reply.setCode(CIMConstant.ReturnCode.CODE_500);
            logger.error("拉取离线消息失败", e);
        }
        return reply;
    }
}
