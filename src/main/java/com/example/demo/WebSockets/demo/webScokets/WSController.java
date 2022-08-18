package com.example.demo.WebSockets.demo.webScokets;

import com.example.demo.models.Chat;
import com.example.demo.models.Message;
import com.example.demo.models.User;
import com.example.demo.repositories.ChatRepository;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.serives.ChatService;
import com.example.demo.serives.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
public class WSController {
    private final WSService service;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    final UserService userService;
    final ChatService chatService;

    public WSController(ChatRepository chatRepository, UserRepository userRepository, MessageRepository messageRepository, UserService userService, WSService service, ChatService chatService) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.service = service;
        this.chatService = chatService;
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestParam("text") String message) {

        service.notifyFrontend(message);

    }

    @PostMapping("/send-private-message")
    public String sendPrivateMessage(@RequestParam("text") String message,
                                     @RequestParam("chatId") Integer chatId,
                                     Principal principal,
                                     Model model) {

        User MyUser = userRepository.findByUsername(principal.getName());
        Message message1 = new Message();
        message1.setText(message);
        message1.setUser(MyUser);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        message1.setTime(LocalDateTime.now().format(dateTimeFormatter));

        Chat chat = chatRepository.findById(chatId).get();

        message1.setChat(chat);

//        chat.getUsers().forEach(x -> System.out.println(x.getUsername()));

        for (User user : chat.getActiveUsers().stream().distinct().collect(Collectors.toList())) {
            service.notifyUser(user.getUsername(), message1);
        }

        messageRepository.save(message1);

        model.addAttribute("chat", chat);
        model.addAttribute("MyUser", MyUser);


        return "Chating";
    }

    public static String makeUrl(HttpServletRequest request)
    {
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }

}
