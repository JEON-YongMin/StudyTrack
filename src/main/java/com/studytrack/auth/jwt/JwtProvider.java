package com.studytrack.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-exp-min}")
    private long accessTokenExpMin;

    public String createAccessToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpMin * 60 * 1000);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

        // 2. Bearer 접두사 제거 및 앞뒤 공백 제거 (.trim() 추가)
        String compactToken = token.trim();
        if (compactToken.startsWith("Bearer ")) {
            compactToken = compactToken.substring(7).trim(); // "Bearer " 이후의 실제 토큰만 추출 후 다시 trim
        }

        Jws<Claims> parsed = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(compactToken);

        return parsed.getBody().getSubject(); // createAccessToken에서 subject에 userId 넣었지
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
