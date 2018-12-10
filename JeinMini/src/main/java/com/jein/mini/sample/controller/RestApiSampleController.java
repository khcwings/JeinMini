package com.jein.mini.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jein.mini.sample.domain.Sample;

import lombok.extern.java.Log;

/**
 * Rest API 확인(http://localhost:8080/mini/swagger-ui.html#/)
 * @author JEINSOFT
 *
 */
@Log
@RestController
@RequestMapping(value="/sample/rest/")
public class RestApiSampleController {
	
	//http://localhost:8080/mini/sample/rest/restApiSample03?sampleId=test
	@RequestMapping(value="restApiSample01", method=RequestMethod.GET)
	public Map<String, Object> RestApiSample01(@RequestParam Map<String, Object> param) {
		log.info("###### REST API SAMPLE01 ######");
		log.info(param.toString());
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("title", "Sample01");
		return retMap;
	}
	
	//http://localhost:8080/mini/sample/rest/restApiSample03?sampleId=test
	@RequestMapping(value="restApiSample02")
	public Map<String, Object> RestApiSample02(Sample param) {
		log.info("###### REST API SAMPLE02 ######");
		log.info(param.toString());
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("title", "Sample02");
		return retMap;
	}
	
	
	@RequestMapping(value="restApiSample03", method=RequestMethod.POST)
	public Map<String, Object> RestApiSample03(Sample param) {
		log.info("###### REST API SAMPLE03 ######");
		log.info(param.toString());
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("title", "Sample03");
		return retMap;
	}
	
	// http://localhost:8080/mini/sample/rest/restApiSample04/33
	@RequestMapping(value="restApiSample04/{restId}")
	public Map<String, Object> RestApiSample04(@PathVariable String restId) {
		log.info("###### REST API SAMPLE04 ######");
		log.info("Rest ID : " + restId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("title", "Sample03");
		retMap.put("restId", restId);
		return retMap;
	}
	
}
