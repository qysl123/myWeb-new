package com.zk.mina.handler;

import com.zk.mina.modle.ReplyBody;
import com.zk.mina.modle.SentBody;
import com.zk.mina.session.CIMSession;

/**
 * Created by Ken on 2017/1/20.
 */
public interface CIMRequestHandler {
    ReplyBody process(CIMSession session,SentBody message);
}
