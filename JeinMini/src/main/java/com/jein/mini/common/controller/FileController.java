package com.jein.mini.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jein.mini.biz.common.domain.CommonFile;
import com.jein.mini.biz.common.persistence.CommonFileRepository;
import com.jein.mini.constant.CommonMessageConstrant;
import com.jein.mini.constant.ExcelConstant;
import com.jein.mini.constant.FileConstant;
import com.jein.mini.service.ExcelDataService;
import com.jein.mini.util.DataUtil;
import com.jein.mini.util.DateTimeUtil;
import com.jein.mini.util.ExcelUtil;
import com.jein.mini.util.FileManager;
import com.jein.mini.util.KeyGenerator;
import com.jein.mini.util.ParseUtil;

@Controller
@RequestMapping(value="/common/file")
public class FileController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private CommonFileRepository fileRepo;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ExcelDataService excelDataService;

	@Value("${file.upload.directory.temp}")
	private String fileTempDirectory;	

	@Value("${file.excel.template.root}")
	private String excelRootPath;

	@Value("${file.excel.temp.file.name}")
	private String excelTempFileName;

	/**
	 * 파일을 업로드 한다. 
	 * @param request
	 * @return
	 */
	@PostMapping("/fileUpload")
	@ResponseBody
	public Map<String, Object> uploadFile(HttpServletRequest request) {
		LOG.debug("##### File Upload #####");

		// 최종 결과 데이터
		Map<String, Object> result 			= new HashMap<String, Object>();
		// 임시 저장 결과 데이터
		Map<String, Object> tempFileResult	= null;

		// 1. Client에서 전송한 파일을 저장할 경로 정보를 획득
		String uploadPath   = DataUtil.getString(request, FileConstant.FILE_UPLOAD_PATH_KEY, "");
		String uploadPreFix = DataUtil.getString(request, FileConstant.FILE_UPLOAD_PRE_FIX, "");
		LOG.debug("##### Client File [Upload Path : " + uploadPath + ", Upload PreFix : " + uploadPreFix + "]");

		// 2. 파일을 임시 저장한다. 
		tempFileResult = FileManager.getInstance().createTempFiles(request, uploadPreFix);     	
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

	/**
	 * 파일 아이디를 전달받아 파일을 다운로드 한다. 
	 * @param param
	 * @param request
	 * @return
	 */
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

			// 4. File을 읽어온다. 
			File file = FileManager.getInstance().getFileObject(fileInfo.getFilePath(), fileInfo.getChangeFileName());	        
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			// 5. Http Response를 설정한다. 
			result = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, DataUtil.getContentDisposition(request.getHeader("User-Agent"), fileName))
					.contentType(DataUtil.getMediaTypeForFileName(this.servletContext, fileName))
					.contentLength(file.length())
					.body(resource);
		} catch (Exception e) {
			LOG.error("#####[FileController-downloadFile]" + e.getMessage());
		}	

		return result;
	}

	/**
	 * 엑셀 파일을 생성한다. 
	 * @param param
	 * @param request
	 * @return
	 */
	@RequestMapping("/downloadExcel")
	public ResponseEntity<InputStreamResource> downloadExcel(@RequestParam Map<String, Object> param, HttpServletRequest request) {
		LOG.debug("#####[FileController-downloadExcel] START #####");
		ResponseEntity<InputStreamResource> result = null;		

		try {
			Path templatePath = null;

			// 1. 템플릿 파일의 경로를 가져온다.
			String filePath         = DataUtil.getString(param, "excelTemplate");			
			LOG.debug("#####[FileController-downloadExcel] TEMPLATE FILE PATH   => " + filePath);
			LOG.debug("#####[FileController-downloadExcel] EXCEL ROOT FILE PATH => " + excelRootPath);

			// 2. 템플릿 파일의 ROOT 경로를 설정한다. 
			if(excelRootPath.indexOf("classpath:") == 0) {
				templatePath = Paths.get(getClass().getClassLoader().getResource(excelRootPath.replace("classpath:", "") + filePath).toURI());
			} else {
				templatePath = Paths.get(excelRootPath + filePath);
			}
			LOG.debug("#####[FileController-downloadExcel] TEMPLATE PATH => " + templatePath);

			// 3. XML FILE 을 Map으로 변경한다. 
			Map<String, Object> xmlMap = ParseUtil.parseXmlFileToMap(templatePath);
			LOG.debug("#####[FileController-downloadExcel] XML TO MAP => " + xmlMap.toString());
			if(xmlMap == null || xmlMap.isEmpty()) {
				LOG.error("#####[FileController-downloadFile]Excel Template File is null");
				return result;
			}

			// 4. EXCEL FILE 생성 정보를 읽어온다. 
			xmlMap = DataUtil.getMap(xmlMap, "configuration");
			Map<String, Object> excelInfo = DataUtil.getMap(xmlMap, "info");
			List<Map<String, Object>> excelHead = DataUtil.getList(DataUtil.getMap(xmlMap, "head"), "item");
			List<Map<String, Object>> excelBody = DataUtil.getList(DataUtil.getMap(xmlMap, "body"), "item");
			LOG.debug("#####[FileController-downloadExcel] MAP Info => " + excelInfo.toString());
			LOG.debug("#####[FileController-downloadExcel] LIST HEAD => " + excelHead.toString());
			LOG.debug("#####[FileController-downloadExcel] LIST Body => " + excelBody.toString());

			String downloadFileName = DataUtil.getString(excelInfo, "filen_name") + "_" + DateTimeUtil.getCurrentDateTimeSSS() + "." + ExcelConstant.EXCEL_DOWNLOAD_FILE_EXTENSION;

			// 5. 데이터를 조회한다. 
			List<Map<String, Object>> bodyDataList = excelDataService.getExcelDataList(param);
			LOG.debug("#####[FileController-downloadExcel] LIST Data => " + bodyDataList.toString());

			// 6. EXCEL DATA 생성
			XSSFWorkbook workbook = ExcelUtil.createExcel(excelInfo, excelHead, excelBody, bodyDataList);

			// 7. EXCEL FILE 생성
			File file = new File(fileTempDirectory + excelTempFileName + "_" + DateTimeUtil.getCurrentDateTimeSSS() + "." + ExcelConstant.EXCEL_DOWNLOAD_FILE_EXTENSION);
			FileOutputStream fos = null;	        
			try {
				fos = new FileOutputStream(file);
				workbook.write(fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(workbook!=null) workbook.close();
					if(fos!=null) fos.close();	                
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 8. File을 읽어온다.    
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			// 9. Http Response를 설정한다. 
			result = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, DataUtil.getContentDisposition(request.getHeader("User-Agent"), downloadFileName))
					.contentType(DataUtil.getMediaTypeForFileName(this.servletContext, downloadFileName))
					.contentLength(file.length())
					.body(resource);
		} catch (Exception e) {
			LOG.error("#####[FileController-downloadFile]" + e.getMessage());
		}	

		return result;
	}

	@PostMapping("/readExcelFile")
	@ResponseBody
	public Map<String, Object> readExcelFile(HttpServletRequest request) {
		LOG.debug("##### Read Excel File #####");

		// 최종 결과 데이터
		Map<String, Object> result 			= new HashMap<String, Object>();
		// 임시 저장 결과 데이터
		Map<String, Object> tempFileResult	= null;

		try {
			// 1. 템플릿 파일의 경로를 가져온다.
			String filePath         = DataUtil.getString(request, "excelTemplate");			
			LOG.debug("#####[FileController-readExcelFile] TEMPLATE FILE PATH   => " + filePath);
			LOG.debug("#####[FileController-readExcelFile] EXCEL ROOT FILE PATH => " + excelRootPath);

			// 2. 템플릿 파일의 ROOT 경로를 설정한다. 
			Path templatePath = null;
			if(excelRootPath.indexOf("classpath:") == 0) {
				templatePath = Paths.get(getClass().getClassLoader().getResource(excelRootPath.replace("classpath:", "") + filePath).toURI());
			} else {
				templatePath = Paths.get(excelRootPath + filePath);
			}
			LOG.debug("#####[FileController-readExcelFile] TEMPLATE PATH => " + templatePath);

			// 3. XML FILE 을 Map으로 변경한다. 
			Map<String, Object> xmlMap = ParseUtil.parseXmlFileToMap(templatePath);
			LOG.debug("#####[FileController-readExcelFile] XML TO MAP => " + xmlMap.toString());
			if(xmlMap == null || xmlMap.isEmpty()) {
				LOG.error("#####[FileController-readExcelFile]Excel Template File is null");
				return result;
			}

			// 4. EXCEL FILE 생성 정보를 읽어온다. 
			List<Map<String, Object>> excelBody = DataUtil.getList(DataUtil.getMap(DataUtil.getMap(xmlMap, "configuration"), "body"), "item");
			LOG.debug("#####[FileController-readExcelFile] LIST Body => " + excelBody.toString());

			// 5. 파일을 임시 저장한다. 
			tempFileResult = FileManager.getInstance().createTempFiles(request, "EXCEL");     	
			LOG.debug("#####[FileController-readExcelFile] FILE INFO => " + tempFileResult.toString());

			// 6. 임시 저장이 성공일 경우 엑셀 파일을 분석한다. 
			if(FileConstant.SUCCESS.equals(DataUtil.getString(tempFileResult, "resultCode"))) {
				createResultMsg(result, FileConstant.SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
				result.put("excelDataList", ExcelUtil.readExcel(DataUtil.getList(tempFileResult, "fileList"), excelBody, fileTempDirectory));
			} else {
				createResultMsg(result, FileConstant.ERROR, CommonMessageConstrant.ERROR_MSG);
			}   
		} catch (URISyntaxException e) {
			e.printStackTrace();
			LOG.debug("#####[FileController-readExcelFile] URISyntaxException => " + e.getMessage());
			createResultMsg(result, FileConstant.ERROR, CommonMessageConstrant.ERROR_UNKNOWN_MSG);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("#####[FileController-readExcelFile] Exception => " + e.getMessage());
			createResultMsg(result, FileConstant.ERROR, CommonMessageConstrant.ERROR_UNKNOWN_MSG);
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
