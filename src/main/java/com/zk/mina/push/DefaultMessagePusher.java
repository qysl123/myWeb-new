package com.zk.mina.push;

import com.zk.mina.modle.Message;
import com.zk.mina.session.CIMSession;
import com.zk.mina.session.DefaultSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ken on 2017/1/20.
 */
public class DefaultMessagePusher implements CIMMessagePusher {
    private final Logger logger = LoggerFactory.getLogger(DefaultMessagePusher.class);
    private DefaultSessionManager sessionManager;

    public void setSessionManager(DefaultSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 向用户发送消息
     *
     * @param msg
     */
    public void push(Message msg) {
        CIMSession session = sessionManager.get(msg.getReceiver());
        if (session != null && session.isConnected()) {
            session.write(msg);
        }
//		服务器集群时，可以在此 判断当前session是否连接于本台服务器，如果是，继续往下走，如果不是，将此消息发往当前session连接的服务器并 return
//        if(!session.isLocalhost()){//判断当前session是否连接于本台服务器，如不是
//			MessageDispatcher.execute(msg, session.getHost());
//			return;
//		}

//        if (session != null && session.isConnected()) {
//			//如果用户标示了DeviceToken 且 需要后台推送（Pushable=1） 说明这是ios设备需要使用anps发送
//			if(StringUtil.isNotEmpty(session.getDeviceToken())&&session.getPushable()==User.PUSHABLE){
//				try {
//					deliverByANPS(msg,session.getDeviceToken());
//					msg.setStatus(Message.STATUS_SEND);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					msg.setStatus(Message.STATUS_NOT_SEND);
//				}
//			}else{
//				//推送消息
//				session.deliver(MessageUtil.transform(msg));
//			}
//
//            //推送消息
//            session.write(msg);
//
//        } else {
//			User target = ((UserService)ContextHolder.getBean("userServiceImpl")).getUserByAccount(msg.getReceiver());
//            //如果用户标示了DeviceToken 且 需要后台推送（Pushable=1） 说明这是ios设备需要使用anps发送
//			if(StringUtil.isNotEmpty(target.getDeviceToken())&&target.getPushable()==User.PUSHABLE)
//			{
//				try {
//					deliverByANPS(msg,target.getDeviceToken());
//					msg.setStatus(Message.STATUS_SEND);
//				} catch (Exception e) {
//					msg.setStatus(Message.STATUS_NOT_SEND);
//				}
//
//			}
//            //未发送
//        }
//        try {
////            可以在这保存消息到数据库
//            ((MessageService) ContextHolder.getBean("messageServiceImpl")).save(msg);
//        } catch (Exception e) {
//            log.warn(" Messages insert to database failure!!");
//        }
    }


//	public void deliverByANPS(Message msg,String deviceToken) throws Exception {
//		    String alert = getMessageTile(msg);
//			// 被推送的iphone应用程序标示符
//			PayLoad payLoad = new PayLoad();
//			payLoad.addAlert(alert);
//			payLoad.addBadge(1);
//			payLoad.addSound("default");
//			PushNotificationManager pushManager = PushNotificationManager.getInstance();
//			pushManager.addDevice(deviceToken, deviceToken);
//			String host = ConfigManager.getInstance().get("apple.anps.host"); // 测试用的苹果推送服务器
//			int port = Integer.parseInt(ConfigManager.getInstance().get("apple.anps.port"));
//
//			String password = ConfigManager.getInstance().get("apple.anps.p12.password");
//            String p12File =  ConfigManager.getInstance().get("apple.anps.p12.file");
//			pushManager.initializeConnection(host, port,this.getClass().getClassLoader().getResourceAsStream(p12File), password,
//					SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
//
//			// Send Push
//			Device client = pushManager.getDevice(deviceToken);
//			pushManager.sendNotification(client, payLoad); // 推送消息
//			pushManager.stopConnection();
//			pushManager.removeDevice(deviceToken);
//
//	}

}
