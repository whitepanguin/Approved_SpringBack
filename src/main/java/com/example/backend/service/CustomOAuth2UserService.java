package com.example.backend.service;

import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import com.example.backend.util.JWTUtil;
import com.example.backend.DTO.OAuthUser;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.stereotype.Service;
import com.example.backend.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CustomOAuth2UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao 등
        String email = null;
        String name = null;
        String profile = null;
        String keyName = "email"; // 기본 키는 email

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profileMap = (Map<String, Object>) kakaoAccount.get("profile");

            Object emailObj = kakaoAccount.get("email");
            email = (emailObj != null) ? emailObj.toString() : "anonymous_" + attributes.get("id") + "@kakao.com";

            name = (String) profileMap.get("nickname");
            profile = (String) profileMap.get("profile_image_url");

            keyName = "id"; // 카카오는 email이 없을 수 있으므로 id를 사용

        } else if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            profile = (String) attributes.get("picture");
            keyName = "email"; // google은 email이 안전하게 존재

        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            email = (String) response.get("email");
            name = (String) response.get("name");
            profile = (String) response.get("profile_image");
            keyName = "email"; // naver도 email 존재
        }

        // 유저 DTO 만들기
        OAuthUser user = new OAuthUser(email, name, profile, registrationId);

        // MongoDB 저장
        User entity = userRepository.findByEmail(email).orElse(null);
        if (entity == null) {
            entity = new User(email, name, profile, registrationId, user.getUserid());
            entity.setPhone("");
            entity.setBusinessType("");
            entity.setAddress("");
            entity.setBirthDate("");
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            userRepository.save(entity);
        }

        // JWT 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("profile", user.getProfile());
        claims.put("provider", user.getProvider());
        claims.put("userid", user.getUserid());
        claims.put("phone", entity.getPhone());
        claims.put("businessType", entity.getBusinessType());
        claims.put("address", entity.getAddress());
        claims.put("birthDate", entity.getBirthDate());

        String token = jwtUtil.generateTokenWithClaims(claims);

        Map<String, Object> mutableAttributes = new HashMap<>(attributes);
        mutableAttributes.put("jwtToken", token);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                mutableAttributes,
                keyName // ✅ 제공자에 따라 식별 키 설정
        );
    }catch (Exception e){
            System.out.println("❗ OAuth2 로그인 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw new OAuth2AuthenticationException(new OAuth2Error("server_error"), e.getMessage());
        }
    }
}
