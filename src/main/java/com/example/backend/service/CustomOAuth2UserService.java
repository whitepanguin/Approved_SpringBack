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

    @SuppressWarnings("unchecked")
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao 등
        String email = null;
        String name = null;
        String profile = null;

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profileMap = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            name = (String) profileMap.get("nickname");
            profile = (String) profileMap.get("profile_image_url");
        } else if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            profile = (String) attributes.get("picture");
        }else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            email = (String) response.get("email");
            name = (String) response.get("name");
            profile = (String) response.get("profile_image");
        }


        // 유저 DTO 만들기
        OAuthUser user = new OAuthUser(email, name, profile, registrationId);

        // MongoDB 저장 (이미 존재하는 경우 생략)
        User entity = userRepository.findByEmail(email).orElse(null);

        if (entity == null) {
            entity = new User(email, name, profile, registrationId, user.getUserid());
            entity.setPhone("");
            entity.setBusinessType("");
            entity.setAddress("");
            entity.setBirthDate("");
            userRepository.save(entity);
        }



        // JWT 생성: 전체 정보 포함
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

        Map<String, Object> originalAttributes = oAuth2User.getAttributes();
        Map<String, Object> mutableAttributes = new HashMap<>(originalAttributes);
        mutableAttributes.put("jwtToken", token);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                mutableAttributes,
                "email"
        );
    }

}
