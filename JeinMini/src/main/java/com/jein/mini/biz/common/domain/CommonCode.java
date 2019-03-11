package com.jein.mini.biz.common.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
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
@Table(name="cm_code")
public class CommonCode {
	@EmbeddedId
	private CodePk id;
	
	@Column(length=128)
	private String codeGrpName;
	@Column(length=128)
	private String codeName;
	@Column(length=512)
	private String codeDesc;
	private int    codeLevel;
	private int    displayOrder;
	private char   useYn='N';
	@Column(length=64)
	private String attrValue1;
	@Column(length=64)
	private String attrValue2;
	@Column(length=64)
	private String attrValue3;
	
	@Column(length=16)
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	@Column(length=16)
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
	
	public String getCodeGrpId() {
		return id.getCodeGrpId();
	}
	public void setCodeGrpId(String codeGrpId) {
		id.setCodeGrpId(codeGrpId);
	}
	public String getCodeId() {
		return id.getCodeGrpId();
	}
	public void setCodeId(String codeGrpId) {
		id.setCodeGrpId(codeGrpId);
	}
	
	@Getter
	@Setter
	@ToString
	@Embeddable
	public static class CodePk implements Serializable {
		private static final long serialVersionUID = 1L;
		@Column(length=8)
		private String codeGrpId;
		@Column(length=10, nullable = true)
		private String codeId;
	}
}
