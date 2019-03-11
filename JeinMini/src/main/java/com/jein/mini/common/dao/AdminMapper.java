package com.jein.mini.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
	/**
	 * 공통 코드 리스트를 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectCodeList(Map<String, Object> params); 
	
	/**
	 * 공통 코드의 다음 그룹코드 아이디값을 조회
	 * @return
	 */
	public String selectNextCodeGrpId();
	
	/**
	 * 유저 리스트를 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectUserList(Map<String, Object> params);
	
	/**
	 * 모듈 리스트 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectModuleList(Map<String, Object> params);
	
	/**
	 * 메뉴 리스트 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectMenuList(Map<String, Object> params);
}
