package com.mall.service;

import com.mall.exception.ApiException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

/**
 * MinIO 对象存储服务
 * 见 PROJECT_MAP.md 第十七章 §17.5
 *
 * bucket 目录约定（project.mdc 全局规则）：
 *   - goods/{yyyy}/{mm}/{uuid}.ext
 *   - avatar/{openid}/{uuid}.ext
 */
@Service
public class MinioService {

    private final MinioClient client;
    private final String bucket;
    private final String publicUrl;

    public MinioService(MinioClient minioClient,
                        @Value("${app.minio.bucket}") String bucket,
                        @Value("${app.minio.public-url}") String publicUrl) {
        this.client = minioClient;
        this.bucket = bucket;
        this.publicUrl = publicUrl;
        ensureBucket();
    }

    /** 启动时确保 bucket 存在，不存在则创建 */
    private void ensureBucket() {
        try {
            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            // 启动期 MinIO 还没起来不要挂掉后端，留个 warn
            System.err.println("[MinioService] bucket 检查失败（MinIO 可能未启动）: " + e.getMessage());
        }
    }

    /** 上传商品图，路径 goods/2026/09/uuid.jpg */
    public String uploadGoodsImage(MultipartFile file) {
        return upload(file, "goods");
    }

    /** 上传用户头像，路径 avatar/{openid}/uuid.jpg */
    public String uploadAvatar(MultipartFile file, String openid) {
        String prefix = "avatar/" + openid;
        return upload(file, prefix);
    }

    private String upload(MultipartFile file, String prefix) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(400, "文件为空");
        }
        String mime = file.getContentType();
        if (mime == null || !mime.startsWith("image/")) {
            throw new ApiException(400, "只允许图片文件");
        }
        long size = file.getSize();
        if (size > 5 * 1024 * 1024) {
            throw new ApiException(400, "图片不能超过 5MB");
        }

        String ext = guessExt(file.getOriginalFilename(), mime);
        LocalDate now = LocalDate.now();
        String key = prefix + "/" + now.getYear() + "/" + String.format("%02d", now.getMonthValue())
                + "/" + UUID.randomUUID() + ext;

        try (InputStream in = file.getInputStream()) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(in, size, -1)
                    .contentType(mime)
                    .build());
        } catch (ErrorResponseException e) {
            throw new ApiException(500, "MinIO 上传失败: " + e.getMessage());
        } catch (Exception e) {
            throw new ApiException(500, "MinIO 上传异常: " + e.getMessage());
        }

        return publicUrl + "/" + key;
    }

    private String guessExt(String original, String mime) {
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot > 0) return original.substring(dot).toLowerCase();
        }
        return switch (mime) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }
}