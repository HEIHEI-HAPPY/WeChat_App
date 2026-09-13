package com.mall.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 调微信 jscode2session，换 openid + session_key
 * 对照 PROJECT_MAP.md 全局规则
 */
@Service
public class WeChatService {

    private final String appid;
    private final String secret;
    private final RestClient http = RestClient.create();
    private final ObjectMapper json = new ObjectMapper();

    public WeChatService(@Value("${app.wechat.appid}") String appid,
                         @Value("${app.wechat.secret}") String secret) {
        this.appid = appid;
        this.secret = secret;
    }

    /** 用 code 换 openid；失败抛 ApiException */
    public String code2openid(String code) {
        if ("PLACEHOLDER_APPID".equals(appid) || "PLACEHOLDER_SECRET".equals(secret)) {
            throw new ApiException(500, "未配置 WX_APPID / WX_SECRET，请编辑 .env");
        }

        String url = "https://api.weixin.qq.com/sns/jscode2session"
                + "?appid=" + URLEncoder.encode(appid, StandardCharsets.UTF_8)
                + "&secret=" + URLEncoder.encode(secret, StandardCharsets.UTF_8)
                + "&js_code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&grant_type=authorization_code";

        try {
            String body = http.get().uri(url).retrieve().body(String.class);
            JsonNode node = json.readTree(body);
            String errcode = node.path("errcode").asText("0");
            if (!"0".equals(errcode)) {
                String errmsg = node.path("errmsg").asText("unknown");
                throw new ApiException(500, "jscode2session 失败: " + errcode + " " + errmsg);
            }
            String openid = node.path("openid").asText(null);
            if (openid == null || openid.isEmpty()) {
                throw new ApiException(500, "jscode2session 未返回 openid");
            }
            return openid;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "调用微信接口异常: " + e.getMessage());
        }
    }
}