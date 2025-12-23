package com.medical.service;

import com.medical.entity.Patient;
import com.medical.mapper.PatientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 患者业务逻辑Service
 * 
 * 作用：处理业务逻辑，调用Mapper操作数据库
 * 
 * @Service: 标识这是一个业务逻辑类，Spring会自动管理
 */
@Slf4j
@Service
public class PatientService {
    
    @Autowired
    private PatientMapper patientMapper;
    
    /**
     * 查询所有患者
     * 注意：移除了@Cacheable注解，避免Redis不可用时的连接错误
     * 如果将来需要缓存，可以手动实现类似StatisticsService的缓存逻辑
     */
    public List<Patient> getAllPatients() {
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"PatientService.java:31\",\"message\":\"getAllPatients called\",\"data\":{\"mapper\":\"%s\"},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis(),
                patientMapper != null ? "not null" : "null"
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        
        log.info("从数据库查询所有患者");
        List<Patient> result = patientMapper.selectAll();
        
        // #region agent log
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"PatientService.java:34\",\"message\":\"getAllPatients result\",\"data\":{\"count\":%d},\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"E\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                System.currentTimeMillis(),
                result != null ? result.size() : 0
            );
            try (java.io.FileWriter writer = new java.io.FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {}
        // #endregion
        
        return result;
    }
    
    /**
     * 根据ID查询患者
     */
    public Patient getPatientById(Long id) {
        log.info("查询患者，ID: {}", id);
        return patientMapper.selectById(id);
    }
    
    /**
     * 根据患者ID查询所有就诊记录
     */
    public List<Patient> getPatientVisitsByPatientId(String patientId) {
        log.info("查询患者就诊记录，患者ID: {}", patientId);
        return patientMapper.selectByPatientId(patientId);
    }
}

