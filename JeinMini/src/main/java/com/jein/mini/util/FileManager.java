package com.jein.mini.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.io.Files;
import com.jein.mini.constant.FileConstant;

import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * 파일 업로드를 수행하는 싱글톤 클래스
 * 
 * @author KHCWINGS
 */
public class FileManager {
	private static final Logger LOG = LoggerFactory.getLogger(FileManager.class);

	// 디폴트 최대 파일 사이즈
	private final long DEFAULT_MAX_FILE_SIZE = 5242880;

	private FileManager() {}

	/**
	 * Thread 환경에서 싱글톤 클래스가 여러개 생성되는 오류를 차단하기 위한 Helper 생성
	 * @author KHCWINGS
	 */
	private static class SingletonHelper {
		private static final FileManager INSTANCE = new FileManager();
	}
	public static FileManager getInstance() {
		return SingletonHelper.INSTANCE;
	}

	/**
	 * 사용자로부터 전달받은 정보로 임시 파일을 생성하고 그 정보를 MAP으로 생성하여 리턴한다. 
	 * 
	 * @param req
	 * @return
	 */
	public Map<String, Object> createTempFiles(HttpServletRequest req, String prefix) {

		Map<String, Object> retMap 			= new HashMap<String, Object>();
		List<Map<String, Object>> fileList 	= new ArrayList<Map<String, Object>>();

		try{
			String strPreFix  			= (prefix==null)?"":prefix;
			String strTempFileDirectory = getProperties("file.upload.directory.temp");
			String strExtensionInfo  	= getProperties("file.upload.extension.list");
			long lMaxFileSize 			= getMaxFileSize();					

			LOG.debug("#[FileManager] System Temp File Directory : " + strTempFileDirectory);
			LOG.debug("#[FileManager] System Extension Info : " + strExtensionInfo);
			LOG.debug("#[FileManager] System Max File Size : " + lMaxFileSize);

			if(req == null) {
				return createResultMsg(retMap, FileConstant.ERROR, "요청 정보가 존재하지 않습니다.");
			}

			MultipartHttpServletRequest multipartRequest =  (MultipartHttpServletRequest)req;

			// 유저가 지정한 콜백함수명 확인 및 설정
			String callBackFunc = multipartRequest.getParameter("upload_callback");
			retMap.put("callBackFunc", (callBackFunc==null?"":callBackFunc));

			// 파일 리스트 가져오기
			List<MultipartFile> uploadFiles = multipartRequest.getFiles(FileConstant.FILE_UPLOAD_KEY);
			LOG.debug("#[FileManager] File List Size : " + uploadFiles.size());

			if(uploadFiles.size() < 1) {
				return createResultMsg(retMap, FileConstant.ERROR, "업로드된 파일이 존재하지 않습니다.");
			}
			for (int i = 0; uploadFiles != null && i < uploadFiles.size(); i++){
				MultipartFile mFile = uploadFiles.get(i);

				// 전달된 파일의 실제 파일명을 가져온다. 
				String originalFileName = mFile.getOriginalFilename();
				// 전달된 파일의 확장자를 가져온다. 
				String fileType = originalFileName.substring(originalFileName.lastIndexOf(".")+1).toUpperCase(); 				
				// 파일 사이즈를 가져온다. 
				long fileSize   = mFile.getSize();
				LOG.debug("#[FileManager] Original File Name : " + originalFileName + ", File Type : " + fileType + ", File Size : " + fileSize);

				if(strExtensionInfo.indexOf("|" + fileType.toUpperCase() + "|") < 0) {
					return createResultMsg(retMap, FileConstant.ERROR, "[" + fileType.toUpperCase() + "] 파일은 저장할 수 없습니다.");
				}

				// 파일의 사이즈가 지정된 사이즈를 넘는다면 파일을 저장하지 않는다. 
				if(fileSize > lMaxFileSize) {
					return createResultMsg(retMap, FileConstant.ERROR, "[" + originalFileName + "] 파일의 크기가 업로드 가능한 크기를 벗어났습니다.");
				}

				if (originalFileName != null && !"".equals(originalFileName)){
					String reFileName = changeFileName(originalFileName, strPreFix);
					LOG.debug("#[FileManager] Change File Name : " + reFileName);
					if(writeFile(mFile, strTempFileDirectory, reFileName)) {

						Map<String, Object> fileInfo = new HashMap<String, Object>();
						fileInfo.put("originalFileName", originalFileName);			// 원본 파일명
						fileInfo.put("changeFileName",   reFileName);				// 변경된 파일명
						fileInfo.put("fileType", 		 fileType);					// 파일 확장자
						fileInfo.put("fileSize", 		 fileSize + "");			// 파일 크기

						LOG.debug("#[FileManager] File Save Info : " + fileInfo.toString());
						fileList.add(fileInfo);
					}else {
						return createResultMsg(retMap, FileConstant.ERROR, "[" + originalFileName + "] 파일 저장 중 오류가 발생하였습니다.");
					}
				}
			}			
		} catch(Exception e) {
			LOG.error("#[FileManager] createTempFiles => " + e.getMessage());
			return createResultMsg(retMap, FileConstant.ERROR, "임시 파일 저장 중 알 수 없는 오류가 발생하였습니다.");
		}
		retMap.put("fileList", fileList);
		return createResultMsg(retMap, FileConstant.SUCCESS, "임시 파일 저장에 성공하였습니다.");
	}

	/**
	 * 임시로 저장된 파일 정보를 실제 사용하는 파일 디렉토리로 전달한다. 
	 * 
	 * @param fileInfo
	 * @param savePath
	 * @return
	 */
	public Map<String, Object> moveFiles(List<Map<String, Object>> fileList, String savePath) {
		Map<String, Object> fileInfo = new HashMap<String, Object>();
		
		// 임시 저장 디렉토리 경로
		String strTempFileDirectory = getProperties("file.upload.directory.temp");

		// 저장 디렉토리 경로 및 값 유무 확인
		String strSaveFileDirectory = (savePath == null || "".equals(savePath))?"":getProperties("file.directory." + savePath);
		strSaveFileDirectory = ("".equals(strSaveFileDirectory))?getProperties("file.directory.default"):strSaveFileDirectory;
		if(strSaveFileDirectory == null || "".equals(strSaveFileDirectory)) {
			return createResultMsg(fileInfo, FileConstant.ERROR, "파일 저장 디렉토기가 존재하지 않습니다.");
		}

		try{
			// 저장되는 디렉토리가 생성되어 있는지 확인한다.         	
			File dir = new File(strSaveFileDirectory);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			for(Map<String, Object> fInfo : fileList) {
				String changeFileName = DataUtil.getString(fInfo, "changeFileName", "");
				File originFile = new File(strTempFileDirectory + changeFileName);		// 원본 파일
				File targetFile = new File(strSaveFileDirectory + changeFileName);		// 신규 파일
				LOG.debug(strTempFileDirectory + changeFileName);
				LOG.debug(strSaveFileDirectory + changeFileName);
			
				Files.move(originFile, targetFile);
			}
		} catch(IOException e) {
			LOG.error("#[FileManager] moveFiles => " + e.getMessage());
			return createResultMsg(fileInfo, FileConstant.ERROR, "파일 저장 중 알수 없는 오류가 발생하였습니다.");
		} catch(Exception e) {
			LOG.error("#[FileManager] moveFiles => " + e.getMessage());
			return createResultMsg(fileInfo, FileConstant.ERROR, "파일 저장 중 알수 없는 오류가 발생하였습니다.");
		}
	
		return createResultMsg(fileInfo, FileConstant.SUCCESS, "파일 저장에 성공하였습니다.");
	}

	/**
	 * 파일 경로 설정값과 파일명을 읽어 파일 오브젝트를 생성하여 리턴한다. 
	 * 
	 * @param targetPath
	 * @param fileName
	 * @return
	 */
	public File getFileObject(String targetPath, String fileName) {
		// 저장 디렉토리 경로 및 값 유무 확인
		String strFileDirectory = (targetPath == null || "".equals(targetPath))?"":getProperties("file.directory." + targetPath);
		strFileDirectory = ("".equals(strFileDirectory))?getProperties("file.directory.default"):strFileDirectory;
		if(strFileDirectory == null || "".equals(strFileDirectory)) {
			LOG.error("#[FileManager] getFileObject => 파일 디렉도리 경로를 찾을 수 없습니다.");
			return null;
		}

		// 파일명에 경로를 상위로 옮기는 코드(..)이 포함되어 있는 경우 제거하고 설정한다. 
		File file =  new File(strFileDirectory + fileName.replaceAll("\\.\\.", ""));
		if (!file.exists()) {
			LOG.error("#[FileManager] getFileObject => 파일 경로를 찾을 수 없습니다.");
			return null;
		}

		return file;
	}

	/**
	 * 파일을 저장한다. 
	 * 
	 * @param multipartFile
	 * @param filePath
	 * @param reFileName
	 * @return
	 * @throws Exception
	 */
	private boolean writeFile(MultipartFile multipartFile, String filePath, String reFileName) throws Exception {
		OutputStream out = null;
		try {
			// 저장되는 디렉토리가 생성되어 있는지 확인한다. 
			if(filePath != null && !"".equals(filePath)) {
				File dir = new File(filePath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}

			// 파일을 저장한다. 
			out = new FileOutputStream(filePath + reFileName);
			BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
			byte[] buffer = new byte[8106];
			int read = bis.read(buffer);
			while (read > 0) {
				out.write(buffer, 0, read);
				read = bis.read(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				out.close();
			} catch(Exception e){     		
			}
		}

		return true;
	}


	/**
	 * 결과값을 Map에 저장하여 리턴한다. 
	 * 
	 * @param retMap
	 * @param resultCode
	 * @param resultMsg
	 * @return
	 */
	private Map<String, Object> createResultMsg(Map<String, Object> retMap, String resultCode, String resultMsg) {
		retMap.put("resultCode", resultCode);
		retMap.put("resultMsg",  resultMsg);
		return retMap;
	}

	/**
	 * 파일명에 prefix와 timestemp를 추가한다. 
	 * 
	 * @param fileName
	 * @param prefix
	 * @return
	 */
	private String changeFileName(String fileName, String prefix) {
		String retFileName      = "";
		String delSpaceFileName = fileName.replaceAll(" ","_");

		retFileName = prefix + "_"
				+ delSpaceFileName.substring(0, delSpaceFileName.lastIndexOf("."))
				+ "_" + getTimestamp()
				+ delSpaceFileName.substring(delSpaceFileName.lastIndexOf("."));

		return retFileName;
	}

	/**
	 * 20자리의 Time Stamp 값을 반환한다. 
	 * 
	 * @return
	 */
	private String getTimestamp() {
		Calendar cal = Calendar.getInstance();
		return new String(cal.getTimeInMillis() + "");
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

	/**
	 * 프로퍼티 파일에서 파일 최대 사이즈 설정을 읽어 가져온다. 
	 * 
	 * @return
	 */
	private long getMaxFileSize() {
		try{
			return Long.parseLong(getProperties("file.upload.max.size")) * 1024 * 1024;
		} catch(NumberFormatException e) {
			LOG.error("#[FileManager] getMaxFileSize => " + e.getMessage());
			return DEFAULT_MAX_FILE_SIZE;
		} catch (Exception e) {
			LOG.error("#[FileManager] getMaxFileSize => " + e.getMessage());
			return DEFAULT_MAX_FILE_SIZE;
		}
	}
	
	
	/**
	 * 파일명에서 MediaType을 추출한다. 
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
}
