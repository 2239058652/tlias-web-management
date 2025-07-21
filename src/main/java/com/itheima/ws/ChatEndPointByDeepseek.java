package com.itheima.ws;

import com.itheima.service.impl.AIDeepseekService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@ServerEndpoint("/deepseek-chat")
public class ChatEndPointByDeepseek {



    private static AIDeepseekService aiService;

    private static final Logger logger = LoggerFactory.getLogger(ChatEndPointByDeepseek.class);
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    // 注入 Spring Bean
    @Autowired
    public void setAIService(AIDeepseekService aiService) {
        ChatEndPointByDeepseek.aiService = aiService;
    }


    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("新的WebSocket连接: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自{}的消息: {}", session.getId(), message);
        // 异步处理 AI 请求
        CompletableFuture.runAsync(() -> {
            try {
                // 调用 AI 服务
                String aiResponse = aiService.getAIResponse(message);

                // 发送 AI 响应
                session.getBasicRemote().sendText(aiResponse);
            } catch (IOException e) {
                logger.error("发送消息失败: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("处理 AI 请求时出错: {}", e.getMessage());
                try {
                    session.getBasicRemote().sendText("处理您的请求时出错");
                } catch (IOException ex) {
                    logger.error("发送错误消息失败: {}", ex.getMessage());
                }
            }
        });
        // 广播消息给所有客户端
        // broadcast("用户" + session.getId() + "说: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        logger.info("连接关闭: {}", session.getId());
    }

    private static void broadcast(String message) {
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("向会话{}发送消息失败: {}", session.getId(), e.getMessage());
            }
        });
    }
}
