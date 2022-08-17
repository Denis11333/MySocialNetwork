package com.example.demo.serives;

import com.example.demo.models.Chat;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Set<Chat> usersChat(User user){
        return user.getChats().stream().filter(x -> x.getUsers().size() > 2).collect(Collectors.toSet());
    }

    public void addFriend(String name, Integer friendId) {
        User user = userRepo.findByUsername(name);
        User friendUser = userRepo.findById(friendId).orElseThrow(() -> new RuntimeException("Add friend error"));

        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        user.getApplicationToFriends().remove(friendUser);
        friendUser.getApplicationToFriends().remove(user);
    }

    public void addToPossibleFriend(String name, Integer FriendId) {
        User user = userRepo.findByUsername(name);
        User friendUser = userRepo.findById(FriendId).orElseThrow(() -> new RuntimeException("Add to possible friends error"));

        user.getApplicationToFriends().add(friendUser);

        if (friendUser.getApplicationToFriends().contains(user)) {
            addFriend(user.getUsername(), FriendId);
        }
    }

    public void deleteFriend(String name, Integer FriendId) {
        User user = userRepo.findByUsername(name);
        User friendUser = userRepo.findById(FriendId).orElseThrow(() -> new RuntimeException("delete Friend error"));

        user.getFriends().remove(friendUser);
        friendUser.getFriends().remove(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

}
