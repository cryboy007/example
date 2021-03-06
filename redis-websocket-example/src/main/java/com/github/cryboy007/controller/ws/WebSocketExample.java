package com.github.cryboy007.controller.ws;

import com.alibaba.fastjson.JSON;
import com.github.cryboy007.constant.RedisConstant;
import com.github.cryboy007.model.Book;
import com.github.cryboy007.to.MessageTo;
import com.github.cryboy007.websocket.AbstractWebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName WebSocketProgress
 * @Author tao.he
 * @Since 2022/1/20 11:01
 */
@Slf4j
@ServerEndpoint("/ws/example/{userId}")
@Component
public class WebSocketExample extends AbstractWebSocketServer {
    /**
     * redis监听
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String content = new String(message.getBody());
            String channel = new String(message.getChannel());
            log.info("redis监听到消息内容: {}", content);
            log.info("消息监听通道: {}", channel);
            if (RedisConstant.MESSAGE_PROGRESS_CHANNEL.equals(channel)) {
                MessageTo messageTo = JSON.parseObject(content, MessageTo.class);
                this.sendMessage(null, messageTo.getMessage(), messageTo.getUserId());
            }
        } catch (IOException e) {
            log.error("-----------------------消息提醒redis监听处理失败-------------------------");
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendMessage(Session session, String message, Long userId) throws IOException {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        if (Objects.nonNull(session)) {
            session.getBasicRemote().sendText(message);
        } else if (Objects.nonNull(userId)) {
            if (WEB_SOCKET_MAP.containsKey(userId)) {
                WEB_SOCKET_MAP.get(userId).getSession().getBasicRemote().sendText(message);
            }
        } else {
            WEB_SOCKET_MAP.values().forEach(item -> {
                try {
                    item.getSession().getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
