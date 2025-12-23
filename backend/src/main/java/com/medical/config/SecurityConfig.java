package com.medical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.io.FileWriter;
import java.time.Instant;

/**
 * Spring Security配置
 * 配置API访问权限，允许所有API请求
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // #region agent log
        logDebug("SecurityConfig.java:35", "Creating SecurityFilterChain", "httpSecurity=" + (http != null ? "not null" : "null"), "A");
        // #endregion
        
        try {
            http
                .csrf().disable()  // 禁用CSRF，方便API调用
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()  // 允许所有请求
                );
            
            // #region agent log
            logDebug("SecurityConfig.java:45", "Before http.build()", "configuring complete", "A");
            // #endregion
            
            SecurityFilterChain chain = http.build();
            
            // #region agent log
            logDebug("SecurityConfig.java:50", "SecurityFilterChain created", "success", "A");
            // #endregion
            
            return chain;
        } catch (Exception e) {
            // #region agent log
            logDebug("SecurityConfig.java:55", "SecurityFilterChain creation failed", "exception=" + e.getClass().getName() + ": " + e.getMessage(), "A");
            // #endregion
            throw e;
        }
    }
}


