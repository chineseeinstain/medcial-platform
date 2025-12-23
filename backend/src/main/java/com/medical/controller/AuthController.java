package com.medical.controller;

import com.medical.common.Result;
import com.medical.dto.LoginRequest;
import com.medical.dto.LoginResponse;
import com.medical.dto.RegisterRequest;
import com.medical.entity.User;
import com.medical.service.UserService;
import com.medical.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * 认证控制器（登录/注册）
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 初始化方法 - 用于诊断Bean注入问题
     */
    @PostConstruct
    public void init() {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"AuthController.java:init\",\"message\":\"AuthController init started\",\"data\":{\"userService\":%s,\"jwtUtil\":%s},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis(),
                userService != null ? "\"injected\"" : "\"null\"",
                jwtUtil != null ? "\"injected\"" : "\"null\""
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        
        log.info("==========================================");
        log.info("AuthController 初始化开始");
        log.info("UserService 注入状态: {}", userService != null ? "✅ 已注入" : "❌ 未注入");
        log.info("JwtUtil 注入状态: {}", jwtUtil != null ? "✅ 已注入" : "❌ 未注入");
        log.info("AuthController 初始化完成");
        log.info("==========================================");
        
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"AuthController.java:init\",\"message\":\"AuthController init completed\",\"data\":{\"status\":\"success\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis()
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
    }
    
    /**
     * 用户注册
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public Result<LoginResponse> register(@Validated @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            
            // 生成Token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());
            
            // 构建响应
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());
            response.setRole(user.getRole());
            response.setUserId(user.getId());
            
            return Result.success("注册成功", response);
        } catch (RuntimeException e) {
            log.error("注册失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("注册异常", e);
            return Result.error("注册失败，请稍后重试");
        }
    }
    
    /**
     * 用户登录
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("收到登录请求: username={}", request.getUsername());
            
            // 验证请求参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            
            User user = userService.login(request);
            log.info("用户验证成功: username={}, role={}", user.getUsername(), user.getRole());
            
            // 生成Token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());
            log.info("Token生成成功: username={}", user.getUsername());
            
            // 构建响应
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());
            response.setRole(user.getRole());
            response.setUserId(user.getId());
            
            return Result.success("登录成功", response);
        } catch (RuntimeException e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("登录异常", e);
            return Result.error("登录失败，请稍后重试: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return Result.error(401, "未授权");
            }
            
            // 从Token中提取用户名（去掉"Bearer "前缀）
            String actualToken = token.replace("Bearer ", "");
            String username = jwtUtil.getUsernameFromToken(actualToken);
            
            User user = userService.findByUsername(username);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            
            // 不返回密码
            user.setPassword(null);
            
            return Result.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败");
        }
    }
}

