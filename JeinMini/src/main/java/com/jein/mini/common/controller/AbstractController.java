package com.jein.mini.common.controller;

import java.util.Map;

public class AbstractController {
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
