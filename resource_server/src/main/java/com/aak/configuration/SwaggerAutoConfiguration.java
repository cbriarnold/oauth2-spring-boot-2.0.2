package com.aak.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
public class SwaggerAutoConfiguration {

	@Bean
	public SecurityConfiguration security() {
		SecurityConfigurationBuilder config = SecurityConfigurationBuilder.builder();

		return config	.scopeSeparator(" ")
						.useBasicAuthenticationWithAccessCodeGrant(true)
						.build();
	}

	@Bean
	public Docket docket() {
		ApiSelectorBuilder apiBuilder = new Docket(
				DocumentationType.SWAGGER_2).select()
											.apis(RequestHandlerSelectors.any());

		apiBuilder.paths(PathSelectors.regex("/v.*"));
		return apiBuilder	.build()
							.apiInfo(apiInfo())
							.securitySchemes(Arrays.asList(
									clientCredentialsSecurityScheme()))
							.securityContexts(Arrays.asList(securityContext()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("test", "test", "test", "test",
				new Contact("", "", "test@test.com"), "test", "test",
				Arrays.asList());
	}

	private String getAccessTokenUri() {
		String accessTokenUri = "http://localhost:8081/oauth/token/";
		return accessTokenUri;
	}

	private SecurityScheme clientCredentialsSecurityScheme() {
		ClientCredentialsGrant grantType2 = new ClientCredentialsGrant(
				getAccessTokenUri());

		SecurityScheme oauth = new OAuthBuilder()	.name(
				"client_credential_scheme")
													.grantTypes(Arrays.asList(
															grantType2))
													.scopes(Arrays.asList(
															scopes()))
													.build();
		return oauth;
	}

	private SecurityContext securityContext() {
		return SecurityContext	.builder()
								.securityReferences(Arrays.asList(
										
										new SecurityReference("password_scheme",
												scopes())))
								.forPaths(PathSelectors.regex("/v.*"))
								.build();
	}

	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scopes = {};
		return scopes;
	}
}
