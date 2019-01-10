package com.jein.mini.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.biz.common.persistence.CommonUserRepository;
import com.jein.mini.common.service.CommonMenuService;
import com.jein.mini.constant.CommonConstant;
import com.jein.mini.constant.SessionConstant;
import com.jein.mini.service.SecurityService;
import com.jein.mini.util.DataUtil;

@Controller
@RequestMapping(value="/common")
public class CommonLoginController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(CommonLoginController.class);

	@Autowired
	private SecurityService securityService;

	@Autowired
	private CommonUserRepository userRepo;

	@Autowired
	private CommonMenuService commonMenuService;

	@Value("${server.context-path}")
	private String contentPath;
	
	@Value("${page.login.url}")
	private String loginUrl;

	@Value("${page.main.url}")
	private String mainPageUrl;

	/**
	 * 로그인 페이지
	 */
	@GetMapping("/view/login")
	public ModelAndView getViewLogin(HttpSession session) {
		LOG.info("##### [CommonLoginController-getViewLogin] VIEW START ######");
		ModelAndView mnv = new ModelAndView();
		
		// 로그인이 되어 있다면 메인 화면으로 전환한다. 
		CommonUser userInfo		= (CommonUser)session.getAttribute(SessionConstant.SESSION_USER_INFO_KEY);
		if(userInfo != null) {
			mnv.setViewName("redirect:" + mainPageUrl);
		}
		return mnv;
	}
	
	/**
	 * 로그아웃 페이지
	 */
	@GetMapping("/view/logout")
	public ModelAndView getViewLogout(HttpSession session) {
		LOG.info("##### [CommonLoginController-getViewLogout] VIEW START ######");
		ModelAndView mnv = new ModelAndView();
		
		// 로그인이 되어 있는 경우에만 로그아웃 로직을 수행한다. 
		CommonUser userInfo		= (CommonUser)session.getAttribute(SessionConstant.SESSION_USER_INFO_KEY);
		if(userInfo != null) {
			// Session Clear
			session.invalidate();
		} else {
			mnv.setViewName("redirect:" + loginUrl);
		}
		return mnv;
	}

	/**
	 * 메인 페이지
	 */
	@GetMapping("/view/main")
	public void getViewMain() {
		LOG.info("###### Common Main : VIEW START ######");
		
	}
	
	/**
	 * 로그인 요청을 처리한다. 
	 */
	@PostMapping("/data/loginProcess")
	@ResponseBody
	public Map<String, Object> executeLoginProcess(HttpSession session, @RequestBody Map<String, Object> params) {
		LOG.info("###### Common Login Process : DATA START ######");
		LOG.info(params.toString());

		// Param 정보에 User Id 추가
		setSessionToParam(session, params); 

		String userId = DataUtil.getString(params, "userId");
		Map<String, Object> retMap = new HashMap<String, Object>();

		// 1. 로그인 정보(ID, PWD)로 유저 정보 확인
		if(userRepo.countByUserIdAndUserPwd(userId, securityService.getSHA256(DataUtil.getString(params, "userPwd"))) > 0) {
			/* 세션에 유저 정보 등록 */
			session.setAttribute(SessionConstant.SESSION_USER_INFO_KEY, userRepo.findOneByUserId(userId));

			/* 세션에 메뉴 정보 등록 */
			session.setAttribute(SessionConstant.SESSION_MENU_LIST_KEY, commonMenuService.getMenuList(params));

			retMap.put("result", CommonConstant.RESULT_SUCCESS);

			// 메인 페이지로 Redirect 
			retMap.put(CommonConstant.RESULT_REDIRECT_URL_NAME, (contentPath.isEmpty() || "/".equals(contentPath))?mainPageUrl:(contentPath + mainPageUrl));
		} else {
			retMap.put("result", CommonConstant.RESULT_ERROR);
		}

		return retMap;
	}

	
}
