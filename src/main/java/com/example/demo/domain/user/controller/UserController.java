package com.example.demo.domain.user.controller;


import com.example.demo.domain.user.dto.UserRequest;
import com.example.demo.domain.user.dto.UserResponse;
import com.example.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserResponse.Signup signup(@RequestBody UserRequest.Signup signUpDto) throws Exception {
        return userService.signUp(signUpDto.username(), signUpDto.password(), signUpDto.nickname());
    }



}
