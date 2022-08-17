package com.example.demo.WebSockets.demo.conrollers;

import com.example.demo.models.Message;
import com.example.demo.repositories.ChatRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
public class MessageController {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public MessageController(UserRepository userRepository, ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message getMessage(final Message message, Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        return message;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/chat/")
    public Message getPrivateMessage(final Message message, Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        return message;
    }
}
