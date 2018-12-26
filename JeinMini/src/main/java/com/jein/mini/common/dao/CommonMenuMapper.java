package com.jein.mini.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMenuMapper {
	public List<Map<String, Object>> selectMenuList();
}
