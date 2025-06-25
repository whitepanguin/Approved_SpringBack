package org.example.backend.security;

import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();


        Set<String> openEndpoints = Set.of("/category-counts", "/public", "/health");

        if (openEndpoints.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }


        System.out.println(">> JwtAuthenticationFilter: 시작");
        String authHeader = request.getHeader("Authorization");
        System.out.println(">> Authorization 헤더: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            System.out.println(">> JWT 추출: " + jwt);

            try {
                Claims claims = jwtUtil.validateToken(jwt).getBody();
                System.out.println(">> Claims 추출: " + claims);

                String email;
                Object userObj = claims.get("user");
                if (userObj instanceof Map) {
                    email = (String) ((Map<?, ?>) userObj).get("email");
                } else {
                    email = (String) claims.get("email");
                }

                System.out.println(">> 이메일로 사용자 조회: " + email);

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                CustomUserDetails userDetails = new CustomUserDetails(
                        user.getEmail(),
                        user.getName(),
                        user.getUserid(),
                        user.getProvider(),
                        user.getProfile(),
                        user.getPhone(),
                        user.getBusinessType(),
                        user.getAddress(),
                        user.getBirthDate()

                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                System.out.println(">> 인증 객체 저장: " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }  catch (JwtException e) {
                System.out.println(">> JWT 예외 발생: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (RuntimeException e) {
                System.out.println(">> 런타임 예외 발생: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}