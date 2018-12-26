package com.jein.mini.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jein.mini.common.dao.CommonMenuMapper;


@Service
public class CommonMenuService {
	
	@Autowired
	private CommonMenuMapper commonMenuMapper;
	
	public List<Map<String, Object>> getMenuList() {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		try {
			retList = commonMenuMapper.selectMenuList();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retList;
	}
}
