package com.medical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.FileWriter;
import java.time.Instant;

/**
 * 跨域配置
 * 允许前端（Vue）跨域访问后端API
 */
@Configuration
public class CorsConfig {
    
    private static void logDebug(String location, String message, Object data, String hypothesisId) {
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"%s\",\"message\":\"%s\",\"data\":%s,\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"%s\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                Instant.now().toEpochMilli(),
                location,
                message,
                data != null ? "\"" + data.toString().replace("\"", "\\\"") + "\"" : "null",
                hypothesisId
            );
            try (FileWriter writer = new FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {
            // 忽略日志写入错误
        }
    }
    
    @Bean
    public CorsFilter corsFilter() {
        // #region agent log
        logDebug("CorsConfig.java:35", "Creating CorsFilter", "starting", "F");
        // #endregion
        
        try {
            CorsConfiguration config = new CorsConfiguration();
            
            // #region agent log
            logDebug("CorsConfig.java:40", "Configuring CORS", "before addAllowedOriginPattern", "F");
            // #endregion
            
            // 允许所有域名跨域
            // 注意：当allowCredentials为true时，不能使用通配符"*"，需要使用addAllowedOriginPattern
            // 但如果同时设置allowCredentials(true)和addAllowedOriginPattern("*")会报错
            // 解决方案：要么使用addAllowedOriginPattern("*")但不设置allowCredentials(true)
            // 要么设置allowCredentials(true)但使用具体的域名列表
            config.addAllowedOriginPattern("*");
            
            // #region agent log
            logDebug("CorsConfig.java:48", "After addAllowedOriginPattern", "configured", "F");
            // #endregion
            
            // 允许所有请求头
            config.addAllowedHeader("*");
            // 允许所有请求方法
            config.addAllowedMethod("*");
            
            // 注意：当使用addAllowedOriginPattern("*")时，不能设置allowCredentials(true)
            // 如果需要携带凭证，需要指定具体的域名，例如：
            // config.addAllowedOrigin("http://localhost:5173");
            // config.setAllowCredentials(true);
            // 这里为了兼容性，不设置allowCredentials，或者设置为false
            config.setAllowCredentials(false);
            
            // #region agent log
            logDebug("CorsConfig.java:60", "CORS configuration complete", "allowCredentials=false", "F");
            // #endregion
            
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            
            // #region agent log
            logDebug("CorsConfig.java:65", "Creating CorsFilter", "before return", "F");
            // #endregion
            
            CorsFilter filter = new CorsFilter(source);
            
            // #region agent log
            logDebug("CorsConfig.java:70", "CorsFilter created", "success", "F");
            // #endregion
            
            return filter;
        } catch (Exception e) {
            // #region agent log
            logDebug("CorsConfig.java:75", "CorsFilter creation failed", "exception=" + e.getClass().getName() + ": " + e.getMessage(), "F");
            // #endregion
            throw e;
        }
    }
}

