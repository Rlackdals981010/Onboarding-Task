package com.example.demo.domain.user.service;


import com.example.demo.domain.user.dto.UserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    public UserResponse.Signup signUp(String username, String password, String nickname) throws Exception {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("이미 가입된 유저입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = userRepository.save(new User(username, encodedPassword, nickname, UserRole.ROLE_USER));

        // UserRole -> AuthorityResponse로 매핑
        List<UserResponse.AuthorityResponse> authorityResponses = List.of(
                new UserResponse.AuthorityResponse(newUser.getUserRole().getAuthorityName())
        );

        return new UserResponse.Signup(
                newUser.getUsername(),
                newUser.getNickname(),
                authorityResponses
        );
    }


}
