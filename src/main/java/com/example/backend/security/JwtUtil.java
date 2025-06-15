package com.example.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;


@Component
public class JwtUtil {



    @Value("${app.jwt.expiration}")
    private long expiration;



    // ✅ JWT 생성
    public String generateToken(Object userInfo) {
        return Jwts.builder()
                .setSubject("user-auth")
                .claim("user", userInfo)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith( SignatureAlgorithm.HS256,"secretkey")
                .compact();
    }

    // ✅ JWT 검증
    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey("secretkey")
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public String generateTokenWithClaims(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith( SignatureAlgorithm.HS256,"secretkey")
                .compact();
    }



    // ✅ user 정보 추출
    public Object extractUser(String token) {
        return validateToken(token).getBody().get("user");
    }
}
