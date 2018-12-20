package com.jein.mini.biz.common.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="ch_mail")
public class CommonHistoryMail {
	@Id
	private String reqTime;
	
	private String userId;
	private String receiver;
	private String subject;
	private char   attachYn = 'N';
	private char   resultCode;
	private String resultMsg;	
}
