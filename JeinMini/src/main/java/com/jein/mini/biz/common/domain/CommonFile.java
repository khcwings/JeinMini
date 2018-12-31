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
@Table(name="cm_file")
public class CommonFile {
	@Id
	@Column(length=32)
	private String 	fileId;				// File Key Id

	@Column(length=1024)
	private String 	originalFileName;	// 원본 파일명
	@Column(length=1024)
	private String 	changeFileName;		// 변경된 파일명
	@Column(length=8)
	private String 	fileType;			// 파일 확장자
	private Long   	fileSize;			// 파일 크기
	@Column(length=256)
	private String	filePath;			// 파일 저장 위치
	@Column(length=32)
	private String  ownerId;			// 대상 Table의 Key ID
	private Integer ownerSeq;			// 대상 Table의 Key ID의 Sequence
	private char	deleteYn = 'N';		// 삭제 여부
	
	@Column(length=16)
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	@Column(length=16)
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
