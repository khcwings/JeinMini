package com.jein.mini.constant;

public class ExcelConstant {
	public static final String EXCEL_DOWNLOAD_FILE_EXTENSION		= "xlsx";			// Download Extension
	public static final String EXCEL_HEADER_TITLE_NO 				= "No.";			// Header Title Number
	public static final String EXCEL_DATA_CODE	 					= "excelDataCode";	// 데이터 조회를 위한 Key

	// TEMPLATE INFOMATION
	public static final String EXCEL_TEMPLATE_TOP_ELEMENT_NAME 		= "configuration";	// 템플릿 파일의 시작 위치
	// TEMPLATE SYTLE INFOMATION
	public static final String EXCEL_TEMPLATE_STYLE_ALIGN			= "align";			// 정렬
	public static final String EXCEL_TEMPLATE_STYLE_ALIGN_CENTER	= "center";			// 가운데 정렬
	public static final String EXCEL_TEMPLATE_STYLE_ALIGN_LEFT		= "left";			// 왼쪽 정렬
	public static final String EXCEL_TEMPLATE_STYLE_ALIGN_RIGHT		= "right";			// 오른쪽 정렬
	

	public static final String EXCEL_TEMPLATE_CELL_WIDTH			= "width";			// 템플릿 파일 셀 넓이 고정값 Key
	public static final int    EXCEL_TEMPLATE_CELL_WIDTH_DEFAULT	= 30;				// 셀 넓이 기본 고정값
	public static final String EXCEL_TEMPLATE_CELL_BACKGROUND_COLOR = "background";		// 템플릿 파일 셀 백그라운드 색상 Key
	public static final String EXCEL_TEMPLATE_CELL_FONT_SIZE		= "font_size";		// 템플릿 파일 셀 폰트 크기 Key
	public static final int    EXCEL_TEMPLATE_CELL_FONT_SIZE_DEFAULT= 11;				// 폰트 기본 크기
}
