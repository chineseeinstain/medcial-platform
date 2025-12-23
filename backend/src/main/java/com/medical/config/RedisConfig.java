package com.medical.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 配置Redis的序列化方式，方便存储Java对象
 */
@Configuration
// 移除@EnableCaching，避免Redis不可用时Spring Cache报错
// 如果需要缓存，代码中已经手动实现了缓存逻辑（如StatisticsService）
public class RedisConfig {
    
    // 如果Redis不可用，这个Bean会失败，但不影响应用启动
    
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"RedisConfig.java:26\",\"message\":\"Creating RedisTemplate\",\"data\":{\"factory\":\"%s\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis(),
                factory != null ? factory.getClass().getName() : "null"
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        
        try {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"RedisConfig.java:40\",\"message\":\"Setting connection factory\",\"data\":{\"step\":\"before\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
                    System.currentTimeMillis(),
                    Integer.toHexString((int)(Math.random() * 1000000)),
                    System.currentTimeMillis()
                );
                try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                    writer.write(logEntry);
                }
            } catch (Exception e) {}
            // #endregion
            
            template.setConnectionFactory(factory);
            
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"RedisConfig.java:52\",\"message\":\"Configuring serializers\",\"data\":{\"step\":\"before\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
                    System.currentTimeMillis(),
                    Integer.toHexString((int)(Math.random() * 1000000)),
                    System.currentTimeMillis()
                );
                try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                    writer.write(logEntry);
                }
            } catch (Exception e) {}
            // #endregion
            
            // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
            Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
            serializer.setObjectMapper(mapper);
            
            // 使用StringRedisSerializer来序列化和反序列化redis的key值
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(serializer);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(serializer);
            
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"RedisConfig.java:68\",\"message\":\"Calling afterPropertiesSet\",\"data\":{\"step\":\"before\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
                    System.currentTimeMillis(),
                    Integer.toHexString((int)(Math.random() * 1000000)),
                    System.currentTimeMillis()
                );
                try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                    writer.write(logEntry);
                }
            } catch (Exception e) {}
            // #endregion
            
            template.afterPropertiesSet();
            
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"RedisConfig.java:80\",\"message\":\"RedisTemplate created\",\"data\":{\"status\":\"success\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
                    System.currentTimeMillis(),
                    Integer.toHexString((int)(Math.random() * 1000000)),
                    System.currentTimeMillis()
                );
                try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                    writer.write(logEntry);
                }
            } catch (Exception e) {}
            // #endregion
            
            return template;
        } catch (Exception e) {
            // #region agent log
            try {
                String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
                String logEntry = String.format(
                    "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"RedisConfig.java:92\",\"message\":\"RedisTemplate creation failed\",\"data\":{\"error\":\"%s\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"D\"}\n",
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
            throw e;
        }
    }
}

