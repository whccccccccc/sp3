package com.atguigu.boot3.crud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiUiConfig {
    /**
     * 配置分组
     *
     * @return
     */
    @Bean
    public GroupedOpenApi uiConfig() {
        return GroupedOpenApi.builder()
                .group("员工管理")
                .pathsToMatch("/emp/**")
                .build();
    }

    @Bean
    public GroupedOpenApi uiConfig2() {
        return GroupedOpenApi.builder()
                .group("部门管理")
                .pathsToMatch("/dept/**")
                .build();
    }

    @Bean
    public OpenAPI docsOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("员工管理").description("员工管理接口文档").license(new License().name("Apache 2.0").url("http://springdoc.org")).version("1.0.0"));
    }
}
