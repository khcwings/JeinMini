package com.jein.mini.common.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/common")
public class CommonController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(CommonController.class);
	
//	@RequestMapping(value = {"/popup/windowPopup01"},  method = {RequestMethod.GET, RequestMethod.POST})
//	public void getWindowPopup01(Model model, @RequestParam Map<String, Object> param) {
//		LOG.info("###### Window Popup 01 : POPUP START ######");
//		LOG.info(param.toString());
//	}
	
	@RequestMapping(value = {"/popup/{pageId}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public void getPopupPage(@PathVariable("pageId") String pageId, final Model model, @RequestParam Map<String, Object> param) {
		LOG.info("###### Design : VIEW START ######");
		model.addAttribute("param", param);
	}
}
