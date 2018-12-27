package com.jein.mini.common.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.constant.SessionConstant;

public class AbstractController {
	protected void setSessionToParam(HttpSession session, Map<String, Object> params) {
		CommonUser user = (CommonUser)session.getAttribute(SessionConstant.SESSION_USER_INFO_KEY);
		if(user != null && params != null) {
			params.put(SessionConstant.SESSION_PARAM_CREATE_ID_KEY, user.getUserId());
			params.put(SessionConstant.SESSION_PARAM_UPDATE_ID_KEY, user.getUserId());
		}		
	}
	
	/**
	 * 결과값을 Map에 저장하여 리턴한다. 
	 * 
	 * @param retMap
	 * @param resultCode
	 * @param resultMsg
	 * @return
	 */
	protected void createResultMsg(Map<String, Object> retMap, String resultCode, String resultMsg) {
		retMap.put("resultCode", resultCode);
		retMap.put("resultMsg",  resultMsg);
	}
}
