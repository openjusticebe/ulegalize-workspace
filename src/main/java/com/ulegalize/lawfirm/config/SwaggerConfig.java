package com.ulegalize.lawfirm.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Autowired
    private BuildProperties buildProperties;

    @Bean
    public Docket api() {
        String version = buildProperties.get("displayVersion");

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("lawfirm-micro-services-" + version)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().host("")
                .securityContexts(List.of(securityContext()))
                .securitySchemes(securitySchemes())
                .apiInfo(new ApiInfoBuilder().version(version).title("Documentation API").description("Documentation for Lawfirm API v" + version).build());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Bearer",
                authorizationScopes));
    }

    private List<SecurityScheme> securitySchemes() {
        return List.of(new ApiKey("Bearer", "Authorization", "header"));
    }

}
