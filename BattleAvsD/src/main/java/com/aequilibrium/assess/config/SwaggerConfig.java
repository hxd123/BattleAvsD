package com.aequilibrium.assess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Java Backend Developer Technical Test")
                .description("Assessment REST endpoints through transformers' game")
                .license("License Version")
                .version("1.0")
                .contact(new Contact("Bill Huang", "https://www.linkedin.com/in/bill-huang-24a642117/", "csxhuang@yahoo.com"))
                .termsOfServiceUrl("Terms Services")
                .build();
    }

    @Bean
    public Docket assessApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aequilibrium"))
//                .paths(PathSelectors.regex("/transformer.*"))
                .paths(PathSelectors.any())
                .build();
    }
}
