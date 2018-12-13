package com.jein.mini.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jein.mini.util.SecurityManager;

public class SecurityManager {
	private static final Logger LOG = LoggerFactory.getLogger(SecurityManager.class);
	private static String KEY = "";

	private SecurityManager() {}

	/**
	 * Thread 환경에서 싱글톤 클래스가 여러개 생성되는 오류를 차단하기 위한 Helper 생성
	 * @author KHCWINGS
	 */
	private static class SingletonHelper {
		private static final SecurityManager INSTANCE = new SecurityManager();
	}
	public static SecurityManager getInstance() {
		return SingletonHelper.INSTANCE;
	}


	/**
	 * 단방향 암호화 알고리즘
	 * 알고리즘 : SHA-256
	 * 사용처   : 패스워드
	 *
	 * @param strVal
	 * @return 무조건 64자리 String
	 */
	public String getSHA256(String strVal) {
		String rtnSHA = "";

		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(strVal.getBytes()); 
			byte byteData[] = sh.digest();

			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			rtnSHA = sb.toString();
		} catch(NoSuchAlgorithmException e){
			LOG.error("# [SecurityManager] getSHA256 => NoSuchAlgorithmException : " + e.getMessage());
			e.printStackTrace(); 
			rtnSHA = null; 
		} catch(Exception e){
			LOG.error("# [SecurityManager] getSHA256 => Exception : " + e.getMessage());
			e.printStackTrace(); 
			rtnSHA = null; 
		}

		return rtnSHA;
	}

	/**
	 * 양방향 암호화 알고리즘
	 * 알고리즘 : AES-128
	 * 
	 * 한글은 3Byte, 영문/특수문자 1Byte 계산으로
	 * 0-15 : 24, 16-31 : 44, 32-47 : 64, 48-63 : 88, 64-79 : 108, 80-95 : 128
	 * 
	 * @param strVal
	 * @return 
	 */
	public String getEncryptAES128(String strVal) {
		String rtnAES = "";
		try{
			byte[] keyData = getAesKey().substring(0,16).getBytes("UTF-8");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, "AES"), new IvParameterSpec(keyData));

			// AES 암호화
			byte[] encrypted = cipher.doFinal(strVal.getBytes("UTF-8"));

			// BASE64 인코딩
			byte[] base64Encoded = Base64.encodeBase64(encrypted);

			rtnAES = new String(base64Encoded, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
			rtnAES = null; 
		}

		return rtnAES;
	}
	
	/**
	 * 양방향 암호화 알고리즘
	 * 알고리즘 : AES-256
	 * 
	 * 한글은 3Byte, 영문/특수문자 1Byte 계산으로
	 * 0-15 : 24, 16-31 : 44, 32-47 : 64, 48-63 : 88, 64-79 : 108, 80-95 : 128
	 * 
	 * AES 256은 미국만 됨. JDK/JER 패치 필요
	 * jdk 설치 경로\jre\lib\security 폴더의 파일을 패치한다. 
	 * (local_policy.jar, US_export_policy.jar)
	 * 
	 * @param strVal
	 * @return
	 */
	public String getEncryptAES256(String strVal) {
		String rtnAES = "";
		try{
			byte[] key256Data = getAesKey().substring(0,32).getBytes("UTF-8");
			byte[] key128Data = getAesKey().substring(0,16).getBytes("UTF-8");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key256Data, "AES"), new IvParameterSpec(key128Data));

			// AES 암호화
			byte[] encrypted = cipher.doFinal(strVal.getBytes("UTF-8"));

			// BASE64 인코딩
			byte[] base64Encoded = Base64.encodeBase64(encrypted);

			rtnAES = new String(base64Encoded, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
			rtnAES = null; 
		}

		return rtnAES;
	}

	/**
	 * 양방향 복호화 알고리즘
	 * 알고리즘 : AES-128
	 *
	 * @param strVal
	 * @return
	 */
	public String getDecryptAES128(String strVal) {
		String rtnAES = "";
		try{
			byte[] keyData = getAesKey().substring(0,16).getBytes("UTF-8");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyData, "AES"), new IvParameterSpec(keyData));

			// BASE64 인코딩
			byte[] base64Decoded = Base64.decodeBase64(strVal.getBytes("UTF-8"));
						
			// AES 복호화
			byte[] decrypted = cipher.doFinal(base64Decoded);

			rtnAES = new String(decrypted, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
			rtnAES = null; 
		}
		return rtnAES;
	}
	
	/**
	 * 양방향 복호화 알고리즘
	 * 알고리즘 : AES-256
	 * 
	 * @param strVal
	 * @return
	 */
	public String getDecryptAES256(String strVal) {
		String rtnAES = "";
		try{
			byte[] key256Data = getAesKey().substring(0,32).getBytes("UTF-8");
			byte[] key128Data = getAesKey().substring(0,16).getBytes("UTF-8");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key256Data, "AES"), new IvParameterSpec(key128Data));

			// BASE64 인코딩
			byte[] base64Decoded = Base64.decodeBase64(strVal.getBytes("UTF-8"));
						
			// AES 복호화
			byte[] decrypted = cipher.doFinal(base64Decoded);

			rtnAES = new String(decrypted, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
			rtnAES = null; 
		}
		return rtnAES;
	}

	
	private String getAesKey() {
		if(KEY == null || "".equals(KEY)) {
			KEY = getProperties("security.aes.key");
		}
		
		return KEY;
	}
	/**
	 * Properties 파일에서 키값에 해당하는 설정값을 읽어 전달한다. 
	 * 
	 * @param propNm
	 * @return
	 */
	private String getProperties(String propNm) {
		// 현재 요청중인 thread local의 HttpServletRequest 객체 가져오기
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		// HttpSession 객체 가져오기
		HttpSession session = request.getSession();
		// ServletContext 객체 가져오기
		ServletContext context = session.getServletContext();
		// Spring Web Context 가져오기
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(context);
		
		String propVal = webAppContext.getEnvironment().getProperty(propNm);
		
		return ((propVal == null)?"":propVal);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strVal = "가나다라마바사아자차타카파하ABCDEFGHIJKLMNOPQRSTUVWXYNZ1234567890!@#$%^&*()_+-=";
		LOG.debug("# " + SecurityManager.getInstance().getSHA256(strVal));
		LOG.debug("# " + SecurityManager.getInstance().getEncryptAES128(strVal));
		LOG.debug("# " + SecurityManager.getInstance().getDecryptAES128(SecurityManager.getInstance().getEncryptAES128(strVal)));
		LOG.debug("# " + SecurityManager.getInstance().getEncryptAES256(strVal));
		LOG.debug("# " + SecurityManager.getInstance().getDecryptAES256(SecurityManager.getInstance().getEncryptAES256(strVal)));
	}
}
