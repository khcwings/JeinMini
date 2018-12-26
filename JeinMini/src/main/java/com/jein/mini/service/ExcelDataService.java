package com.jein.mini.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jein.mini.common.service.CommonMenuService;
import com.jein.mini.constant.ExcelConstant;
import com.jein.mini.util.DataUtil;

@Service
public class ExcelDataService {
	@Autowired
	private CommonMenuService commonMenuService;
	
	public List<Map<String, Object>> getExcelDataList(Map<String, Object> param) {
		List<Map<String, Object>> retList = null;
		
		String excelDataCode = DataUtil.getString(param, ExcelConstant.EXCEL_DATA_CODE);
		switch (excelDataCode) {
		case "CM0001":
			retList = commonMenuService.getMenuList();
			break;

		default:
			break;
		}
		
		return retList;
	}
}
