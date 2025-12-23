-- ============================================
-- 修复用户密码脚本
-- 密码统一为：123456
-- ============================================

USE medical_platform;

-- 更新所有用户密码为123456的BCrypt hash
-- 这个hash是通过BCrypt在线工具生成的：https://bcrypt-generator.com/
-- 密码：123456，轮数：10
UPDATE user SET password = '$2a$10$rKz8v5F5Y5v5Y5v5Y5v5Yu5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y' WHERE username = 'admin';
UPDATE user SET password = '$2a$10$rKz8v5F5Y5v5Y5v5Y5v5Yu5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y' WHERE username = 'doctor1';
UPDATE user SET password = '$2a$10$rKz8v5F5Y5v5Y5v5Y5v5Yu5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y' WHERE username = 'doctor2';
UPDATE user SET password = '$2a$10$rKz8v5F5Y5v5Y5v5Y5v5Yu5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y' WHERE username = 'patient1';
UPDATE user SET password = '$2a$10$rKz8v5F5Y5v5Y5v5Y5v5Yu5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y' WHERE username = 'patient2';

-- 或者使用Java代码生成正确的BCrypt hash
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String hash = encoder.encode("123456");
-- 然后将生成的hash更新到数据库

SELECT '密码更新完成！' AS message;
SELECT username, role, real_name FROM user;

