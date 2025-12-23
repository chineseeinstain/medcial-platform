# Spring Boot 常用注解用法

> 简洁实用的注解说明，结合项目实际代码示例

---

## 📦 一、组件注解（告诉Spring这是组件）

### 1. @Component
**作用**：标记为Spring组件，会被Spring管理  
**使用场景**：工具类、通用组件

```java
@Component
public class JwtUtil {
    // Spring会自动创建这个类的实例
}
```

### 2. @Service
**作用**：标记为业务逻辑层组件  
**使用场景**：Service层类

```java
@Service
public class UserService {
    // 业务逻辑代码
}
```

### 3. @Controller
**作用**：标记为控制器，处理HTTP请求  
**使用场景**：传统MVC控制器（返回视图）

```java
@Controller
public class PageController {
    @GetMapping("/index")
    public String index() {
        return "index";  // 返回视图名称
    }
}
```

### 4. @RestController
**作用**：`@Controller` + `@ResponseBody`，返回JSON数据  
**使用场景**：RESTful API控制器（最常用）

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        return Result.success(userService.getAllUsers());
    }
}
```

**区别**：
- `@Controller`：返回视图（HTML页面）
- `@RestController`：返回JSON数据（API接口）

---

## 🔌 二、依赖注入注解（自动装配）

### 1. @Autowired
**作用**：自动注入依赖对象  
**使用场景**：注入Service、Mapper、工具类等

```java
@RestController
public class UserController {
    @Autowired
    private UserService userService;  // 自动注入UserService
    
    @Autowired
    private UserMapper userMapper;    // 自动注入UserMapper
}
```

**注意**：
- 被注入的类必须也是Spring组件（@Service、@Component等）
- 一个类只能有一个构造函数时，可以省略@Autowired

### 2. @Value
**作用**：从配置文件读取值  
**使用场景**：读取application.yml中的配置

```java
@Component
public class JwtUtil {
    @Value("${jwt.secret}")  // 从配置文件读取jwt.secret的值
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
}
```

**配置文件**（application.yml）：
```yaml
jwt:
  secret: mySecretKey
  expiration: 86400000
```

---

## 🌐 三、Web请求注解（定义API接口）

### 1. @RequestMapping
**作用**：定义请求路径的基础路径  
**使用场景**：类级别，定义整个Controller的基础路径

```java
@RestController
@RequestMapping("/api/users")  // 基础路径
public class UserController {
    // 所有方法的路径都会加上 /api/users
}
```

**属性**：
- `value` 或 `path`：路径
- `method`：HTTP方法（GET、POST等）

### 2. @GetMapping
**作用**：处理GET请求  
**使用场景**：查询数据

```java
@GetMapping("/list")  // GET /api/users/list
public Result<List<User>> getUserList() {
    return Result.success(userService.getAllUsers());
}

@GetMapping("/{id}")  // GET /api/users/1
public Result<User> getUserById(@PathVariable Long id) {
    return Result.success(userService.getUserById(id));
}
```

### 3. @PostMapping
**作用**：处理POST请求  
**使用场景**：创建数据、登录等

```java
@PostMapping("/login")  // POST /api/auth/login
public Result<LoginResponse> login(@RequestBody LoginRequest request) {
    return Result.success(userService.login(request));
}

@PostMapping  // POST /api/users
public Result<User> createUser(@RequestBody User user) {
    return Result.success(userService.createUser(user));
}
```

### 4. @PutMapping
**作用**：处理PUT请求  
**使用场景**：更新数据

```java
@PutMapping("/{id}")  // PUT /api/users/1
public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    return Result.success(userService.updateUser(id, user));
}
```

### 5. @DeleteMapping
**作用**：处理DELETE请求  
**使用场景**：删除数据

```java
@DeleteMapping("/{id}")  // DELETE /api/users/1
public Result<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return Result.success();
}
```

**HTTP方法对照表**：
| 注解 | HTTP方法 | 用途 |
|------|---------|------|
| @GetMapping | GET | 查询数据 |
| @PostMapping | POST | 创建数据 |
| @PutMapping | PUT | 更新数据 |
| @DeleteMapping | DELETE | 删除数据 |

---

## 📥 四、参数注解（接收请求参数）

### 1. @PathVariable
**作用**：从URL路径中获取参数  
**使用场景**：RESTful风格，路径参数

```java
@GetMapping("/{id}")  // URL: /api/users/123
public Result<User> getUserById(@PathVariable Long id) {
    // id = 123
    return Result.success(userService.getUserById(id));
}

@GetMapping("/users/{userId}/posts/{postId}")
public Result<Post> getPost(
    @PathVariable Long userId,    // 从路径获取userId
    @PathVariable Long postId     // 从路径获取postId
) {
    // URL: /api/users/1/posts/2
    // userId = 1, postId = 2
}
```

### 2. @RequestParam
**作用**：从URL查询参数中获取值  
**使用场景**：查询参数（?key=value）

```java
@GetMapping("/search")
public Result<List<User>> searchUsers(
    @RequestParam String keyword,           // ?keyword=admin
    @RequestParam(defaultValue = "1") int page,  // ?page=1（默认值1）
    @RequestParam(required = false) String role  // ?role=admin（可选）
) {
    // URL: /api/users/search?keyword=admin&page=1&role=doctor
    // keyword = "admin", page = 1, role = "doctor"
}
```

**属性**：
- `value`：参数名
- `required`：是否必须（默认true）
- `defaultValue`：默认值

### 3. @RequestBody
**作用**：从请求体中获取JSON数据  
**使用场景**：POST/PUT请求，传递复杂对象

```java
@PostMapping("/login")
public Result<LoginResponse> login(@RequestBody LoginRequest request) {
    // 前端发送JSON: {"username":"admin","password":"123456"}
    // Spring自动转换为LoginRequest对象
    return Result.success(userService.login(request));
}
```

**前端示例**：
```javascript
axios.post('/api/auth/login', {
  username: 'admin',
  password: '123456'
})
```

### 4. @RequestHeader
**作用**：从HTTP请求头中获取值  
**使用场景**：获取Token、认证信息等

```java
@GetMapping("/me")
public Result<User> getCurrentUser(
    @RequestHeader(value = "Authorization", required = false) String token
) {
    // 从请求头获取: Authorization: Bearer xxxxx
    // token = "Bearer xxxxx"
    return Result.success(userService.getCurrentUser(token));
}
```

**参数注解对比**：
| 注解 | 来源 | 示例 |
|------|------|------|
| @PathVariable | URL路径 | `/api/users/{id}` |
| @RequestParam | URL查询参数 | `/api/users?name=admin` |
| @RequestBody | 请求体（JSON） | POST请求的body |
| @RequestHeader | HTTP请求头 | `Authorization: Bearer xxx` |

---

## ⚙️ 五、配置注解

### 1. @Configuration
**作用**：标记为配置类  
**使用场景**：配置类，定义Bean

```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // 配置代码
        return http.build();
    }
}
```

### 2. @Bean
**作用**：定义Bean，交给Spring管理  
**使用场景**：在配置类中创建第三方库的对象

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置Redis
        return template;
    }
}
```

---

## ✅ 六、验证注解

### 1. @Validated
**作用**：启用参数验证  
**使用场景**：验证请求参数

```java
@PostMapping("/register")
public Result<LoginResponse> register(
    @Validated @RequestBody RegisterRequest request
) {
    // @Validated会验证RegisterRequest中的验证注解
    // 如果验证失败，会抛出异常
    return Result.success(userService.register(request));
}
```

### 2. @Valid
**作用**：与@Validated类似，但功能稍弱  
**使用场景**：验证嵌套对象

```java
@PostMapping("/create")
public Result<User> createUser(@Valid @RequestBody User user) {
    return Result.success(userService.createUser(user));
}
```

**在DTO中使用验证注解**：
```java
@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少6位")
    private String password;
    
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

---

## 🔄 七、生命周期注解

### 1. @PostConstruct
**作用**：Bean初始化后执行  
**使用场景**：初始化数据、检查依赖注入等

```java
@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    
    @PostConstruct
    public void init() {
        // Spring创建完AuthController后，自动执行这个方法
        log.info("AuthController初始化完成");
        log.info("UserService注入状态: {}", userService != null ? "已注入" : "未注入");
    }
}
```

**执行时机**：
1. Spring创建Bean实例
2. 注入依赖（@Autowired）
3. **执行@PostConstruct方法**
4. Bean可以使用

### 2. @PreDestroy
**作用**：Bean销毁前执行  
**使用场景**：清理资源、关闭连接等

```java
@Component
public class DataSourceManager {
    @PreDestroy
    public void cleanup() {
        // Spring销毁Bean前，执行这个方法
        log.info("清理数据源连接");
    }
}
```

---

## 🛠️ 八、Lombok注解（简化代码）

### 1. @Data
**作用**：自动生成getter、setter、toString、equals、hashCode  
**使用场景**：实体类、DTO类

```java
@Data
public class User {
    private Long id;
    private String username;
    private String password;
}

// 等价于手动写：
// public Long getId() { return id; }
// public void setId(Long id) { this.id = id; }
// public String getUsername() { return username; }
// ... 等等
```

### 2. @Slf4j
**作用**：自动生成日志对象（log）  
**使用场景**：需要打印日志的类

```java
@Slf4j
@RestController
public class UserController {
    public void someMethod() {
        log.info("这是一条信息日志");
        log.error("这是一条错误日志");
        log.debug("这是一条调试日志");
    }
}

// 等价于手动写：
// private static final Logger log = LoggerFactory.getLogger(UserController.class);
```

---

## 🎯 九、其他常用注解

### 1. @RestControllerAdvice
**作用**：全局异常处理  
**使用场景**：统一处理所有Controller的异常

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return Result.error(e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统错误，请稍后重试");
    }
}
```

### 2. @EnableWebSecurity
**作用**：启用Spring Security  
**使用场景**：安全配置类

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // 安全配置
        return http.build();
    }
}
```

---

## 📋 十、注解使用总结

### 完整示例（结合使用）

```java
@Slf4j                    // Lombok：生成日志对象
@RestController           // Spring：RESTful控制器
@RequestMapping("/api/users")  // Web：基础路径
public class UserController {
    
    @Autowired            // 依赖注入：自动注入Service
    private UserService userService;
    
    @PostConstruct        // 生命周期：初始化方法
    public void init() {
        log.info("UserController初始化完成");
    }
    
    @GetMapping("/list")  // Web：GET请求
    public Result<List<User>> getUserList() {
        return Result.success(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")  // Web：GET请求，路径参数
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }
    
    @PostMapping          // Web：POST请求
    public Result<User> createUser(
        @Validated @RequestBody User user  // 验证 + 请求体
    ) {
        return Result.success(userService.createUser(user));
    }
    
    @PutMapping("/{id}")  // Web：PUT请求
    public Result<User> updateUser(
        @PathVariable Long id,
        @RequestBody User user
    ) {
        return Result.success(userService.updateUser(id, user));
    }
    
    @DeleteMapping("/{id}")  // Web：DELETE请求
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
```

---

## 🎓 快速记忆

### 组件注解（4个）
- `@Component`：通用组件
- `@Service`：业务逻辑层
- `@Controller`：控制器（返回视图）
- `@RestController`：RESTful控制器（返回JSON）

### 依赖注入（2个）
- `@Autowired`：自动注入
- `@Value`：读取配置

### Web请求（5个）
- `@RequestMapping`：基础路径
- `@GetMapping`：GET请求
- `@PostMapping`：POST请求
- `@PutMapping`：PUT请求
- `@DeleteMapping`：DELETE请求

### 参数注解（4个）
- `@PathVariable`：路径参数 `/api/users/{id}`
- `@RequestParam`：查询参数 `/api/users?name=admin`
- `@RequestBody`：请求体（JSON）
- `@RequestHeader`：请求头

### 配置注解（2个）
- `@Configuration`：配置类
- `@Bean`：定义Bean

### 验证注解（2个）
- `@Validated`：启用验证
- `@Valid`：验证嵌套对象

### 生命周期（2个）
- `@PostConstruct`：初始化后执行
- `@PreDestroy`：销毁前执行

### Lombok注解（2个）
- `@Data`：生成getter/setter
- `@Slf4j`：生成日志对象

---

## 💡 常见问题

### Q1: @Controller和@RestController的区别？
**答**：
- `@Controller`：返回视图名称（HTML页面）
- `@RestController`：返回JSON数据（API接口）

### Q2: @Autowired可以省略吗？
**答**：如果类只有一个构造函数，可以省略。但建议保留，代码更清晰。

### Q3: @PathVariable和@RequestParam的区别？
**答**：
- `@PathVariable`：从URL路径获取 `/api/users/{id}`
- `@RequestParam`：从查询参数获取 `/api/users?id=1`

### Q4: @Validated和@Valid的区别？
**答**：
- `@Validated`：Spring的，支持分组验证
- `@Valid`：Java标准的，功能稍弱

### Q5: 什么时候用@PostConstruct？
**答**：需要在Bean创建后、使用前做一些初始化工作，比如检查依赖注入、初始化数据等。

---

**最后更新**：2025-12-22  
**适用项目**：区域基层医疗机构运营数据分析平台

