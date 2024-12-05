package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.UserRequest;
import com.example.demo.domain.user.dto.UserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.dto.JwtUtilRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

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

        given(userRepository.findByUsername(signupDto.username())).willReturn(Optional.empty());
        given(passwordEncoder.encode(signupDto.password())).willReturn(encodedPassword);
        given(userRepository.save(Mockito.any(User.class))).willReturn(newUser);

        UserResponse.Signup response = userService.signUp(signupDto.username(), signupDto.password(), signupDto.nickname());

        assertThat(signupDto.username()).isEqualTo(response.username());
        assertThat(signupDto.nickname()).isEqualTo(response.nickname());
        assertThat(1).isEqualTo(response.authorities().size());
        assertThat(UserRole.ROLE_USER.getAuthorityName()).isEqualTo(response.authorities().get(0).authorityName());

    }

    @Test
    void 회원가입_실패_이미가입된유저() {
        given(userRepository.findByUsername(signupDto.username())).willReturn(Optional.of(new User()));

        Exception exception = assertThrows(Exception.class, () -> {
            userService.signUp(signupDto.username(), signupDto.password(), signupDto.nickname());
        });

        assertThat("이미 가입된 유저입니다.").isEqualTo(exception.getMessage());
    }

    @Test
    void 로그인_성공() {
        // given
        String username = "test";
        String password = "testPassword";
        String encodedPassword = "encodedTestPassword";
        User user = new User(username, encodedPassword, "testNickname", UserRole.ROLE_USER);

        JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(username, "testNickname");
        String token = "testJwtToken";

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
        given(jwtUtil.createToken(createToken)).willReturn(token);

        // when
        UserResponse.Sign response = userService.sign(username, password);

        // then
        assertThat(response.token()).isEqualTo(token);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void 로그인_실패_존재하지_않는_사용자() {
        // given
        String username = "nonExistentUser";
        String password = "password";

        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.sign(username, password);
        });

        // then
        assertThat("사용자를 찾을 수 없습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void 로그인_실패_잘못된_비밀번호() {
        // given
        String username = "test";
        String password = "wrongPassword";
        String encodedPassword = "encodedTestPassword";
        User user = new User(username, encodedPassword, "testNickname", UserRole.ROLE_USER);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(false);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.sign(username, password);
        });

        // then
        assertThat("잘못된 비밀번호 입니다.").isEqualTo(exception.getMessage());
    }





}