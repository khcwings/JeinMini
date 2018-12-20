package com.jein.mini.common.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/common")
public class CommonController {
	private static final Logger LOG = LoggerFactory.getLogger(CommonController.class);
	
	@RequestMapping(value = {"/popup/windowPopup01"},  method = {RequestMethod.GET, RequestMethod.POST})
	public void getWindowPopup01(Model model, @RequestParam Map<String, Object> param) {
		LOG.info("###### Window Popup 01 : POPUP START ######");
		LOG.info(param.toString());
	}
}
