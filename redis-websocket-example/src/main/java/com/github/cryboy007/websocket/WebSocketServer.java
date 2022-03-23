package com.github.cryboy007.websocket;

import javax.websocket.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @InterfaceName WebSocketServer
 * @Author HETAO
 * @Date 2022/1/20 10:37
 */
public interface WebSocketServer {
    AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    ConcurrentHashMap<Long,CustomSession> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    @OnOpen
    void onOpen(Session session,Long userId);

    @OnClose
    void onClose(Session session);

    @OnMessage
    default void onMessage(Session session, String message) {};

    @OnError
    void onError(Session session, Throwable error);

    void sendMessage(Session session, String message, Long userId) throws IOException;

    default int getOnlineCount() {
        return ONLINE_COUNT.get();
    };

    default void addOnlineCount() {
        ONLINE_COUNT.incrementAndGet();
    };

    default void subOnlineCount() {
        if (ONLINE_COUNT.get() > 0) {
            ONLINE_COUNT.decrementAndGet();
        }
    };
}
