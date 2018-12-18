package com.jein.mini;

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
import com.jein.mini.biz.common.domain.CommonUser;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;
import com.jein.mini.biz.common.persistence.CommonUserRepository;
import com.jein.mini.service.MailService;
import com.jein.mini.service.SecurityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JeinMiniApplicationTests {
	private static final Logger LOG = LoggerFactory.getLogger(JeinMiniApplicationTests.class);

	@Autowired
	private CommonMenuRepository menuRepo;

	@Autowired
	private CommonUserRepository userRepo;

	@Autowired
	private SecurityService securityService;	

	@Autowired
	private MailService mailService;

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
		
		LOG.debug("##### TEST MAIL SEND START 1 #####");
		// 1인 Text Mail Text
		mailService.sendTextMail("khcwings@jeinsoft.co.kr", "[1인]JEIN MINI TEST => TEXT MAIL", content);
		// 다수 Text Mail Text
		mailService.sendTextMail(receiver, "[다수]JEIN MINI TEST => TEXT MAIL", content);
		// 다수 Text Mail Text 첨부
		mailService.sendTextMail(receiver, "[다수]JEIN MINI TEST => TEXT MAIL + FILE", content, attachFiles);
		
		
		// 1인 Template Mail Text
		mailService.sendTemplateMail("mail/mailSampleTemplate", "khcwings@jeinsoft.co.kr", "[1인 ]JEIN MINI TEST => TEMPLATE MAIL", model);
		// 다수 Template Mail Text
		mailService.sendTemplateMail("mail/mailSampleTemplate", receiver, "[다수]JEIN MINI TEST => TEMPLATE MAIL", model);
		// 다수 Template Mail Text 첨부
		mailService.sendTemplateMail("mail/mailSampleTemplate", receiver, "[다수]JEIN MINI TEST => TEMPLATE MAIL + FILE", model, attachFiles);
		LOG.debug("##### TEST MAIL SEND END #####");
	}
}
