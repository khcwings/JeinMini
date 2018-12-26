package com.jein.mini.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.env.PropertySource;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jein.mini.constant.CommonConstant;

public class DataUtil {
	
	/**
	 * 파일명에서 MediaType을 추출한다. 
	 * @author 김희철
	 * @since  2018-12-26
	 * @param servletContext
	 * @param fileName
	 * @return
	 */
	public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
	
	/**
	 * Content Disposition 정보를 생성한다.
	 * @author 김희철
	 * @since  2018-12-26
	 * @param userAgent
	 * @param fileName
	 * @return
	 */
	public static String getContentDisposition(String userAgent, String fileName) {
		String contentDisposition = "";	
		try {
			if(userAgent.indexOf("MSIE 5.5") > -1)	{
				contentDisposition = "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";";
			} else if(userAgent.indexOf("MSIE") > -1)	{
				contentDisposition = "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";";
			} else {
				contentDisposition = "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "latin1").replaceAll("\\+", "\\ ") + ";";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contentDisposition;
	}
	
	
	
	@SuppressWarnings({ "unchecked", "serial" })
	public static final List<Map<String, Object>> getList(Map<String, Object> param, String key) {
		List<Map<String, Object>> item = new ArrayList<Map<String, Object>>();
		if (!gNVL(param) && isKey(param, key)) {
			Object o = param.get(key);
			if (o instanceof List<?>) {
				item = (List<Map<String, Object>>)o;
			} else if (o instanceof Map<?,?>) {
				item.add((Map<String, Object>)o);
			} else {
				final String k = key;
				final Object v = gRNVL(o);
				item.add(new HashMap<String, Object>(){{put(k, v);}});
			}
		}
		return item;
	}	
	
	public static final boolean gNVL(Object o) {
		if (null == o) {
			return true;
		}
		return false;
	}
	
	public static final String gRNVL(Object o) throws IllegalArgumentException {
		String value = "";
		try {
			if (!gNVL(o)) {
				if ((o instanceof String) || (o instanceof Number) || (o instanceof Boolean)) {
					value = String.valueOf(o);
				} else {
					throw new IllegalArgumentException("Not Support Object Type");
				}
			} 
		} catch (Exception e) {
			value = "";
		}
		return value.trim();
	}	
	
	/**
	 * <pre>
	 * Map에 key에 해당하는 값이 존재하는지 체크
	 * </pre>
	 *
	 * @param param Map데이터
	 * @param key 키
	 * @return boolean
	 */
	public static final boolean getContainKey(Map<String, ? extends Object> param, String key) {
		if (!gNVL(param)) {
			return param.containsKey(key);
		}
		return false;
	}
	
	/**
	 * <pre>
	 * Map에 해당하는 key가 존재하는지 체크
	 * </pre>
	 *
	 * @param param Map데이터
	 * @param key 키
	 * @return boolean
	 */
	public static final boolean isKey(Map<String, ? extends Object> param, String key) {
		return getContainKey(param, key);
	}
	
	
	/**
	 * <pre>
	 * Map에서 key에 해당하는 Map 객체를 가져온다. <br/>
	 * </pre>
	 *
	 * @param param
	 * @param key
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static final Map<String, Object> getMap(Map<String, Object> param, String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!gNVL(param) && isKey(param, key)) {
			Object o = param.get(key);
			if (o instanceof Map<?,?>) {
				map = (Map<String, Object>)o;
			} else {
				map.put(key, o);
			}
		} 
		return map;
	}
	
	/**
	 * <pre>
	 * Map에서 key에 해당하는 값을 가져온다. <br/>
	 * 해당하는 값이 없으면 default value를 가져온다.
	 * </pre>
	 *
	 * @param param Map데이터
	 * @param key 키
	 * @param defValue 디폴트 값
	 * @return String
	 */
	public static final String getString(Map<String, ? extends Object> param, String key, String defValue) {
		if (!gNVL(param) && isKey(param, key)) {
			Object o = param.get(key);
			if (o instanceof String || o instanceof Number || o instanceof Boolean) {
				return gRNVL(String.valueOf(o));
			}
		}
		return defValue;
	}
	
	/**
	 * <pre>
	 * Map에서 key에 해당하는 값을 가져온다.
	 * </pre>
	 *
	 * @param param Map데이터
	 * @param key 키
	 * @return String
	 */
	public static final String getString(Map<String, ? extends Object> param, String key) {
		return getString(param, key, "");
	}
	
	/**
	 * <pre>
	 * HttpSession에서 sessionKey에 해당하는 값 가져오기<br/>
	 * 값이 없을 경우 defaultValue를 가져온다.
	 * </pre>
	 *
	 * @param session HttpSession
	 * @param sessionKey 세션키
	 * @param defValue 디폴트값
	 * @return String
	 */
	public static String getString(HttpSession session, String sessionKey, String defValue){
		String result = defValue;
		if (!gNVL(session)){
			result = (String)session.getAttribute(sessionKey);			
			if(result == null || result.isEmpty()){
				result = defValue;
			}
		}
		return result;
	}
	
	/**
	 * <pre>
	 * HttpSession에서 sessionKey에 해당하는 값 가져오기<br/>
	 * 값이 없을 경우 공백을 리턴한다.
	 * </pre>
	 *
	 * @param session HttpSession
	 * @param sessionKey 세션키
	 * @return String
	 */
	public static String getString(HttpSession session, String sessionKey){
		return getString(session, sessionKey, "");
	}
	
	/**
	 * <pre>
	 * PropertySource에서 propKey와 일치하는 Property 값 가져오기
	 * </pre>
	 *
	 * @param propertySource
	 * @param propKey
	 * @return String 
	 */
	public static String getString(PropertySource<?> propertySource, String propKey){
		return getString(propertySource, propKey, "");
	}
	
	/**
	 * <pre>
	 * PropertySource에서 propKey와 일치하는 Property 값 가져오기
	 * 해당하는 property의 값이 없으면 default value로 지정한 값을 리턴한다.
	 * </pre>
	 *
	 * @param propertySource 프로프티 소스
	 * @param propKey 프로퍼티 키
	 * @param defaultValue 디폴트 값
	 * @return String
	 *
	 */
	public static String getString(PropertySource<?> propertySource, String propKey, String defaultValue){
		String value = defaultValue;
		if(propertySource != null && propertySource.containsProperty(propKey)){
			value = propertySource.getProperty(propKey).toString();
		}
		return value;
	}
	
	/**
	 * <pre>
	 * HttpServletRequest에서 paramName에 해당하는 값 가져오기<br/>
	 * 값이 없을 경우 defaultValue를 가져온다.
	 * </pre>
	 *
	 * @param request 	HttpServletRequest
	 * @param paramName Parameter Name
	 * @param defValue 	디폴트값
	 * @return String
	 */
	public static String getString(HttpServletRequest request, String paramName, String defValue){
		String result = defValue;
		if (!gNVL(request)){
			result = request.getParameter(paramName);	
			if(result == null || result.isEmpty()){
				result = defValue;
			}
		}
		return result;
	}
	
	/**
	 * <pre>
	 * HttpServletRequest에서 paramName에 해당하는 값 가져오기<br/>
	 * 값이 없을 경우 공백을 리턴한다.
	 * </pre>
	 *
	 * @param request 	HttpServletRequest
	 * @param paramName Parameter Name
	 * @return String
	 */
	public static String getString(HttpServletRequest request, String paramName){
		return getString(request, paramName, "");
	}
	
	/**
	 * JsonNode에서 key에 해당하는 JsonNode를 찾아 문자열의 값을 반환합니다. 
	 * 
	 * @param node
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(JsonNode node, String key, String defaultValue) {
		if (node.has(key)) {
			return node.get(key).asText(defaultValue);
		} 
		return defaultValue;
	}
	
	
	/**
	 * JsonNode에서 key에 해당하는 JsonNode를 찾아 문자열의 값을 반환합니다. 
	 * 
	 * @param node
	 * @param key
	 * @return
	 */
	public static String getString(JsonNode node, String key) {
		if (node.has(key)) {
			return node.get(key).asText("");
		} 
		return "";		
	}
	
	/**
	 * <pre>
	 * 전달받은 String Data를 int 형으로 변환시겨 반환한다. 
	 * </pre>
	 *
	 * @param String	int형으로 변환시킬 String Data
	 * @param defValue 디폴트 값
	 * @return Integer		변환될 결과값
	 */
	public static int getInteger(String val, int defValue) {
		int iResult = 0;
		try {
			iResult = Integer.parseInt(val);
		} catch(NumberFormatException e) {
			iResult = defValue;
		}
		return iResult;
	}
	
	/**
	 * <pre>
	 * Map에서 key에 해당하는 값을 가져온다. <br/>
	 * 해당하는 값이 없으면 default value를 가져온다.
	 * </pre>
	 *
	 * @param param Map데이터
	 * @param key 키
	 * @param defValue 디폴트 값
	 * @return Long
	 */
	public static long getLong(Map<String, ? extends Object> param, String key, long defValue) {
		return getLong(getString(param, key, defValue + ""), defValue);
	}
	
	
	/**
	 * <pre>
	 * 전달받은 String Data를 Long 형으로 변환시겨 반환한다. 
	 * </pre>
	 *
	 * @param String	Long형으로 변환시킬 String Data
	 * @param defValue  디폴트 값
	 * @return Long		변환될 결과값
	 */
	public static long getLong(String val, long defValue) {
		long iResult = 0L;
		try {
			iResult = Long.parseLong(val);
		} catch(NumberFormatException e) {
			iResult = defValue;
		}
		return iResult;
	}
	
	/**
	 * <pre>
	 * Map에서 key에 해당하는 값을 가져온다. <br/>
	 * 해당하는 값이 없으면 default value를 가져온다.
	 * </pre>
	 *
	 * @param param Map데이터
	 * @param key 키
	 * @param defValue 디폴트 값
	 * @return Integer
	 */
	public static int getInteger(Map<String, ? extends Object> param, String key, int defValue) {
		return getInteger(getString(param, key, defValue + ""), defValue);
	}
	
	
	/**
	 * <pre>
	 * HttpServletRequest 에서 Session을 추출한다. 
	 * 없는 경우 신규로 생성한다. 
	 * </pre>
	 *
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}
		return session;
	}
	
	/**
	 * <pre>
	 * 연속된 문자열을 체크한다. 
	 * </pre>
	 *
	 * @param str		체크할 문자열
	 * @param limit		연속 가능한 값 (ex : 3 => 4자리 반복부터는 false)
	 * @return
	 */
	public static boolean repeatedStr(String str, int limit) {
		return (str.matches(".*([a-zA-Z0-9`~!@#$%^&*()-=_+\\[\\]{}:;',./<>?\\\\|])\\1{"+limit+",}.*"));		// 알파벳, 숫자, 특수문자 연속3자리 체크 정규식
	}
	
	/**
	 * <pre>
	 * 알파벳, 숫자의 반복된 문자열을 체크한다. 
	 * </pre>
	 *
	 * @param str		체크할 문자열
	 * @param limit		반복 가능한 값 (ex : 3 => 4자리 반복부터는 false)
	 * @return
	 */
	public static boolean consecutiveStr(String str, int limit) {		
		String alphabatWordAsc  = "abcdefghijklmnopqrstuvwxyz";
		String alphabatWordDesc = "zyxwvutsrqponmlkjihgfedcba";
		String numberWordAsc    = "0123456789";
		String numberWordDesc   = "9876543210";
		
		String lowStr = str.toLowerCase();
		int iLimit = limit + 1;
		for(int i = 0; i < lowStr.length(); i++) {
			if( i <= (lowStr.length()-iLimit) ) {
				String checkWord = lowStr.substring(i, (i+iLimit));
				if(alphabatWordAsc.indexOf(checkWord) > -1 || alphabatWordDesc.indexOf(checkWord) > -1 || 
						numberWordAsc.indexOf(checkWord) > -1 || numberWordDesc.indexOf(checkWord) > -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <pre>
	 * 기준 문자열 중 입력받은 길이만큼의 동일한 문자열이 비교대상 문자열에 들어 있는지 체크한다. 
	 * </pre>
	 *
	 * @param strStandard		기준되는 문자열
	 * @param strTarget			비교대상 문자열
	 * @param limit				일치 가능한 값 (ex : 3 => 4자리 반복부터는 false)
	 * @return
	 */
	public static boolean consecutiveStr(String strStandard, String strTarget, int limit) {
		if(strStandard == null || strStandard.length() < limit) {
			return false;
		}
		
		String lowStandard 	= strStandard.toLowerCase();
		String lowTarget 	= strTarget.toLowerCase();
		int iLimit = limit + 1;
		
		for(int i = 0; i < lowTarget.length(); i++) {
			if( i <= (lowTarget.length()-iLimit) ) {
				String checkWord = lowTarget.substring(i, (i+iLimit));
				if(lowStandard.indexOf(checkWord) > -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <pre>
	 * Object를 Map으로 변환
	 * </pre>
	 *
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> convertObjectToMap(Object obj) {
		
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Map<String, Object> resultMap = new HashMap<>();
			for(int i=0; i<fields.length; i++) {
				fields[i].setAccessible(true);
					resultMap.put(fields[i].getName(), gRNVL(fields[i].get(obj)));
			}
			return resultMap;
		
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	/**
	 * <pre>
	 * Map 데이터를 Json String 으로 변환
	 * </pre>
	 *
	 * @param map
	 * @return String
	 */
	public static String convertMapToJsonStr(Map<String, ? extends Object> map) {
		String result = CommonConstant.VALUE_EMPTY;
		try {
			if(!gNVL(map)) {
				ObjectMapper objectMapper = new ObjectMapper();
				result = objectMapper.writeValueAsString(map);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * Json String 데이터를 Map 으로 변환
	 * </pre>
	 *
	 * @param String
	 * @return map
	 */
	public static Map<String, Object> convertJsonStrToMap(String jsonStr) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			if(jsonStr == null || "".equals(jsonStr)) {
				return retMap;
			} else {
				ObjectMapper objectMapper = new ObjectMapper();
				retMap = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>(){});
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retMap;
	}
	
	public static String convertNvlToStr(String str) {
		if(str == null || "".equals(str)) {
			return "";
		} else {
			return str.trim();
		}
	}
	
	public static void convertSearchMsg(Map<String, Object> obj, String targetParamNm, String resultParamNm) {
		String searMsg = DataUtil.getString(obj,  targetParamNm, "");
		if(!(searMsg == null || "".equals(searMsg))) {
			searMsg = searMsg.trim();
			
			int iLength = searMsg.length();
			int iStart  = searMsg.indexOf("*");
			int iEnd    = searMsg.lastIndexOf("*");
			if("*".equals(searMsg)) {
				searMsg = "";
				obj.put(resultParamNm, 4); 
			} else if(iStart == 0 && iEnd > 0 && iEnd == (iLength-1)) {
				searMsg = searMsg.substring(1, iLength);
				searMsg = searMsg.substring(0, (iLength - 2));
				obj.put(resultParamNm, 3); 
			} else if(iStart == 0) {
				searMsg = searMsg.substring(1, iLength);
				obj.put(resultParamNm, 1); 
			} else if(iEnd == (iLength-1)) {
				searMsg = searMsg.substring(0, (iLength - 1));
				obj.put(resultParamNm, 2); 
			} else {
				obj.put(resultParamNm, 0);
			}
		} else {
			obj.put(resultParamNm, 0);
		}
		obj.put(targetParamNm,  searMsg);
	}
	
	/**
	 * <pre>
	 * 메시지의 파라미터부분을 해당 값으로 치환
	 * </pre>
	 *
	 * @param message 메시지
	 * @param params 치환 값
	 * @return String
	 */
	public static String replaceMessageByParam(String message, Object[] params) {
		String resultMessage = message;
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		for(int i=0; i<params.length; i++) {
			StringBuilder key = new StringBuilder("\\{").append(i).append("\\}");
			String value = String.valueOf(params[i]);
			paramsMap.put(key.toString(), value);
		}
		
		Iterator<Map.Entry<String, String>> ie = paramsMap.entrySet().iterator();
		while (ie.hasNext()) {
			Map.Entry<String, String> entry = ie.next();
			resultMessage = resultMessage.replaceAll(entry.getKey(), entry.getValue());
		}
		
		return resultMessage;
	}
}
