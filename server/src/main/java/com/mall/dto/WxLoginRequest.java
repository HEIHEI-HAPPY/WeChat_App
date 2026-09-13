package com.mall.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 微信登录入参：前端 wx.login 拿到的 code
 */
public class WxLoginRequest {

    @NotBlank(message = "code 不能为空")
    private String code;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}