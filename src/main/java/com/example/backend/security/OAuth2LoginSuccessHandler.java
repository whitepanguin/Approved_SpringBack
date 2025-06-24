package com.example.backend.security;

import com.example.backend.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private static final String CLIENT_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // CustomOAuth2UserService에서 저장된 jwtToken 꺼냄
        String jwt = (String) oauthUser.getAttributes().get("jwtToken");

        response.sendRedirect(CLIENT_URL + "?jwtToken=" + jwt);
    }
}
