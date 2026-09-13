package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.exception.ApiException;
import com.mall.service.JwtService;
import com.mall.service.MinioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传
 * POST /api/upload  字段名 file，图片，最大 5MB
 */
@RestController
@RequestMapping("/api")
public class UploadController {

    private final MinioService minioService;
    private final JwtService jwtService;

    public UploadController(MinioService minioService, JwtService jwtService) {
        this.minioService = minioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestParam MultipartFile file,
                                                   @RequestParam(defaultValue = "goods") String type,
                                                   HttpServletRequest req) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(400, "请选择文件");
        }

        String url;
        if ("avatar".equals(type)) {
            String token = (String) req.getAttribute("token");
            String openid = jwtService.getOpenid(token);
            url = minioService.uploadAvatar(file, openid);
        } else {
            url = minioService.uploadGoodsImage(file);
        }

        return ApiResponse.ok(Map.of("url", url));
    }
}