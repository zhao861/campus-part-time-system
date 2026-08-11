# 校园兼职系统

#### 介绍
项目描述：采用前后端分离架构开发校园兼职管理系统，覆盖多角色（学生/商家/管理员）的兼职全流程管理，核心解决校园兼职信息分散、管理效率低的问题。

#### 软件架构

采用前后端分离模式开发，前端vue3。 后端springboot3，数据持久层采用mybatis框架，数据库使用mysql。

#### 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端 | Spring Boot 3.2.0、MyBatis、MySQL、Lombok、Knife4j (OpenAPI 3) |
| 前端 | Vue 3、Vite、TypeScript、Element Plus、Pinia、Vue Router、Axios |
| 数据库 | MySQL 8.x |
| JDK | Java 17 |
| Node | Node.js ≥ 20.19.0  |

#### 核心工作
1. 基于SpringBoot3 搭建项目，整合MyBatis优化SQL，设计RESTful API并处理跨域、参数校验；引入全局异常处理机制与统一响应封装，规范前后端交互标准。

2. 用 Vue3 组合式 API+Element Plus 开发前端，封装 Axios、实现Vue Router 路由权限拦截。

3. 设计 MySQL 核心表及外键关联，为关键字段添加索引优化查询。

4. 实现基于 JWT 的用户认证与接口鉴权模块，保障不同角色数据隔离与操作安全。

5. 对接ECharts完成兼职数据可视化看板，为管理员提供岗位热度、投递趋势等决策支持。

6. 使用knife4j 生成 openAPI 规范文档，并测试接口。

#### 项目结构

```
.
├── jobManager/             # 后端 Spring Boot 项目
│   ├── src/main/java/com/rgzn/zcy/jobmanager
│   │   ├── controller/     # 控制器层
│   │   ├── service/        # 业务逻辑层
│   │   ├── mapper/         # 数据访问层
│   │   ├── bean/           # 实体类
│   │   ├── DTO/            # 数据传输对象
│   │   ├── config/         # 配置类（跨域等）
│   │   └── handler/        # 全局异常处理
│   └── src/main/resources/application.yml
├── vue-jobManager/         # 前端 Vue 项目
│   ├── src/
│   │   ├── api/            # 接口封装
│   │   ├── views/          # 页面（登录、注册、仪表盘、个人中心等）
│   │   ├── router/         # 路由
│   │   ├── stores/         # Pinia 状态管理
│   │   └── utils/          # axios 请求封装
│   └── vite.config.ts
└── create_database.sql     # 数据库初始化脚本

```

#### 安装教程


##### 安装相应的依赖。

1. **JDK 17**（推荐 IntelliJ IDEA 自带或官网下载）
2. **Maven 3.6+**（或使用项目自带的 `mvnw`）
3. **MySQL 8.x**（启动并保证可登录）
4. **Node.js 20.19.0+ / 22.12.0+**（推荐使用 nvm 管理版本）
5. **npm**（随 Node 一起安装，也可使用 pnpm / yarn）
6. **IntelliJ IDEA**（后端）与 **VS Code**（前端，安装 Vue 官方插件）

##### 启动后端（jobManager）

1. 使用 IntelliJ IDEA 打开 `jobManager` 目录（或 `File → Open` 选择该子目录）。
2. 修改数据库连接配置：打开 `jobManager/src/main/resources/application.yml`，将 `username` 和 `password` 改为你本机的 MySQL 账号密码。
3. 运行启动类 `JobManagerApplication.java`。
4. Knife4j 接口文档地址：`http://localhost:8080/doc.html`（可在此查看并测试所有接口）。

##### 启动前端（vue-jobManager）

1. 使用 VS Code 打开 `vue-jobManager` 目录。
2. 安装依赖，启动开发服务器`npm run dev`。

