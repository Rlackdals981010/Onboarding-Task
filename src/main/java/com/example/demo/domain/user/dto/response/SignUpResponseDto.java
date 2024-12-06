package com.example.demo.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class SignUpResponseDto {

    private String username;
    private String nickname;
    private List<AuthorityResponse> authoritie;


    public SignUpResponseDto(String username, String nickname, List<AuthorityResponse> authoritie) {
        this.username = username;
        this.nickname = nickname;
        this.authoritie = authoritie;
    }

    @Getter
    public static class AuthorityResponse {
        private String authorityName;

        public AuthorityResponse(String authorityName){
            this.authorityName = authorityName;
        }
    }
}
