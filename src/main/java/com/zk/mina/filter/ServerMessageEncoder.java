package com.zk.mina.filter;

import com.alibaba.fastjson.JSONObject;
import com.zk.mina.constants.CIMConstant;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ken on 2017/1/20.
 */
public class ServerMessageEncoder extends ProtocolEncoderAdapter {
    protected final Logger logger = LoggerFactory.getLogger(ServerMessageEncoder.class);

    @Override
    public void encode(IoSession iosession, Object message, ProtocolEncoderOutput out) throws Exception {

        IoBuffer buff = IoBuffer.allocate(320).setAutoExpand(true);
        buff.put(message.toString().getBytes(CIMConstant.UTF8));
        buff.put(CIMConstant.MESSAGE_SEPARATE);
        buff.flip();
        out.write(buff);
        logger.debug(JSONObject.toJSONString(message));
    }
}
