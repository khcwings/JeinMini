package com.jein.mini.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jein.mini.biz.common.persistence.CommonCodeRepository;
import com.jein.mini.constant.CommonConstant;
import com.jein.mini.constant.CommonMessageConstrant;

@Controller
@RequestMapping(value="/admin")
public class AdminController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
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
	
	@PostMapping("/data/codeGrpList")
	@ResponseBody
	public Map<String, Object> getCodeGrpList(@RequestParam Map<String, Object> param) {
		LOG.debug("##### [AdminController-getCodeGrpList] DATA START ######");
		LOG.debug("##### [AdminController-getCodeGrpList] Params => " + param.toString());
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("codeList", codeRepo.findByCodeLevelOrderByIdAsc(1));
		
		createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
		return retMap;
	}
}
