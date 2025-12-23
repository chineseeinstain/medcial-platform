package com.medical.controller;

import com.medical.common.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于生成BCrypt密码hash（仅用于开发测试）
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    /**
     * 生成BCrypt密码hash
     * GET /api/test/password?pwd=123456
     */
    @GetMapping("/password")
    public Result<Map<String, String>> generatePassword(@RequestParam(defaultValue = "123456") String pwd) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(pwd);
        
        Map<String, String> result = new HashMap<>();
        result.put("password", pwd);
        result.put("bcryptHash", hash);
        result.put("verify", String.valueOf(encoder.matches(pwd, hash)));
        
        return Result.success(result);
    }
}










