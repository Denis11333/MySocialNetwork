package com.example.demo.conrollers;

import com.example.demo.models.Languages;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class ProfileController {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String myProfile(Model model, Principal principal) {

        User user = userRepository.findByUsername(principal.getName());

        model.addAttribute("MyUser", user);
        return "MyProfile";
    }


    @PostMapping("/profile")
    private String str(@RequestParam("id") Integer id,
                       final @RequestParam("image") MultipartFile file,
                       @RequestParam Map<String, String> form) throws IOException {

        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElse(new User());

        if (!Objects.equals(file.getOriginalFilename(), "")) {

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidfile = UUID.randomUUID().toString();
            String resultFileName = uuidfile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            if(user.getAvatarName() != null){
                FileUtils.forceDelete(new File(uploadPath + "/" + user.getAvatarName()));
            }

            user.setAvatarName(resultFileName);
        }

        user.getProgrammingLanguages().clear();

        for (String key : form.keySet()) {
            if (languages().contains(key)) {
                user.getProgrammingLanguages().add(key);
            }
        }

        userRepository.save(user);

        return "redirect:/profile";
    }
    @ModelAttribute("ProgrammingLanguages")
    public List<String> languages() {
        return Languages.languages();
    }
}
