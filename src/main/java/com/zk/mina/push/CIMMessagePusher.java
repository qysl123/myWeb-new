package com.zk.mina.push;

import com.zk.mina.modle.Message;

/**
 * Created by Ken on 2017/1/20.
 */
public interface CIMMessagePusher {
    /**
     * 向用户发送消息
     */
    void push(Message msg);
}
