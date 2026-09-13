# WeChat Mall - 微信小程序商城

一个完整的微信小程序商城系统，包含微信小程序前端和 Spring Boot 后端。

## 📱 项目截图

<!-- 可以在这里添加项目截图 -->

## 🏗️ 项目结构

```
mall/
├── wechat/                 # 微信小程序前端
│   ├── pages/             # 页面
│   │   ├── home/         # 首页
│   │   ├── goods/        # 商品详情
│   │   ├── cart/         # 购物车
│   │   ├── order/        # 订单
│   │   ├── address/      # 收货地址
│   │   └── me/           # 个人中心
│   ├── components/       # 自定义组件
│   ├── utils/            # 工具函数
│   └── images/           # 图片资源
│
├── server/                 # Spring Boot 后端
│   ├── src/
│   │   └── main/
│   │       ├── java/com/mall/
│   │       │   ├── controller/   # 控制器
│   │       │   ├── service/      # 业务逻辑
│   │       │   ├── repository/   # 数据访问
│   │       │   ├── entity/       # 实体类
│   │       │   ├── dto/          # 数据传输对象
│   │       │   └── config/       # 配置类
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
│
└── docs/                   # 项目文档
```

## ✨ 功能特性

### 前端功能
- 🏠 首页商品展示
- 📦 商品详情浏览
- 🛒 购物车管理
- 📋 订单创建与管理
- 📍 收货地址管理
- 👤 个人中心

### 后端功能
- 🔐 微信小程序登录
- 🎫 JWT 认证授权
- 📦 商品管理 API
- 🛒 购物车 API
- 📋 订单管理 API
- 📍 地址管理 API
- 📁 文件上传 (MinIO)

## 🛠️ 技术栈

### 前端
- 微信小程序原生框架
- WXSS 样式
- JavaScript ES6+

### 后端
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- MinIO (对象存储)
- Redis (缓存)
- JWT (认证)

## 🚀 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- PostgreSQL 14+
- MinIO
- Redis
- 微信开发者工具

### 后端启动

1. **配置环境变量**
   ```bash
   cd server
   cp .env.example .env
   # 编辑 .env 文件，填入你的配置
   ```

2. **启动数据库和服务**
   ```bash
   # 确保 PostgreSQL、MinIO、Redis 已启动
   ```

3. **运行后端**
   ```bash
   cd server
   mvn spring-boot:run
   ```
   或者使用提供的脚本：
   ```bash
   ./start-backend.bat
   ```

### 前端启动

1. **使用微信开发者工具打开项目**
   - 打开微信开发者工具
   - 导入 `wechat/` 目录
   - 配置你的 AppID

2. **配置后端地址**
   - 编辑 `wechat/utils/request.js`
   - 修改 `BASE_URL` 为你的后端地址

## 📝 API 文档

### 认证相关
- `POST /api/wx/login` - 微信小程序登录

### 商品相关
- `GET /api/goods` - 获取商品列表
- `GET /api/goods/{id}` - 获取商品详情

### 购物车相关
- `GET /api/cart` - 获取购物车
- `POST /api/cart` - 添加到购物车
- `PUT /api/cart/{id}` - 更新购物车
- `DELETE /api/cart/{id}` - 删除购物车项

### 订单相关
- `POST /api/orders` - 创建订单
- `GET /api/orders` - 获取订单列表
- `GET /api/orders/{id}` - 获取订单详情

### 地址相关
- `GET /api/addresses` - 获取地址列表
- `POST /api/addresses` - 创建地址
- `PUT /api/addresses/{id}` - 更新地址
- `DELETE /api/addresses/{id}` - 删除地址

## 🔧 配置说明

### 环境变量

| 变量名 | 说明 | 示例 |
|--------|------|------|
| `WX_APPID` | 微信小程序 AppID | `wxf989773af922395e` |
| `WX_SECRET` | 微信小程序 Secret | `your_secret` |
| `JWT_SECRET` | JWT 签名密钥 | `your_jwt_secret` |
| `MINIO_ENDPOINT` | MinIO 地址 | `http://localhost:9000` |
| `MINIO_ACCESS_KEY` | MinIO Access Key | `minioadmin` |
| `MINIO_SECRET_KEY` | MinIO Secret Key | `minioadmin` |

## 📦 部署

### Docker 部署 (推荐)

```bash
# 构建后端
cd server
mvn clean package

# 使用 Docker Compose 部署
docker-compose up -d
```

### 传统部署

1. 构建后端 JAR 包
2. 配置 Nginx 反向代理
3. 部署 PostgreSQL、MinIO、Redis
4. 运行后端服务

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 👨‍💻 作者

- **HEIHEI-HAPPY** - [GitHub](https://github.com/HEIHEI-HAPPY)

## 🙏 致谢

- 微信小程序开发文档
- Spring Boot 官方文档
- 所有贡献者
