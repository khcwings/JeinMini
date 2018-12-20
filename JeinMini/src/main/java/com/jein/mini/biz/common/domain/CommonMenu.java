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
	private String menuId;
	
	private String menuName;
	private String menuUrl;
	private String menuPath;
	private String upperMenuId;
	private Integer menuLevel;
	private String menuType; // COMMON, VIEW, POPUP 
	
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
