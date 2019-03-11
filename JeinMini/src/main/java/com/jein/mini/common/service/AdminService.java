package com.jein.mini.common.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.biz.common.persistence.CommonUserRepository;
import com.jein.mini.common.dao.AdminMapper;
import com.jein.mini.constant.CommonMessageConstrant;
import com.jein.mini.service.SecurityService;
import com.jein.mini.util.DataUtil;

@Service
public class AdminService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AdminService.class);
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private CommonUserRepository userRepo;
	
	@Autowired
	private SecurityService securityService;
	
	/**
	 * 공통 코드 리스트를 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getCodeList(Map<String, Object> params) {
		return adminMapper.selectCodeList(params);
	}
	
	/**
	 * 공통 코드의 다음 그룹코드 아이디값을 조회
	 * @param params
	 * @return
	 */
	public String getNextCodeGrpId() {
		return adminMapper.selectNextCodeGrpId();
	}
	
	/**
	 * 유저 리스트를 조회한다.
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getUserList(Map<String, Object> params){
		System.out.println(params.get("userId").toString());
		System.out.println(params.get("userName").toString());
		return adminMapper.selectUserList(params);
	}
	
	/**
	 * 유저 리스트를 생성/수정/삭제 한다.
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String setUserList(Map<String, Object> params){
		
		StringBuffer retMsg = new StringBuffer();
		
		try{
			
			List<Map<String, Object>> gridList = (List<Map<String, Object>>) params.get("gridData");
			int cnt = 0;
			String rowStatus = "";
			CommonUser commonUser = new CommonUser();
			for(Map<String, Object> gridData : gridList){
				rowStatus = DataUtil.getString(gridData, "ROW_STATUS");
				if("D".equals(rowStatus)){
					//삭제
					userRepo.deleteUser("Y", DataUtil.getString(params, "userId"), DataUtil.getString(gridData, "USER_ID"));
				}else{
					//저장
					commonUser.setUserId(DataUtil.getString(gridData, "USER_ID"));
					commonUser.setUserEmail(DataUtil.getString(gridData, "USER_EMAIL"));
					commonUser.setUserName(DataUtil.getString(gridData, "USER_NAME"));
					commonUser.setUserPhone(DataUtil.getString(gridData, "USER_PHONE"));
					commonUser.setUserPwd(securityService.getSHA256("0000"));
					commonUser.setCreateId(DataUtil.getString(params, "userId"));
					commonUser.setUpdateId(DataUtil.getString(params, "userId"));
					commonUser.setDeleteYn(DataUtil.getString(gridData, "DELETE_YN").charAt(0));
					userRepo.save(commonUser);
				}
				cnt++;
			}
			
			retMsg.append(cnt + "건의 데이터가 변경되었습니다.");
			
		}catch(Exception e){
			LOG.error("##### [AdminService-setUserList] Exception : " + e.getMessage());
			retMsg.append(CommonMessageConstrant.ERROR_MSG);
		}
		
		
		return retMsg.toString();
	}
	
	/**
	 * 모듈 리스트 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getModuleList(Map<String, Object> params) {
		return adminMapper.selectModuleList(params);
	}
	
	/**
	 * 메뉴 리스트 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMenuList(Map<String, Object> params) {
		return adminMapper.selectMenuList(params);
	}
}
