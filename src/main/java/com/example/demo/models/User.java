package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.persistence.*;
import java.security.Principal;
import java.util.*;

@Entity
@Table(name = "usr")
@Getter
@Setter
public class User extends DefaultHandshakeHandler implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String groupNumber;
    private String facultyNumber;
    private boolean active;
    private String avatarName;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Chat> chats;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_languages", joinColumns = @JoinColumn(name= "user_id"))
    @JsonIgnore
    private List<String> programmingLanguages = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<User> friends = new TreeSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_application_To_Friends", joinColumns = @JoinColumn(
            name = "user_id"))
    @JsonIgnore
    private List<User> applicationToFriends = new ArrayList<>();

    public String getStringRoles(){
        return roles.toString();
    }

    @Override
    public String toString() {
        return "Name="  + username + "\n faculty=" + facultyNumber +
        "\ngroup=" + groupNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        return new Principal() {
            @Override
            public String getName() {
                return getUsername();
            }
        };
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

}
