-- ============================================
-- 区域基层医疗机构运营数据分析平台
-- 数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS medical_platform 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE medical_platform;

-- ============================================
-- 1. 患者诊疗表
-- ============================================
CREATE TABLE IF NOT EXISTS patient_visit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    patient_id VARCHAR(50) NOT NULL COMMENT '患者ID（脱敏后）',
    visit_date DATE NOT NULL COMMENT '就诊日期',
    visit_time TIME COMMENT '就诊时间',
    department VARCHAR(50) NOT NULL COMMENT '科室',
    doctor_id VARCHAR(50) COMMENT '医生ID',
    diagnosis VARCHAR(200) COMMENT '诊断',
    disease_code VARCHAR(50) COMMENT '疾病编码（ICD-10）',
    cost DECIMAL(10,2) DEFAULT 0.00 COMMENT '费用',
    payment_method VARCHAR(20) COMMENT '支付方式（现金/医保/自费）',
    visit_type VARCHAR(20) COMMENT '就诊类型（门诊/急诊/住院）',
    status VARCHAR(20) DEFAULT '正常' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_patient_id (patient_id),
    INDEX idx_visit_date (visit_date),
    INDEX idx_department (department),
    INDEX idx_visit_type (visit_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者诊疗表';

-- ============================================
-- 2. 科室运营表
-- ============================================
CREATE TABLE IF NOT EXISTS department_operation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    department VARCHAR(50) NOT NULL COMMENT '科室名称',
    stat_date DATE NOT NULL COMMENT '统计日期',
    outpatient_count INT DEFAULT 0 COMMENT '门诊量',
    inpatient_count INT DEFAULT 0 COMMENT '住院量',
    total_revenue DECIMAL(12,2) DEFAULT 0.00 COMMENT '总收入',
    total_cost DECIMAL(12,2) DEFAULT 0.00 COMMENT '总成本',
    profit DECIMAL(12,2) DEFAULT 0.00 COMMENT '利润',
    avg_visit_time DECIMAL(5,2) COMMENT '平均就诊时长（分钟）',
    bed_utilization_rate DECIMAL(5,2) COMMENT '床位使用率（%）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    UNIQUE KEY uk_dept_date (department, stat_date),
    INDEX idx_stat_date (stat_date),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室运营表';

-- ============================================
-- 3. 医保结算表
-- ============================================
CREATE TABLE IF NOT EXISTS insurance_settlement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    settlement_no VARCHAR(50) NOT NULL COMMENT '结算单号',
    patient_id VARCHAR(50) NOT NULL COMMENT '患者ID（脱敏后）',
    visit_id BIGINT COMMENT '就诊记录ID',
    settlement_date DATE NOT NULL COMMENT '结算日期',
    total_cost DECIMAL(10,2) NOT NULL COMMENT '总费用',
    insurance_pay DECIMAL(10,2) DEFAULT 0.00 COMMENT '医保支付',
    personal_pay DECIMAL(10,2) DEFAULT 0.00 COMMENT '个人支付',
    insurance_type VARCHAR(20) COMMENT '医保类型（城镇职工/城乡居民）',
    drg_group VARCHAR(50) COMMENT 'DRG病组',
    status VARCHAR(20) DEFAULT '已结算' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    UNIQUE KEY uk_settlement_no (settlement_no),
    INDEX idx_patient_id (patient_id),
    INDEX idx_settlement_date (settlement_date),
    INDEX idx_drg_group (drg_group)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医保结算表';

-- ============================================
-- 4. 设备使用表
-- ============================================
CREATE TABLE IF NOT EXISTS equipment_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    equipment_id VARCHAR(50) NOT NULL COMMENT '设备ID',
    equipment_name VARCHAR(100) NOT NULL COMMENT '设备名称',
    department VARCHAR(50) COMMENT '所属科室',
    usage_date DATE NOT NULL COMMENT '使用日期',
    usage_hours DECIMAL(5,2) DEFAULT 0.00 COMMENT '使用时长（小时）',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    maintenance_date DATE COMMENT '维护日期',
    status VARCHAR(20) DEFAULT '正常' COMMENT '设备状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_equipment_id (equipment_id),
    INDEX idx_usage_date (usage_date),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备使用表';

-- ============================================
-- 5. 药品库存表
-- ============================================
CREATE TABLE IF NOT EXISTS drug_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    drug_id VARCHAR(50) NOT NULL COMMENT '药品ID',
    drug_name VARCHAR(100) NOT NULL COMMENT '药品名称',
    drug_type VARCHAR(50) COMMENT '药品类型',
    specification VARCHAR(50) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位',
    current_stock INT DEFAULT 0 COMMENT '当前库存',
    min_stock INT DEFAULT 0 COMMENT '最低库存',
    purchase_price DECIMAL(10,2) COMMENT '进价',
    sale_price DECIMAL(10,2) COMMENT '售价',
    supplier VARCHAR(100) COMMENT '供应商',
    expiry_date DATE COMMENT '有效期',
    status VARCHAR(20) DEFAULT '正常' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_drug_id (drug_id),
    INDEX idx_drug_name (drug_name),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品库存表';

-- ============================================
-- 6. 人员信息表
-- ============================================
CREATE TABLE IF NOT EXISTS staff_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    staff_id VARCHAR(50) NOT NULL COMMENT '员工ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    department VARCHAR(50) COMMENT '科室',
    position VARCHAR(50) COMMENT '职位',
    title VARCHAR(50) COMMENT '职称',
    phone VARCHAR(20) COMMENT '电话',
    email VARCHAR(100) COMMENT '邮箱',
    hire_date DATE COMMENT '入职日期',
    status VARCHAR(20) DEFAULT '在职' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_staff_id (staff_id),
    INDEX idx_department (department),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员信息表';

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入患者就诊记录（测试数据）
INSERT INTO patient_visit (patient_id, visit_date, department, diagnosis, disease_code, cost, visit_type) VALUES
('P001', '2024-01-01', '内科', '高血压', 'I10', 100.50, '门诊'),
('P002', '2024-01-01', '外科', '外伤', 'S00', 200.00, '门诊'),
('P003', '2024-01-02', '内科', '感冒', 'J00', 50.00, '门诊'),
('P004', '2024-01-02', '儿科', '发热', 'R50', 80.00, '门诊'),
('P005', '2024-01-03', '内科', '高血压', 'I10', 100.50, '门诊'),
('P001', '2024-01-05', '内科', '复查', 'Z00', 50.00, '门诊'),
('P006', '2024-01-06', '外科', '手术', 'Z47', 5000.00, '住院'),
('P007', '2024-01-07', '妇科', '产检', 'Z34', 150.00, '门诊'),
('P008', '2024-01-08', '内科', '糖尿病', 'E11', 120.00, '门诊'),
('P009', '2024-01-09', '儿科', '咳嗽', 'R05', 60.00, '门诊'),
('P010', '2024-01-10', '内科', '高血压', 'I10', 100.50, '门诊'),
('P002', '2024-01-11', '外科', '复查', 'Z00', 100.00, '门诊'),
('P011', '2024-01-12', '急诊科', '急性腹痛', 'R10', 300.00, '急诊'),
('P012', '2024-01-13', '内科', '冠心病', 'I25', 200.00, '门诊'),
('P013', '2024-01-14', '儿科', '腹泻', 'A09', 70.00, '门诊'),
('P014', '2024-01-15', '内科', '高血压', 'I10', 100.50, '门诊'),
('P015', '2024-01-16', '外科', '外伤', 'S00', 200.00, '门诊'),
('P016', '2024-01-17', '妇科', '妇科检查', 'Z00', 120.00, '门诊'),
('P017', '2024-01-18', '内科', '感冒', 'J00', 50.00, '门诊'),
('P018', '2024-01-19', '儿科', '发热', 'R50', 80.00, '门诊'),
('P019', '2024-01-20', '内科', '高血压', 'I10', 100.50, '门诊'),
('P020', '2024-01-21', '外科', '手术', 'Z47', 5000.00, '住院'),
('P021', '2024-01-22', '内科', '糖尿病', 'E11', 120.00, '门诊'),
('P022', '2024-01-23', '急诊科', '急性腹痛', 'R10', 300.00, '急诊'),
('P023', '2024-01-24', '内科', '高血压', 'I10', 100.50, '门诊'),
('P024', '2024-01-25', '儿科', '咳嗽', 'R05', 60.00, '门诊'),
('P025', '2024-01-26', '内科', '冠心病', 'I25', 200.00, '门诊'),
('P026', '2024-01-27', '外科', '外伤', 'S00', 200.00, '门诊'),
('P027', '2024-01-28', '妇科', '产检', 'Z34', 150.00, '门诊'),
('P028', '2024-01-29', '内科', '高血压', 'I10', 100.50, '门诊'),
('P029', '2024-01-30', '儿科', '腹泻', 'A09', 70.00, '门诊'),
('P030', '2024-01-31', '内科', '感冒', 'J00', 50.00, '门诊');

-- 插入科室运营数据（测试数据）
-- 使用 INSERT IGNORE 避免重复键错误
INSERT IGNORE INTO department_operation (department, stat_date, outpatient_count, total_revenue, total_cost, profit) VALUES
('内科', '2024-01-01', 5, 5000.00, 3000.00, 2000.00),
('外科', '2024-01-01', 3, 6000.00, 4000.00, 2000.00),
('儿科', '2024-01-01', 4, 3200.00, 2000.00, 1200.00),
('妇科', '2024-01-01', 2, 3000.00, 1800.00, 1200.00),
('急诊科', '2024-01-01', 1, 3000.00, 2000.00, 1000.00);

-- 插入设备使用数据（测试数据）
INSERT INTO equipment_usage (equipment_id, equipment_name, department, usage_date, usage_hours, usage_count) VALUES
('E001', 'CT机', '影像科', '2024-01-01', 8.5, 10),
('E002', 'MRI', '影像科', '2024-01-01', 6.0, 5),
('E003', 'B超', '影像科', '2024-01-01', 10.0, 20),
('E004', 'X光机', '影像科', '2024-01-01', 7.5, 15),
('E005', '心电图', '内科', '2024-01-01', 9.0, 25);

-- 插入药品库存数据（测试数据）
-- 使用 INSERT IGNORE 避免重复键错误
INSERT IGNORE INTO drug_inventory (drug_id, drug_name, drug_type, specification, unit, current_stock, min_stock, purchase_price, sale_price, supplier, expiry_date, status) VALUES
('DRUG001', '阿莫西林胶囊', '抗生素', '0.25g*24粒', '盒', 150, 50, 12.50, 18.00, '华北制药', '2026-12-31', '正常'),
('DRUG002', '布洛芬缓释胶囊', '解热镇痛', '0.3g*20粒', '盒', 200, 80, 15.00, 22.00, '中美天津史克', '2026-06-30', '正常'),
('DRUG003', '复方甘草片', '止咳化痰', '100片', '瓶', 120, 40, 8.50, 12.00, '北京同仁堂', '2026-09-30', '正常'),
('DRUG004', '奥美拉唑肠溶胶囊', '消化系统', '20mg*14粒', '盒', 180, 60, 25.00, 35.00, '阿斯利康', '2026-11-30', '正常'),
('DRUG005', '硝苯地平缓释片', '心血管', '30mg*30片', '盒', 100, 30, 18.00, 28.00, '拜耳医药', '2026-08-31', '正常'),
('DRUG006', '二甲双胍片', '降糖药', '0.25g*30片', '盒', 160, 50, 10.00, 15.00, '中美上海施贵宝', '2026-10-31', '正常'),
('DRUG007', '头孢克肟胶囊', '抗生素', '0.1g*12粒', '盒', 140, 40, 28.00, 42.00, '华北制药', '2026-07-31', '正常'),
('DRUG008', '对乙酰氨基酚片', '解热镇痛', '0.5g*20片', '盒', 250, 100, 5.00, 8.00, '中美天津史克', '2026-12-31', '正常'),
('DRUG009', '蒙脱石散', '止泻药', '3g*10袋', '盒', 90, 30, 12.00, 18.00, '博福-益普生', '2026-05-31', '正常'),
('DRUG010', '维生素C片', '维生素', '0.1g*100片', '瓶', 300, 100, 8.00, 12.00, '华北制药', '2026-12-31', '正常');

-- 插入人员信息（测试数据）
-- 使用 INSERT IGNORE 避免重复键错误
INSERT IGNORE INTO staff_info (staff_id, name, gender, department, position, title) VALUES
('S001', '张医生', '男', '内科', '医生', '主任医师'),
('S002', '李医生', '女', '外科', '医生', '副主任医师'),
('S003', '王医生', '男', '儿科', '医生', '主治医师'),
('S004', '赵医生', '女', '妇科', '医生', '主任医师'),
('S005', '刘医生', '男', '急诊科', '医生', '副主任医师');

-- ============================================
-- 7. 用户表
-- ============================================
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    role VARCHAR(20) DEFAULT 'patient' COMMENT '角色（admin/doctor/patient）',
    status INT DEFAULT 1 COMMENT '状态（1-正常，0-禁用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试用户
-- 密码统一为：123456 (BCrypt加密)
-- 使用BCrypt在线工具生成：https://bcrypt-generator.com/
-- 密码123456的BCrypt hash: $2a$10$rKz8v5F5Y5v5Y5v5Y5v5Yu5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y5v5Y
-- 实际使用正确的BCrypt hash
-- 使用 INSERT IGNORE 避免重复键错误
INSERT IGNORE INTO user (username, password, email, real_name, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC', 'admin@medical.com', '系统管理员', 'admin', 1),
('doctor1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC', 'doctor1@medical.com', '张医生', 'doctor', 1),
('doctor2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC', 'doctor2@medical.com', '李医生', 'doctor', 1),
('patient1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC', 'patient1@medical.com', '王患者', 'patient', 1),
('patient2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC', 'patient2@medical.com', '赵患者', 'patient', 1);

-- ============================================
-- 完成提示
-- ============================================
SELECT '数据库初始化完成！' AS message;
SELECT COUNT(*) AS '患者记录数' FROM patient_visit;
SELECT COUNT(*) AS '科室运营记录数' FROM department_operation;
SELECT COUNT(*) AS '设备使用记录数' FROM equipment_usage;
SELECT COUNT(*) AS '人员信息记录数' FROM staff_info;

