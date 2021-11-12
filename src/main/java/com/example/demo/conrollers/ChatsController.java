package com.example.demo.conrollers;

import com.example.demo.models.User;
import com.example.demo.repositories.ChatRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chats")
public class ChatsController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatsController(UserRepository userRepository, ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String showAllChats(Model model, Principal principal){
        User user = userRepository.findByUsername(principal.getName());

        List<User> friends = user.getFriends();

        model.addAttribute("friends", friends);

        return "Chats";
    }

    @GetMapping("/All")
    public String str(){
        return "test";
    }
}
