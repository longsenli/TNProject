package com.tnpy.mes.service.pushNotification.impl;

import com.alibaba.fastjson.JSONObject;
import com.tnpy.common.utils.token.Token;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/19 14:14
 */
@ServerEndpoint(value = "/websocket")
@Component
public class websocketManageService {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<websocketManageService> webSocketSet = new CopyOnWriteArraySet<websocketManageService>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private Token token;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        //  System.out.println(JSONObject.toJSON(session).toString());
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        /*
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("当前在线人数为" + getOnlineCount());
        } catch (IOException e) {
            System.out.println("IO异常");
        }*/
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        this.token = (Token) JSONObject.toJavaObject(JSONObject.parseObject(message), Token.class);
        //群发消息
      /*
        for (websocketManageService item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (websocketManageService item : webSocketSet) {
            try {

                item.sendMessage(message);
            } catch (IOException e) {
                System.out.println("============Wrong" + e.getMessage());
                continue;
            }
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfoToUserList(String message, HashSet<String> userSet) throws IOException {
      //  System.out.println(userSet.toString());
        for (websocketManageService item : webSocketSet) {
            try {
               // System.out.println(item.token.getUserid() + "============");
                if (userSet.contains(item.token.getUserid()))
                {
                   // System.out.println(item.token.getUserid() + "============sended");
                    item.sendMessage(message);
                }

            } catch (IOException e) {
                System.out.println("============Wrong" + e.getMessage());
                continue;
            }
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfoPortion(String role, String message) throws IOException {
        for (websocketManageService item : webSocketSet) {
            try {
                if (item.token.getUserid().equals(role))
                    item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        websocketManageService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        websocketManageService.onlineCount--;
    }
}
