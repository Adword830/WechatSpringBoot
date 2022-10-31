package cn.percent.common.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: zhangpengju
 * Date: 2021/12/16
 * Time: 15:06
 * Description:
 */
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket/{userId}")
public class WebSocketServer {
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 以用户的id为key，WebSocketServer为对象保存起来
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>(12);

    /**
     * 会话
     */
    private Session session;

    /**
     * 用户名称
     */
    private String userId;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session)
    {
        onlineNumber++;
        log.info("现在来连接的客户id："+session.getId()+"用户名："+userId);
        this.userId = userId;
        this.session = session;
        clients.put(userId,session);
        log.info("有新连接加入！ 当前在线人数" + onlineNumber);

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("服务端发生了错误"+error.getMessage());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        clients.remove(userId);
        log.info("有连接关闭！ 当前在线人数" + clients.size());
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     */
    @OnMessage
    public void onMessage(String message){
        log.info("来自客户端消息：" + message);
    }

    /**
     *
     * @param message
     * @param toUserId
     * @throws IOException
     */
    public void sendMessageTo(String message, String toUserId) {

        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            if(sessionEntry.getKey().equals(toUserId)){
                sessionEntry.getValue().getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     *
     * @param message
     * @param fromUserId
     * @throws IOException
     */
    public void sendMessageAll(String message,String fromUserId) throws IOException {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            sessionEntry.getValue().getAsyncRemote().sendText(message);
        }
    }

    
}
