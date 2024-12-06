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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
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

    private SignUpRequestDto signUpRequest;
    private SignUpResponseDto signUpResponse;

    private SignRequestDto signRequest;
    private SignResponseDto signResponse;

    @BeforeEach
    void setUp() {
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
        when(userService.signUp(anyString(), anyString(), anyString()))
                .thenReturn(signUpResponse);

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(signUpResponse.getUsername()))
                .andExpect(jsonPath("$.nickname").value(signUpResponse.getNickname()))
                .andExpect(jsonPath("$.authoritie[0].authorityName").value("ROLE_USER"));

    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        when(userService.sign(signRequest.getUsername(), signRequest.getPassword()))
                .thenReturn(signResponse);

        // when & then
        mockMvc.perform(post("/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(signResponse.getToken()));
    }





}
