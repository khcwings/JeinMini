package com.jein.mini.web.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class ServletConfiguration extends WebMvcConfigurerAdapter {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.
		addResourceHandler("/css/**").
		addResourceLocations("classpath:static/css/").
		setCachePeriod(31556926).
		resourceChain(false).
		addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
		
		registry.
		addResourceHandler("/js/**").
		addResourceLocations("classpath:static/js/").
		setCachePeriod(31556926).
		resourceChain(false).
		addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));

		registry.
		addResourceHandler("/images/**").
		addResourceLocations("classpath:static/images/").
		setCachePeriod(31556926).
		resourceChain(false).
		addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
	}
/*	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false)
		.favorParameter(true)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_ATOM_XML)
		.mediaType("json", MediaType.APPLICATION_JSON);
	}
	
	@Bean
	public MappingJackson2JsonView mappingJackson2JsonView() {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setBeanName("mappingJackson2JsonView");
		jsonView.setPrefixJson(false);
		return jsonView;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_HTML));
		return converter;
	}*/
}
