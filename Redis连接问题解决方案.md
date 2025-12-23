# Redis连接问题解决方案

## 问题描述

加载患者列表时出现错误：
```
Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost:6379
```

## 问题原因

1. **Redis服务未启动**：应用尝试连接Redis，但Redis服务未运行
2. **使用了@Cacheable注解**：`PatientService.getAllPatients()`方法使用了`@Cacheable`注解，需要Redis缓存管理器
3. **Spring Cache自动配置**：`RedisConfig`中启用了`@EnableCaching`，会在启动时尝试连接Redis

## 解决方案

### 方案1：移除缓存注解（已实施，推荐）

**优点**：
- 简单直接，不需要Redis也能正常运行
- 患者列表数据可能经常变化，缓存意义不大

**修改内容**：
1. 移除了`PatientService`中的`@Cacheable`注解
2. 移除了`RedisConfig`中的`@EnableCaching`注解

**结果**：
- 应用不再依赖Redis缓存
- 即使Redis未启动，应用也能正常运行
- 数据直接从数据库查询，性能仍然可以接受

---

### 方案2：启动Redis服务（如果需要使用缓存）

**Windows**：
```bash
# 下载Redis for Windows
# 解压后运行
redis-server.exe

# 或者使用WSL（Windows Subsystem for Linux）
wsl
redis-server
```

**Linux/Mac**：
```bash
# Ubuntu/Debian
sudo apt-get install redis-server
sudo systemctl start redis

# Mac
brew install redis
brew services start redis

# 验证Redis是否运行
redis-cli ping
# 应该返回：PONG
```

---

### 方案3：禁用Redis自动配置（完全禁用Redis功能）

如果确定不需要Redis，可以完全禁用Redis自动配置：

**修改`application.yml`**：
```yaml
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
      - org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
```

或者**注释掉Redis配置**：
```yaml
# spring:
#   redis:
#     host: localhost
#     port: 6379
```

---

## 当前代码的容错处理

代码中已经实现了Redis容错处理（`StatisticsService`）：

```java
@Autowired(required = false)  // Redis不可用时，redisTemplate可以为null
private RedisTemplate<String, Object> redisTemplate;

public List<TrendDataDTO> getOutpatientTrend() {
    // 1. 先查缓存（如果Redis可用）
    if (redisTemplate != null) {
        try {
            // 使用缓存
        } catch (Exception e) {
            log.warn("Redis缓存不可用，直接查询数据库");
        }
    }
    
    // 2. 查数据库
    List<TrendDataDTO> data = statisticsMapper.selectOutpatientTrend();
    
    // 3. 存入缓存（如果Redis可用）
    if (redisTemplate != null) {
        try {
            redisTemplate.opsForValue().set(cacheKey, data, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis缓存写入失败，跳过缓存");
        }
    }
    
    return data;
}
```

这种方式的好处：
- Redis可用时：使用缓存提升性能
- Redis不可用时：自动降级到直接查询数据库，不影响功能

---

## 验证修复

修复后，请按以下步骤验证：

1. **确保Redis未运行**（测试容错能力）

2. **重新编译后端**：
```bash
cd backend
mvn clean compile
```

3. **启动后端服务**：
```bash
mvn spring-boot:run
```

4. **测试患者列表接口**：
```bash
# 浏览器访问
http://localhost:8080/api/patients/list

# 或使用curl
curl http://localhost:8080/api/patients/list
```

5. **预期结果**：
- 应用正常启动，无Redis连接错误
- 患者列表接口正常返回数据
- 日志中可能看到"Redis缓存不可用"的警告，但不影响功能

---

## 后续建议

### 如果将来需要使用Redis缓存：

1. **启动Redis服务**（参考方案2）

2. **恢复缓存功能**（可选）：
```java
// 如果需要在PatientService中加缓存
@Autowired(required = false)
private RedisTemplate<String, Object> redisTemplate;

public List<Patient> getAllPatients() {
    String cacheKey = "patients:all";
    
    // 先查缓存
    if (redisTemplate != null) {
        try {
            List<Patient> cached = (List<Patient>) redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return cached;
            }
        } catch (Exception e) {
            log.warn("Redis缓存不可用，直接查询数据库");
        }
    }
    
    // 查数据库
    List<Patient> result = patientMapper.selectAll();
    
    // 存入缓存
    if (redisTemplate != null && result != null && !result.isEmpty()) {
        try {
            redisTemplate.opsForValue().set(cacheKey, result, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis缓存写入失败");
        }
    }
    
    return result;
}
```

---

## 总结

✅ **已实施的修复**：
- 移除了`@Cacheable`注解（避免Spring Cache依赖Redis）
- 移除了`@EnableCaching`注解（避免启动时连接Redis）

✅ **现在的状态**：
- 应用可以在Redis未启动时正常运行
- 数据直接从数据库查询
- `StatisticsService`中的缓存逻辑仍然有效（有容错处理）

✅ **如果需要Redis**：
- 启动Redis服务
- 应用会自动使用缓存功能

---

**最后更新**：2025-12-22

