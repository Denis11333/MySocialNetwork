package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String name);
    List<User> findByUsernameNot(String name);
    List<User> findByIdIn(List<Integer> integers);

}
