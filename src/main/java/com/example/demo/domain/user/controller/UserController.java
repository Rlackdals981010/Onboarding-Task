package com.example.demo.domain.user.controller;


import com.example.demo.domain.user.dto.request.SignRequestDto;
import com.example.demo.domain.user.dto.request.SignUpRequestDto;
import com.example.demo.domain.user.dto.response.SignResponseDto;
import com.example.demo.domain.user.dto.response.SignUpResponseDto;
import com.example.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto signUpDto) throws Exception {
        return userService.signUp(signUpDto.getUsername(), signUpDto.getPassword(), signUpDto.getNickname());
    }

    @PostMapping("/sign")
    public SignResponseDto sign(@RequestBody SignRequestDto signDto) throws Exception {
        return userService.sign(signDto.getUsername(), signDto.getPassword());
    }


    @GetMapping("/health")
    public String health(){
        return "동작중";
    }

}
