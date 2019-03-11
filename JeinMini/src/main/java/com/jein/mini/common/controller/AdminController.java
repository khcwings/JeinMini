package com.jein.mini.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jein.mini.biz.common.domain.CommonCode;
import com.jein.mini.biz.common.persistence.CommonCodeRepository;
import com.jein.mini.common.service.AdminService;
import com.jein.mini.constant.CommonConstant;
import com.jein.mini.constant.CommonMessageConstrant;
import com.jein.mini.util.DataUtil;

@Controller
@RequestMapping(value="/admin")
public class AdminController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private CommonCodeRepository codeRepo;

	@GetMapping("/view/menuManager")
	public void getMenuManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.debug("##### [AdminController-getMenuManager] VIEW START ######");
		LOG.debug("##### [AdminController-getMenuManager] Params => " + param.toString());
	}
	
	@GetMapping("/view/userManager")
	public void getUserManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.debug("##### [AdminController-getUserManager] VIEW START ######");
		LOG.debug("##### [AdminController-getUserManager] Params => " + param.toString());
	}

	@GetMapping("/view/codeManager")
	public void getCodeManager(Model model, @RequestParam Map<String, Object> param) {
		LOG.debug("##### [AdminController-getCodeManager] VIEW START ######");
		LOG.debug("##### [AdminController-getCodeManager] Params => " + param.toString());
	}

	/**
	 * 공통 코드 리스트를 조회한다. 
	 * @param param
	 * @return
	 */
	@PostMapping("/data/getCodeList")
	@ResponseBody
	public Map<String, Object> getCodeList(@RequestBody Map<String, Object> param) {
		LOG.debug("##### [AdminController-getCodeGrpList] Params => " + param.toString());

		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 서비스 조회
		retMap.put("codeList", adminService.getCodeList(param));
				
		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
	}
	
	/**
	 * 공통 코드 중 그룹 코드를 생성/수정/삭제 한다. 
	 * @param session
	 * @param param
	 * @return
	 */
	@PostMapping("/data/setCodeGrp")
	@ResponseBody
	public Map<String, Object> setCodeGrp(HttpSession session, @RequestBody Map<String, Object> param) {
		LOG.debug("##### [AdminController-setCodeGrp] Params => " + param.toString());
		
		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		StringBuffer retMsg = new StringBuffer();
		
		// 서비스 구분
		String serviceType 						= DataUtil.getString(param, "serviceType");
		List<Map<String, Object>> codeList		= DataUtil.getList(param, "codeList");
		
		int iSucc = 0;
		retMsg.append("요청하신 데이터 " + codeList.size() + "개 중 ");
		if("U".equalsIgnoreCase(serviceType) && !codeList.isEmpty()) {				// 생성 / 수정
			CommonCode code = new CommonCode();
			for(Map<String, Object> item : codeList) {
				try {
					String codeGrpId = DataUtil.getString(item, "CODE_GRP_ID");
					
					CommonCode.CodePk codePk = new CommonCode.CodePk();					
					if("".equals(codeGrpId)) {
						codePk.setCodeGrpId(adminService.getNextCodeGrpId());
						codePk.setCodeId("");
					} else {
						codePk.setCodeGrpId(codeGrpId);
						codePk.setCodeId("");
					}

					code.setId(codePk);
					code.setCodeGrpName(DataUtil.getString(item, "CODE_GRP_NAME"));
					code.setCodeDesc(DataUtil.getString(item, "CODE_DESC"));
					code.setUseYn(DataUtil.getString(item, "USE_YN").charAt(0));
					code.setCodeLevel(1);
					code.setCreateId(getUserId(session));
					code.setUpdateId(getUserId(session));
					
					codeRepo.save(code);
					iSucc++;
				} catch (Exception e) {
					LOG.error("##### [AdminController-setCodeGrp] Exception : " + e.getMessage());
				}
			}
			retMsg.append(iSucc + "개 저장에 성공하였습니다.");
		} else if("D".equalsIgnoreCase(serviceType) && !codeList.isEmpty()) {		// 삭제			
			for(Map<String, Object> item : codeList) {
				try {
					codeRepo.deleteCodeGrpId(DataUtil.getString(item, "codeGrpId"));
					iSucc++;
				} catch (Exception e) {
					LOG.error("##### [AdminController-setCodeGrp] Exception : " + e.getMessage());
				}
			}
			retMsg.append(iSucc + "개 삭제에 성공하였습니다.");
		} else {
			retMsg = new StringBuffer();
			retMsg.append("요청하신 데이터가 존재하지 않습니다. ");
		}

		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, retMsg.toString());
	}
	
	/**
	 * 공통 코드 중 그룹 하위 코드를 생성/수정/삭제 한다. 
	 * @param session
	 * @param param
	 * @return
	 */
	@PostMapping("/data/setCode")
	@ResponseBody
	public Map<String, Object> setCode(HttpSession session, @RequestBody Map<String, Object> param) {
		LOG.debug("##### [AdminController-setCode] Params => " + param.toString());
		
		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		StringBuffer retMsg = new StringBuffer();
		
		// 서비스 구분
		String serviceType 						= DataUtil.getString(param, "serviceType");
		List<Map<String, Object>> codeList		= DataUtil.getList(param, "codeList");
		
		int iSucc = 0;
		retMsg.append("요청하신 데이터 " + codeList.size() + "개 중 ");
		if("U".equalsIgnoreCase(serviceType) && !codeList.isEmpty()) {				// 생성 / 수정
			CommonCode code = new CommonCode();
			for(Map<String, Object> item : codeList) {
				try {
					CommonCode.CodePk codePk = new CommonCode.CodePk();					
					codePk.setCodeGrpId(DataUtil.getString(item, "CODE_GRP_ID"));
					codePk.setCodeId(DataUtil.getString(item, "CODE_ID"));

					code.setId(codePk);
					code.setCodeName(DataUtil.getString(item, "CODE_NAME"));
					code.setCodeDesc(DataUtil.getString(item, "CODE_DESC"));
					code.setDisplayOrder(DataUtil.getInteger(item, "DISPLAY_ORDER", 1));
					code.setAttrValue1(DataUtil.getString(item, "ATTR_VALUE1"));
					code.setAttrValue2(DataUtil.getString(item, "ATTR_VALUE2"));
					code.setAttrValue3(DataUtil.getString(item, "ATTR_VALUE3"));
					code.setUseYn(DataUtil.getString(item, "USE_YN").charAt(0));
					code.setCodeLevel(2);
					code.setCreateId(getUserId(session));
					code.setUpdateId(getUserId(session));
					
					codeRepo.save(code);
					iSucc++;
				} catch (Exception e) {
					LOG.error("##### [AdminController-setCode] Exception : " + e.getMessage());
				}
			}
			retMsg.append(iSucc + "개 저장에 성공하였습니다.");
		} else if("D".equalsIgnoreCase(serviceType) && !codeList.isEmpty()) {		// 삭제			
			for(Map<String, Object> item : codeList) {
				try {
					codeRepo.deleteCodeId(DataUtil.getString(item, "codeGrpId"), DataUtil.getString(item, "codeId"));
					iSucc++;
				} catch (Exception e) {
					LOG.error("##### [AdminController-setCode] Exception : " + e.getMessage());
				}
			}
			retMsg.append(iSucc + "개 삭제에 성공하였습니다.");
		} else {
			retMsg = new StringBuffer();
			retMsg.append("요청하신 데이터가 존재하지 않습니다. ");
		}

		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, retMsg.toString());
	}
	
	/**
	 * 유저 리스트를 조회한다.
	 * @param session
	 * @param param
	 * @return
	 */
	@PostMapping("/data/getUserList")
	@ResponseBody
	public Map<String, Object> getUser(HttpSession session, @RequestBody Map<String, Object> param) {
		LOG.debug("##### [AdminController-getUser] Params => " + param.toString());

		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 서비스 조회
		retMap.put("userList", adminService.getUserList(param));
				
		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
	}
	
	/**
	 * 유저 리스트를 생성/수정/삭제 한다.
	 * @param session
	 * @param param
	 * @return
	 */
	@PostMapping("/data/setUser")
	@ResponseBody
	public Map<String, Object> setUser(HttpSession session, @RequestBody Map<String, Object> param){
		LOG.debug("##### [AdminController-setUser] Params => " + param.toString());

		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		param.put("userId", getUserId(session));
		
		// 서비스 실행
		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, adminService.setUserList(param));
	}
	
	/**
	 * 모듈 리스트를 조회한다.
	 * @param param
	 * @return
	 */
	@PostMapping("/data/getModuleList")
	@ResponseBody
	public Map<String, Object> getModuleList(@RequestBody Map<String, Object> param){
		LOG.debug("##### [AdminController-getModuleList] Params => " + param.toString());
		
		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 서비스 조회
		retMap.put("moduleList", adminService.getModuleList(param));
				
		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
	}
	
	/**
	 * 메뉴 리스트를 조회한다
	 * @param param
	 * @return
	 */
	@PostMapping("/data/getMenuList")
	@ResponseBody
	public Map<String, Object> getMenuList(@RequestBody Map<String, Object> param){
		LOG.debug("##### [AdminController-getMenuList] Params => " + param.toString());
		
		// 리턴을 위한 Map
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		// 서비스 조회
		retMap.put("menuList", adminService.getMenuList(param));
				
		return createResultMsg(retMap, CommonConstant.RESULT_SUCCESS, CommonMessageConstrant.SUCCESS_MSG);
	}
}
