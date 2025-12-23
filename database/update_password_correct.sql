-- ============================================
-- 更新用户密码为正确的BCrypt hash
-- 密码：123456
-- BCrypt Hash: $2a$10$79imhW.J5vjisUumlo9.zO2lmfxqbtiqnidV./7q9Kr5xX0xzI.tq
-- ============================================

USE medical_platform;

-- 更新所有用户密码为123456的正确BCrypt hash
UPDATE user SET password = '$2a$10$79imhW.J5vjisUumlo9.zO2lmfxqbtiqnidV./7q9Kr5xX0xzI.tq' WHERE username IN ('admin', 'doctor1', 'doctor2', 'patient1', 'patient2');

-- 验证更新结果
SELECT username, role, real_name, 
       SUBSTRING(password, 1, 30) AS password_hash_prefix,
       LENGTH(password) AS password_hash_length
FROM user 
WHERE username IN ('admin', 'doctor1', 'doctor2', 'patient1', 'patient2');

SELECT '密码更新完成！所有用户密码已设置为：123456' AS message;

