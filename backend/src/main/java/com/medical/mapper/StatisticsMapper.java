package com.medical.mapper;

import com.medical.dto.TrendDataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统计分析Mapper接口
 */
@Mapper
public interface StatisticsMapper {
    
    /**
     * 查询门诊量趋势（最近30天）
     */
    List<TrendDataDTO> selectOutpatientTrend();
    
    /**
     * 查询指定日期范围的门诊量趋势
     */
    List<TrendDataDTO> selectOutpatientTrendByDateRange(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
}

