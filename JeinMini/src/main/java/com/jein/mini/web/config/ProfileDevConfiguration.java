package com.jein.mini.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Profile("dev")
@PropertySources({
	@PropertySource(name="file",   		value="classpath:/properties/file/file-dev.properties"),
	@PropertySource(name="security",   	value="classpath:/properties/security/security-dev.properties")
})
public class ProfileDevConfiguration {

}
