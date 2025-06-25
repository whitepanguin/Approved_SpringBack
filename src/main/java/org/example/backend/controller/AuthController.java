package org.example.backend.controller;

import org.example.backend.security.CustomUserDetails;
import org.example.backend.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/jwt")
    public ResponseEntity<?> verifyJwtToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "인증되지 않은 사용자입니다."));
        }

        // Spring Security에 저장된 유저 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", userDetails.getEmail());
        userData.put("name", userDetails.getName());
        userData.put("userid", userDetails.getUserid());
        userData.put("profile", userDetails.getProfile());
        userData.put("provider", userDetails.getProvider());
        userData.put("phone", userDetails.getPhone());
        userData.put("businessType", userDetails.getBusinessType());
        userData.put("address", userDetails.getAddress());
        userData.put("birthDate", userDetails.getBirthDate());


        return ResponseEntity.ok(Map.of(
                "message", "자동 로그인 성공",
                "user", userData
        ));
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
