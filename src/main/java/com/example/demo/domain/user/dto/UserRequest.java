package com.example.demo.domain.user.dto;



public sealed interface UserRequest permits UserRequest.Signup, UserRequest.Sign{

    // 회원가입 요청을 위한 Record
    record Signup(
            String username,
            String password,
            String nickname
    ) implements UserRequest {}

    // 로그인 요청을 위한 Record
    record Sign(
            String username,
            String password
    ) implements UserRequest {}

}
