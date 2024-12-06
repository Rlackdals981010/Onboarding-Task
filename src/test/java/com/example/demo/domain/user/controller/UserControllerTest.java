package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.request.SignRequestDto;
import com.example.demo.domain.user.dto.request.SignUpRequestDto;
import com.example.demo.domain.user.dto.response.SignResponseDto;
import com.example.demo.domain.user.dto.response.SignUpResponseDto;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private SignUpRequestDto signUpRequest;
    private SignUpResponseDto signUpResponse;

    private SignRequestDto signRequest;
    private SignResponseDto signResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // @Mock과 @InjectMocks 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        signUpRequest = new SignUpRequestDto();
        ReflectionTestUtils.setField(signUpRequest, "username", "testUser");
        ReflectionTestUtils.setField(signUpRequest, "password", "password123");
        ReflectionTestUtils.setField(signUpRequest, "nickname", "testNickname");

        signUpResponse = new SignUpResponseDto(
                "testUser",
                "testNickname",
                List.of(new SignUpResponseDto.AuthorityResponse(UserRole.ROLE_USER.getAuthorityName()))
        );

        signRequest = new SignRequestDto();
        ReflectionTestUtils.setField(signRequest, "username", "testUser");
        ReflectionTestUtils.setField(signRequest, "password", "password123");

        signResponse = new SignResponseDto("testJwtToken");
    }

    @Test
    void 가입성공() throws Exception {
        // given
        when(userService.signUp(signUpRequest))
                .thenReturn(signUpResponse);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        when(userService.sign(signRequest))
                .thenReturn(new SignResponseDto("sample_token"));

        // when & then
        mockMvc.perform(post("/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signRequest)))
                .andExpect(status().isOk());

    }
}
