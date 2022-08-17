package com.example.demo.repositories;

import com.example.demo.models.Chat;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Chat findByChatName(String chatName);
    Chat findByChatNameContains(String chatName);

    Optional<Chat> findById(Integer id);

    @Query(value = "select u.chat_id from users_chats u where u.user_id=?1",
    nativeQuery = true)
    List<Integer> findAllUserByQuery(Integer id);
}
