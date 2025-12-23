package com.medical.service;

import com.medical.dto.TrendDataDTO;
import com.medical.entity.Patient;
import com.medical.mapper.PatientMapper;
import com.medical.mapper.StatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 统计分析Service
 */
@Service
@Slf4j
public class StatisticsService {
    
    @Autowired
    private StatisticsMapper statisticsMapper;
    
    @Autowired
    private PatientMapper patientMapper;
    
    @Autowired(required = false)  // Redis不可用时，redisTemplate可以为null
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 获取门诊量趋势数据
     * 
     * 流程：
     * 1. 先查Redis缓存
     * 2. 缓存没有 → 查数据库
     * 3. 存入Redis缓存（5分钟过期）
     */
    public List<TrendDataDTO> getOutpatientTrend() {
        String cacheKey = "statistics:outpatient-trend";
        
        // 1. 先查缓存（如果Redis可用）
        if (redisTemplate != null) {
            try {
                @SuppressWarnings("unchecked")
                List<TrendDataDTO> cached = (List<TrendDataDTO>) redisTemplate.opsForValue().get(cacheKey);
                
                if (cached != null) {
                    log.info("从Redis缓存获取门诊量趋势数据");
                    return cached;
                }
            } catch (Exception e) {
                log.warn("Redis缓存不可用，直接查询数据库: {}", e.getMessage());
            }
        }
        
        // 2. 缓存没有，查数据库
        log.info("从数据库查询门诊量趋势数据");
        List<TrendDataDTO> data = statisticsMapper.selectOutpatientTrend();
        
        // 3. 存入缓存（5分钟过期，如果Redis可用）
        if (redisTemplate != null) {
            try {
                if (data != null && !data.isEmpty()) {
                    redisTemplate.opsForValue().set(cacheKey, data, 5, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                log.warn("Redis缓存写入失败，跳过缓存: {}", e.getMessage());
            }
        }
        
        return data;
    }
    
    /**
     * 获取统计数据概览
     */
    public Map<String, Object> getOverview() {
        List<Patient> allPatients = patientMapper.selectAll();
        
        // 总患者数（去重）
        long totalPatients = allPatients.stream()
            .map(Patient::getPatientId)
            .distinct()
            .count();
        
        // 今日门诊量
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long todayVisits = allPatients.stream()
            .filter(p -> p.getVisitDate() != null && 
                p.getVisitDate().toString().startsWith(today))
            .count();
        
        // 科室数量（去重）
        long departmentCount = allPatients.stream()
            .map(Patient::getDepartment)
            .filter(d -> d != null && !d.isEmpty())
            .distinct()
            .count();
        
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalPatients", totalPatients);
        overview.put("todayVisits", todayVisits);
        overview.put("departmentCount", departmentCount);
        overview.put("totalVisits", allPatients.size());
        
        return overview;
    }
    
    /**
     * 获取科室分布统计
     */
    public List<Map<String, Object>> getDepartmentDistribution() {
        List<Patient> allPatients = patientMapper.selectAll();
        
        // 按科室分组统计
        Map<String, Long> departmentMap = allPatients.stream()
            .filter(p -> p.getDepartment() != null && !p.getDepartment().isEmpty())
            .collect(Collectors.groupingBy(
                Patient::getDepartment,
                Collectors.counting()
            ));
        
        // 转换为List<Map>
        return departmentMap.entrySet().stream()
            .map(entry -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey());
                item.put("value", entry.getValue());
                return item;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 获取医保控费分析数据（最近6个月）
     */
    public Map<String, Object> getInsuranceCostControl() {
        // 查询最近6个月的医保结算数据
        // 这里简化处理，直接查询数据库
        // 实际应该通过Mapper查询，但为了快速实现，我们使用Service直接查询
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟数据（实际应该从数据库查询）
        // 这里返回一个结构化的数据，前端可以展示
        List<Map<String, Object>> monthlyData = new java.util.ArrayList<>();
        
        // 生成最近6个月的数据
        for (int i = 5; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusMonths(i);
            String month = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            // 模拟数据，实际应该从insurance_settlement表查询
            monthData.put("insurancePay", 100 + i * 10); // 医保支付（万元）
            monthData.put("personalPay", 60 + i * 5);    // 个人支付（万元）
            monthlyData.add(monthData);
        }
        
        result.put("monthlyData", monthlyData);
        result.put("totalInsurancePay", monthlyData.stream()
            .mapToDouble(m -> ((Number) m.get("insurancePay")).doubleValue())
            .sum());
        result.put("totalPersonalPay", monthlyData.stream()
            .mapToDouble(m -> ((Number) m.get("personalPay")).doubleValue())
            .sum());
        
        return result;
    }
    
    /**
     * 获取设备使用率数据
     */
    public List<Map<String, Object>> getEquipmentUsage() {
        // 模拟数据，实际应该从equipment_usage表查询
        // 计算每个设备的使用率（使用时长/总时长 * 100）
        List<Map<String, Object>> equipmentList = new java.util.ArrayList<>();
        
        Map<String, Object> equipment1 = new HashMap<>();
        equipment1.put("name", "CT机");
        equipment1.put("usageRate", 85);
        equipmentList.add(equipment1);
        
        Map<String, Object> equipment2 = new HashMap<>();
        equipment2.put("name", "MRI");
        equipment2.put("usageRate", 75);
        equipmentList.add(equipment2);
        
        Map<String, Object> equipment3 = new HashMap<>();
        equipment3.put("name", "B超");
        equipment3.put("usageRate", 90);
        equipmentList.add(equipment3);
        
        Map<String, Object> equipment4 = new HashMap<>();
        equipment4.put("name", "X光机");
        equipment4.put("usageRate", 65);
        equipmentList.add(equipment4);
        
        Map<String, Object> equipment5 = new HashMap<>();
        equipment5.put("name", "心电图");
        equipment5.put("usageRate", 80);
        equipmentList.add(equipment5);
        
        return equipmentList;
    }
}

