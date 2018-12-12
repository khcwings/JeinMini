package com.jein.mini.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Profile("local")
@PropertySources({
	@PropertySource(name="file",   value="classpath:/properties/file/file-local.properties")
})
public class ProfileLocalConfiguration {

} 
