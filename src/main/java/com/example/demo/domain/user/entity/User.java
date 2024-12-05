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

    private String userName;
    private String password;
    @Column(unique = true)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    public User(String userName, String password , String nickName ){
        this.userName=userName;
        this.password=password;
        this.nickName=nickName;
    }

}
