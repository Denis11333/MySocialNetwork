package com.example.demo.conrollers;

import com.example.demo.models.Languages;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.serives.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FriendController {
    private final UserRepository userRepository;
    private final UserService userService;

    public FriendController(UserRepository userRepository, UserService userService) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/findFriends")
    public String addToPossibleFriends(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());

        List<User> users = userRepository.findByUsernameNot(user.getUsername());
        model.addAttribute("users", users);
        model.addAttribute("MyUser", user);
        return "findFriends";
    }

    @PostMapping("/addToPossibleFriend/{id}")
    public String addToPossibleFriends(@PathVariable("id") Integer id,
                                       Principal principal) {
        userService.addToPossibleFriend(principal.getName(), id);
        return "redirect:/findFriends";
    }

    @PostMapping("/addFriend/{id}")
    public String addFriend(@PathVariable("id") Integer id, Principal principal) {
        userService.addFriend(principal.getName(), id);
        return "redirect:/MyFriends";
    }

    @GetMapping("/MyFriends")
    public String friends(Principal principal, Model model) {
        List<User> users = userRepository.findAll();

        User user = userRepository.findByUsername(principal.getName());
        List<User> userFriends = user.getFriends();

        model.addAttribute("Friends", userFriends);

        model.addAttribute("users", users.stream().filter(x -> x.getApplicationToFriends().contains(user))
                .collect(Collectors.toList()));

        return "MyFriends";
    }

    @PostMapping("/findByLanguages")
    public String findByLanguages(@RequestParam Map<String, String> form, Model model, Principal principal) {
        List<String> languagesList = new ArrayList<>();

        User user = userRepository.findByUsername(principal.getName());

        List<User> users = userRepository.findByUsernameNot(user.getUsername());


        for (String key : form.keySet()) {
            if (languages().contains(key)) {
                languagesList.add(key);
            }
        }

        List<User> userList = new ArrayList<>();

        if (languagesList.size() != 0) {
            for (User usr : users) {
                int counter = 0;
                for (int j = 0; j < languagesList.size(); j++) {
                    if (usr.getProgrammingLanguages().contains(languagesList.get(j))) {
                        counter++;
                    }
                    if (counter == languagesList.size()) {
                        userList.add(usr);
                    }
                }
            }
        }else {
            userList = users;
        }

        model.addAttribute("users", userList);
        model.addAttribute("MyUser", user);

        return "findFriends";
    }

    @PostMapping("/deleteFriend")
    public String deleteFriend(Principal principal,
                               @RequestParam("id") Integer idFriend){

        userService.deleteFriend(principal.getName(), idFriend);

        return "redirect:/MyFriends";
    }

    @ModelAttribute("ProgrammingLanguages")
    public List<String> languages() {
        return Languages.languages();
    }
}
