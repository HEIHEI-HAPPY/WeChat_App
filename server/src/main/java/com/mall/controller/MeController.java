package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.dto.UserDto;
import com.mall.service.MeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 我的
 * GET    /api/me    当前用户信息（要 token）
 * PATCH  /api/me    更新昵称/头像/手机号（要 token）
 */
@RestController
@RequestMapping("/api")
public class MeController {

    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping("/me")
    public ApiResponse<UserDto> me(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(meService.getMe(userId));
    }

    @PatchMapping("/me")
    public ApiResponse<UserDto> updateMe(@RequestBody UserDto patch, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(meService.updateMe(userId, patch));
    }
}