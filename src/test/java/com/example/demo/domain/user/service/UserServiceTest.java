package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.UserRequest;
import com.example.demo.domain.user.dto.UserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequest.Signup signupDto;


    @BeforeEach
    void setUp() {
        // signupDto 초기화
        signupDto = new UserRequest.Signup("test", "testtest", "test1");
    }

    @Test
    void 회원가입_성공() throws Exception {
        String encodedPassword = "encodedTestPassword";
        User newUser = new User(signupDto.username(), encodedPassword, signupDto.nickname(), UserRole.ROLE_USER);

        Mockito.when(userRepository.findByUsername(signupDto.username())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(signupDto.password())).thenReturn(encodedPassword);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);

        UserResponse.Signup response = userService.signUp(signupDto.username(), signupDto.password(), signupDto.nickname());

        Assertions.assertEquals(signupDto.username(), response.username());
        Assertions.assertEquals(signupDto.nickname(), response.nickname());
        Assertions.assertEquals(1, response.authorities().size());
        Assertions.assertEquals(UserRole.ROLE_USER.getAuthorityName(), response.authorities().get(0).authorityName());
    }

    @Test
    void 회원가입_실패_이미가입된유저() {
        Mockito.when(userRepository.findByUsername(signupDto.username())).thenReturn(Optional.of(new User()));

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            userService.signUp(signupDto.username(), signupDto.password(), signupDto.nickname());
        });

        Assertions.assertEquals("이미 가입된 유저입니다.", exception.getMessage());
    }
}