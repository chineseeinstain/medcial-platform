package com.medical.controller;

import com.medical.common.Result;
import com.medical.dto.TrendDataDTO;
import com.medical.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析Controller
 */
@RestController
@RequestMapping("/api/statistics")
@Slf4j
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    /**
     * 获取门诊量趋势
     * GET /api/statistics/outpatient-trend
     */
    @GetMapping("/outpatient-trend")
    public Result<List<TrendDataDTO>> getOutpatientTrend() {
        log.info("查询门诊量趋势");
        List<TrendDataDTO> data = statisticsService.getOutpatientTrend();
        return Result.success(data);
    }
    
    /**
     * 获取统计数据概览
     * GET /api/statistics/overview
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        log.info("查询统计数据概览");
        Map<String, Object> overview = statisticsService.getOverview();
        return Result.success(overview);
    }
    
    /**
     * 获取科室分布统计
     * GET /api/statistics/department-distribution
     */
    @GetMapping("/department-distribution")
    public Result<List<Map<String, Object>>> getDepartmentDistribution() {
        log.info("查询科室分布统计");
        List<Map<String, Object>> data = statisticsService.getDepartmentDistribution();
        return Result.success(data);
    }
    
    /**
     * 获取医保控费分析
     * GET /api/statistics/insurance-cost-control
     */
    @GetMapping("/insurance-cost-control")
    public Result<Map<String, Object>> getInsuranceCostControl() {
        log.info("查询医保控费分析");
        Map<String, Object> data = statisticsService.getInsuranceCostControl();
        return Result.success(data);
    }
    
    /**
     * 获取设备使用率
     * GET /api/statistics/equipment-usage
     */
    @GetMapping("/equipment-usage")
    public Result<List<Map<String, Object>>> getEquipmentUsage() {
        log.info("查询设备使用率");
        List<Map<String, Object>> data = statisticsService.getEquipmentUsage();
        return Result.success(data);
    }
}

