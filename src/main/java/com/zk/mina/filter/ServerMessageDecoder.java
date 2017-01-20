package com.zk.mina.filter;

import com.zk.mina.constants.CIMConstant;
import com.zk.mina.modle.SentBody;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * 123
 * Created by Ken on 2017/1/20.
 */
public class ServerMessageDecoder extends CumulativeProtocolDecoder {
    protected final Logger logger = LoggerFactory.getLogger(ServerMessageDecoder.class);

    @Override
    public boolean doDecode(IoSession iosession, IoBuffer iobuffer, ProtocolDecoderOutput out) throws Exception {

        boolean complete = false;
        IoBuffer tBuffer = IoBuffer.allocate(320).setAutoExpand(true);

        iobuffer.mark();

        while (iobuffer.hasRemaining()) {
            byte b = iobuffer.get();
            /**
             * CIMConstant.MESSAGE_SEPARATE 为消息界限
             * 当一次收到多个消息时，以此分隔解析多个消息
             */
            if (b == CIMConstant.MESSAGE_SEPARATE) {

                complete = true;
                break;
            } else if (b == CIMConstant.FLEX_DATA_SEPARATE)//flex客户端 安全策略验证时会收到<policy-file- request/>\0的消息，忽略此消息内容
            {
                complete = true;
                break;
            } else {
                tBuffer.put(b);
            }
        }
        if (complete) {
            tBuffer.flip();
            byte[] bytes = new byte[tBuffer.limit()];
            tBuffer.get(bytes);

            String message = new String(bytes, CIMConstant.UTF8);

            logger.info(message);

            tBuffer.clear();
            try {

                Object body = getSentBody(message);
                out.write(body);
            } catch (Exception e) {
                out.write(message);//解析xml失败 是返回原始的xml数据到上层处理,比如flex sokcet的 安全验证请求xml
                logger.error("解析失败!", e);
            }
        } else {
            iobuffer.reset();//如果消息没有接收完整，对buffer进行重置，下次继续读取
        }
        return complete;
    }

    public Object getSentBody(String message) throws Exception {

        if (CIMConstant.CMD_HEARTBEAT_RESPONSE.equalsIgnoreCase(message)) {
            return CIMConstant.CMD_HEARTBEAT_RESPONSE;
        }

        if (CIMConstant.FLEX_POLICY_REQUEST.equalsIgnoreCase(message)) {
            return CIMConstant.FLEX_POLICY_REQUEST;
        }

        SentBody body = new SentBody();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(message.getBytes(CIMConstant.UTF8)));
        body.setKey(doc.getElementsByTagName("key").item(0).getTextContent());

        NodeList datas = doc.getElementsByTagName("data");
        if (datas != null && datas.getLength() > 0) {
            NodeList items = datas.item(0).getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                body.getData().put(node.getNodeName(), node.getTextContent());
            }
        }
        return body;
    }
}
