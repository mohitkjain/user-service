/**
 * Copyright (c) 2021
 * Unauthorised copying of this file, via any medium is strictly prohibited. It is proprietary and confidential.
 *
 * @author: mohitkjain < mohitkjain@outlook.com>
 * Created: 25/07/21
 */

package com.example.user.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig
{
	static final String mediaTypeVersion1 = "application/v1+json";

	@Bean
	public Docket api()
	{
		return new Docket(DocumentationType.SWAGGER_2).groupName("user-service-api-1").select().apis(p ->
		{
			if (p.produces() != null)
			{
				for (MediaType mt : p.produces())
				{
					if (mt.toString().equals(mediaTypeVersion1))
					{
						return true;
					}
				}
			}
			return false;
		}).build().produces(Collections.singleton(mediaTypeVersion1)).apiInfo(new ApiInfoBuilder().version("1").title("User Service API").description(
				"Documentation User Service API v1").build());
	}
}
