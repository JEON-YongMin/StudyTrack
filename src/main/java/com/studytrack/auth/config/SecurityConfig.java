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

                        // API 허용
                        .requestMatchers("/api/auth/**", "/api/user/**").permitAll()

                        // 공통 화면 및 정적 리소스 허용
                        .requestMatchers("/", "/dashboard", "/study/**", "/auth/**").permitAll()

                        // 유저 관련 화면 허용
                        .requestMatchers("/user/**").permitAll()

                        // Swagger UI 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // H2 콘솔 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        // CSS, JS, 이미지 등 정적 파일 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // 나머지는 전부 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터 등록
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
