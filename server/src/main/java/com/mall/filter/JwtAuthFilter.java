package com.mall.filter;

import com.mall.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT 鉴权过滤器
 * 见 PROJECT_MAP.md 全局规则：Authorization: Bearer <token>
 *
 * 行为：
 * 1. 放行白名单（/api/wx-login、OPTIONS）
 * 2. 从 header 拿 token，解析失败 → 401
 * 3. 解析成功 → 把 userId / token 写到 request attribute
 * 4. 继续过滤器链
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper json = new ObjectMapper();

    /** 不需要鉴权的路径（白名单）*/
    private static final List<String> WHITELIST = Arrays.asList(
            "/api/wx-login"
    );

    /** 公开访问的前缀（GET 商品、店铺列表等）*/
    private static final List<String> PUBLIC_PREFIXES = Arrays.asList(
            "/api/goods",
            "/api/stores"
    );

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (path.startsWith("/h2-console") || "OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        if (WHITELIST.contains(path)) {
            return true;
        }
        // GET 请求访问公开前缀也放行
        if ("GET".equalsIgnoreCase(method)) {
            for (String prefix : PUBLIC_PREFIXES) {
                if (path.equals(prefix) || path.startsWith(prefix + "/")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            writeUnauthorized(res, "缺少 Authorization 头");
            return;
        }

        String token = header.substring("Bearer ".length()).trim();

        try {
            Claims claims = jwtService.parse(token);
            Long userId = Long.valueOf(claims.getSubject());
            req.setAttribute("userId", userId);
            req.setAttribute("token", token);
        } catch (JwtException e) {
            writeUnauthorized(res, "Token 无效或已过期: " + e.getMessage());
            return;
        }

        chain.doFilter(req, res);
    }

    private void writeUnauthorized(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        ApiResponse<?> body = ApiResponse.error(401, msg);
        res.getWriter().write(json.writeValueAsString(body));
    }
}