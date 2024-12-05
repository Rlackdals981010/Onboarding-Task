package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Security 필터 비활성화
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest.Signup signUpRequest;
    private UserResponse.Signup signUpResponse;

    private UserRequest.Sign signRequest;
    private UserResponse.Sign signResponse;

    @BeforeEach
    void setUp() {
        signUpRequest = new UserRequest.Signup("testUser", "password123", "testNickname");
        signUpResponse = new UserResponse.Signup(
                "testUser",
                "testNickname",
                List.of(new UserResponse.AuthorityResponse(UserRole.ROLE_USER.getAuthorityName()))
        );

        signRequest = new UserRequest.Sign("testUser", "password123");
        signResponse = new UserResponse.Sign("testJwtToken");
    }

    @Test
    void 가입성공() throws Exception {
        // given
        when(userService.signUp(anyString(), anyString(), anyString()))
                .thenReturn(signUpResponse);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(signUpResponse.username()))
                .andExpect(jsonPath("$.nickname").value(signUpResponse.nickname()))
                .andExpect(jsonPath("$.authorities[0].authorityName").value("ROLE_USER"));
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        when(userService.sign(signRequest.username(), signRequest.password()))
                .thenReturn(signResponse);

        // when & then
        mockMvc.perform(post("/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(signResponse.token()));
    }





}
