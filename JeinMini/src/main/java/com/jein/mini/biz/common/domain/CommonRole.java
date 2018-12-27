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
@Table(name="cm_role")
public class CommonRole {
	@Id
	private String 	roleId;				// ROLE ID
	
	private String 	roleName;			// ROLE에 대한 간단 설명
	private String 	roleDesc;			// ROLE에 대한 상세 설명
	private char	useYn = 'N';		// 사용 여부
	
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
