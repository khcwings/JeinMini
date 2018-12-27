package com.jein.mini.biz.common.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="cm_menu")
public class CommonMenu {
	
	@Id
	private String 	menuId;				// MENU ID
	
	private String 	menuName;			// MENU NAME
	private String 	menuUrl;			// MENU URL
	private String 	menuPath;			// MENU FILE PATH
	private String 	upperMenuId;		// 상위 MENU ID
	private Integer menuLevel;			// MENU LEVEL
	private Integer displayOrder;		// DISPLAY ORDER
	private String  menuType; 			// MENU TYPE : COMMON, VIEW, POPUP 
	private char  	loginYn = 'Y'; 		// 로그인이 필요한 페이지 여부
	private char	useYn = 'N';		// 사용 여부
	
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
