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
		menu.setMenuId("CM00001");
		menu.setMenuUrl("/common/login");
		menu.setMenuName("로그인");
		menu.setMenuPath("/common/login");
		menu.setMenuType("COMMON");
		menu.setMenuLevel(1);
		menu.setDisplayOrder(1);
		menu.setLoginYn('N');
		menuRepo.save(menu);

		// 로그아웃 페이지
		menu.setMenuId("CM00002");
		menu.setMenuUrl("/common/loginout");
		menu.setMenuName("로그아웃");
		menu.setMenuPath("/common/loginout");
		menu.setMenuType("COMMON");
		menu.setMenuLevel(1);
		menu.setDisplayOrder(1);
		menu.setLoginYn('N');
		menuRepo.save(menu);

		// 메인 페이지
		menu.setMenuId("CM00003");
		menu.setMenuUrl("/common/view/main");
		menu.setMenuName("메인");
		menu.setMenuPath("/common/main");
		menu.setMenuType("COMMON");
		menu.setMenuLevel(1);
		menu.setDisplayOrder(1);
		menuRepo.save(menu);

		// 윈도우 팝업 페이지
		menu.setMenuId("CP00001");
		menu.setMenuUrl("/common/popup/windowPopup01");
		menu.setMenuName("윈도우 팝업 유형 01");
		menu.setMenuPath("/layout/windowPopup01");
		menu.setMenuType("POPUP");
		menu.setMenuLevel(1);
		menu.setDisplayOrder(1);
		menu.setLoginYn('N');
		menuRepo.save(menu);

		// 샘플 팝업 페이지
		menu.setMenuId("SP00001");
		menu.setMenuUrl("/sample/view/layerPopup");
		menu.setMenuName("레이어 팝업");
		menu.setMenuPath("/sample/layerPopup");
		menu.setMenuType("POPUP");
		menu.setMenuLevel(1);
		menu.setDisplayOrder(1);
		menuRepo.save(menu);

		// 샘플 TOP 페이지
		menu.setMenuId("SM00001");
		menu.setMenuUrl("/sample/view/layerPopup");
		menu.setMenuName("샘플 TOP");
		menu.setMenuType("VIEW");
		menu.setMenuLevel(1);
		menu.setDisplayOrder(1);
		menuRepo.save(menu);

		for(int i = 2; i < 5; i++) {
			menu.setMenuId("SM0000" + i);
			menu.setMenuUrl("/sample/view/sample0" + i);
			menu.setMenuName("샘플 0" + i);
			menu.setMenuPath("/sample/sample0" + i);
			menu.setUpperMenuId("SM00001");
			menu.setMenuType("VIEW");
			menu.setMenuLevel(2);
			menu.setDisplayOrder(i);
			menuRepo.save(menu);
		}
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
	}
	
	/**
	 * 권한과 유저간의 관계
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
