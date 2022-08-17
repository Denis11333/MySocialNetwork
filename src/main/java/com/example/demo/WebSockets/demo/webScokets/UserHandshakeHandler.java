package com.example.demo.WebSockets.demo.webScokets;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Random;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);


    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();
        LOG.info("User with ID '{}' opened the page", principal.getName());

        return new UserPrincipal(principal.getName());
    }
}
