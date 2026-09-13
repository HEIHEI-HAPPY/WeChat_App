package com.mall.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT 服务
 * 对照 PROJECT_MAP.md 第五章鉴权
 * 对照 Spring Security：手写的 JwtService，约等于 Spring Security 的 JwtAuthenticationFilter
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expireHours;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expire-hours}") long expireHours) {
        // JJWT 0.12+ 要求密钥至少 256 bit（32 字节）
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireHours = expireHours;
    }

    /** 签发 token，payload 放 userId */
    public String generate(Long userId, String openid) {
        Instant now = Instant.now();
        Instant exp = now.plus(expireHours, ChronoUnit.HOURS);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("openid", openid)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    /** 解析 token；失败抛运行时异常，由 GlobalExceptionHandler 兜底 */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        return Long.valueOf(parse(token).getSubject());
    }

    public String getOpenid(String token) {
        return parse(token).get("openid", String.class);
    }
}