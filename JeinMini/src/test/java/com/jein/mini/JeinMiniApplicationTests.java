package com.jein.mini;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jein.mini.biz.common.domain.CommonMenu;
import com.jein.mini.biz.common.persistence.CommonMenuRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JeinMiniApplicationTests {
	@Autowired
	private CommonMenuRepository menuRepo;

	@Test
	public void contextLoads() {
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
	public void insertMenuLayout() {
		for(int i = 1; i < 3; i++) {
		CommonMenu menu = new CommonMenu();

		menu.setMenuId("Layout0" + i);
		menu.setMenuUrl("/sample/view/layout0" + i);
		menu.setMenuPath("/sample/layout0" + i);
		menuRepo.save(menu);
		}
	}

}
