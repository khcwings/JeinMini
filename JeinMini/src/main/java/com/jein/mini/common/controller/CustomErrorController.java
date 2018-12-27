package com.jein.mini.common.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Whitelabel 오류 페이지 사용자 정의
 * @author 김희철
 */
@Controller
public class CustomErrorController implements ErrorController {	
	@Value("${page.error.default.src}")
	private String errorDefaultSrc;
	
	@Value("${page.error.404.src}")
	private String error404Src;
	
	@Value("${page.error.500.src}")
	private String error500Src;
			
	@RequestMapping(value = "/error")
	public String error(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	     
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());	     
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return error404Src;
	        } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return error500Src;
	        }
	    }
	    return errorDefaultSrc;		
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
