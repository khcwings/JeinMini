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
@Table(name="cm_user")
public class CommonUser {
	@Id
	private String userId;		// 유저 ID
	
	private String userPwd;		// 유저 Password
	private String userName;	// 유저 이름
	private String userEmail;	// 유저 이메일
	private String userPhone;	// 유저 핸드폰
	
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
