package com.medical.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 趋势数据DTO
 * 用于返回给前端的统计数据
 */
@Data
public class TrendDataDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 日期
     */
    private Date date;
    
    /**
     * 数量（门诊量、费用等）
     */
    private Integer count;
    
    /**
     * 金额（可选）
     */
    private Double amount;
}













