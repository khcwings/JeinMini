package com.jein.mini.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.java.Log;

@Log
public class FileCheckInterceptor extends HandlerInterceptorAdapter {
	
	@Value("${server.context-path}")
	private String contentPath;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		boolean bResult = true;
		
		log.info("###### FileCheckInterceptor preHandle ######");
		
		// Content Root Path Remove
		String menuURL 			= request.getRequestURI();
		log.info("##### Menu URL : " + menuURL);
		if(contentPath != null && !contentPath.isEmpty() && !"/".equals(contentPath)) {
			 menuURL 			= menuURL.replaceAll(contentPath, "");
		}
		log.info("##### Replace Menu URL : " + menuURL);
		
		// 서버 수행시간 체크를 위한 시작 시간 설정
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		
		return bResult;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub		
		log.info("###### FileCheckInterceptor postHandle ######");
		long startTime  = (Long)request.getAttribute("startTime");
		long endTime	= System.currentTimeMillis();
		long executeTime = endTime - startTime;		
		request.removeAttribute("startTime");
		
		super.postHandle(request, response, handler, modelAndView);		
		log.info("###### FileCheckInterceptor URL[" + request.getRequestURI() + "], ExcuteTime[" + executeTime + "]");
	}
}
