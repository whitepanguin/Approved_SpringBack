package org.example.backend.service;

import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(user); // ✅ Bean에서 안전한 키로 생성됨
    }
}
