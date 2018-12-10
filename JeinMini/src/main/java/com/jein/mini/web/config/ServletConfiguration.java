package com.jein.mini.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import lombok.extern.java.Log;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@Log
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
	
	@Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

	/*@Bean
	@Description("Thymeleaf template resolver serving HTML 5")
	public ClassLoaderTemplateResolver templateResolver() {
		log.info("##### Thymeleaf template resolver serving HTML 5");
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

		templateResolver.setPrefix("templates/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");        
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");

		return templateResolver;
	}

	@Bean
	@Description("Thymeleaf template engine with Spring integration")
	public SpringTemplateEngine templateEngine() {
		log.info("##### Thymeleaf template engine with Spring integration");
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());

		return templateEngine;
	}

	@Bean
	@Description("Thymeleaf view resolver")
	public ViewResolver viewResolver() {
		log.info("##### Thymeleaf view resolver");
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();

		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");

		return viewResolver;
	}    */
}
