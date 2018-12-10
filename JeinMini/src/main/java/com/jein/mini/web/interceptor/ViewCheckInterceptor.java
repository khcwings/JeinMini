package com.jein.mini.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jein.mini.biz.common.domain.CommonMenu;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;

import lombok.extern.java.Log;

/**
 * 화면 요청에 대한 Interceptor
 * 
 * @author JEINSOFT
 */
@Log
public class ViewCheckInterceptor extends HandlerInterceptorAdapter {
	
	//@Value("${server.context-path}")
	//private String contentPath;
	
	@Autowired
	private CommonMenuRepository menuRepo;
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		boolean bResult = true;
		
		log.info("###### ViewCheckInterceptor preHandle ######");
		String uri 				= request.getRequestURI();
		//String menuURL 			= uri.replaceAll(contextRoot, "");
		log.info("##### URL : " + uri);
	//	log.info("##### contentPath : " + contentPath);
		if(menuRepo ==  null) {
			log.info("IS NULL");
		}
		
		CommonMenu menuInfo = menuRepo.findOneByMenuUrl(uri);
		if(menuInfo == null) {
			bResult = false;
		} else {
			log.info(menuInfo.toString());
			request.setAttribute("viewPath", menuInfo.getMenuPath());
			
			long startTime = System.currentTimeMillis();
			request.setAttribute("startTime", startTime);
		}
		
		
		
		return bResult;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub		
		log.info("###### ViewCheckInterceptor postHandle ######");
		long startTime  = (Long)request.getAttribute("startTime");
		long endTime	= System.currentTimeMillis();
		long executeTime = endTime - startTime;
		
		request.removeAttribute("startTime");
		
		modelAndView.setViewName((String)request.getAttribute("viewPath"));
		
		request.removeAttribute("startTime");
		request.removeAttribute("viewPath");
		super.postHandle(request, response, handler, modelAndView);		
		
		log.info("###### ViewCheckInterceptor URL[" + request.getRequestURI() + "], ExcuteTime[" + executeTime + "]");
	}
}
