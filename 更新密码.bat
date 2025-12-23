@echo off
chcp 65001 >nul
echo ============================================
echo 更新用户密码脚本
echo ============================================
echo.
echo 正在执行SQL脚本更新密码...
echo.

mysql -u root -p < database\update_password_correct.sql

echo.
echo ============================================
echo 密码更新完成！
echo ============================================
echo.
echo 所有用户密码已设置为：123456
echo 请重新测试登录功能
echo.
pause

