package com.jein.mini.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jein.mini.biz.common.domain.CommonMenu;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;

public class PopupCheckInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(PopupCheckInterceptor.class);
	
	@Value("${server.context-path}")
	private String contentPath;
	
	@Autowired
	private CommonMenuRepository menuRepo;
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		boolean bResult = true;
		
		LOG.debug("###### PopupCheckInterceptor preHandle ######");
		
		// Content Root Path Remove
		String menuURL 			= request.getRequestURI();
		LOG.debug("##### Menu URL : " + menuURL);
		if(contentPath != null && !contentPath.isEmpty() && !"/".equals(contentPath)) {
			 menuURL 			= menuURL.replaceAll(contentPath, "");
		}
		LOG.debug("##### Replace Menu URL : " + menuURL);
		
		// 접속 Url이 메뉴 테이블에 등록되어 있는지 조회
		CommonMenu menuInfo = menuRepo.findOneByMenuUrl(menuURL);
		
		if(menuInfo == null) {			// 등록되지 않은 URL로 접근시
			bResult = false;
		} else {						// 등록된 URL로 접근시
			LOG.debug(menuInfo.toString());
			
			// 실제 Thymeleaf Template File Path - Post Handdle에서 삭제
			request.setAttribute("viewPath", menuInfo.getMenuPath());
			
			// Menu Title Name 설정
			request.setAttribute("viewName", menuInfo.getMenuName());
			
			// 서버 수행시간 체크를 위한 시작 시간 설정
			long startTime = System.currentTimeMillis();
			request.setAttribute("startTime", startTime);
		}		
		
		return bResult;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub		
		LOG.debug("###### PopupCheckInterceptor postHandle ######");
		long startTime  = (Long)request.getAttribute("startTime");
		long endTime	= System.currentTimeMillis();
		long executeTime = endTime - startTime;		
		request.removeAttribute("startTime");
		
		modelAndView.setViewName((String)request.getAttribute("viewPath"));
		request.removeAttribute("viewPath");
		
		Map<String, Object> model = modelAndView.getModel();
		LOG.debug(model.toString());
		super.postHandle(request, response, handler, modelAndView);		
		
		LOG.debug("###### PopupCheckInterceptor URL[" + request.getRequestURI() + "], ExcuteTime[" + executeTime + "]");
	}
}