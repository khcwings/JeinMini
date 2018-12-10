package com.jein.mini.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.java.Log;

@Log
public class DataCheckInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		log.info("###### DataCheckInterceptor preHandle ######");
		String uri 				= request.getRequestURI();
		log.info("##### URL : " + uri);
		
		//log.info("##### menuURL : " + menuURL);
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub		
		log.info("###### DataCheckInterceptor postHandle ######");
		long startTime  = (Long)request.getAttribute("startTime");
		long endTime	= System.currentTimeMillis();
		long executeTime = endTime - startTime;
		
		request.removeAttribute("startTime");
		
		//modelAndView.setViewName("/sample/sample01");
		super.postHandle(request, response, handler, modelAndView);
		
		log.info("###### DataCheckInterceptor URL[" + request.getRequestURI() + "], ExcuteTime[" + executeTime + "]");
	}
}
