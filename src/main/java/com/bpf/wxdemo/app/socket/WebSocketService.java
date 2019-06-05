package com.bpf.wxdemo.app.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * websocket服务
 * @author baipengfei
 */
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}
