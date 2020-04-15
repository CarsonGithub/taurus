package com.taurus.common.config;

import com.taurus.common.constant.CommonConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 *
 * @author 郑楷山
 **/

@EnableSwagger2
@Configuration
@ConditionalOnProperty(value = "swagger.enable", havingValue = "true")
public class SwaggerConfig {

	@Bean
	public Docket createBackendApi() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		tokenPar.name("Authorization")
				.description("Token")
				.modelRef(new ModelRef("string"))
				.parameterType("header")
				.required(false)
				.defaultValue(null)
				.build();
		pars.add(tokenPar.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("后台")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(CommonConstant.BASE_PACKAGE))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(pars);
	}

	// 基本信息
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("API")
				.description("create by 郑楷山")
				.version("1.0")
				.build();
	}

}
