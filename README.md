# 基于大模型的校园兼职智能审核与管理平台

#### 介绍
项目描述：采用前后端分离架构开发校园兼职管理系统，覆盖多角色（学生 / 兼职发布者 / 管理员）的兼职全流程管理，核心解决校园兼职信息分散、真假混杂、管理效率低的问题。系统引入 AI 初审 + 人工复审的两级审核机制，对发布的兼职岗位进行风险识别，保障学生安全。

#### 软件架构

采用前后端分离模式开发，前端 Vue 3 + TypeScript，后端 Spring Boot 3，数据持久层采用 MyBatis 框架，数据库使用 MySQL，并通过 Knife4j 对外提供 OpenAPI 3 规范接口文档。岗位发布环节接入 DeepSeek 大模型进行 AI 风险初审，再由管理员人工复审，形成两级审核闭环。

#### 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端 | Spring Boot 3.2.0、MyBatis 3.0.5、MySQL Connector/J、Lombok、Knife4j 4.4.0 (OpenAPI 3)、Spring Boot Validation、JetBrains Annotations |
| 前端 | Vue 3.5、Vite 7、TypeScript 5.9、Element Plus 2.13、Pinia 3、Vue Router 4.6、Axios 1.13 |
| 测试 | Vitest（单元测试）、@vue/test-utils、Playwright（E2E 测试） |
| 工程化 | Prettier、vue-tsc、vite-plugin-vue-devtools、npm-run-all2 |
| 数据库 | MySQL 8.x |
| AI 能力 | DeepSeek（deepseek-chat 模型，用于兼职岗位风险初审） |
| JDK | Java 17 |
| Node | Node.js ≥ 20.19.0 或 ≥ 22.12.0 |

#### 核心工作
1. 基于 Spring Boot 3 搭建项目，整合 MyBatis 优化 SQL，设计 RESTful API 并处理跨域、参数校验；引入全局异常处理机制（`GlobalExceptionHandler`）与统一响应封装（`Result`），规范前后端交互标准。

2. 用 Vue 3 组合式 API + Element Plus 开发前端，封装 Axios（`utils/request.js`）、实现 Vue Router 路由权限拦截（`requiresAuth` 守卫）。

3. 设计 MySQL 核心表（`user` / `job` / `wish_job` / `register_user`）及外键关联，为关键字段（如 `audit_status`）添加索引优化查询。

4. 实现基于角色的用户认证与接口鉴权模块（`permission`：0-学生，1-兼职发布者，2-管理员），保障不同角色数据隔离与操作安全。

5. 接入 DeepSeek 大模型实现兼职岗位 AI 风险初审（`AiAuditService`），按 `low / medium / high` 风险等级分流：低风险自动放行，中高风险转人工复审，形成两级审核闭环。

6. 对接 ECharts 完成兼职数据可视化看板（`Dashboard.vue`），为管理员提供岗位热度、投递趋势等决策支持。

7. 使用 Knife4j 生成 OpenAPI 规范文档，并测试接口。

#### 角色与功能

| 角色 | 主要功能 |
| --- | --- |
| 学生 | 注册 / 登录、浏览兼职、报名兼职（`register_user`）、收藏心仪岗位（`wish_job`）、查看个人中心 |
| 兼职发布者 | 发布兼职、修改 / 删除自己发布的兼职、查看报名者名单 |
| 管理员 | 人工复审待审岗位（通过 / 驳回）、用户管理、数据可视化看板 |

#### 项目结构

```
.
├── jobManager/             # 后端 Spring Boot 项目
│   ├── src/main/java/com/rgzn/zcy/jobmanager
│   │   ├── controller/     # 控制器层（登录、注册、岗位增删改查、报名、心愿单、审核等）
│   │   ├── service/         # 业务接口
│   │   │   └── impl/        # 业务实现（含 AiAuditServiceImpl 等）
│   │   ├── mapper/          # MyBatis 数据访问层
│   │   ├── bean/            # 实体类（Job / User / WishJob / RegisterUser / Result 等）
│   │   ├── DTO/             # 数据传输对象（UserDTO / UpdateJobRequest / ReviewDecision 等）
│   │   ├── config/          # 配置类（CorsConfig / RestClientConfig / AiAuditProperties）
│   │   └── handler/         # 全局异常处理
│   └── src/main/resources/application.yml
├── vue-jobManager/         # 前端 Vue 项目
│   ├── src/
│   │   ├── api/            # 接口封装
│   │   │   └── modules/    # job / review / signup / user / wishJob 接口模块
│   │   ├── views/          # 页面（Login / Register / Dashboard / Profile）
│   │   ├── router/         # 路由 + 鉴权守卫
│   │   ├── stores/         # Pinia 状态管理
│   │   ├── components/      # 通用组件
│   │   └── utils/          # axios 请求封装
│   ├── e2e/                # Playwright E2E 测试
│   └── vite.config.ts
└── create_database.sql     # 数据库初始化脚本
```

#### 数据库设计

| 表名 | 说明 | 关键字段 |
| --- | --- | --- |
| `user` | 用户表 | `permission`：0-学生，1-兼职发布者，2-管理员 |
| `job` | 兼职岗位表 | `audit_status`：0-待AI审核，1-已发布，2-待人工审核，3-已驳回；含 `risk_level`、`ai_reason`、`reviewer_name` 等审核字段 |
| `wish_job` | 心愿岗位表 | 学生收藏的岗位，`status`：0-审核中，1-通过 |
| `register_user` | 报名记录表 | 记录学生报名某岗位的联系人信息 |

初始化：执行根目录 `create_database.sql` 即可创建数据库 `jobmanager` 及上述 4 张表。

#### 安装教程

##### 安装相应的依赖。
1. **JDK 17**（推荐 IntelliJ IDEA 自带或官网下载）
2. **Maven 3.6+**（或使用项目自带的 `mvnw`）
3. **MySQL 8.x**（启动并保证可登录）
4. **Node.js 20.19.0+ / 22.12.0+**（推荐使用 nvm 管理版本）
5. **npm**（随 Node 一起安装，也可使用 pnpm / yarn）
6. **IntelliJ IDEA**（后端）与 **VS Code**（前端，安装 Vue 官方插件）
7. **DeepSeek API Key**（可选，用于 AI 风险初审；未配置时可关闭该功能）

##### 启动后端（jobManager）

1. 使用 IntelliJ IDEA 打开 `jobManager` 目录（或 `File → Open` 选择该子目录）。
2. 初始化数据库：在 MySQL 中执行根目录的 `create_database.sql`。
3. 修改数据库连接配置：打开 `jobManager/src/main/resources/application.yml`，将 `username` 和 `password` 改为你本机的 MySQL 账号密码。
4. （可选）配置 AI 审核：在环境变量中设置 `AI_API_KEY` 为你的 DeepSeek API Key；如不启用，可将 `ai.audit.enabled` 改为 `false`。
5. 运行启动类 `JobManagerApplication.java`。
6. Knife4j 接口文档地址：`http://localhost:8080/doc.html`（可在此查看并测试所有接口）。

##### 启动前端（vue-jobManager）

1. 使用 VS Code 打开 `vue-jobManager` 目录。
2. 安装依赖：`npm install`。
3. 启动开发服务器：`npm run dev`。
4. 构建生产包：`npm run build`。
5. 预览生产构建：`npm run preview`。

##### 测试

- 单元测试：`npm run test:unit`（Vitest）
- E2E 测试：`npm run test:e2e`（Playwright）

#### 默认访问地址

- 前端开发服务器：`http://localhost:5173`（Vite 默认）
- 后端服务：`http://localhost:8080`
- 接口文档：`http://localhost:8080/doc.html`
