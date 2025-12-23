package com.medical.service;

import com.medical.dto.LoginRequest;
import com.medical.dto.RegisterRequest;
import com.medical.entity.User;
import com.medical.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户服务类
 */
@Slf4j
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 用户注册
     */
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        existingUser = userMapper.selectByEmail(request.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密密码
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRealName(request.getRealName());
        // 设置角色：如果指定了角色且为doctor，则设置为doctor，否则为patient
        // 注意：admin角色只能由管理员创建，不能通过注册获得
        if (request.getRole() != null && request.getRole().equals("doctor")) {
            user.setRole("doctor");
        } else {
            user.setRole("patient");
        }
        user.setStatus(1); // 默认状态：正常
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        
        userMapper.insert(user);
        log.info("用户注册成功: {}", request.getUsername());
        
        return user;
    }
    
    /**
     * 用户登录
     */
    public User login(LoginRequest request) {
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
            fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"login-debug\",\"hypothesisId\":\"A\",\"location\":\"UserService.java:69\",\"message\":\"登录请求开始\",\"data\":{\"username\":\"%s\",\"passwordLength\":%d},\"timestamp\":%d}\n", 
                request.getUsername() != null ? request.getUsername() : "null", 
                request.getPassword() != null ? request.getPassword().length() : 0,
                System.currentTimeMillis()));
            fw.close();
        } catch (Exception e) {}
        // #endregion
        log.info("开始验证用户: username={}", request.getUsername());
        
        User user = userMapper.selectByUsername(request.getUsername());
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
            fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"login-debug\",\"hypothesisId\":\"B\",\"location\":\"UserService.java:73\",\"message\":\"数据库查询结果\",\"data\":{\"userFound\":%s,\"username\":\"%s\"},\"timestamp\":%d}\n", 
                user != null ? "true" : "false",
                request.getUsername() != null ? request.getUsername() : "null",
                System.currentTimeMillis()));
            fw.close();
        } catch (Exception e) {}
        // #endregion
        if (user == null) {
            log.warn("用户不存在: username={}", request.getUsername());
            throw new RuntimeException("用户名或密码错误");
        }
        
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
            fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"login-debug\",\"hypothesisId\":\"C\",\"location\":\"UserService.java:78\",\"message\":\"用户密码hash信息\",\"data\":{\"username\":\"%s\",\"passwordHashLength\":%d,\"passwordHashPrefix\":\"%s\"},\"timestamp\":%d}\n", 
                user.getUsername(),
                user.getPassword() != null ? user.getPassword().length() : 0,
                user.getPassword() != null && user.getPassword().length() > 20 ? user.getPassword().substring(0, 20) : (user.getPassword() != null ? user.getPassword() : "null"),
                System.currentTimeMillis()));
            fw.close();
        } catch (Exception e) {}
        // #endregion
        log.info("找到用户: username={}, passwordHash={}", user.getUsername(), 
            user.getPassword() != null ? user.getPassword().substring(0, Math.min(20, user.getPassword().length())) : "null");
        
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
            fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"login-debug\",\"hypothesisId\":\"D\",\"location\":\"UserService.java:82\",\"message\":\"密码验证前\",\"data\":{\"inputPassword\":\"%s\",\"storedHashPrefix\":\"%s\"},\"timestamp\":%d}\n", 
                request.getPassword() != null ? request.getPassword() : "null",
                user.getPassword() != null && user.getPassword().length() > 20 ? user.getPassword().substring(0, 20) : (user.getPassword() != null ? user.getPassword() : "null"),
                System.currentTimeMillis()));
            fw.close();
        } catch (Exception e) {}
        // #endregion
        // 验证密码
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
            fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"login-debug\",\"hypothesisId\":\"E\",\"location\":\"UserService.java:83\",\"message\":\"密码验证结果\",\"data\":{\"matches\":%s,\"username\":\"%s\"},\"timestamp\":%d}\n", 
                passwordMatches ? "true" : "false",
                user.getUsername(),
                System.currentTimeMillis()));
            fw.close();
        } catch (Exception e) {}
        // #endregion
        log.info("密码验证结果: matches={}", passwordMatches);
        
        if (!passwordMatches) {
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter("d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log", true);
                fw.write(String.format("{\"sessionId\":\"debug-session\",\"runId\":\"login-debug\",\"hypothesisId\":\"F\",\"location\":\"UserService.java:86\",\"message\":\"密码验证失败\",\"data\":{\"username\":\"%s\",\"inputPassword\":\"%s\",\"storedHash\":\"%s\"},\"timestamp\":%d}\n", 
                    user.getUsername(),
                    request.getPassword() != null ? request.getPassword() : "null",
                    user.getPassword() != null ? user.getPassword() : "null",
                    System.currentTimeMillis()));
                fw.close();
            } catch (Exception e) {}
            // #endregion
            log.warn("密码错误: username={}", request.getUsername());
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("用户已被禁用: username={}, status={}", request.getUsername(), user.getStatus());
            throw new RuntimeException("用户已被禁用");
        }
        
        log.info("用户登录成功: username={}, role={}", request.getUsername(), user.getRole());
        return user;
    }
    
    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    /**
     * 根据ID查询用户
     */
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
    
    /**
     * 查询所有用户
     */
    public List<User> getAllUsers() {
        List<User> users = userMapper.selectAll();
        // 不返回密码
        users.forEach(user -> user.setPassword(null));
        return users;
    }
    
    /**
     * 更新用户信息
     */
    public User updateUser(User user) {
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 如果提供了新密码，则加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 保持原密码
            user.setPassword(existingUser.getPassword());
        }
        
        user.setUpdateTime(new Date());
        userMapper.update(user);
        log.info("用户信息更新成功: id={}, username={}", user.getId(), user.getUsername());
        
        // 返回更新后的用户（不包含密码）
        User updatedUser = userMapper.selectById(user.getId());
        updatedUser.setPassword(null);
        return updatedUser;
    }
    
    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.deleteById(id);
        log.info("用户删除成功: id={}, username={}", id, user.getUsername());
    }
    
    /**
     * 切换用户状态
     */
    public User toggleUserStatus(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        user.setUpdateTime(new Date());
        userMapper.update(user);
        log.info("用户状态切换成功: id={}, username={}, status={}", id, user.getUsername(), user.getStatus());
        
        User updatedUser = userMapper.selectById(id);
        updatedUser.setPassword(null);
        return updatedUser;
    }
}

