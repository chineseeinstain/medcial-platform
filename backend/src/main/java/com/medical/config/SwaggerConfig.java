package com.medical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.FileWriter;
import java.time.Instant;

/**
 * Swagger配置类
 * 配置API文档
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    
    private static void logDebug(String location, String message, Object data, String hypothesisId) {
        try {
            String logPath = "d:\\code\\java\\MDEICAL-PLATFORM\\.cursor\\debug.log";
            String logEntry = String.format(
                "{\"id\":\"log_%d_%s\",\"timestamp\":%d,\"location\":\"%s\",\"message\":\"%s\",\"data\":%s,\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"%s\"}\n",
                System.currentTimeMillis(),
                Integer.toHexString((int)(Math.random() * 1000000)),
                Instant.now().toEpochMilli(),
                location,
                message,
                data != null ? "\"" + data.toString().replace("\"", "\\\"") + "\"" : "null",
                hypothesisId
            );
            try (FileWriter writer = new FileWriter(logPath, true)) {
                writer.write(logEntry);
            }
        } catch (Exception e) {
            // 忽略日志写入错误
        }
    }
    
    @Bean
    public Docket createRestApi() {
        // #region agent log
        logDebug("SwaggerConfig.java:35", "Creating Swagger Docket", "DocumentationType.OAS_30", "B");
        // #endregion
        
        try {
            // #region agent log
            logDebug("SwaggerConfig.java:38", "Before Docket creation", "starting", "B");
            // #endregion
            
            Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(true)  // 显式启用Swagger
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.medical.controller"))
                .paths(PathSelectors.any())
                .build();
            
            // #region agent log
            logDebug("SwaggerConfig.java:48", "Swagger Docket created", "success", "B");
            // #endregion
            
            return docket;
        } catch (Exception e) {
            // #region agent log
            logDebug("SwaggerConfig.java:53", "Swagger Docket creation failed", "exception=" + e.getClass().getName() + ": " + e.getMessage(), "B");
            // #endregion
            throw e;
        }
    }
    
    private ApiInfo apiInfo() {
        // #region agent log
        logDebug("SwaggerConfig.java:58", "Creating ApiInfo", "building", "B");
        // #endregion
        
        try {
            ApiInfo info = new ApiInfoBuilder()
                .title("区域基层医疗机构运营数据分析平台 API文档")
                .description("API接口文档")
                .version("1.0.0")
                .build();
            
            // #region agent log
            logDebug("SwaggerConfig.java:67", "ApiInfo created", "success", "B");
            // #endregion
            
            return info;
        } catch (Exception e) {
            // #region agent log
            logDebug("SwaggerConfig.java:72", "ApiInfo creation failed", "exception=" + e.getClass().getName() + ": " + e.getMessage(), "B");
            // #endregion
            throw e;
        }
    }
    
    /**
     * 修复Swagger 3.0.0与Spring Boot 2.7+的兼容性问题
     * 解决documentationPluginsBootstrapper的NullPointerException
     */
    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}


