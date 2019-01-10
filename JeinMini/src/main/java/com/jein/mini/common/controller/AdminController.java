package com.jein.mini.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jein.mini.biz.common.domain.CommonCode;
import com.jein.mini.biz.common.persistence.CommonCodeRepository;
import com.jein.mini.common.service.AdminService;
import com.jein.mini.constant.CommonConstant;
import com.jein.mini.constant.CommonMessageConstrant;
import com.jein.mini.util.DataUtil;

@Controller
@RequestMapping(value="/admin")
public class AdminController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CommonCodeRepository codeRepo;

	@GetMapping("/view/menuManager")
	public void getMenuManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.debug("##### [AdminController-getMenuManager] VIEW START ######");
		LOG.debug("##### [AdminController-getMenuManager] Params => " + param.toString());
	}

	@GetMapping("/view/codeManager")
	public void getCodeManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.debug("##### [AdminController-getCodeManager] VIEW START ######");
		LOG.debug("##### [AdminController-getCodeManager] Params => " + param.toString());
	}

	@PostMapping("/data/getCodeList")
	@ResponseBody
	public Map<String, Object> getCodeList(@RequestBody Map<String, Object> param) {
		LOG.debug("##### [AdminController-getCodeGrpList] DATA START ######");
		LOG.debug("##### [AdminController-getCodeGrpList] Params => " + param.toString());

		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 서비스 조회
		retMap.put("codeList", adminService.getCodeList(param));
				
		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
	}

	@PostMapping("/data/setCodeGrp")
	@ResponseBody
	public Map<String, Object> setCodeGrp(@RequestBody Map<String, Object> param) {
		LOG.debug("##### [AdminController-setCodeGrp] DATA START ######");
		LOG.debug("##### [AdminController-setCodeGrp] Params => " + param.toString());
		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 서비스 구분
		String serviceType 	= DataUtil.getString(param, "serviceType");
		List<Map<String, Object>> codeGrpList = DataUtil.getList(param, "codeGrpIdList");
		LOG.debug("##### [AdminController-setCodeGrp] codeGrpList => " + codeGrpList.toString());
		

		if("C".equalsIgnoreCase(serviceType)) {				// 생성
			//codeRepo.save(code);	
		} else if("U".equalsIgnoreCase(serviceType)) {		// 수정
			//codeRepo.save(code);		
		} else if("D".equalsIgnoreCase(serviceType) && !codeGrpList.isEmpty()) {		// 삭제
			for(Map<String, Object> item : codeGrpList) {
				codeRepo.deleteCodeGrpId(DataUtil.getString(item, "codeGrpId"));
			}
		} 

		createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);

		return retMap;
	}
}
