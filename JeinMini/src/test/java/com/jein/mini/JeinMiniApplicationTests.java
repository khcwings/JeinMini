package com.jein.mini;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jein.mini.biz.common.domain.CommonMenu;
import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;
import com.jein.mini.biz.common.persistence.CommonUserRepository;
import com.jein.mini.service.SecurityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JeinMiniApplicationTests {
	@Autowired
	private CommonMenuRepository menuRepo;
	
	@Autowired
	private CommonUserRepository userRepo;
		
	@Autowired
	private SecurityService securityService;

	@Test
	public void testCrypt() {

		String strVal = "가나다라마바사아자차타카파하ABCDEFGHIJKLMNOPQRSTUVWXYNZ1234567890!@#$%^&*()_+-=";
		
		System.out.println("# " + securityService.getSHA256(strVal));
		System.out.println("# " + securityService.getEncryptAES128(strVal));
		System.out.println("# " + securityService.getDecryptAES128(securityService.getEncryptAES128(strVal)));
		System.out.println("# " + securityService.getEncryptAES256(strVal));
		System.out.println("# " + securityService.getDecryptAES256(securityService.getEncryptAES256(strVal)));
		
		
		
		/*# c38aed5a070aac9d8bd9180164b4234fae205897ebca07dbca324aeb4ce8ac64
		# ArPOI0Zk/vQ6BswbFZFdCqP45aCcADV+vjwdH/AGSZXmdLYLXtCXmpxxQx8A2b4/VDpIoPreDwajjOXN0Nasfsfu/nNWQRTuTJpH7IJhn2+UGGWu8drI84q6v1gw03tN
		# 가나다라마바사아자차타카파하ABCDEFGHIJKLMNOPQRSTUVWXYNZ1234567890!@#$%^&*()_+-=
		# LGfU+65D9MpiXt+ZY8NzHP18Wm5ALaigfFa244usTy3VNT+vnuDgZICSO0QYOEdWwvIxzLidzZuMrHrPJNKBXV2mo/mqNfIQkPZMIanlN2I90EQhLHELmXLOJAo4+rzv
		# 가나다라마바사아자차타카파하ABCDEFGHIJKLMNOPQRSTUVWXYNZ1234567890!@#$%^&*()_+-=*/
	}

	@Test
	public void insertMenu() {
		for(int i = 1; i < 5; i++) {
		CommonMenu menu = new CommonMenu();

		menu.setMenuId("SAMPLE0" + i);
		menu.setMenuUrl("/sample/view/sample0" + i);
		menu.setMenuName("샘플 0" + i);
		menu.setMenuPath("/sample/sample0" + i);
		menuRepo.save(menu);
		}
	}
	
	@Test
	public void insertCommonMenu() {
		// 로그인 페이지
		CommonMenu menu = new CommonMenu();
		menu.setMenuId("CM00001");
		menu.setMenuUrl("/common/login");
		menu.setMenuName("로그인");
		menu.setMenuPath("/common/login");
		menuRepo.save(menu);
				
		// 공통 메인 페이지
		menu.setMenuId("CM00002");
		menu.setMenuUrl("/common/view/main");
		menu.setMenuName("메인");
		menu.setMenuPath("/common/main");
		menuRepo.save(menu);
	}
	
	@Test
	public void insertCommonUser() {
		// 로그인 페이지
		CommonUser user = new CommonUser();
		user.setUserId("jeinsoft");
		user.setUserPwd(securityService.getSHA256("jein0915!"));
		user.setUserName("테스트유저");
		user.setUserEmail("khcwings@jeinsoft.co.kr");
		user.setUserPhone("010-3102-4426");
		userRepo.save(user);
	}
}
