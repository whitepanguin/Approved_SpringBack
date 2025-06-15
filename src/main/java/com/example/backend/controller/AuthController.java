package com.example.backend.controller;

import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/jwt")
    public Map<String, Object> validateJwt(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Map.of("success", false, "message", "토큰이 없습니다.");
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = JWTUtil.validateToken(token);
            String userId = claims.getSubject();

            return Map.of("success", true, "userId", userId);
        } catch (Exception e) {
            return Map.of("success", false, "message", "유효하지 않은 토큰입니다.");
        }
    }



    @PostMapping("/local")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        try {
            String token = authService.login(email, password);
            return Map.of(
                    "loginSuccess", true,
                    "token", token
            );
        } catch (Exception e) {
            return Map.of(
                    "loginSuccess", false,
                    "message", e.getMessage()
            );
        }
    }
}
