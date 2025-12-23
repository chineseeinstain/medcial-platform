package com.medical.mapper;

import com.medical.entity.Patient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 患者数据访问Mapper接口
 * 
 * 作用：定义数据库操作方法，对应SQL映射文件
 * 
 * @Mapper: 标识这是MyBatis的Mapper接口
 */
@Mapper
public interface PatientMapper {
    
    /**
     * 查询所有患者
     * 对应SQL: mapper/PatientMapper.xml中的selectAll
     */
    List<Patient> selectAll();
    
    /**
     * 根据ID查询患者
     */
    Patient selectById(@Param("id") Long id);
    
    /**
     * 插入患者
     */
    int insert(Patient patient);
    
    /**
     * 更新患者信息
     */
    int update(Patient patient);
    
    /**
     * 删除患者
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据患者ID查询所有就诊记录
     */
    List<Patient> selectByPatientId(@Param("patientId") String patientId);
}

