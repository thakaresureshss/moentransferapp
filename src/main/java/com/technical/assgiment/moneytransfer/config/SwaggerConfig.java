package com.technical.assgiment.moneytransfer.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class configure swagger for application.
 * 
 * @author Suresh Thakare
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.technical.assgiment.moneytransfer.rest"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.securitySchemes(Collections.singletonList(apiKey()));
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Authorization", "header");
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Money Transfer API")
				.description("This API manages oeprations related to Banking Money Transfer").build();
	}


}
