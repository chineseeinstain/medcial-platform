-- ============================================
-- 更新用户密码脚本
-- 密码统一为：123456
-- ============================================
-- 注意：这个脚本需要在应用启动后，通过API注册用户或使用Java代码生成正确的BCrypt hash
-- 或者运行 PasswordGenerator.java 生成hash后更新

USE medical_platform;

-- 方法1：删除旧用户，重新注册（推荐）
-- DELETE FROM user WHERE username IN ('admin', 'doctor1', 'doctor2', 'patient1', 'patient2');

-- 方法2：使用Java代码生成BCrypt hash后更新
-- UPDATE user SET password = '生成的BCrypt hash' WHERE username = 'admin';
-- UPDATE user SET password = '生成的BCrypt hash' WHERE username = 'doctor1';
-- UPDATE user SET password = '生成的BCrypt hash' WHERE username = 'doctor2';
-- UPDATE user SET password = '生成的BCrypt hash' WHERE username = 'patient1';
-- UPDATE user SET password = '生成的BCrypt hash' WHERE username = 'patient2';

-- 临时方案：使用简单的密码hash（仅用于测试，生产环境必须使用BCrypt）
-- 密码：123456 的简单MD5（不推荐，仅用于快速测试）
-- UPDATE user SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC' WHERE username IN ('admin', 'doctor1', 'doctor2', 'patient1', 'patient2');

SELECT '请使用Java代码生成正确的BCrypt hash后更新密码' AS message;

