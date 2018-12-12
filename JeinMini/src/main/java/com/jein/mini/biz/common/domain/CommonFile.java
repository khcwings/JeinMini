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
@Table(name="cm_file")
public class CommonFile {
	@Id
	private String 	fileId;				// File Key Id
	
	private String 	originalFileName;	// 원본 파일명
	private String 	changeFileName;		// 변경된 파일명
	private String 	fileType;			// 파일 확장자
	private Long   	fileSize;			// 파일 크기
	private String	filePath;			// 파일 저장 위치
	private String  ownerId;			// 대상 Table의 Key ID
	private Integer ownerSeq;			// 대상 Table의 Key ID의 Sequence
	private char	deleteYn = 'N';		// 삭제 여부
	
	private String createId;
	@CreationTimestamp
	private Timestamp createDt;
	
	private String updateId;
	@UpdateTimestamp
	private Timestamp updateDt;
}
