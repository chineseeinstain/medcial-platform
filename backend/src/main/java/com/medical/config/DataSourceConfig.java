package com.medical.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据源配置检查
 * 启动时检查数据库连接
 */
@Slf4j
@Configuration
public class DataSourceConfig {
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @PostConstruct
    public void checkDataSource() {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"DataSourceConfig.java:30\",\"message\":\"Checking database connection\",\"data\":{\"url\":\"%s\",\"username\":\"%s\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis(),
                url != null ? url.replace("\"", "\\\"") : "null",
                username != null ? username : "null"
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        
        try {
            // 提取JDBC URL（去掉jdbc:mysql://前缀）
            String jdbcUrl = url;
            log.info("尝试连接数据库: {}", jdbcUrl.replaceAll("password=[^&]*", "password=***"));
            
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            conn.close();
            
            log.info("数据库连接成功！");
            
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"DataSourceConfig.java:45\",\"message\":\"Database connection successful\",\"data\":{\"status\":\"success\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\"}\n",
                    System.currentTimeMillis(),
                    Integer.toHexString((int)(Math.random() * 1000000)),
                    System.currentTimeMillis()
                );
                try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                    writer.write(logEntry);
                }
            } catch (Exception e) {}
            // #endregion
            
        } catch (Exception e) {
            log.error("==========================================");
            log.error("数据库连接失败！");
            log.error("错误信息: {}", e.getMessage());
            log.error("请检查以下配置：");
            log.error("1. MySQL服务是否已启动");
            log.error("2. 数据库是否已创建: medical_platform");
            log.error("3. 用户名和密码是否正确（当前: {}/{})", username, password.isEmpty() ? "空" : "***");
            log.error("4. 配置文件: backend/src/main/resources/application.yml");
            log.error("==========================================");
            
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"DataSourceConfig.java:60\",\"message\":\"Database connection failed\",\"data\":{\"error\":\"%s\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"C\"}\n",
                    System.currentTimeMillis(),
                    Integer.toHexString((int)(Math.random() * 1000000)),
                    System.currentTimeMillis(),
                    e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "unknown"
                );
                try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                    writer.write(logEntry);
                }
            } catch (Exception ex) {}
            // #endregion
            
            // 不抛出异常，让应用继续启动（但数据库操作会失败）
            // throw new RuntimeException("数据库连接失败，请检查配置", e);
        }
    }
}

