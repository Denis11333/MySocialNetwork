package com.example.demo.WebSockets.demo.webScokets;

import com.example.demo.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyFrontend(final String message) {
        Message message1 = new Message();
        message1.setText(message);

        messagingTemplate.convertAndSend("/topic/method", message1);
    }

    public void notifyUser(final String id, final Message message) {
        messagingTemplate.convertAndSendToUser(id, "/topic/private-messages", message);
    }
}
