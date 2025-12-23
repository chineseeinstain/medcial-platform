package com.medical.controller;

import com.medical.common.Result;
import com.medical.entity.Patient;
import com.medical.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者管理Controller
 * 
 * 作用：接收前端HTTP请求，调用Service处理业务，返回JSON响应
 * 
 * @RestController: 标识这是一个RESTful API控制器
 * @RequestMapping: 定义API的基础路径
 */
@Slf4j
@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    /**
     * 查询患者列表
     * GET /api/patients/list
     */
    @GetMapping("/list")
    public Result<List<Patient>> getPatientList() {
        log.info("查询患者列表");
        List<Patient> patients = patientService.getAllPatients();
        return Result.success(patients);
    }
    
    /**
     * 根据ID查询患者详情
     * GET /api/patients/{id}
     */
    @GetMapping("/{id}")
    public Result<Patient> getPatientById(@PathVariable Long id) {
        log.info("查询患者详情，ID: {}", id);
        Patient patient = patientService.getPatientById(id);
        return Result.success(patient);
    }
    
    /**
     * 查询患者就诊记录
     * GET /api/patients/{id}/visits
     */
    @GetMapping("/{id}/visits")
    public Result<List<Patient>> getPatientVisits(@PathVariable Long id) {
        log.info("查询患者就诊记录，ID: {}", id);
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            return Result.error(404, "患者不存在");
        }
        List<Patient> visits = patientService.getPatientVisitsByPatientId(patient.getPatientId());
        log.info("查询到 {} 条就诊记录", visits.size());
        return Result.success(visits);
    }
}

