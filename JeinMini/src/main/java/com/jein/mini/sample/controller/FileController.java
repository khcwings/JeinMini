package com.jein.mini.sample.controller;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jein.mini.biz.common.domain.CommonFile;
import com.jein.mini.biz.common.persistence.CommonFileRepository;
import com.jein.mini.constant.FileConstant;
import com.jein.mini.util.DataUtil;
import com.jein.mini.util.FileManager;
import com.jein.mini.util.KeyGenerator;

@Controller
@RequestMapping(value="/file/common")
public class FileController {
	private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private CommonFileRepository fileRepo;
	
	@Autowired
	private ServletContext servletContext;

	@PostMapping("/fileUpload")
	@ResponseBody
    public Map<String, Object> uploadFile(HttpServletRequest request) {
		LOG.debug("##### File Upload #####");
        
        // 최종 결과 데이터
        Map<String, Object> result 			= new HashMap<String, Object>();
        // 임시 저장 결과 데이터
        Map<String, Object> tempFileResult	= null;
        
        // 1. Client에서 전송한 파일을 저장할 경로 정보를 획득
     	String uploadPath = DataUtil.getString(request, FileConstant.FILE_UPLOAD_PATH_KEY, "");
     	LOG.debug("##### Client File Upload Path : " + uploadPath);
     	
     	// 2. 파일을 임시 저장한다. 
     	tempFileResult = FileManager.getInstance().createTempFiles(request, "");     	
     	LOG.debug(tempFileResult.toString());
     	
     	// 3. 임시 저장이 성공일 경우 파일을 실제 저장 경로로 이동한다.
     	if(FileConstant.SUCCESS.equals(DataUtil.getString(tempFileResult, "resultCode"))) {
     		// 3-1. 임시 저장된 파일 정보
     		List<Map<String, Object>> fileList = DataUtil.getList(tempFileResult, "fileList");
     		
     		// 3-2. 임시 저장 폴더에서 실제 저장 경로로 파일을 이동시킨다.
     		result = FileManager.getInstance().moveFiles(fileList, uploadPath);

     		// 4. 성공일 경우 DB에 그 결과를 등록한다.
     		if(FileConstant.SUCCESS.equals(DataUtil.getString(result, "resultCode"))) {
     			// 4-1. 파일을 저장하고 전달할 내용을 리턴받아 등록한다.
     			result.put("FILE_LIST", saveFileUpload(fileList, uploadPath));
     		}
     	} else {
     		result = tempFileResult;
     	}     	
     	
		return result;
    }
	
	@RequestMapping("/downloadFile")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam Map<String, Object> param, HttpServletRequest request) {
		
		ResponseEntity<InputStreamResource> result = null;		
		
		try {
			// 1. 다운로드 요청 File Id를 가져온다. 
			String fileId = DataUtil.getString(param, "fileId");
			if(fileId.isEmpty()) {
				LOG.error("#####[FileController-downloadFile]File ID is null");
				return result;
			}
			
			// 2. 다운로드 요청 파일 정보를 조회한다. 
			CommonFile fileInfo = fileRepo.findOneByFileId(fileId);
			if(fileInfo == null) {
				LOG.error("#####[FileController-downloadFile]File Infomation is null");
				return null;
			}
			
			// 3. 원본 파일명을 가져온다. 
			String fileName = fileInfo.getOriginalFileName();
			
			// 4. MediaType을 가져온다. 
	        MediaType mediaType = FileManager.getMediaTypeForFileName(this.servletContext, fileName);
	        
	        // 5. Content-Disposition 설정한다. 
	        String contentDisposition = "";
	        String userAgent = request.getHeader("User-Agent");		
			if(userAgent.indexOf("MSIE 5.5") > -1)	{
				contentDisposition = "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";";
			} else if(userAgent.indexOf("MSIE") > -1)	{
				contentDisposition = "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";";
			} else {
				contentDisposition = "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "latin1").replaceAll("\\+", "\\ ") + ";";
			}
			
			// 6. File을 읽어온다. 
	        File file = FileManager.getInstance().getFileObject(fileInfo.getFilePath(), fileInfo.getChangeFileName());	        
	        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
	        
	        // 7. Http Response를 설정한다. 
	        result = ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
	                .contentType(mediaType)
	                .contentLength(file.length())
	                .body(resource);
		} catch (Exception e) {
			LOG.error("#####[FileController-downloadFile]" + e.getMessage());
		}	
 
        return result;
    }
	
	
	/**
	 * 업로드한 파일을 저장하고 화면에 전달한 결과를 생성한다. 
	 * 
	 * @param fileList
	 * @param savePath
	 * @return
	 */
	private List<Map<String, Object>> saveFileUpload(List<Map<String, Object>> fileList, String savePath) {
		// 화면에 전달한 업데이트된 파일 정보 리스트
		List<Map<String, Object>> saveReult = new ArrayList<Map<String, Object>>();
		// DB에 등록할 파일 정보 리스트
		List<CommonFile> cFileList = new ArrayList<CommonFile>();
		
		for(Map<String, Object> fInfo : fileList) {
			// 1. 파일 정보 생성
			CommonFile cFile = new CommonFile();
			cFile.setFileId(KeyGenerator.next());
			cFile.setOriginalFileName(DataUtil.getString(fInfo, "originalFileName"));
			cFile.setChangeFileName(DataUtil.getString(fInfo, "changeFileName"));
			cFile.setFileSize(DataUtil.getLong(fInfo, "fileSize", 0L));
			cFile.setFileType(DataUtil.getString(fInfo, "fileType"));
			cFile.setFilePath(savePath);			
			cFileList.add(cFile);
			
			// 2. 전달될 정보 생성
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("fileId",   cFile.getFileId());
			item.put("fileName", cFile.getOriginalFileName());
			item.put("fileSize", cFile.getFileSize());			
			saveReult.add(item);			
		}

		// 3. 파일 DB 등록
		fileRepo.save(cFileList);
		
		return saveReult;
	}
}