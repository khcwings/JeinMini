package com.jein.mini;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jein.mini.biz.common.domain.CommonMenu;
import com.jein.mini.biz.common.domain.CommonRole;
import com.jein.mini.biz.common.domain.CommonRoleMenu;
import com.jein.mini.biz.common.domain.CommonRoleUser;
import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;
import com.jein.mini.biz.common.persistence.CommonRoleMenuRepository;
import com.jein.mini.biz.common.persistence.CommonRoleRepository;
import com.jein.mini.biz.common.persistence.CommonRoleUserRepository;
import com.jein.mini.biz.common.persistence.CommonUserRepository;
import com.jein.mini.service.MailService;
import com.jein.mini.service.SecurityService;
import com.jein.mini.util.ParseUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JeinMiniApplicationTests {
	private static final Logger LOG = LoggerFactory.getLogger(JeinMiniApplicationTests.class);


	@Autowired
	private CommonUserRepository userRepo;

	@Autowired
	private CommonRoleRepository roleRepo;	

	@Autowired
	private CommonMenuRepository menuRepo;
	
	@Autowired
	private CommonRoleUserRepository roleUserRepo;

	@Autowired
	private CommonRoleMenuRepository roleMenuRepo;
	

	@Autowired
	private SecurityService securityService;	

	@Autowired
	private MailService mailService;

	/**
	 * 유저 등록
	 */
	@Test
	public void insertCommonUser() {
		CommonUser user = new CommonUser();
		user.setUserId("jeinsoft");
		user.setUserPwd(securityService.getSHA256("jein0915!"));
		user.setUserName("테스트유저");
		user.setUserEmail("khcwings@jeinsoft.co.kr");
		user.setUserPhone("010-3102-4426");
		user.setCreateId("jeinsoft");
		userRepo.save(user);
	}

	/**
	 * ROLE 등록
	 */
	@Test
	public void insertCommonRole() {
		CommonRole role = new CommonRole();
		role.setRoleId("DEFAULT_001");
		role.setRoleName("기본 메뉴 등록");
		role.setRoleDesc("모든 유저에게 주어지는 권한");
		role.setCreateId("jeinsoft");
		roleRepo.save(role);

		role.setRoleId("ADMIN_001");
		role.setRoleName("관리자 메뉴 등록");
		role.setRoleDesc("관리자 유저에게 주어지는 권한");
		role.setCreateId("jeinsoft");
		roleRepo.save(role);
	}

	/**
	 * 메뉴 등록
	 */
	@Test
	public void insertCommonMenu() {
		CommonMenu menu = new CommonMenu();
		menu.setCreateId("jeinsoft");
		
		// 로그인 페이지
		setMenu("CMV00001", "/common/view/login", "로그인", "/common/login", "", "COMMON", 1, 1, 'N');

		// 로그아웃 페이지
		setMenu("CMV00002", "/common/view/loginout", "로그아웃", "/common/loginout", "", "COMMON", 1, 1, 'N');

		// 메인 페이지
		setMenu("CMV00003", "/common/view/main", "메인", "/common/main", "", "COMMON", 1, 1);

		// 윈도우 팝업 페이지
		setMenu("CMP00001", "/common/popup/windowPopup01", "윈도우 팝업", "/layout/windowPopup01", "", "POPUP", 1, 1, 'N');

		// 샘플 팝업 페이지
		setMenu("SP00001", "/sample/view/layerPopup", "레이어 팝업", "/sample/layerPopup", "", "POPUP", 1, 1);

		// 샘플 TOP 페이지		
		setMenu("SM00001", "", "Sample", "", "", "VIEW", 1, 1);
		for(int i = 2; i < 5; i++) {
			setMenu("SM0000" + i, "/sample/view/sample0" + i, "샘플 0" + i, "/sample/sample0" + i, "SM00001", "VIEW", 2, i);
		}
		setMenu("ADV00001", "", "Admin", "", "", "VIEW", 1, 1);
		setMenu("ADV00002", "", "메뉴", "", "ADV00001", "VIEW", 2, 1);
		setMenu("ADV00003", "", "유저", "", "ADV00001", "VIEW", 2, 2);
		setMenu("ADV00004", "", "권한", "", "ADV00001", "VIEW", 2, 3);
		setMenu("ADV00005", "/admin/view/menuManager", "메뉴관리", "/admin/menuManager", "ADV00002", "VIEW", 3, 1);
		setMenu("ADV00006", "/admin/view/userManager", "유저관리", "/admin/userManager", "ADV00003", "VIEW", 3, 1);
		setMenu("ADV00007", "/admin/view/roleManager", "권한관리", "/admin/roleManager", "ADV00004", "VIEW", 3, 1);
		setMenu("ADV00008", "/admin/view/roleUserManager", "유저권한관리", "/admin/roleUserManager", "ADV00005", "VIEW", 3, 2);
		setMenu("ADV00009", "/admin/view/roleMenuManager", "메뉴권한관리", "/admin/roleMenuManager", "ADV00005", "VIEW", 3, 3);
	}
	public void setMenu(String menuId, String menuUrl, String menuName, String menuPath, String upperMenuId, String menuType, int menuLevel, int displayOrder) {
		setMenu(menuId, menuUrl, menuName, menuPath, upperMenuId, menuType, menuLevel, displayOrder, 'Y');
	}
	
	public void setMenu(String menuId, String menuUrl, String menuName, String menuPath, String upperMenuId, String menuType, int menuLevel, int displayOrder, char loginYn) {
		CommonMenu menu = new CommonMenu();
		menu.setCreateId("jeinsoft");
		menu.setMenuId(menuId);
		menu.setMenuUrl(menuUrl);
		menu.setMenuName(menuName);
		menu.setMenuPath(menuPath);
		menu.setUpperMenuId(upperMenuId);
		menu.setMenuType(menuType);
		menu.setMenuLevel(menuLevel);
		menu.setLoginYn(loginYn);
		menu.setDisplayOrder(displayOrder);
		menuRepo.save(menu);
	}
	/**
	 * 권한과 유저간의 관계
	 */
	@Test
	public void insertCommonRoleUser() {
		CommonRoleUser roleUser = new CommonRoleUser();
		CommonRoleUser.RoleUserPk roleUserPk = new CommonRoleUser.RoleUserPk();
		roleUserPk.setRoleId("DEFAULT_001");
		roleUserPk.setUserId("jeinsoft");
		roleUser.setId(roleUserPk);
		roleUser.setStartDt("20181201");
		roleUser.setEndDt("20201231");
		roleUser.setCreateId("jeinsoft");
		roleUserRepo.save(roleUser);
		
		roleUserPk.setRoleId("ADMIN_001");
		roleUserRepo.save(roleUser);
	}
	
	/**
	 * 권한과 메뉴ㅜ의 관계
	 */
	@Test
	public void insertCommonRoleMenu() {
		CommonRoleMenu roleMenu = new CommonRoleMenu();
		CommonRoleMenu.RoleMenuPk roleMenuPk= new CommonRoleMenu.RoleMenuPk();
		roleMenuPk.setRoleId("DEFAULT_001");
		roleMenuPk.setMenuId("CM00001");
		roleMenu.setId(roleMenuPk);
		roleMenu.setCreateId("jeinsoft");
		roleMenuRepo.save(roleMenu);
		
		roleMenuPk.setMenuId("CM00002");
		roleMenuRepo.save(roleMenu);
		
		roleMenuPk.setMenuId("CM00003");
		roleMenuRepo.save(roleMenu);
		
		roleMenuPk.setMenuId("SM00001");
		roleMenuRepo.save(roleMenu);

		roleMenuPk.setMenuId("SM00002");
		roleMenuRepo.save(roleMenu);

		roleMenuPk.setMenuId("SM00003");
		roleMenuRepo.save(roleMenu);

		roleMenuPk.setMenuId("SM00004");
		roleMenuRepo.save(roleMenu);
		
		roleMenuPk.setRoleId("ADMIN_001");
		roleMenuPk.setMenuId("ADV00001");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00002");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00003");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00004");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00005");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00006");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00007");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00008");
		roleMenuRepo.save(roleMenu);
		roleMenuPk.setMenuId("ADV00009");
		roleMenuRepo.save(roleMenu);
	}

	/*
@Test
public void testCrypt() {

	String strVal = "가나다라마바사아자차타카파하ABCDEFGHIJKLMNOPQRSTUVWXYNZ1234567890!@#$%^&*()_+-=";

	System.out.println("# " + securityService.getSHA256(strVal));
	System.out.println("# " + securityService.getEncryptAES128(strVal));
	System.out.println("# " + securityService.getDecryptAES128(securityService.getEncryptAES128(strVal)));
	System.out.println("# " + securityService.getEncryptAES256(strVal));
	System.out.println("# " + securityService.getDecryptAES256(securityService.getEncryptAES256(strVal)));
}




@Test
public void mailSend() {
	LOG.debug("##### TEST MAIL SEND START #####");
	Map<String, Object> model = new HashMap<String, Object>();
	model.put("logoImg", "http://jeinsoft.co.kr/img/jein_logo.png");
	model.put("name", "김희철");
	model.put("location", "(463-400) 경기 성남시 분당구 삼평동 679 삼환하이펙스 B동 403호");

	List<String> receiver = new ArrayList<String>();
	receiver.add("khcwings@jeinsoft.co.kr");
	receiver.add("khcwings@naver.com");

	List<String> attachFiles = new ArrayList<String>();
	attachFiles.add("C:\\FileTest\\Biz\\Jein\\계약관리안내문.pdf");

	String content = "안녕하세요.\n\r김희철입니다.\n\r\n\r메일 발송 테스트 중입니다.\n\n이상입니다.";

	// 1인 Text Mail Text
	//mailService.sendTextMail("khcwings@jeinsoft.co.kr", "[1인]JEIN MINI TEST => TEXT MAIL", content);
	//// 다수 Text Mail Text
	//mailService.sendTextMail(receiver, "[다수]JEIN MINI TEST => TEXT MAIL", content);
	// 다수 Text Mail Text 첨부
	mailService.sendTextMail(receiver, "[다수]JEIN MINI TEST => TEXT MAIL + FILE", content, attachFiles);


	// 1인 Template Mail Text
	//mailService.sendTemplateMail("mail/mailSampleTemplate", "khcwings@jeinsoft.co.kr", "[1인 ]JEIN MINI TEST => TEMPLATE MAIL", model);
	// 다수 Template Mail Text
	//mailService.sendTemplateMail("mail/mailSampleTemplate", receiver, "[다수]JEIN MINI TEST => TEMPLATE MAIL", model);
	// 다수 Template Mail Text 첨부
	mailService.sendTemplateMail("mail/mailSampleTemplate", receiver, "[다수]JEIN MINI TEST => TEMPLATE MAIL + FILE", model, attachFiles);
	LOG.debug("##### TEST MAIL SEND END #####");
}*/
}
