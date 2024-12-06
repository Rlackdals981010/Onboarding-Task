package com.example.demo.domain.jwt;

import com.example.demo.security.JwtUtil;
import com.example.demo.security.dto.JwtUtilRequest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // 테스트용 secretKey 설정
        String testSecretKey = "testSecretKey12345678901234567890";
        String encodedKey = Base64.getEncoder().encodeToString(testSecretKey.getBytes());

        // Reflection을 사용하여 private 필드에 접근하고 값 설정
        ReflectionTestUtils.setField(jwtUtil, "secretKey", encodedKey);

        // secretKey를 기반으로 key를 초기화
        jwtUtil.init();
    }

    @Test
    void testCreateToken() {
        // Mock 객체 생성
        JwtUtilRequest.CreateToken createToken = Mockito.mock(JwtUtilRequest.CreateToken.class);
        when(createToken.username()).thenReturn("testUser");
        when(createToken.nickname()).thenReturn("testNickname");

        // 토큰 생성
        String token = jwtUtil.createToken(createToken);

        // 토큰이 생성되었는지 확인
        assertThat(token).isNotNull().contains(".");

        // 토큰에서 Claims 추출
        Claims claims = jwtUtil.extractClaims(token);

        // Claims의 내용 검증
        assertThat(claims.getSubject()).isEqualTo("testUser");
        assertThat(claims.get("nickname")).isEqualTo("testNickname");
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}
