package com.zk.mina.handler;

import com.zk.mina.constants.CIMConstant;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * Created by Ken on 2017/1/20.
 */
public class ServerKeepAliveFactoryImpl implements KeepAliveMessageFactory {
    @Override
    public Object getRequest(IoSession arg0) {
        return CIMConstant.CMD_HEARTBEAT_REQUEST;
    }

    @Override
    public Object getResponse(IoSession arg0, Object arg1) {
        return null;
    }

    @Override
    public boolean isRequest(IoSession arg0, Object arg1) {
        return false;
    }

    @Override
    public boolean isResponse(IoSession arg0, Object arg1) {
        return CIMConstant.CMD_HEARTBEAT_RESPONSE.equalsIgnoreCase(arg1.toString());
    }
}
