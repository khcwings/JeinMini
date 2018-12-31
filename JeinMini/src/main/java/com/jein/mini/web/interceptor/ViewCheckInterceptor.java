package com.jein.mini.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jein.mini.biz.common.domain.CommonMenu;
import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;
import com.jein.mini.constant.SessionConstant;
import com.jein.mini.util.DataUtil;

/**
 * 화면 요청에 대한 Interceptor
 * 
 * @author JEINSOFT
 */
public class ViewCheckInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(ViewCheckInterceptor.class);
	
	@Value("${server.context-path}")
	private String contentPath;
	
	@Value("${page.login.url}")
	private String loginUrl;
	
	@Autowired
	private CommonMenuRepository menuRepo;
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		boolean bResult 		= true;	// 최종 체크 결과		
		LOG.debug("##### [ViewCheckInterceptor-preHandle] #####");
		
		// Content Root Path Remove
		String menuURL 			= request.getRequestURI();
		if(contentPath != null && !contentPath.isEmpty() && !"/".equals(contentPath)) {
			 menuURL 			= menuURL.replaceAll(contentPath, "");
		}
		LOG.debug("##### Menu URL : " + menuURL);
		
		HttpSession session 	= DataUtil.getSession(request);
		CommonUser userInfo		= (CommonUser)session.getAttribute(SessionConstant.SESSION_USER_INFO_KEY);
		
		// 접속 Url이 메뉴 테이블에 등록되어 있는지 조회
		CommonMenu menuInfo = menuRepo.findOneByMenuUrl(menuURL);
		
		if(menuInfo == null) {												// 서버에 등록되지 않은 URL로 접근시
			bResult = false;
		} else if(menuInfo.getLoginYn() == 'Y' && userInfo == null) {		// 필수 로그인 여부 확인
			bResult = false;
			response.sendRedirect(contentPath + loginUrl);
		} else {															// 등록된 URL로 접근시			
			LOG.debug(menuInfo.toString());
			request.setAttribute("viewPath", menuInfo.getMenuPath());			// 실제 Thymeleaf Template File Path - Post Handdle에서 삭제
			request.setAttribute("viewMenuId", menuInfo.getMenuId());			// Menu Id 
			request.setAttribute("viewMenuName", menuInfo.getMenuName());		// Menu Title Name 설정
			request.setAttribute("startTime", System.currentTimeMillis());		// 서버 수행시간 체크를 위한 시작 시간 설정
		}		
		
		return bResult;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		LOG.debug("##### [ViewCheckInterceptor-postHandle] #####");
		
		long startTime  = (Long)request.getAttribute("startTime");
		long endTime	= System.currentTimeMillis();
		long executeTime = endTime - startTime;		
		request.removeAttribute("startTime");
		
		String viewPath =(String)request.getAttribute("viewPath");
		if(modelAndView.getViewName().indexOf("redirect") != 0) {
			modelAndView.setViewName(viewPath);
		}
		request.removeAttribute("viewPath");
		
		super.postHandle(request, response, handler, modelAndView);				
		LOG.debug("##### [ViewCheckInterceptor-postHandle] URL[" + request.getRequestURI() + "], ExcuteTime[" + executeTime + "]");
	}
}
