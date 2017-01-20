package com.zk.mina.constants;

/**
 * 123123
 * Created by Ken on 2017/1/20.
 */
public interface CIMConstant {

    class ReturnCode {
        public static String CODE_404 = "404";
        public static String CODE_403 = "403";
        public static String CODE_405 = "405";
        public static String CODE_200 = "200";
        public static String CODE_206 = "206";
        public static String CODE_500 = "500";
    }

    String UTF8 = "UTF-8";

    byte MESSAGE_SEPARATE = '\b';
    //flex客户端 安全策略验证时会收到<policy-file- request/>\0
    byte FLEX_DATA_SEPARATE = '\0';

    int CIM_DEFAULT_MESSAGE_ORDER = 1;

    String SESSION_KEY = "account";

    String HEARTBEAT_KEY = "heartbeat";

    /**
     * FLEX 客户端socket请求发的安全策略请求，需要特殊处理，返回安全验证报文
     */
    String FLEX_POLICY_REQUEST = "<policy-file-request/>";

    String FLEX_POLICY_RESPONSE = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";

    /**
     * 服务端心跳请求命令  cmd_server_hb_request
     */
    String CMD_HEARTBEAT_REQUEST = "S_H_RQ";
    /**
     * 客户端心跳响应命令  cmd_client_hb_response
     */
    String CMD_HEARTBEAT_RESPONSE = "C_H_RS";

    class SessionStatus {
        public static int STATUS_OK = 0;
        public static int STATUS_CLOSED = 1;

    }

    class MessageType {
        //用户会 踢出下线消息类型
        public static String TYPE_999 = "999";

    }

}
