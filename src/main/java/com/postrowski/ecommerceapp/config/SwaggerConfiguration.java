package com.postrowski.ecommerceapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    private static final String HOST = "localhost:8080";
    private static final String HTTP_PROTOCOL = "http";
    private static final String CONTENT_TYPE = "application/json";
    private static final String BASE_PACKAGE_IDENTIFIER = "com.postrowski.ecommerceapp";

    @Bean
    public Docket demeterApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE_IDENTIFIER))
                .build()
                .host(HOST)
                .protocols(newHashSet(HTTP_PROTOCOL))
                .produces(new HashSet<>(Arrays.asList(CONTENT_TYPE)))
                .consumes(new HashSet<>(Arrays.asList(CONTENT_TYPE)));
    }
}
