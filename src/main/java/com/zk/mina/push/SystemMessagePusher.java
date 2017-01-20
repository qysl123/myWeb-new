package com.zk.mina.push;

import com.zk.mina.modle.Message;

/**
 * Created by Ken on 2017/1/20.
 */
public class SystemMessagePusher extends DefaultMessagePusher {
    @Override
    public void push(Message message) {
        message.setSender("system");
        super.push(message);

    }
}
