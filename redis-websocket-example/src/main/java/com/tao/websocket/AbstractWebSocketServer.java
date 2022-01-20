package com.tao.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.ObjectUtils;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * @ClassName AbstractWebSocketServer
 * @Author tao.he
 * @Since 2022/1/20 10:46
 */
@Slf4j
public abstract class AbstractWebSocketServer implements WebSocketServer, MessageListener {

    @Override
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") Long userId) {
        addOnlineCount();
        log.info("有连接加入! 用户ID: {},当前在线人数为: {}", userId, getOnlineCount());
        if (!ObjectUtils.isEmpty(userId)) {
            WEB_SOCKET_MAP.put(userId,new CustomSession(session,userId));
        }
    }

    @Override
    @OnClose
    public void onClose(Session session) {
        WEB_SOCKET_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getSession().getId().equals(session.getId()))
                .forEach(entry -> WEB_SOCKET_MAP.remove(entry.getKey()));

        subOnlineCount();
        log.info("有连接关闭!当前在线人数为:{}", getOnlineCount());
    }


    @Override
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误: {}, Session ID: {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }
}
