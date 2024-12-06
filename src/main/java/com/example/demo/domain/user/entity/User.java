package com.example.demo.domain.user.entity;

import com.example.demo.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    public User(String username, String password , String nickname, UserRole userRole ){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

}
