package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return JWTUtil.generateToken(user.getId());
    }

}
