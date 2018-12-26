package com.jein.mini.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseUtil {
	
	public static Map<String, Object> parseXmlFileToMap(String filePath) {
		return parseXmlFileToMap(Paths.get(filePath));
	}
	
	public static Map<String, Object> parseXmlFileToMap(Path filePath) {
		Map<String, Object> retMap	= null;
		Stream<String> lines 		= null;
		try {
			// 1. XML FILE => XML STRING DATA 
			String strXml = Files.lines(filePath).collect(Collectors.joining("\n"));
			
			// 2. XML STRING DATA => JSON
			JSONObject jsonData = XML.toJSONObject(strXml);
			
			// 3. JSON => MAP
			retMap = (new ObjectMapper()).readValue(jsonData.toString(), new TypeReference<Map<String, Object>>() {});
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(lines != null) {
				lines.close();
			}
		}
		
		return retMap;
	}
}
