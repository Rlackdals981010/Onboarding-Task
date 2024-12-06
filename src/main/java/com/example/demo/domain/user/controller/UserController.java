package com.example.demo.domain.user.controller;


import com.example.demo.domain.user.dto.request.SignRequestDto;
import com.example.demo.domain.user.dto.request.SignUpRequestDto;
import com.example.demo.domain.user.dto.response.SignResponseDto;
import com.example.demo.domain.user.dto.response.SignUpResponseDto;
import com.example.demo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpRequestDto signUpDto) throws Exception {
        SignUpResponseDto response = userService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sign")
    public ResponseEntity<SignResponseDto> sign(@Valid @RequestBody SignRequestDto signDto) {
        SignResponseDto response = userService.sign(signDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("동작중");
    }
}

