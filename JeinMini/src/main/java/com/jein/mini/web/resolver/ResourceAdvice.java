package com.jein.mini.web.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

@ControllerAdvice
public class ResourceAdvice {

	@Autowired
	private ResourceUrlProvider resourceUrlProvider;
	
	@ModelAttribute("rUrl")
	public ResourceUrlProvider versionResourceResolver() {
		return this.resourceUrlProvider;
	}
}
