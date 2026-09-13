package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.dto.WxLoginRequest;
import com.mall.dto.WxLoginResponse;
import com.mall.service.WxLoginService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信登录
 * POST /api/wx-login
 */
@RestController
@RequestMapping("/api")
public class WxLoginController {

    private final WxLoginService wxLoginService;

    public WxLoginController(WxLoginService wxLoginService) {
        this.wxLoginService = wxLoginService;
    }

    @PostMapping("/wx-login")
    public ApiResponse<WxLoginResponse> wxLogin(@Valid @RequestBody WxLoginRequest req) {
        WxLoginResponse data = wxLoginService.login(req.getCode());
        return ApiResponse.ok(data);
    }
}