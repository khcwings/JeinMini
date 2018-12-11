package com.jein.mini.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

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
}
