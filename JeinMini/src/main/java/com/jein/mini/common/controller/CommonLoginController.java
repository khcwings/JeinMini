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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jein.mini.biz.common.persistence.CommonUserRepository;
import com.jein.mini.constant.CommonConstant;
import com.jein.mini.service.SecurityService;
import com.jein.mini.util.DataUtil;

@Controller
@RequestMapping(value="/common")
public class CommonLoginController {
	private static final Logger LOG = LoggerFactory.getLogger(CommonLoginController.class);
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private CommonUserRepository userRepo;
	
	@Value("${server.context-path}")
	private String contentPath;
	
	@Value("${page.main.url}")
	private String mainPageUrl;
	
	
	/**
	 * 로그인 페이지
	 */
	@GetMapping("/login")
	public ModelAndView getViewLogin() {
		LOG.info("###### Common Login : VIEW START ######");
		ModelAndView mnv = new ModelAndView();
		//mnv.setViewName("redirect:" + mainPageUrl);
		return mnv;
		
	}
	
	/**
	 * 로그인 요청을 처리한다. 
	 */
	@PostMapping("/data/loginProcess")
	@ResponseBody
	public Map<String, Object> executeLoginProcess(HttpSession session, @RequestParam Map<String, Object> param) {
		LOG.info("###### Common Login Process : DATA START ######");
		LOG.info(param.toString());
		
		String userId = DataUtil.getString(param, "userId");
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 1. 로그인 정보(ID, PWD)로 유저 정보 확인
		if(userRepo.countByUserIdAndUserPwd(userId, securityService.getSHA256(DataUtil.getString(param, "userPwd"))) > 0) {
			/* 세션 등록 로직 */
			session.setAttribute("userInfo", userRepo.findOneByUserId(userId));
			
			
			retMap.put("result", CommonConstant.RESULT_SUCCESS);
			
			// 메인 페이지로 Redirect 
			retMap.put(CommonConstant.RESULT_REDIRECT_URL_NAME, (contentPath.isEmpty() || "/".equals(contentPath))?mainPageUrl:(contentPath + mainPageUrl));
		} else {
			retMap.put("result", CommonConstant.RESULT_ERROR);
		}
		
		return retMap;
	}
	
	/**
	 * 메인 페이지
	 */
	@GetMapping("/view/main")
	public void getViewMain() {
		LOG.info("###### Common Main : VIEW START ######");
	}
}
