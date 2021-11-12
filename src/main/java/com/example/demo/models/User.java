package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "usr")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String groupNumber;
    private String facultyNumber;
    private boolean active;
    private String avatarName;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Chat> chats = new TreeSet<>();

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_languages", joinColumns = @JoinColumn(name= "user_id"))
    private List<String> programmingLanguages = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    @CollectionTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"))
    private List<User> friends = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @CollectionTable(name = "user_application_To_Friends", joinColumns = @JoinColumn(
            name = "user_id"))
    private List<User> applicationToFriends = new ArrayList<>();

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
