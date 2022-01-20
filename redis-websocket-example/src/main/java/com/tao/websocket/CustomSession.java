package com.tao.websocket;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.Session;
import java.util.List;

/**
 * @ClassName CustomSession
 * @Author tao.he
 * @Since 2022/1/20 10:42
 */
@Data
@AllArgsConstructor
public class CustomSession {
    private Session session;

    private List<Session> sessionList;

    private Long userId;

    public CustomSession(Session session, Long userId) {
        this.session = session;
        this.userId = userId;
        this.sessionList = Lists.newArrayList();
        this.sessionList.add(session);
    }
}
