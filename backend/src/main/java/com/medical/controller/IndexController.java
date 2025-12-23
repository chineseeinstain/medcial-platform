package com.medical.controller;

import com.medical.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页Controller
 * 提供API信息和链接
 */
@Slf4j
@RestController
public class IndexController {
    
    @PostConstruct
    public void init() {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"IndexController.java:18\",\"message\":\"IndexController initialized\",\"data\":\"bean created\",\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"G\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis()
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        log.info("IndexController初始化完成");
    }
    
    /**
     * 根路径映射
     * GET /
     */
    @GetMapping("/")
    public Result<Map<String, Object>> index() {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"IndexController.java:24\",\"message\":\"IndexController.index() called\",\"data\":\"root path accessed\",\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"G\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis()
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        
        log.info("访问根路径");
        Map<String, Object> info = new HashMap<>();
        info.put("name", "区域基层医疗机构运营数据分析平台");
        info.put("version", "1.0.0");
        info.put("status", "running");
        info.put("swagger", "http://localhost:8080/swagger-ui/index.html");
        
        Map<String, String> apis = new HashMap<>();
        apis.put("patients", "/api/patients");
        apis.put("statistics", "/api/statistics");
        info.put("apis", apis);
        
        return Result.success(info);
    }
    
    /**
     * 健康检查接口
     * GET /health
     */
    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return Result.success(status);
    }
    
    /**
     * 测试接口 - 确认Controller是否工作
     * GET /test
     */
    @GetMapping("/test")
    public Result<String> test() {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"IndexController.java:90\",\"message\":\"IndexController.test() called\",\"data\":\"test endpoint accessed\",\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"G\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis()
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        return Result.success("IndexController工作正常");
    }
}

