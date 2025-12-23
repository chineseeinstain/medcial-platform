package com.medical.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 患者实体类
 * 
 * 作用：对应数据库表patient_visit，存储患者就诊信息
 * 
 * @Data: Lombok注解，自动生成getter/setter/toString等方法
 */
@Data
public class Patient implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 患者ID（脱敏后）
     */
    private String patientId;
    
    /**
     * 就诊日期
     */
    private Date visitDate;
    
    /**
     * 科室
     */
    private String department;
    
    /**
     * 诊断
     */
    private String diagnosis;
    
    /**
     * 费用
     */
    private BigDecimal cost;
}

