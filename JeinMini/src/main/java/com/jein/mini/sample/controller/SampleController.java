package com.jein.mini.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping(value="/sample")
public class SampleController {
	
	@RequestMapping(value = {"/design/{pageId}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getViewDesign(@PathVariable("pageId") String pageId) {
		log.info("###### Design : VIEW START ######");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/design/" + pageId);
		return mav;
	}
	
	@RequestMapping(value = {"/view/layerPopup"},  method = {RequestMethod.GET, RequestMethod.POST})
	public void getViewLayerPopup(Model model, @RequestParam Map<String, Object> param) {
		log.info("###### Sample 02 : VIEW START ######");
		log.info(param.toString());
		model.addAttribute("Sample", "test1234");
	}
	
	
	@GetMapping("/view/sample01")
	public void getViewSample01(Model model, @RequestParam Map<String, Object> param) {
		log.info("###### Sample 01 : VIEW START ######");
		log.info(param.toString());
		model.addAttribute("Sample", "test1234");
	}
	
	@GetMapping("/view/sample02")
	public void getViewSample02(Model model, @RequestParam Map<String, Object> param) {
		log.info("###### Sample 02 : VIEW START ######");
		log.info(param.toString());
		model.addAttribute("Sample", "test1234");
	}
	
	@GetMapping("/view/sample03")
	public void getViewSample03(Model model, @RequestParam Map<String, Object> param) {
		log.info("###### Sample 03 : VIEW START ######");
		log.info(param.toString());
		model.addAttribute("sampleTitle", "Sample Controller Title");
		model.addAttribute("sampleUText", "<h3 style='color:red;'>Sample Controller Title</h3>");
		

		model.addAttribute("footerInfo", "[샘플-Controller]Sample Footer Test");
	}
	
	@GetMapping("/view/layout01")
	public void getViewLayout01(Model model, @RequestParam Map<String, Object> param) {
		log.info("###### Layout 02 : VIEW START ######");
		log.info(param.toString());
		model.addAttribute("Layout", "test1234");
	}
	
	@PostMapping("/data/sampleData01")
	@ResponseBody
	public Map<String, Object> getSampleData01(@RequestParam Map<String, Object> param) {
		log.info("###### SampleData 01 : DATA START ######");
		log.info(param.toString());
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("result", "S");
		return retMap;
	}
}
