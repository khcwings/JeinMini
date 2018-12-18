package com.jein.mini.biz.common.vo;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultVO {
	private String resultCd;
	private String resultMsg;
	private Map<String, Object> resultMap;
	
	public void setStatus(String resultCd, String resultMsg) {
		this.setResultCd(resultCd);
		this.setResultMsg(resultMsg);
	}
}
