# Coffee·咖点 完整项目地图

> V1.2 · 2026-09-10 · 含目录、接口、数据库、微信上传、技术决策、AI 协作、FAQ
> 单一后端（Java Spring Boot）方案

---

## 目录

- [一、项目一句话](#一项目一句话)
- [二、最终完整目录结构](#二最终完整目录结构)
- [三、关键设计原则](#三关键设计原则)
- [四、接口清单（30 个）](#四接口清单30-个)
- [五、数据库 Schema（10 张表）](#五数据库-schema10-张表)
- [六、关键技术决策](#六关键技术决策)
- [七、部署架构](#七部署架构)
- [八、微信上传流程](#八微信上传流程)
- [九、开发 → 上线 Checklist](#九开发--上线-checklist)
- [十、当前状态 & 变更日志](#十当前状态--变更日志)
- [十一、命名规范](#十一命名规范)
- [十二、Java 知识对照](#十二java-知识对照)
- [十三、问题地图](#十三问题地图)
- [★ 十四、数据库选型详解](#十四数据库选型详解)
- [★ 十五、AI 协作 Prompt 模板](#十五ai-协作-prompt-模板)
- [★ 十六、FAQ（你可能会问的）](#十六faq你可能会问的)
- [★ 十七、MinIO 对象存储](#十七minio-对象存储)

---

## 一、项目一句话

**面向 C 端的咖啡点餐微信小程序**，覆盖到店自取 + 外卖双场景，提供个性化定制、会员体系、营销活动。

**技术架构一句话**：小程序（原生） + 后台（Vue3，可选）+ 后端（Java Spring Boot 单体）+ MySQL/SQLite/PG/H2（任选）。

---

## 二、最终完整目录结构

```
mall/
│
├── wechat/                              # 【前端 1】微信小程序（顾客端）
│   ├── app.js
│   ├── app.json
│   ├── app.wxss
│   ├── project.config.json
│   ├── project.private.config.json
│   ├── sitemap.json
│   │
│   ├── components/                       # 自研组件
│   │   ├── goods-card/
│   │   ├── empty/
│   │   ├── loading/
│   │   └── status-bar/
│   │
│   ├── pages/                            # 21 个页面
│   │   ├── home/index/                   # ① 首页（Tab）
│   │   ├── menu/index/                   # ② 菜单（Tab）
│   │   ├── order/
│   │   │   ├── list/index/               # ③ 订单列表（Tab）
│   │   │   ├── confirm/index/            # ④ 订单确认
│   │   │   └── detail/index/             # ⑤ 订单详情
│   │   ├── me/index/                     # ⑥ 我的（Tab）
│   │   ├── goods/detail/index/           # ⑦ 商品详情
│   │   ├── cart/index/                   # ⑧ 购物车
│   │   ├── login/index/                  # ⑨ 登录
│   │   ├── member/index/                 # ⑩ 会员中心
│   │   ├── coupon/{list,center}/index/   # ⑪⑫
│   │   ├── address/{list,edit}/index/    # ⑬⑭
│   │   ├── store/{list,detail}/index/    # ⑮⑯
│   │   ├── refund/apply/index/           # ⑰
│   │   ├── evaluate/index/               # ⑱
│   │   ├── kefu/index/                   # ⑲
│   │   ├── about/index/                  # ⑳
│   │   └── settings/index/               # ㉑
│   │
│   ├── utils/
│   │   ├── request.js                    # 封装 wx.request
│   │   ├── auth.js
│   │   ├── format.js
│   │   └── api.js
│   │
│   ├── images/
│   │   ├── goods/
│   │   ├── icons/                        # Tab Bar 4 图标
│   │   ├── banners/
│   │   └── common/
│   │
│   └── miniprogram_npm/                  # 自动生成
│
├── server/                               # 【后端】Java Spring Boot
│   ├── pom.xml
│   ├── .env.example
│   ├── .gitignore
│   ├── README.md
│   ├── data/                             # H2 文件或 MySQL dump
│   │
│   └── src/main/
│       ├── java/com/mall/
│       │   ├── MallApplication.java
│       │   ├── DataSeeder.java
│       │   │
│       │   ├── config/
│       │   │   ├── CorsConfig.java
│       │   │   └── FilterConfig.java
│       │   │
│       │   ├── controller/
│       │   │   ├── WxLoginController.java
│       │   │   ├── GoodsController.java
│       │   │   ├── MeController.java
│       │   │   ├── CartController.java   # [待]
│       │   │   ├── OrdersController.java # [待]
│       │   │   ├── WxPayController.java  # [待]
│       │   │   ├── CouponsController.java
│       │   │   ├── MemberController.java
│       │   │   ├── AddressController.java
│       │   │   └── StoreController.java
│       │   │
│       │   ├── service/
│       │   │   ├── JwtService.java
│       │   │   ├── WeChatService.java
│       │   │   ├── WxLoginService.java
│       │   │   ├── GoodsService.java
│       │   │   └── ...
│       │   │
│       │   ├── repository/
│       │   │   ├── WxUserRepository.java
│       │   │   ├── GoodsRepository.java
│       │   │   └── ...
│       │   │
│       │   ├── entity/
│       │   │   ├── WxUser.java
│       │   │   ├── Goods.java
│       │   │   └── ...
│       │   │
│       │   ├── dto/
│       │   │   ├── ApiResponse.java
│       │   │   ├── WxLoginRequest.java
│       │   │   ├── WxLoginResponse.java
│       │   │   └── UserDto.java
│       │   │
│       │   ├── filter/
│       │   │   └── JwtAuthFilter.java
│       │   │
│       │   └── exception/
│       │       ├── ApiException.java
│       │       └── GlobalExceptionHandler.java
│       │
│       └── resources/
│           └── application.yml
│
├── admin/                                # 【前端 2】后台（Vue3 + Element Plus）
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── index.html
│   └── src/
│       ├── main.ts
│       ├── App.vue
│       ├── router/index.ts
│       ├── stores/
│       ├── api/
│       ├── views/
│       ├── layouts/
│       └── styles/
│
├── docs/                                 # 文档
│   ├── ARCHITECTURE.md
│   ├── SPEC.md
│   ├── SPEC_SIMPLE.md
│   └── PROJECT_MAP.md                    # 你正在看的这个
│
├── scripts/                              # 运维脚本
│   ├── deploy.sh
│   ├── backup-db.sh
│   ├── mall-server.service               # systemd 守护
│   └── nginx.conf
│
├── .gitignore                            # 根 gitignore
└── README.md
```

---

## 三、关键设计原则

### 1. 三层各管一段

| 端 | 谁用 | 关注点 |
|---|---|---|
| `wechat/` | 顾客 | 快速、好看、3 步下单 |
| `admin/` | 店长 | 数据密集、批量操作 |
| `server/` | 两个前端 | 业务、数据库、鉴权、支付 |

**后端只此一份**——两个前端共享同一套 API、同一份数据。

### 2. 后端分层（标准 Spring Boot）

```
controller/   →  接 HTTP 请求
service/      →  业务逻辑 + 事务边界
repository/   →  数据库（JPA 自动）
entity/       →  表映射
dto/          →  入参/出参（不暴露 entity 内部）
filter/       →  鉴权横切
exception/    →  全局异常
config/       →  CORS / Filter 注册
```

### 3. 小程序分层

```
pages/       →  21 个页面
components/  →  可复用 UI
utils/       →  request / auth / format
images/      →  静态资源
```

---

## 四、接口清单（30 个）

> 鉴权：`Authorization: Bearer <token>`
> 返回：`{code:0, message, data}` / 错误 `{code:4xxx}` / 未授权 `401`

| 模块 | Method | Path | 说明 |
|---|---|---|---|
| **登录** | POST | /api/wx-login | 微信登录换 openid |
| **商品** | GET | /api/goods | 列表（分页+搜索） |
| | GET | /api/goods/:id | 详情 |
| **购物车** | GET | /api/cart | 当前购物车 |
| | POST | /api/cart | 加车 |
| | PATCH | /api/cart/:id | 改数量 |
| | DELETE | /api/cart/:id | 删除 |
| **订单** | POST | /api/orders | 创建 |
| | GET | /api/orders | 列表 |
| | GET | /api/orders/:id | 详情 |
| | POST | /api/orders/:id/cancel | 取消 |
| | POST | /api/orders/:id/refund | 申请退款 |
| | POST | /api/orders/:id/pay | 调起支付 |
| **支付** | POST | /api/wx-pay/notify | 微信回调 |
| | POST | /api/wx-pay/refund | 退款 |
| **优惠券** | GET | /api/coupons | 我的券 |
| | GET | /api/coupons/center | 领券中心 |
| | POST | /api/coupons/:id/receive | 领券 |
| **用户** | GET | /api/me | 我的信息 |
| | PATCH | /api/me | 更新 |
| **会员** | GET | /api/member | 等级/成长值 |
| | POST | /api/member/sign | 每日签到 |
| **地址** | GET/POST | /api/addresses | 列表/新增 |
| | PATCH/DELETE | /api/addresses/:id | 修改/删除 |
| **门店** | GET | /api/stores | 附近门店 |
| | GET | /api/stores/:id | 详情 |
| **Admin** | POST | /api/admin/login | admin 登录 |
| | GET | /api/admin/stats | 看板数据 |
| | CRUD | /api/admin/goods | admin 商品 |
| | CRUD | /api/admin/orders | admin 订单 |

---

## 五、数据库 Schema（10 张表）

```sql
wx_users        (id, openid UQ, nickname, avatar_url, phone, level, points, growth, created_at, updated_at)
goods           (id, name, category, base_price, image_url, description, specs JSON, addons JSON, stock, sell_time JSON, status)
cart_items      (id, user_id, goods_id, sku JSON, quantity, store_id, created_at)
orders          (id, order_no UQ, user_id, total, status, pickup_type, store_id, address_id, coupon_id, paid_at, transaction_id, remark, created_at)
order_items     (id, order_id, goods_id, price_snapshot, quantity, sku_snapshot JSON)
coupon_templates(id, name, type, amount, threshold, valid_days, status)
user_coupons    (id, user_id, template_id, status, received_at, used_at, valid_from, valid_to)
addresses       (id, user_id, name, phone, province, city, district, detail, is_default)
stores          (id, name, address, lat, lng, business_hours JSON, status)
admin_users     (id, username UQ, password_hash, role, created_at, last_login_at)
```

**索引**：`wx_users(openid)` / `orders(user_id, created_at)` / `orders(order_no)` / `order_items(order_id)`

---

## 六、关键技术决策（带理由）

| 维度 | 选型 | 理由 |
|---|---|---|
| 小程序框架 | **原生** | 你在学，原生最扎实；Taro/uni-app 是后期锦上添花 |
| 小程序 UI | **WeUI** | 微信官方设计语言、风格一致；`npm i weui-wxss` 一行安装 |
| 后端语言 | **Java 17** | 你熟；招聘市场广；类型安全；和 admin TS 统一心智 |
| 后端框架 | **Spring Boot 3.3** | 主流；JPA 免手写 SQL；生态完整 |
| ORM | **Spring Data JPA** | 一行 Repository 接口拿 80% CRUD；后期可换 MyBatis-Plus |
| 开发库 | **H2 内存** | 零安装、随项目走；切 MySQL 改 1 个 URL |
| 生产库 | **MySQL 8** 或 **PostgreSQL 16** | 主流；运维简单；PG 略优（JSONB） |
| 鉴权 | **JWT (jjwt 0.12)** | 无状态、跨端；不用共享 session |
| 支付 | **微信支付 V3** | V2 已弱、V3 安全（AES-256-GCM） |
| 后台 | **Vue3 + Element Plus + TS + Vite + Pinia** | 你会 Vue、TS 主流、Element Plus 是 Element UI 的 Vue3 版 |
| 部署 | **阿里云轻量 + Nginx + systemd** | Spring Boot jar 跑就行，不用 PM2 |
| **对象存储** | **MinIO (Docker / 自建)** | 商品图、头像；S3 兼容免锁厂；学完 = 掌握 AWS S3 / 腾讯 COS / 阿里 OSS |

---

## 七、部署架构

```
   ┌────────────────┐
   │  微信用户       │
   │  (小程序客户端)  │
   └────────┬───────┘
            │ HTTPS
            ▼
   ┌────────────────────────────────┐
   │  阿里云轻量应用服务器           │
   │  ──────────────────────────  │
   │  Nginx (反向代理 + HTTPS)     │
   │       │                        │
   │       ├─→ /api/*  → Java jar  │
   │       │            (systemd)   │
   │       │                │        │
   │       │                ├─→ MySQL 8
   │       │                │
   │       │                └─→ 微信支付 API
   │       │
   │       └─→ /admin/*  → 静态资源 (Vue3 打包)
   └────────────────────────────────┘
```

**关键文件**：
- `scripts/nginx.conf` — Nginx 配置
- `scripts/mall-server.service` — systemd 守护
- `pom.xml` — 打包成 `target/mall-server-1.0.0.jar`

---

## 八、微信上传流程

### 步骤 1：准备
| 条件 | 说明 |
|---|---|
| 正式 AppID | mp.weixin.qq.com 注册（5 分钟） |
| ICP 备案域名 | 7-20 天 ⚠️ 最慢 |
| HTTPS 证书 | Let's Encrypt 免费 |
| 后端跑在公网 | 阿里云轻量 |
| 类目资质 | 商城需企业营业执照 |

### 步骤 2：上传代码
```
微信开发者工具 → 右上「上传」→ 填版本号 1.0.0 → 上传成功
```

### 步骤 3：提审
```
mp.weixin.qq.com → 版本管理 → 提交审核
  → 类目：餐饮-饮品
  → 5 张截图
  → 等待 1-3 天
  → 审核通过 → 发布
```

---

## 九、开发 → 上线 Checklist

### 阶段 1：后端骨架 ✅
- [x] pom.xml + 依赖
- [x] MallApplication 入口
- [x] WxUser / Goods 实体
- [x] JwtService / WeChatService
- [x] 3 个核心 Controller
- [x] JwtAuthFilter
- [x] DataSeeder 5 条咖啡
- [x] 编译通过

### 阶段 2：脚手架
- [ ] wechat/app.json 注册 21 个页面
- [ ] wechat/utils/request.js 公共请求
- [ ] wechat/components/ 几个通用组件
- [ ] wechat/ 装 weui-wxss

### 阶段 3：核心页面
- [ ] 首页 / 菜单 / 商品详情
- [ ] 购物车 / 订单确认 / 订单列表 / 订单详情
- [ ] 我的 / 登录

### 阶段 4：业务接口
- [ ] cart 接口 CRUD
- [ ] orders 接口 + 状态机
- [ ] 优惠券 / 会员 / 地址

### 阶段 5：支付
- [ ] 申请商户号
- [ ] 统一下单接口
- [ ] 微信回调 + 验签
- [ ] 退款接口

### 阶段 6：体验优化
- [ ] 骨架屏
- [ ] 加购动画
- [ ] 错误/空/网络异常统一处理

### 阶段 7：后台（可选）
- [ ] Vite + Vue3 + Element Plus
- [ ] 登录 / 商品管理 / 订单查询

### 阶段 8：上线
- [ ] 备案（最先做）
- [ ] HTTPS 证书
- [ ] 服务器部署 jar
- [ ] 小程序提审

---

## 十、当前状态 & 变更日志

### 当前状态

| 端 | 进度 | 状态 |
|---|---|---|
| `wechat/` | 🟡 10% | 仅 1 个 index 骨架 |
| `server/` (Java) | 🟢 60% | 3 个接口跑通，5 条咖啡 seed |
| `admin/` | 🔴 0% | 空目录（未建） |
| `docs/` | 🟢 100% | 4 份文档齐全 |
| `project.mdc` | 🟢 已更新 | Java 后端规则写入 |

**已完成**：
- ✅ 后端 Java 骨架（编译通过）
- ✅ 3 个核心接口：wx-login、goods、me
- ✅ JWT 鉴权 + CORS
- ✅ DataSeeder 5 条咖啡
- ✅ project.mdc 更新

**待办（按优先级）**：
1. 把 `server-java/src` 移到 `server/src`
2. 删 `server-java/` 整个目录
3. 删 `wechat/cloudfunctions/` 残留
4. 加根 `.gitignore` + `README.md`
5. 跑通 `mvn package` + `java -jar` 验证可启动
6. 写小程序端 21 个页面

### 变更日志

| 版本 | 日期 | 改动 |
|---|---|---|
| V1.0 | 2026-09-09 | 初始版本，含双后端（Node + Java） |
| V1.1 | 2026-09-10 | 切换为 Java 单后端；移除 Node 引用；添加部署架构图 |
| V1.2 | 2026-09-10 | 新增第 14/15/16 章（数据库选型、AI 协作、FAQ）；扩展技术决策理由；加变更日志 |

---

## 十一、命名规范

| 类型 | 规则 | 示例 |
|---|---|---|
| Java 类 | PascalCase | `WxLoginService` |
| 包名 | 全小写 | `com.mall.controller` |
| 常量 | UPPER_SNAKE | `MAX_RETRY_COUNT` |
| 资源文件 | kebab-case | `application-prod.yml` |
| 小程序页面路径 | kebab-case | `pages/order/list/` |
| 小程序文件 | 小写 | `order-list.js` |
| 数据库表 | snake_case | `wx_users` |
| 字段名 | snake_case | `avatar_url` |
| 接口路径 | kebab-case | `/api/wx-login` |
| 环境变量 | UPPER_SNAKE | `WX_APPID` |

---

## 十二、Java 知识对照（你的 Spring Boot 经验直接套）

| 概念 | 你会的 | 本项目 |
|---|---|---|
| 包 = 分类文件夹 | `package com.mall.controller` | `controller/` |
| 类 = 一个功能 | `class WxLoginController` | `WxLoginController.java` |
| Bean = 单例 | `@Service` | Spring 自动管理 |
| 依赖注入 | `@Autowired` | 构造器（手写） |
| Controller 注解 | `@RestController` | 等价于 @Controller + @ResponseBody |
| 路由 | `@RequestMapping("/api")` | 类级 + 方法级 |
| HTTP 方法 | `@PostMapping` / `@GetMapping` | 直接对应 |
| 路径变量 | `@PathVariable Long id` | `/api/goods/{id}` |
| 请求体 | `@RequestBody WxLoginRequest req` | JSON 自动反序列化 |
| 响应 | return `ApiResponse.ok(data)` | 自动序列化为 JSON |
| 中间件 | `OncePerRequestFilter` | `JwtAuthFilter` |
| 全局异常 | `@RestControllerAdvice` | `GlobalExceptionHandler` |
| 事务 | `@Transactional` | 加在 Service 方法上 |
| 配置 | `application.yml` | Spring Boot 标准 |

---

## 十三、问题地图

| 你想问 | 看哪 |
|---|---|
| 不知道怎么开始？ | 第九章 Checklist，按阶段勾 |
| 不知道前端怎么写？ | SPEC_SIMPLE 第四章布局 + 找 AI 出代码 |
| 不知道后端怎么写？ | ARCHITECTURE 第五章鉴权 + 看 server/ 的 3 个 Controller |
| 不知道接口长啥样？ | 本文件第四章接口清单 |
| 不知道数据库怎么设计？ | 本文件第五章 Schema |
| 不知道如何上线？ | 本文件第八章 + ARCHITECTURE 第十章 |
| 不知道如何让 AI 帮我写？ | **第十五章 AI 协作 Prompt** |
| 不知道怎么管图片/头像？ | **第十七章 MinIO 对象存储** |
| 不知道该用哪个数据库？ | **第十四章数据库选型** |

---

## ★ 十四、数据库选型详解

### 4 个 DB 全景对比

| 维度 | H2 | SQLite | MySQL | **PostgreSQL** |
|---|---|---|---|---|
| 类型 | 嵌入式 | 嵌入式 | 服务端 | 服务端 |
| 存哪 | 内存 / 文件 | **文件** | 服务进程 | 服务进程 |
| 持久化 | 默认 ❌ | ✅ | ✅ | ✅ |
| 启动方式 | 跟随 Spring Boot | 跟随程序 | 单独服务 | 单独服务 |
| 配置复杂度 | 0 | 0 | 中 | 中 |
| 标准 SQL | 中 | 中 | 中 | ⭐ 最佳 |
| JSON 支持 | 一般 | 一般 | 一般 | ⭐ JSONB |
| 地理信息 | ❌ | 简单 | 一般 | ⭐ PostGIS |
| 并发 | 单进程 | 单进程 | 强 | 强 |
| 单机文件上限 | 内存大小 | 1TB+ | 取决于磁盘 | 取决于磁盘 |
| 适用场景 | 开发/测试 | 小项目/单文件 | 生产 | 生产 |
| 中文社区 | Java 系 | 全栈 | ⭐ 最旺 | 中等 |
| 上手难度 | 极低 | 极低 | 低 | 中 |

### 决策树

```
你要做什么？
│
├─ 学习/写 demo/单元测试
│  └─ H2 内存模式 ⭐ 首选
│
├─ 小工具/单机小项目
│  └─ SQLite 文件模式
│
├─ 生产部署，国内电商/金融
│  ├─ 团队熟 MySQL  → MySQL 8
│  └─ 团队熟 PG / 想用 JSONB / 地理 / 复杂查询 → PostgreSQL 16
│
└─ 还在纠结
   └─ 选 H2，学完换 MySQL，2 步切换
```

### 阶段对应推荐

| 阶段 | 推荐 | 理由 |
|---|---|---|
| **现在学 Java** | H2 内存 | 零配置、跟随 Spring Boot 启动、关掉就丢 |
| **真机调试** | H2 内存 或 SQLite 文件 | 调试时想看 db：SQLite 直接打开文件；H2 用 H2 Console |
| **生产部署** | MySQL 8 或 PG 16 | 看团队/云厂商偏好 |

### 切换成本

| 从 | 切到 | 改什么 |
|---|---|---|
| H2 | MySQL | `application.yml` URL + 加 MySQL 驱动 |
| H2 | PostgreSQL | `application.yml` URL + 加 PG 驱动 |
| H2 | SQLite | `application.yml` URL + 加 SQLite JDBC |

**Entity / Repository / Service / Controller 一行不用改**——JPA 自动适配方言。

### 我们的项目建议

- **现在**：保持 H2（编译已经过了）
- **阶段 5-6 上线前**：选 MySQL 或 PG（看你方便）
- **判据**：
  - 阿里云 RDS → MySQL 默认
  - 腾讯云 TDSQL → 兼容 MySQL
  - 想学 JSONB / PostGIS / 高级 SQL → PG
  - 团队 / 朋友会的 → 跟着选

---

## ★ 十五、AI 协作 Prompt 模板

AI 帮你写代码的关键：**描述质量决定产出质量**。

### 万能模板

```markdown
# 任务：<一句话>

## 背景
- 用的什么技术栈（Spring Boot / 小程序原生 / Vue3）
- 在哪个文件下写

## 要求
- 字段/参数/返回值（参考现有接口 / 数据库）
- 命名规范（参考本项目第十一章）
- 接口格式（参考本项目第四章）

## 风格
- 简单/复杂
- 是否需要注释
- 是否要 JSDoc

## 限制
- 不用 XXX 库
- 不要超过 N 行
- 必须兼容 X
```

### 实际示例

#### 示例 1：让 AI 写商品详情页

> 任务：写小程序的商品详情页 WXML/WXSS/JS
>
> 背景：原生微信小程序，放在 wechat/pages/goods/detail/ 目录
>
> 要求：
> - 顶部 swiper 图片轮播（图片字段 cover，目前是空字符串用占位图）
> - 商品名 + 价格（price 是分，/100 显示）
> - 4 维规格：杯型/温度/糖度/奶类（每个选项有 extra_price）
> - 加料多选（addons 数组）
> - 数量 -1+
> - 底部 [加入购物车] [立即购买] 两个按钮
> - 调用后端 GET /api/goods/:id
>
> 风格：参考 SPEC 第六章 6.3 节布局，WeUI 风格
>
> 限制：不要装新组件库，用原生 + WeUI 类名

#### 示例 2：让 AI 写 cart 接口

> 任务：写 Java Spring Boot 的购物车 CRUD 接口
>
> 背景：放在 server/src/main/java/com/mall/controller/CartController.java
>
> 要求：
> - GET /api/cart - 当前用户购物车（要 token）
> - POST /api/cart - 加车，body {goods_id, sku, quantity}
> - PATCH /api/cart/:id - 改 quantity
> - DELETE /api/cart/:id - 删除
> - 全部要鉴权，挂在 @RequestMapping("/api")
> - 返回 ApiResponse<List<CartItem>> / ApiResponse.ok()
> - Service 层做事务，Controller 只调 Service
>
> 风格：参考现有 WxLoginController / WxLoginService 的写法
>
> 限制：不要加 Lombok（Java 25 兼容问题），手写 getter/setter

#### 示例 3：让 AI 解释一段代码

> 任务：解释 server/src/main/java/com/mall/filter/JwtAuthFilter.java
>
> 背景：这是我们项目的鉴权过滤器
>
> 要求：
> - 逐行讲清楚
> - 标出哪些是「Spring Security 标准做法」/ 哪些是「手写实现」
> - 给一个时序图说明请求怎么走到这

### 反面教材（不要这样问）

| ❌ 模糊 | ✅ 清晰 |
|---|---|
| "写个首页" | "按 SPEC 6.1 写首页，WeUI 风格，2 个区块" |
| "做个好看的" | "主色 #6F4E37，圆角 12rpx，3 张 Banner" |
| "参考淘宝" | "顶部 88rpx，Banner 300rpx，4 个快捷入口横排" |
| "加个用户功能" | "微信登录 + 头像昵称 + 我的页面" |

**信息密度决定输出质量**。

---

## ★ 十六、FAQ（你可能会问的）

### Q1. 后端一定要用 Java 吗？
**不一定**。Node 也能做，我们最初用的就是 Node + Express。Java 适合：
- 你想找 Java 后端工作
- 团队 / 公司用 Java
- 项目要长期维护、要严谨

如果只是学习，**用你顺手的语言**。语言只是工具。

### Q2. Java 17 跟 Java 25 差很多吗？
**对你学习没差**。Java 17 是 LTS（长期支持），Java 25 是最新。**99% 的语法你用不到**。我们项目 compile target 是 17，能跑在 17-25 任意版本上。

### Q3. 为什么不用 Spring Security？
**学习阶段不要**。Spring Security 强大但复杂，一上来就搞你会懵。
我们手写 `JwtAuthFilter` 实现核心鉴权——**代码 70 行，比 Spring Security 简单 10 倍**，且让你懂原理。
后期生产可以切 Spring Security 加 RBAC / OAuth。

### Q4. admin 端什么时候做？
**可选**。你最早说「我仅仅学习写小程序前后端代码」，所以 admin 是**锦上添花**。
学完小程序 21 个页面 + 业务接口后，有空再做。

### Q5. 我现在能跑通的小程序有什么？
- 微信登录：code → 后端换 openid → 拿 token
- 商品列表：5 条咖啡
- 我的信息：拿 openid 和 uid

**3 个核心流程**已通。其他页面就是把后端接口用 WXML 渲染出来。

### Q6. H2 内存库关掉就没了，咋办？
**用户数据靠 openid**——微信服务器记着。关掉再启，登录一次就回来了。
**业务数据（订单/支付）**在内存里**真会丢**——所以生产前要切 MySQL/PG。

### Q7. 支付怎么联调？需要商户号吗？
**联调**用微信支付沙箱，**不需要真实商户号**。
- 商户平台 → 产品中心 → 沙箱工具
- 沙箱环境可调下单 + 回调，跑通完整链路
- **不花一分钱**

### Q8. 备案要多久？
**7-20 天**，看地区。
- 上海 / 北京：5-7 天
- 偏远地区：15-20 天
- **建议第一时间备案**，因为这是最慢一步

### Q9. 小程序能跨端（iOS/Android）吗？
**不能**。小程序是微信生态专属。
要做 Android / iOS App 得用 uni-app / Taro 跨端框架，**或者**重新写原生（Swift + Kotlin）。

### Q10. 我能从这个项目学到什么？
完整学完能掌握：
- ✅ 微信小程序原生开发（找工作够用）
- ✅ Spring Boot + JPA（Java 后端主流）
- ✅ JWT 鉴权（无状态登录）
- ✅ 微信支付（V3 全流程）
- ✅ RESTful API 设计
- ✅ SQL 数据库设计
- ✅ 前后端联调
- ✅ 项目部署

**学习周期**：2-3 个月业余时间。

### Q11. 我能直接用这个项目找工作吗？
**学习完可以**。但**还需要**：
- 加 admin 端（上面要求的）
- 加单元测试（JUnit + Mockito）
- 加 Docker 部署
- 整理一份能讲清楚的 README

### Q12. 我卡住了怎么办？
1. 读本文档对应章节
2. 看 SPEC 是否有要求
3. 问 AI：「按 SPEC 6.3 写商品详情页，要求 ...」

---

## ★ 十七、MinIO 对象存储

### 17.1 一句话总结

用 MinIO 存商品图、用户头像、所有用户上传的文件；S3 兼容协议，学完 = 掌握 AWS S3 / 腾讯 COS / 阿里 OSS。

**后端只存 URL 字符串，文件本身交给 MinIO**——这是所有分布式系统的标准做法。

### 17.2 为什么是 MinIO（vs 本地磁盘 / 腾讯云 COS）

| 维度 | 本地磁盘 `uploads/` | 腾讯云 COS | **MinIO** |
|---|---|---|---|
| 跑在哪 | 服务器本地 | 别人机房 | **你自己机器上**（Docker 一行） |
| 价格 | 0 | 几分/GB/月 | **0**（只算服务器钱） |
| 多服务器共享 | ❌ 难 | ✅ | ✅ |
| 公网访问 | 配 Nginx | 域名即用 | 自建域名 |
| S3 协议 | ❌ | 部分兼容 | **完全兼容** |
| 学习价值 | 低 | 中（绑定厂商） | **高**（学完通用） |
| 适合阶段 | 入门 | 商业上线 | **学习 / 内网 / 不想付费** |

**决策路径**：
- 学习阶段 / 多台服务器 / 想学标准 S3 → **MinIO**
- 正式商业上线（国内访问快）→ 腾讯云 COS
- 就一台单机 / demo → 本地磁盘也行

### 17.3 本地开发环境（Docker）

```bash
docker run -d \
  --name minio \
  -p 9000:9000 \
  -p 9001:9001 \
  -v D:/minio-data:/data \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  minio/minio server /data --console-address ":9001"
```

- API 端口：`9000`
- 控制台：`http://localhost:9001`（账号 `minioadmin / minioadmin`）
- 进控制台手动建 bucket：`mall`

### 17.4 生产环境（自建 / 云厂商 MinIO）

| 方式 | 适用 |
|---|---|
| 自购服务器 + Docker | 学习 / 内网 / 中小流量 |
| 阿里云 MinIO 镜像 | 国内上线，免运维 |
| Kubernetes + MinIO Operator | 大流量 / 多节点 |
| 腾讯云 COS / 阿里 OSS | 不想运维 / 跨地域复制 |

> **生产环境绝对不要用 minioadmin/minioadmin**——改密码、用 IAM、限制内网。

### 17.5 Spring Boot 集成（两种 SDK 都行）

#### 方案 A：官方 MinIO Java SDK（推荐学习 ⭐）

`pom.xml`：
```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.10</version>
</dependency>
```

`application.yml`：
```yaml
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket: mall
  public-url: http://localhost:9000/mall  # 拼接返回 URL 用
```

`MinioConfig.java`：
```java
@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}") private String endpoint;
    @Value("${minio.access-key}") private String accessKey;
    @Value("${minio.secret-key}") private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
```

`MinioService.java`：
```java
@Service
public class MinioService {
    @Autowired private MinioClient minioClient;
    @Value("${minio.bucket}") private String bucket;
    @Value("${minio.public-url}") private String publicUrl;

    public String upload(MultipartFile file) throws Exception {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String key = "goods/" + UUID.randomUUID() + "." + ext;

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .object(key)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );

        return publicUrl + "/" + key;
    }
}
```

`UploadController.java`：
```java
@RestController
@RequestMapping("/api")
public class UploadController {
    @Autowired private MinioService minioService;

    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestParam MultipartFile file) {
        try {
            String url = minioService.upload(file);
            return ApiResponse.ok(Map.of("url", url));
        } catch (Exception e) {
            return ApiResponse.error(500, "上传失败: " + e.getMessage());
        }
    }
}
```

#### 方案 B：AWS SDK V2（标准化，学完通用）

`pom.xml`：
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
</dependency>
```

**对比 Spring Boot**：
- 两种 SDK 都能用，本项目**推荐官方 MinIO SDK**（API 简单，文档全）
- 想换 COS / OSS / S3 时，**改 3 个变量**即可，业务代码零改动

### 17.6 Bucket 内部目录结构

```
mall/                                   # bucket 名
├── goods/                              # 商品图
│   ├── 2026/
│   │   ├── 09/
│   │   │   ├── abc123.jpg
│   │   │   └── def456.png
│   ├── 2026/
│   └── ...
│
├── avatar/                             # 用户头像
│   └── {openid}/
│       └── {uuid}.jpg
│
├── coupon/                             # 优惠券图片
└── refund/                             # 退款凭证
```

**设计原则**：
- 按**业务**分前缀（`goods/`, `avatar/`），不按用户分（用户文件太多会拖慢 list）
- 文件名一律 `UUID.扩展名`，**永不暴露原始文件名**（防中文乱码、防路径穿越）
- **不要**用 `user_id` 当文件名（GDPR / 隐私合规）

### 17.7 上传 / 下载流程

```
┌──────────────┐              ┌────────────────────┐              ┌──────────────┐
│   小程序/前端 │              │   Spring Boot       │              │    MinIO      │
└──────┬───────┘              └─────────┬──────────┘              └──────┬───────┘
       │                               │                                │
       │ 1. POST /api/upload           │                                │
       │    (multipart/form-data)       │                                │
       │───────────────────────────────►│                                │
       │                               │ 2. 调 MinioClient.putObject    │
       │                               │────────────────────────────────►│
       │                               │    stream = file.getInputStream │
       │                               │    key = "goods/uuid.jpg"       │
       │                               │                                │
       │                               │ 3. 返回 URL                     │
       │                               │◄────────────────────────────────│
       │ 4. {url: "http://..."}        │                                │
       │◄───────────────────────────────│                                │
       │                               │                                │
       │ 5. PUT /api/me {avatar_url}    │                                │
       │───────────────────────────────►│                                │
       │                               │ 6. UPDATE wx_users             │
       │                               │    SET avatar_url = ?           │
       │                               │    (写到 H2/MySQL/PG)           │
```

**关键点**：
- **DB 存 URL 字符串**（`http://minio.xxx.com/mall/goods/abc.jpg`），**不存文件**
- 前端 `<image src="{{avatarUrl}}">` 直接渲染
- 下载不需要走后端，**前端直接 GET MinIO URL**（配 bucket 公共读，或用预签名 URL）

### 17.8 与现有架构的集成

| 已有的 | 怎么集成 |
|---|---|
| **H2 / SQLite / MySQL / PG** | 加一列 `image_url VARCHAR(255)`，存 MinIO 返回的 URL |
| **`goods` 表** | 加 `image_url` 字段；DataSeeder 给 5 条咖啡填示例图 |
| **`wx_users` 表** | 加 `avatar_url` 字段；用户上传头像时更新 |
| **JWT 鉴权** | `/api/upload` 接口要鉴权，避免被刷流量 |
| **CORS** | MinIO 默认不允许跨域；需在 MinIO 控制台 / `mc` 命令配 CORS 规则 |
| **Nginx 反代** | 生产环境用 `nginx.conf` 把 `mall.xxx.com/uploads` 反代到 MinIO `:9000` |

**最小改动**：
- `pom.xml` 加 1 个依赖
- `application.yml` 加 5 行配置
- 新建 3 个文件（Config / Service / Controller）
- 已有的 Service 调用 `minioService.upload()` 即可
- **前端零改动**——它只看到 URL

### 17.9 私有 vs 公共读

| 场景 | 桶策略 | URL 类型 |
|---|---|---|
| 商品图 / Banner / 静态资源 | **public** | 永久 URL（前端直接用） |
| 用户头像 | public | 永久 URL |
| 身份证 / 支付凭证 / 退款单 | **private** | **预签名 URL**（带过期时间） |

预签名 URL 示例（私有桶）：
```java
String presignedUrl = minioClient.getPresignedObjectUrl(
    GetPresignedObjectUrlArgs.builder()
        .method(Method.GET)
        .bucket(bucket)
        .object(key)
        .expiry(60 * 60) // 1 小时过期
        .build()
);
```

### 17.10 监控 & 安全 Checklist

| 项 | 怎么做 |
|---|---|
| 容量监控 | MinIO 控制台 → Metrics；Prometheus + Grafana |
| 访问日志 | MinIO 默认开启，输出到 `/var/log/minio` |
| 防盗链 | Nginx `valid_referers` + `secure_link` |
| 内容审核 | 上传后调腾讯云「内容安全」API |
| 病毒扫描 | ClamAV 集成（学习阶段跳过） |
| 备份 | `mc mirror` 跨桶复制；生产至少 2 副本 |
| HTTPS | 生产必须；MinIO 启动参数加证书路径 |

### 17.11 与其他章节的关系

| 章节 | 关系 |
|---|---|
| **六、技术决策** | 本章是新决策的详细解释 |
| **七、部署架构** | MinIO 进架构图（Nginx → Spring Boot → MinIO） |
| **九、Checklist** | 阶段 2「脚手架」加 MinIO 集成项 |
| **十二、Java 对照** | MultipartFile → MinioClient.putObject 对照 |
| **十四、数据库选型** | MinIO 不属于 DB，跟 H2/PG 是**正交**的（一个存结构化数据，一个存文件） |

---

## 附录 A：本文档版本说明

| 版本 | 日期 | 改动 |
|---|---|---|
| V1.0 | 2026-09-09 | 初始版本 |
| V1.1 | 2026-09-10 | 切 Java 单后端 |
| V1.2 | 2026-09-10 | +3 章节（数据库/AI/FAQ），强化决策理由 |
| V1.3 | 2026-09-10 | +1 章节（MinIO 对象存储），第六章加 MinIO 决策行，第十三章问题地图加 MinIO 索引 |

**维护者提示**：本文档是项目地图，详细的规范/接口/Schema 见另三份文档。每改一项技术决策，更新对应章节 + 第十章变更日志。
