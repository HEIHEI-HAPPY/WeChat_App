# mall-server · Coffee·咖点后端

Spring Boot 3.3 + JPA + MinIO + JWT

## 启动

```bash
# 1. 准备 .env（按 .env.example 填值）
cp .env.example .env
notepad .env   # Windows

# 2. 起 MinIO（已有 Docker）
docker run -d --name minio \
  -p 9000:9000 -p 9001:9001 \
  -v D:/minio-data:/data \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  minio/minio server /data --console-address ":9001"

# 3. 跑后端
mvn spring-boot:run
```

启动后访问：
- API: http://localhost:3000/api/goods
- H2 控制台: http://localhost:3000/h2-console
- MinIO 控制台: http://localhost:9001

## 接口

详见 `../docs/PROJECT_MAP.md` 第四章（30 个接口）

## 端口

| 服务 | 端口 |
|---|---|
| mall-server | 3000 |
| MinIO API | 9000 |
| MinIO Console | 9001 |

## 切换数据库

- dev: H2 内存（自动建表）
- prod: MySQL 8 / PostgreSQL 16（改 `application-prod.yml` + 设 `SPRING_PROFILES_ACTIVE=prod`）