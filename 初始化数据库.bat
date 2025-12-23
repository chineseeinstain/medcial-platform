@echo off
chcp 65001 >nul
echo ========================================
echo 数据库初始化脚本
echo ========================================
echo.

REM 查找MySQL安装路径
set MYSQL_PATH=
if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin
) else if exist "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.1\bin
) else if exist "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set MYSQL_PATH=C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin
) else (
    echo [错误] 未找到MySQL安装路径
    echo 请手动设置MYSQL_PATH变量，或使用完整路径
    echo.
    echo 常见路径：
    echo   C:\Program Files\MySQL\MySQL Server 8.0\bin
    echo   C:\Program Files\MySQL\MySQL Server 8.1\bin
    pause
    exit /b 1
)

echo [√] 找到MySQL: %MYSQL_PATH%\mysql.exe
echo.

REM 检查MySQL服务是否运行
echo [1/4] 检查MySQL服务状态...
sc query MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo [√] MySQL服务正在运行
) else (
    echo [警告] MySQL服务可能未运行
    echo [提示] 请确保MySQL服务已启动
    echo.
)

echo.
echo [2/4] 创建数据库...
echo [提示] 请输入MySQL root用户密码
"%MYSQL_PATH%\mysql.exe" -u root -p -e "CREATE DATABASE IF NOT EXISTS medical_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if %errorlevel% neq 0 (
    echo [错误] 创建数据库失败
    echo [提示] 请检查：
    echo   1. MySQL服务是否已启动
    echo   2. root用户密码是否正确
    pause
    exit /b 1
)
echo [√] 数据库创建成功

echo.
echo [3/4] 初始化数据库表...
echo [提示] 请再次输入MySQL root用户密码
cd /d %~dp0
"%MYSQL_PATH%\mysql.exe" -u root -p medical_platform < database\init.sql
if %errorlevel% neq 0 (
    echo [错误] 初始化数据库表失败
    pause
    exit /b 1
)
echo [√] 数据库表初始化成功

echo.
echo [4/4] 验证数据库...
"%MYSQL_PATH%\mysql.exe" -u root -p -e "USE medical_platform; SHOW TABLES;" medical_platform
if %errorlevel% equ 0 (
    echo [√] 数据库验证成功
) else (
    echo [警告] 数据库验证失败，但可能不影响使用
)

echo.
echo ========================================
echo 数据库初始化完成！
echo ========================================
echo.
echo 下一步：
echo 1. 检查 backend\src\main\resources\application.yml 中的数据库配置
echo 2. 启动后端服务：cd backend ^&^& mvn spring-boot:run
echo.
pause

