package com.jein.mini.biz.common.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
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
@Table(name="cm_user")
public class CommonUser {
	@Id
	@Column(length=16)
	private String userId;			// 유저 ID
	
	@Column(length=128)
	private String userPwd;			// 유저 Password
	@Column(length=64)
	private String userName;		// 유저 이름
	@Column(length=512)
	private String userEmail;		// 유저 이메일
	@Column(length=16)
	private String userPhone;		// 유저 핸드폰	
	private char   deleteYn = 'N';	// 삭제 여부
	
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
