package com.jein.mini.common.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/admin")
public class AdminController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
	
	@GetMapping("/view/menuManager")
	public void getMenuManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.info("##### [AdminController-getMenuManager] VIEW START ######");
		LOG.info("##### [AdminController-getMenuManager] Params => " + param.toString());
	}
	
	@GetMapping("/view/codeManager")
	public void getCodeManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.info("##### [AdminController-getCodeManager] VIEW START ######");
		LOG.info("##### [AdminController-getCodeManager] Params => " + param.toString());
	}
}
