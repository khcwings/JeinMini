package com.jein.mini.util;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jein.mini.constant.ExcelConstant;

public class ExcelUtil {
	
	/**
	 * Excel을 생성한다. 
	 * @param infoMap
	 * @param headInfoList
	 * @param bodyInfoList
	 * @param bodyDataList
	 * @return
	 */
	public static XSSFWorkbook createExcel(Map<String, Object> infoMap, List<Map<String, Object>> headInfoList, 
			List<Map<String, Object>> bodyInfoList, List<Map<String, Object>> bodyDataList) {
		XSSFWorkbook workbook = new XSSFWorkbook();		
		XSSFSheet sheet = workbook.createSheet(DataUtil.getString(infoMap, "sheet_name"));

		// Title 생성
		XSSFRow titleRow  = sheet.createRow(0);
		Cell cell		  = titleRow.createCell(0);
		cell.setCellValue(DataUtil.getString(infoMap, "title"));
		cell.setCellStyle(createStyle(workbook, infoMap));
		// Title Cell Merge
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, headInfoList.size()));

		// Header 생성
		XSSFRow headRow = sheet.createRow(2);
		// Header Titie 'No.' 생성
		createHeadNo(workbook, headRow);
		// Header Title 생성
		if(headInfoList != null && !headInfoList.isEmpty()) {			
			int iHeadCellCnt = 1;
			for(Map<String, Object> headInfo : headInfoList) {
				// Cell Style 설정
				CellStyle headerStyle = createStyle(workbook, headInfo);
				// Cell 생성
				Cell headCell = headRow.createCell(iHeadCellCnt);		
				headCell.setCellValue(DataUtil.getString(headInfo, "title"));
				headCell.setCellStyle(headerStyle);

				// Cell Width Setting
				sheet.setColumnWidth(iHeadCellCnt, 
						((DataUtil.getInteger(
								DataUtil.getString(headInfo, ExcelConstant.EXCEL_TEMPLATE_CELL_WIDTH), 
								ExcelConstant.EXCEL_TEMPLATE_CELL_WIDTH_DEFAULT)
								) * 100)
						);
				iHeadCellCnt++;
			}
		}
		
		
		// Data 생성
		if(bodyDataList != null && !bodyDataList.isEmpty()) {
			int bodyCnt = 3;
			for(Map<String, Object> bodyData : bodyDataList) {
				XSSFRow bodyRow = sheet.createRow(bodyCnt);
				// Body Titie 'No.' 생성
				createBodyNo(workbook, bodyRow, (bodyCnt-2));

				int iRowCellCnt = 1;
				for(Map<String, Object> bodyInfo : bodyInfoList) {
					// Cell Style 설정
					CellStyle bodyStyle = createStyle(workbook, bodyInfo);
					// Cell 생성
					Cell bodyCell = bodyRow.createCell(iRowCellCnt++);		
					bodyCell.setCellValue(DataUtil.getString(bodyData, DataUtil.getString(bodyInfo, "column")));
					bodyCell.setCellStyle(bodyStyle);
				}

				bodyCnt++;
			}
		}		

		return workbook;
	}

	/**
	 * 기본적인 Cell Style 설정
	 * @param workbook
	 * @return
	 */
	private static CellStyle createDefaultCellStyle(XSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN); 
		cellStyle.setBorderLeft(BorderStyle.THIN); 
		cellStyle.setBorderRight(BorderStyle.THIN); 
		cellStyle.setBorderTop(BorderStyle.THIN); 
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return cellStyle;
	}

	/**
	 * Header Titie No Create
	 * @param workbook
	 * @param headRow
	 */
	private static void createHeadNo(XSSFWorkbook workbook, XSSFRow headRow) {
		// Cell Style 설정
		CellStyle style = createDefaultCellStyle(workbook);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);			
		style.setFillForegroundColor((short)5);
		
		// Cell 생성
		Cell headCell = headRow.createCell(0);		
		headCell.setCellValue(ExcelConstant.EXCEL_HEADER_TITLE_NO);
		headCell.setCellStyle(style);
	}

	/**
	 * Body Titie No Create
	 * @param workbook
	 * @param bodyRow
	 * @param iNo
	 */
	private static void createBodyNo(XSSFWorkbook workbook, XSSFRow bodyRow, int iNo) {
		// Cell Style 설정
		CellStyle style = createDefaultCellStyle(workbook);
		// Cell 생성
		Cell bodyCell = bodyRow.createCell(0);		
		bodyCell.setCellValue(iNo);
		bodyCell.setCellStyle(style);
	}

	/**
	 * Template File의 Style을 적용한다.
	 * @param workbook
	 * @param styleInfo
	 * @return
	 */
	private static CellStyle createStyle(XSSFWorkbook workbook, Map<String, Object> styleInfo) {
		CellStyle cellStyle = createDefaultCellStyle(workbook);

		// 가로 정렬
		String align = DataUtil.getString(styleInfo, ExcelConstant.EXCEL_TEMPLATE_STYLE_ALIGN);
		if(!align.isEmpty()) {
			if(ExcelConstant.EXCEL_TEMPLATE_STYLE_ALIGN_LEFT.equals(align.toLowerCase())) {
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
			} else if(ExcelConstant.EXCEL_TEMPLATE_STYLE_ALIGN_RIGHT.equals(align.toLowerCase())) {
				cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			}
		}
		
		// Back Ground Color
		String background = DataUtil.getString(styleInfo, ExcelConstant.EXCEL_TEMPLATE_CELL_BACKGROUND_COLOR);
		if(!background.isEmpty()) {			
			// 단색 채우기 설정
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);	
			
			cellStyle.setFillForegroundColor(Short.parseShort(background)); 
		}
		
		// Font
		int fontSize = DataUtil.getInteger(DataUtil.getString(styleInfo, ExcelConstant.EXCEL_TEMPLATE_CELL_FONT_SIZE), ExcelConstant.EXCEL_TEMPLATE_CELL_FONT_SIZE_DEFAULT);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)fontSize);
		cellStyle.setFont(font);
		
		return cellStyle;
	}

}
