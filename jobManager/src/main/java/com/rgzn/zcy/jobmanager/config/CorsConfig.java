package com.rgzn.zcy.jobmanager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置（解决前端 OPTIONS 预检请求 403 问题）
 */
//@Configuration // 标记为配置类，Spring 启动时加载
//public class CorsConfig {
//
//    /**
//     * 配置跨域过滤器
//     */
//    @Bean // 将过滤器注入 Spring 容器
//    public CorsFilter corsFilter() {
//        // 1. 创建跨域配置对象
//        CorsConfiguration config = new CorsConfiguration();
//
//        // ========== 核心配置项（必须配）==========
//        // 允许的前端域名（关键！生产环境一定要写具体域名，不要用*）
//        // 示例：前端运行在 http://localhost:5173（Vite 默认端口）或 http://localhost:8081（Vue CLI）
//        config.addAllowedOrigin("http://localhost:5173"); // 若有多个前端域名，可多次 add
//        config.addAllowedOrigin("http://localhost:8081");
//
//        // 允许跨域的请求方法（包含 OPTIONS 预检请求、POST 登录请求）
//        config.addAllowedMethod("OPTIONS"); // 预检请求必须允许
//        config.addAllowedMethod("POST");    // 登录/注册的 POST 请求
//        config.addAllowedMethod("GET");     // 若有 GET 接口也加上（可选）
//
//        // 允许的请求头（前端发送的 Content-Type 等）
//        config.addAllowedHeader("*"); // 允许所有请求头（简单场景够用）
//
//        // 允许携带 Cookie/Token（若登录后需要传 token 鉴权，必须设为 true）
//        config.setAllowCredentials(true);
//
//        // 预检请求的缓存时间（3600 秒 = 1 小时，避免浏览器频繁发 OPTIONS 请求）
//        config.setMaxAge(3600L);
//
//        // ========== 路径匹配（关键！确保覆盖你的接口）==========
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // 匹配所有接口路径（/* 只匹配一级，/** 匹配所有层级，如 /login/、/register/users 都覆盖）
//        source.registerCorsConfiguration("/**", config);
//
//        // 2. 创建并返回跨域过滤器
//        return new CorsFilter(source);
//    }
//}
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 允许所有源
                .allowCredentials(true)      // 允许携带凭证（Cookie、Session等）
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}