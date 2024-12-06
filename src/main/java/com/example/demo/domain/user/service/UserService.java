package com.example.demo.domain.user.service;


import com.example.demo.domain.user.dto.response.SignResponseDto;
import com.example.demo.domain.user.dto.response.SignUpResponseDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.dto.JwtUtilRequest;
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
    public SignUpResponseDto signUp(String username, String password, String nickname) throws Exception {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("이미 가입된 유저입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = userRepository.save(new User(username, encodedPassword, nickname, UserRole.ROLE_USER));

        // UserRole -> AuthorityResponse로 매핑
        List<SignUpResponseDto.AuthorityResponse> authorityResponses = List.of(
                new SignUpResponseDto.AuthorityResponse(newUser.getUserRole().getAuthorityName())
        );

        return new SignUpResponseDto(
                newUser.getUsername(),
                newUser.getNickname(),
                authorityResponses
        );
    }


    public SignResponseDto sign(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(
            user.getUsername(), user.getNickname()
        );

        String token = jwtUtil.createToken(createToken);

        return new SignResponseDto(token);
    }
}
