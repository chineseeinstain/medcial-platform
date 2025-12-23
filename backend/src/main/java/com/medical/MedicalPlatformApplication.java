package com.medical;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * 区域基层医疗机构运营数据分析平台 - 主启动类
 * 
 * @SpringBootApplication: 标识这是一个Spring Boot应用
 * @EnableScheduling: 启用定时任务
 * @MapperScan: 扫描MyBatis Mapper接口
 * 
 * 注意：移除了@EnableCaching，避免Redis不可用时Spring Cache报错
 * 如果需要缓存，代码中已经手动实现了缓存逻辑（如StatisticsService）
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.medical.mapper")
public class MedicalPlatformApplication {

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
        } catch (IOException e) {
            // 忽略日志写入错误
        }
    }

    public static void main(String[] args) {
        // #region agent log
        logDebug("MedicalPlatformApplication.java:48", "Application starting", "main method entry", "A");
        // #endregion
        
        try {
            // #region agent log
            logDebug("MedicalPlatformApplication.java:52", "Before SpringApplication.run", "args.length=" + args.length, "E");
            // #endregion
            
            SpringApplication app = new SpringApplication(MedicalPlatformApplication.class);
            
            // 添加启动事件监听器
            app.addListeners((ApplicationListener<ApplicationStartingEvent>) event -> {
                logDebug("MedicalPlatformApplication.java:58", "ApplicationStartingEvent", "Spring Boot starting", "E");
            });
            
            app.addListeners((ApplicationListener<ApplicationStartedEvent>) event -> {
                logDebug("MedicalPlatformApplication.java:62", "ApplicationStartedEvent", "Spring Boot started", "E");
            });
            
            app.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
                logDebug("MedicalPlatformApplication.java:66", "ApplicationReadyEvent", "Spring Boot ready", "E");
            });
            
            app.addListeners((ApplicationListener<ContextRefreshedEvent>) event -> {
                logDebug("MedicalPlatformApplication.java:70", "ContextRefreshedEvent", "ApplicationContext refreshed", "E");
            });
            
            // #region agent log
            logDebug("MedicalPlatformApplication.java:73", "SpringApplication created with listeners", "before run", "E");
            // #endregion
            
            app.run(args);
            
            // #region agent log
            logDebug("MedicalPlatformApplication.java:77", "After SpringApplication.run", "application started", "E");
            // #endregion
            
            System.out.println("=================================");
            System.out.println("医疗平台后端服务启动成功！");
            System.out.println("访问地址: http://localhost:8080");
            System.out.println("API文档: http://localhost:8080/swagger-ui/index.html");
            System.out.println("=================================");
            
            // #region agent log
            logDebug("MedicalPlatformApplication.java:87", "Startup complete", "success", "A");
            // #endregion
            
        } catch (Exception e) {
            // #region agent log
            logDebug("MedicalPlatformApplication.java:91", "Startup failed", "exception=" + e.getClass().getName() + ": " + e.getMessage() + " | cause=" + (e.getCause() != null ? e.getCause().getClass().getName() : "null"), "C");
            // #endregion
            
            System.err.println("启动失败: " + e.getMessage());
            e.printStackTrace();
            
            // 打印完整的堆栈跟踪
            if (e.getCause() != null) {
                System.err.println("根本原因: " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            }
        }
    }
}

