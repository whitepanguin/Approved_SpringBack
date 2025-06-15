package com.example.backend.controller;

import jakarta.servlet.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;


@RestController
public class OAuthController {

    private static final String CLIENT_URL = "http://localhost:3000";

    @GetMapping("/auth/google/callback")
    public void googleCallback(@AuthenticationPrincipal OAuth2User oauthUser,
                               HttpServletResponse response) throws Exception {

        String jwt = (String) oauthUser.getAttributes().get("jwtToken");
        response.sendRedirect(CLIENT_URL + "?jwtToken=" + jwt);
    }

    @GetMapping("/auth/kakao/callback")
    public void kakaoCallback(@AuthenticationPrincipal OAuth2User oauthUser,
                              HttpServletResponse response) throws IOException {
        String jwt = (String) oauthUser.getAttributes().get("jwtToken");
        response.sendRedirect(CLIENT_URL + "?jwtToken=" + jwt);
    }

    @GetMapping("/auth/naver/callback")
    public void naverCallback(@AuthenticationPrincipal OAuth2User oauthUser,
                              HttpServletResponse response) throws IOException {
        String jwt = (String) oauthUser.getAttributes().get("jwtToken");
        response.sendRedirect("http://localhost:3000?jwtToken=" + jwt);
    }

}
