package com.jein.mini.biz.common.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name="cm_role_menu")
public class CommonRoleMenu {
	@EmbeddedId
	private RoleMenuPk id;
	
	private char createYn = 'N';		// 생성 권한
	private char updateYn = 'N';		// 수정 권한
	private char deleteYn = 'N';		// 삭제 권한
	private char readYn   = 'Y';		// 읽기 권한
		
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
	
	@Getter
	@Setter
	@ToString
	@Embeddable
	public static class RoleMenuPk implements Serializable {
		private static final long serialVersionUID = 1L;

		private String roleId;
		private String menuId;
	}
}
