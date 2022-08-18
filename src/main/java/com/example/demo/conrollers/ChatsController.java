package com.example.demo.conrollers;

import com.example.demo.models.Chat;
import com.example.demo.models.User;
import com.example.demo.repositories.ChatRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.serives.ChatService;
import com.example.demo.serives.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/chats")
public class ChatsController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private final ChatService chatService;

    public ChatsController(UserRepository userRepository, ChatRepository chatRepository,
                           UserService userService, ChatService chatService) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @GetMapping("/createChat")
    public String createChat(Principal principal,
                             Model model) {
        User user = userRepository.findByUsername(principal.getName());

        model.addAttribute("friends", user.getFriends());

        return "createChat";
    }

    @PostMapping("/createChat")
    public String createChat(
            @RequestParam Map<String, String> form, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Chat chat = new Chat();

        StringBuilder chatName = new StringBuilder("Room : ");

        for (String key : form.keySet()) {
            for (User users : user.getFriends()) {
                if (users.getUsername().contains(key)) {
                    chat.addUser(userRepository.findByUsername(key));
                    chatName.append(key + "-");
                }
            }
        }

        chat.addUser(user);
        chatName.append("CREATOR-" + user.getUsername());
        chat.setChatName(String.valueOf(chatName));
        if (chat.getUsers().size() > 2)
            chatRepository.save(chat);

        return "redirect:/chats";
    }

    @PostMapping()
    public String showChats(Model model, Principal principal) {

        User user = userRepository.findByUsername(principal.getName());

        Set<User> friends = user.getFriends();

        userService.deleteAllActivity(userService.usersActiveChat(user), user);

        model.addAttribute("friends", friends);
        model.addAttribute("usersChats", userService.usersChatUsersBiggestThanTwo(user));


        return "Chats";
    }

    @GetMapping
    public String showAllChats(Model model, Principal principal) {

        User user = userRepository.findByUsername(principal.getName());

        Set<User> friends = user.getFriends();

        userService.deleteAllActivity(userService.usersActiveChat(user), user);

        model.addAttribute("friends", friends);
        model.addAttribute("usersChats", userService.usersChatUsersBiggestThanTwo(user));

        return "Chats";
    }

    @PostMapping("/start")  // поменять на post
    public String chating(@RequestParam("recipientId") Integer id, Principal principal,
                          Model model) {
        User myUser = userRepository.findByUsername(principal.getName());

        User recipientId = userRepository.findById(id).get();

        String first = recipientId.getId() + "_" + myUser.getId();

        Chat firstChat = chatRepository.findByChatNameContains(myUser.getId() + "_" + recipientId.getId());
        Chat secondChat = chatRepository.findByChatNameContains(recipientId.getId() + "_" + myUser.getId());


        Chat myChat = new Chat();
        if (firstChat == null && secondChat == null) {

            myChat.setChatName(first);

            myChat.addUser(myUser);
            myChat.addUser(recipientId);

            chatRepository.save(myChat);
        } else if (firstChat == null) {
            myChat = secondChat;
        } else {
            myChat = firstChat;
        }

        chatService.addToActiveUser(myChat,userRepository.findByUsername(principal.getName()));

        model.addAttribute("chat", myChat);
        model.addAttribute("MyUser", myUser);

        return "Chating";
    }

    @PostMapping("/startRoom")
    public String chatingRoom(Model model, @RequestParam("chatId") Integer chatId, Principal principal) {
        model.addAttribute("chat", chatRepository.findById(chatId).get());
        Chat chat = chatRepository.findById(chatId).get();

        System.out.println(chat.getId());
        System.out.println(chat.getUsers().size());

        chatService.addToActiveUser(chat,userRepository.findByUsername(principal.getName()));

        model.addAttribute("MyUser", userRepository.findByUsername(principal.getName()));
        return "Chating";
    }
}
