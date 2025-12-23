package com.medical.controller;

import com.medical.common.Result;
import com.medical.entity.User;
import com.medical.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理Controller
 */
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 初始化方法 - 用于诊断Bean注入问题
     */
    @javax.annotation.PostConstruct
    public void init() {
        log.info("==========================================");
        log.info("UserController 初始化开始");
        log.info("UserService 注入状态: {}", userService != null ? "✅ 已注入" : "❌ 未注入");
        log.info("UserController 初始化完成");
        log.info("==========================================");
    }
    
    /**
     * 查询所有用户
     * GET /api/users/list
     */
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
            fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"user-list-debug\",\"hypothesisId\":\"A\",\"location\":\"UserController.java:35\",\"message\":\"用户列表请求开始\",\"timestamp\":%d}\n", 
                System.currentTimeMillis()));
            fw.close();
        } catch (Exception e) {}
        // #endregion
        log.info("查询用户列表");
        try {
            List<User> users = userService.getAllUsers();
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
                fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"user-list-debug\",\"hypothesisId\":\"B\",\"location\":\"UserController.java:40\",\"message\":\"用户列表查询成功\",\"data\":{\"userCount\":%d},\"timestamp\":%d}\n", 
                    users != null ? users.size() : 0,
                    System.currentTimeMillis()));
                fw.close();
            } catch (Exception e) {}
            // #endregion
            return Result.success(users);
        } catch (Exception e) {
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
                fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"user-list-debug\",\"hypothesisId\":\"C\",\"location\":\"UserController.java:45\",\"message\":\"用户列表查询失败\",\"data\":{\"error\":\"%s\"},\"timestamp\":%d}\n", 
                    e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "unknown",
                    System.currentTimeMillis()));
                fw.close();
            } catch (Exception ex) {}
            // #endregion
            log.error("查询用户列表失败", e);
            return Result.error("查询用户列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询用户
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        log.info("查询用户详情，ID: {}", id);
        User user = userService.findById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return Result.success(user);
    }
    
    /**
     * 创建用户
     * POST /api/users
     */
    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        try {
            log.info("创建用户: username={}", user.getUsername());
            
            // 检查用户名是否已存在
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null) {
                return Result.error("用户名已存在");
            }
            
            // 检查邮箱是否已存在
            if (user.getEmail() != null) {
                // 这里需要添加根据邮箱查询的方法，暂时跳过
            }
            
            // 设置默认值
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("patient");
            }
            
            // 创建用户（需要密码）
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return Result.error("密码不能为空");
            }
            
            // 使用register方法创建用户
            com.medical.dto.RegisterRequest registerRequest = new com.medical.dto.RegisterRequest();
            registerRequest.setUsername(user.getUsername());
            registerRequest.setPassword(user.getPassword());
            registerRequest.setEmail(user.getEmail());
            registerRequest.setPhone(user.getPhone());
            registerRequest.setRealName(user.getRealName());
            registerRequest.setRole(user.getRole());
            
            User newUser = userService.register(registerRequest);
            newUser.setPassword(null);
            
            return Result.success("用户创建成功", newUser);
        } catch (RuntimeException e) {
            log.error("创建用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("创建用户异常", e);
            return Result.error("创建用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            log.info("更新用户信息: id={}", id);
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return Result.success("用户信息更新成功", updatedUser);
        } catch (RuntimeException e) {
            log.error("更新用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("更新用户异常", e);
            return Result.error("更新用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除用户
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            log.info("删除用户: id={}", id);
            userService.deleteUser(id);
            return Result.<Void>success();
        } catch (RuntimeException e) {
            log.error("删除用户失败: {}", e.getMessage());
            return Result.<Void>error(e.getMessage());
        } catch (Exception e) {
            log.error("删除用户异常", e);
            return Result.<Void>error("删除用户失败: " + e.getMessage());
        }
    }
    
    /**
     * 切换用户状态（启用/禁用）
     * PUT /api/users/{id}/toggle-status
     */
    @PutMapping("/{id}/toggle-status")
    public Result<User> toggleUserStatus(@PathVariable Long id) {
        try {
            log.info("切换用户状态: id={}", id);
            User updatedUser = userService.toggleUserStatus(id);
            return Result.success("用户状态切换成功", updatedUser);
        } catch (RuntimeException e) {
            log.error("切换用户状态失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("切换用户状态异常", e);
            return Result.error("切换用户状态失败: " + e.getMessage());
        }
    }
}

