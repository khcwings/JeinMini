package com.jein.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
	@PropertySource(name="common",   value="classpath:/properties/common.properties")
})
public class JeinMiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(JeinMiniApplication.class, args);
	}
}
