package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.request.SignUpRequestDto;
import com.example.demo.domain.user.dto.response.SignResponseDto;
import com.example.demo.domain.user.dto.response.SignUpResponseDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.dto.JwtUtilRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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

    private SignUpRequestDto signupDto;

    @BeforeEach
    void setUp() {
        // SignUpRequestDto 초기화
        signupDto = new SignUpRequestDto();
        ReflectionTestUtils.setField(signupDto, "username", "testUser");
        ReflectionTestUtils.setField(signupDto, "password", "password123");
        ReflectionTestUtils.setField(signupDto, "nickname", "testNickname");
    }

    @Test
    void 회원가입_성공() throws Exception {
        // given
        String encodedPassword = "encodedTestPassword";
        User newUser = new User(
                signupDto.getUsername(),
                encodedPassword,
                signupDto.getNickname(),
                UserRole.ROLE_USER
        );

        given(userRepository.findByUsername(signupDto.getUsername())).willReturn(Optional.empty());
        given(passwordEncoder.encode(signupDto.getPassword())).willReturn(encodedPassword);
        given(userRepository.save(Mockito.any(User.class))).willReturn(newUser);

        // when
        SignUpResponseDto signUp = userService.signUp(
                signupDto.getUsername(),
                signupDto.getPassword(),
                signupDto.getNickname()
        );

        // then
        assertThat(signupDto.getUsername()).isEqualTo(signUp.getUsername());
        assertThat(signupDto.getNickname()).isEqualTo(signUp.getNickname());
        assertThat(1).isEqualTo(signUp.getAuthoritie().size());
        assertThat(UserRole.ROLE_USER.getAuthorityName()).isEqualTo(signUp.getAuthoritie().get(0).getAuthorityName());
    }

    @Test
    void 회원가입_실패_이미가입된유저() {
        // given
        given(userRepository.findByUsername(signupDto.getUsername())).willReturn(Optional.of(new User()));

        // when
        Exception exception = assertThrows(Exception.class, () -> {
            userService.signUp(signupDto.getUsername(), signupDto.getPassword(), signupDto.getNickname());
        });

        // then
        assertThat("이미 가입된 유저입니다.").isEqualTo(exception.getMessage());
    }

    @Test
    void 로그인_성공() {
        // given
        String encodedPassword = "encodedTestPassword";
        User user = new User(
                signupDto.getUsername(),
                encodedPassword,
                signupDto.getNickname(),
                UserRole.ROLE_USER
        );

        JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(
                signupDto.getUsername(),
                signupDto.getNickname()
        );
        String token = "testJwtToken";

        given(userRepository.findByUsername(signupDto.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signupDto.getPassword(), encodedPassword)).willReturn(true);
        given(jwtUtil.createToken(createToken)).willReturn(token);

        // when
        SignResponseDto response = userService.sign(signupDto.getUsername(), signupDto.getPassword());

        // then
        assertThat(response.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void 로그인_실패_존재하지_않는_사용자() {
        // given
        given(userRepository.findByUsername(signupDto.getUsername())).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.sign(signupDto.getUsername(), signupDto.getPassword());
        });

        // then
        assertThat("사용자를 찾을 수 없습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void 로그인_실패_잘못된_비밀번호() {
        // given
        String encodedPassword = "encodedTestPassword";
        User user = new User(
                signupDto.getUsername(),
                encodedPassword,
                signupDto.getNickname(),
                UserRole.ROLE_USER
        );

        given(userRepository.findByUsername(signupDto.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(signupDto.getPassword(), encodedPassword)).willReturn(false);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.sign(signupDto.getUsername(), signupDto.getPassword());
        });

        // then
        assertThat("잘못된 비밀번호 입니다.").isEqualTo(exception.getMessage());
    }
}
