@echo off
chcp 65001 >nul
echo ========================================
echo 测试后端服务连接
echo ========================================
echo.

echo [1/4] 测试后端健康检查接口...
curl -s http://localhost:8080/health
if %errorlevel% neq 0 (
    echo ❌ 后端服务未启动或无法连接
    echo 请检查：
    echo   1. 后端服务是否已启动（端口8080）
    echo   2. 防火墙是否阻止了连接
    echo.
) else (
    echo ✅ 后端服务正常
    echo.
)

echo [2/4] 测试根路径...
curl -s http://localhost:8080/
if %errorlevel% neq 0 (
    echo ❌ 根路径访问失败
    echo.
) else (
    echo ✅ 根路径正常
    echo.
)

echo [3/4] 测试登录接口（POST）...
curl -s -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"123456\"}"
if %errorlevel% neq 0 (
    echo ❌ 登录接口访问失败
    echo.
) else (
    echo ✅ 登录接口可访问
    echo.
)

echo [4/4] 检查所有API路径...
echo 可用的API路径：
echo   - GET  http://localhost:8080/
echo   - GET  http://localhost:8080/health
echo   - GET  http://localhost:8080/test
echo   - POST http://localhost:8080/api/auth/login
echo   - POST http://localhost:8080/api/auth/register
echo   - GET  http://localhost:8080/api/auth/me
echo.

echo ========================================
echo 测试完成
echo ========================================
pause

