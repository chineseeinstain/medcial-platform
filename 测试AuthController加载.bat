@echo off
chcp 65001 >nul
echo ========================================
echo 测试AuthController加载状态
echo ========================================
echo.

echo [1] 检查class文件...
if exist "backend\target\classes\com\medical\controller\AuthController.class" (
    echo ✅ AuthController.class 文件存在
) else (
    echo ❌ AuthController.class 文件不存在
    echo 需要重新编译: cd backend ^&^& mvn clean compile
    goto :end
)
echo.

echo [2] 检查依赖类文件...
if exist "backend\target\classes\com\medical\service\UserService.class" (
    echo ✅ UserService.class 文件存在
) else (
    echo ❌ UserService.class 文件不存在
)
if exist "backend\target\classes\com\medical\util\JwtUtil.class" (
    echo ✅ JwtUtil.class 文件存在
) else (
    echo ❌ JwtUtil.class 文件不存在
)
echo.

echo [3] 测试接口（GET请求测试接口是否存在）...
echo 测试 /api/auth/me (GET)...
curl -s http://localhost:8080/api/auth/me 2>nul | findstr "未授权\|code" >nul
if %errorlevel% equ 0 (
    echo ✅ /api/auth/me 接口存在（返回401是正常的）
) else (
    echo ❌ /api/auth/me 返回404（接口不存在）
)
echo.

echo [4] 检查Swagger UI中的接口...
echo 请手动访问: http://localhost:8080/swagger-ui/index.html
echo 查看是否能找到 /api/auth/login 和 /api/auth/register
echo.

echo [5] 建议操作：
echo   1. 停止后端服务（Ctrl+C）
echo   2. 重新编译: cd backend ^&^& mvn clean package
echo   3. 重新启动: mvn spring-boot:run
echo   4. 查看启动日志，查找AuthController相关的错误
echo.

:end
pause

