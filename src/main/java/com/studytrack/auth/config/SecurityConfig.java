package com.studytrack.auth.config;

import com.studytrack.auth.jwt.JwtAuthFilter;
import com.studytrack.auth.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                .authorizeHttpRequests(auth -> auth

                        // 회원가입/로그인은 토큰 없이 허용
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()

                        // Swagger UI 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // H2 콘솔 허용
                        .requestMatchers("/h2-console/**").permitAll()

                        // 홈 화면 허용
                        .requestMatchers("/", "/dashboard", "/study/**", "/auth/**").permitAll()

                        // 로그인/회원가입 화면 허용
                        .requestMatchers("/auth/**").permitAll()

                        // 나머지는 전부 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터 등록
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
