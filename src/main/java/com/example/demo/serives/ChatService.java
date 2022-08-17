package com.example.demo.serives;

import com.example.demo.models.Chat;
import com.example.demo.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ChatService {
    final
    ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat getChat(Integer chatId){
        Chat chat = chatRepository.findById(chatId).get();

        return chat;
    }
}
