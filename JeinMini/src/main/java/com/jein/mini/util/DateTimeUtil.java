package com.jein.mini.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

	/** Locale */
	private static Locale locale = Locale.getDefault();

	/**
	 * Date 체크
	 * @param value String
	 */
	public static void check(String value) throws java.lang.Exception {

		DateTimeUtil.check(value, "yyyy-MM-dd");
	}

	/**
	 * Date 체크
	 * @param value String
	 * @param pattern String
	 * @param String
	 */
	public static void check(String value, String pattern) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		Date date = null;

		try {
			date = format.parse(value);
		} catch(Exception e) {
			throw new ParseException(e.getMessage() + " with pattern \"" + pattern + "\"", 0);		
		}

		if(!format.format(date).equals(value)){
			throw new ParseException("Out of bound date : \"" + value + "\" with pattern \"" + pattern + "\"", 0);
		}
	}

	/**
	 * Date 체크 2
	 * @param value String
	 * @param pattern String
	 * @return boolean
	 */
	public static boolean check2(String value, String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		Date date = null;

		try {
			date = format.parse(value);
		} catch(Exception e) {
			return false;
		}

		if(!format.format(date).equals(value)) {			
			return false;
		} 

		return true;		
	}

	/**
	 * 입력받은 두날짜의 차이를 구한다
	 * @param String
	 * @param String
	 * @param String
	 * @return int
	 */
	public static int daysBetween(String from, String to, String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		Date date1 = null;
		Date date2 = null;

		try {
			date1 = format.parse(from);
			date2 = format.parse(to);
		} catch(ParseException e) {
			return -999;
		}

		if(!format.format(date1).equals(from)){
			return -999;
		}
		if(!format.format(date2).equals(to)){
			return -999;
		}

		long duration = date2.getTime() - date1.getTime();

		return (int)(duration/(1000 * 60 * 60 * 24));
	}

	/**
	 * 현재 날짜를 구한다
	 * @return String
	 */
	public static String getDateString() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", locale);

		return format.format(new Date());
	}
	
	/**
	 * <pre>
	 * 입력받은 timeMillis로 yyyyMMddHHmmss 형태의 날짜를 구한다.
	 * </pre>
	 *
	 * @param timeMillis
	 * @return
	 * @throws ParseException
	 */
	public static String getDateStringByTimeMillis(long timeMillis) throws ParseException {
		return getFormatStringByTimeMillis(timeMillis, "yyyyMMddHHmmss");
	}
	

	/**
	 * 현재 일을 구한다
	 * @return int
	 */
	public static int getDay() {

		return getNumberByFormat("dd");
	}

	public static String getDay(String dateString) {
		
		final String [] weekDay = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

		Calendar calendar = Calendar.getInstance();
		
		int year  = Integer.parseInt(dateString.substring(0, 4));
		int month = Integer.parseInt(dateString.substring(4, 6));
		int date  = Integer.parseInt(dateString.substring(6));
		
		calendar.set(year, month-1, date);
		
		return weekDay[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}
	
	/**
	 * yyyyMMddhhmmss 형식의 현재 날짜를 구한다
	 * @param String
	 * @param String
	 * @return String
	 */
	public static String getFormatString(String dateString, String pattern) throws ParseException {

		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddhhmmss", locale);
		Date date = format1.parse(dateString);

		SimpleDateFormat format2 = new SimpleDateFormat(pattern, locale);

		return format2.format(date);
	}

	/**
	 * 입력받은 포멧으로 날짜를 구한다
	 * @param String
	 * @return String
	 */
	public static String getFormatString(String pattern) {

		SimpleDateFormat format = new SimpleDateFormat (pattern, locale);
		String date = format.format(new Date());

		return date;
	}

	/**
	 * <pre>
	 * 입력받은 timeMillis로 원하는 포맷의 날짜를 구한다.
	 * </pre>
	 *
	 * @param timeMillis
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String getFormatStringByTimeMillis(long timeMillis, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		String date = format.format(new Date(timeMillis));
		
		return date;
	}
	
	/**
	 * 현재 월(MM)을 구한다
	 * @return int
	 */
	public static int getMonth() {

		return getNumberByFormat("MM");
	}

	/**
	 * 입력받은 형식의 날짜를 구한다(현재의 년월일(yyyyMMdd) , 월(MM) , 일(dd).....)
	 * @param String
	 * @return int
	 */
	public static int getNumberByFormat(String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		String date = format.format(new Date());

		return Integer.parseInt(date);
	}

	/**
	 * yyyyMMdd 형식의 현재 날짜를 구한다
	 * @return String
	 */
	public static String getShortDateString() {

		SimpleDateFormat format = new SimpleDateFormat ("yyyyMMdd", locale);

		return format.format(new Date());
	}

	/**
	 * 현재 시간(시,분,초)을 구한다
	 * @return String
	 */
	public static String getShortTimeString() {

		SimpleDateFormat format = new SimpleDateFormat("HHmmss", locale);

		return format.format(new Date());
	}
	
	/**
	 * HHmmss 형식의 세계 시간을 구한다
	 * @param TimeZone
	 * @return String
	 */
	public static String getShortTimeString(TimeZone timeZone) {
		
		SimpleDateFormat format = new SimpleDateFormat("HHmmss", locale);
		
		format.setTimeZone(timeZone);
		
		return format.format(new Date());
	}

	/**
	 * yyyy-MM-dd-HH:mm:ss:SSS 형식의 현재 날짜를 구한다
	 * @return String
	 */
	public static String getTimeStampString() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", locale);

		return format.format(new Date());
	}

	/**
	 * HH:mm:ss 형식의 현재 시간를 구한다
	 * @return String
	 */
	public static String getTimeString() {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", locale);

		return format.format(new Date());
	}

	/**
	 * HH:mm:ss 형식의 세계 시간을 구한다
	 * @return String
	 */
	public static String getTimeString(TimeZone timeZone) {
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", locale);
		
		format.setTimeZone(timeZone);
		
		return format.format(new Date());
	}

	/**
	 *현재 요일을 구한다
	 * @return String
	 */
	public static String getWeekDay() {

		return Calendar.getInstance().getTime().toString().substring(0, 3);
	}

	/**
	 * 현재 년도를 구한다
	 * @return String
	 */
	public static int getYear() {

		return getNumberByFormat("yyyy");
	}

	/**
	 * 입력받은 두날짜의 차이를 구한다
	 * @param String
	 * @param String
	 * @return int
	 */
	public static int daysBetween(String from, String to) {

		return daysBetween(from, to, "yyyyMMdd");
	}	

	/**
	 * 입력받은 일을 현재날째에 더한다
	 * @param int
	 * @return Date
	 */
	public static Date getAddDate(int addDate) {

		Calendar calendar = Calendar.getInstance(locale);

		calendar.add(Calendar.DATE, addDate);

		return calendar.getTime();
	}	

	/**
	 * 입력받은 날짜에 입력받은 일은 더한다 
	 * @param int
	 * @param Date
	 * @return Date
	 */
	public static Date getAddDate(int addDate, Date date) {

		Calendar calendar = Calendar.getInstance(locale);		

		calendar.setTime(date);
		calendar.add(Calendar.DATE, addDate);

		return calendar.getTime();
	}	

	/**
	 * 현재 날짜를 yyyyMMdd 포멧의 String 형식으로 보여준다 
	 * @param int
	 * @return String
	 */
	public static String getAddDateString(int addDate) {
		
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DATE, addDate);

		int yearTemp = calendar.get(Calendar.YEAR);
		int monthTemp = calendar.get(Calendar.MONTH) + 1;
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String day = dayTemp < 10 ? "0" + Integer.toString(dayTemp) : Integer.toString(dayTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + day;
	}	

	/**
	 * 입력받은 String 형식의 날짜에 입력받은 일을 더해 yyyyMMdd 포멧의 String 형식으로 보여준다 
	 * @param int
	 * @param String
	 * @return String
	 */
	public static String getAddDateString(int addDate, String dateString) {

		Calendar calendar = Calendar.getInstance();

		int yearTemp = Integer.parseInt(dateString.substring(0, 4));
		int monthTemp = Integer.parseInt(dateString.substring(4, 6));
		int dateTemp = Integer.parseInt(dateString.substring(6));

		calendar.set(yearTemp, monthTemp-1, dateTemp);
		calendar.add(Calendar.DATE, addDate);

		yearTemp = calendar.get(Calendar.YEAR);
		monthTemp = calendar.get(Calendar.MONTH) + 1;
		dateTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String date = dateTemp < 10 ? "0" + Integer.toString(dateTemp) : Integer.toString(dateTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + date;
	}	

	/**
	 * 입력받은 Date 형식의 날짜에 입력받은 일을 더해 yyyyMMdd 포멧의 String 형식으로 보여준다 
	 * @param int
	 * @param Date
	 * @return String
	 */
	public static String getAddDateString(int addDate, Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);		
		calendar.add(Calendar.DATE, addDate);

		int yearTemp = calendar.get(Calendar.YEAR);
		int monthTemp = calendar.get(Calendar.MONTH) + 1;
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String day = dayTemp < 10 ? "0" + Integer.toString(dayTemp) : Integer.toString(dayTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + day;
	}	

	/**
	 * 입력받은 개월수를 현재의 달에 더한다
	 * @param int
	 * @return Date
	 */
	public static Date getAddMonth(int addMonth) {

		Calendar calendar = Calendar.getInstance(locale);

		calendar.add(Calendar.MONTH, addMonth);

		return calendar.getTime();
	}	

	/**
	 * 입력한 날짜에 입력받은 개월수를 더한다 
	 * @param int
	 * @param Date
	 * @return Date
	 */
	public static Date getAddMonth(int addMonth, Date date) {

		Calendar calendar = Calendar.getInstance(locale);

		calendar.setTime(date);	
		calendar.add(Calendar.MONTH, addMonth);

		return calendar.getTime();
	}	

	/**
	 * 입력받은 개월수를 현재의 달에 더해 yyyyMMdd형식의 포멧으로 보여준다
	 * @param int
	 * @return String
	 */
	public static String getAddMonthString(int addMonth) {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MONTH, addMonth);

		int yearTemp = calendar.get(Calendar.YEAR);
		int monthTemp = calendar.get(Calendar.MONTH) + 1;
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String day = dayTemp < 10 ? "0" + Integer.toString(dayTemp) : Integer.toString(dayTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + day;
	}	

	/**
	 * 입력받은 개월수를 입력받은 날짜에 더해 yyyyMMdd형식의 포멧으로 보여준다
	 * @param int
	 * @param Date
	 * @return String
	 */
	public static String getAddMonthString(int addMonth, Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);		
		calendar.add(Calendar.MONTH, addMonth);

		int yearTemp = calendar.get(Calendar.YEAR);
		int monthTemp = calendar.get(Calendar.MONTH) + 1;
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String day = dayTemp < 10 ? "0" + Integer.toString(dayTemp) : Integer.toString(dayTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + day;
	}	

	/**
	 * 입력받은 년수를 현재의 년도에 더한다
	 * @param int
	 * @return Date
	 */
	public static Date getAddYear(int addYear) {

		Calendar calendar = Calendar.getInstance(locale);

		calendar.add(Calendar.YEAR, addYear);

		return calendar.getTime();
	}	

	/**
	 * 입력받은 년수를 입력받은 년도에 더한다
	 * @param int
	 * @param Date
	 * @return Date
	 */
	public static Date getAddYear(int addYear, Date date) {

		Calendar calendar = Calendar.getInstance(locale);

		calendar.setTime(date);	
		calendar.add(Calendar.YEAR, addYear);

		return calendar.getTime();
	}	

	/**
	 * 입력받은 년수를 현재의 년도에 더하여 yyyyMMdd 형식의 포멧으로 보여준다
	 * @param int
	 * @return String
	 */
	public static String getAddYearString(int addYear) {

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.YEAR, addYear);

		int yearTemp = calendar.get(Calendar.YEAR);
		int monthTemp = calendar.get(Calendar.MONTH) + 1;
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String day = dayTemp < 10 ? "0" + Integer.toString(dayTemp) : Integer.toString(dayTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + day;
	}	

	/**
	 * 입력받은 년수를 입력받은 년도에 더하여 yyyyMMdd 형식의 포멧으로 보여준다
	 * @param int
	 * @param Date
	 * @return String
	 */
	public static String getAddYearString(int addYear, Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);		
		calendar.add(Calendar.YEAR, addYear);

		int yearTemp = calendar.get(Calendar.YEAR);
		int monthTemp = calendar.get(Calendar.MONTH) + 1;
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);

		String year = Integer.toString(yearTemp);
		String month = monthTemp < 10 ? "0" + Integer.toString(monthTemp) : Integer.toString(monthTemp);
		String day = dayTemp < 10 ? "0" + Integer.toString(dayTemp) : Integer.toString(dayTemp);		

		//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", locale);	

		return year + month + day;
	}	

	/**
	 * 입력받은 년도와 입력받은 월의 마지막 일을 구한다
	 * @param int
	 * @param int
	 * @return int
	 */
	public static int getDaysOfMonth(int year, int month) {

		int count = 0;

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);

		count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return count;
	}	

	/**
	 * 입력받은 Date를 yyyyMMdd형식의 포멧으로 보여준다
	 * @param Date
	 * @return String
	 */
	public static String getShortDateString(Date date) {

		SimpleDateFormat format = new SimpleDateFormat ("yyyyMMdd", locale);

		return format.format(date);
	}	

	/**
	 * 현재 몇재주인지 보여준다
	 * @return String
	 */
	public static String getWeekOfMonth() {

		Calendar calendar = Calendar.getInstance();

		return String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH));
	}

	/**
	 * 현재 날짜를 yyyy/MM/dd HH:mm:ss 형식의 포멧으로 보여준다
	 * @return String
	 */
	public static String getCurrentDateTime() {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", locale);
		Date currentTime = new Date();
		String dTime = fmt.format(currentTime); 

		return dTime;
	}

	/**
	 * 현재 날짜를 yyyyMMddHHmmss 형식의 포멧으로 보여준다
	 * @return
	 */
	public static String getCurrentDateTimeSimple() {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss", locale);
		Date currentTime = new Date();
		String dTime = fmt.format(currentTime); 

		return dTime;
	}

	/**
	 * 현재 날짜를 yyyyMMddHHmmssS 형식의 포멧으로 보여준다
	 * @return
	 */
	public static String getCurrentDateTimeSSS() {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS", locale);
		Date currentTime = new Date();
		String dTime = fmt.format(currentTime); 

		return dTime;
	}
	
	/**
	 * <pre>
	 * 입력받은 날짜를 더한 시간을 yyyyMMddHHmmss 형식의 포멧으로 리턴한다.
	 * 2017-01-26 KHCWINGS 추가 
	 * </pre>
	 *
	 * @param addDate 더하거나 뺄 날짜
	 * @return
	 */
	public static String getAddDateTime(int addDate){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, addDate);
		
		return "" + calendar.get(Calendar.YEAR) + 
				((calendar.get(Calendar.MONTH) + 1)<10?"0" + (calendar.get(Calendar.MONTH) + 1):(calendar.get(Calendar.MONTH) + 1)) +
				(calendar.get(Calendar.DAY_OF_MONTH)<10?"0" + calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH)) + 
				(calendar.get(Calendar.HOUR_OF_DAY)<10?"0" + calendar.get(Calendar.HOUR_OF_DAY):calendar.get(Calendar.HOUR_OF_DAY)) +  
				(calendar.get(Calendar.MINUTE)<10?"0" + calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE)) +  
				(calendar.get(Calendar.SECOND)<10?"0" + calendar.get(Calendar.SECOND):calendar.get(Calendar.SECOND));
	}
	
	/**
	 * <pre>
	 * 입력받은 날짜를 더한 시간을 yyyyMMddHHmmssSSS 형식의 포멧으로 리턴한다.
	 * 2017-01-26 KHCWINGS 추가 
	 * </pre>
	 *
	 * @param addDate 더하거나 뺄 날짜
	 * @return
	 */
	public static String getAddDateTimeS(int addDate){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, addDate);
		
		return "" + calendar.get(Calendar.YEAR) + 
				((calendar.get(Calendar.MONTH) + 1)<10?"0" + (calendar.get(Calendar.MONTH) + 1):(calendar.get(Calendar.MONTH) + 1)) +
				(calendar.get(Calendar.DAY_OF_MONTH)<10?"0" + calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH)) + 
				(calendar.get(Calendar.HOUR_OF_DAY)<10?"0" + calendar.get(Calendar.HOUR_OF_DAY):calendar.get(Calendar.HOUR_OF_DAY)) +  
				(calendar.get(Calendar.MINUTE)<10?"0" + calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE)) +  
				(calendar.get(Calendar.SECOND)<10?"0" + calendar.get(Calendar.SECOND):calendar.get(Calendar.SECOND)) +  
				(calendar.get(Calendar.MILLISECOND)<10?"00" + calendar.get(Calendar.MILLISECOND):(calendar.get(Calendar.MILLISECOND)<100?"0" + calendar.get(Calendar.MILLISECOND):calendar.get(Calendar.MILLISECOND)));
	}
	
	/**
	 * <pre>
	 * 두가지의 시간을 입력받아 지정된 시간(분)보다 큰지 작은지를 판단한다.
	 * overTime 보다 크지 않느면 무조건 false값을 Return한다.  
	 * </pre>
	 *
	 * @param from
	 * @param to
	 * @param pattern
	 * @param overTime	분 - 비교값으로 넘겨받은 값보다 작으면 false, 크면 true
	 * @return false : overTime 값보다 작거나 오류, true : overTime값보다 큼
	 */
	public static boolean isOverTime(String from, String to, String pattern, String overTime) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		
		int iOverTime = 60; // 한시간(60분)
		Date date1 = null;
		Date date2 = null;

		try {
			iOverTime = Integer.parseInt(overTime);
			date1 	  = format.parse(from);
			date2 	  = format.parse(to);
		} catch(ParseException e) {
			return false;
		}

		// 변환된 시간값이 입력받은 값과 같은지 검증
		if(!format.format(date1).equals(from) || !format.format(date2).equals(to)){
			return false;
		}

		int compareMinute = (int)((date2.getTime() - date1.getTime())/(1000 * 60));
		if(compareMinute < iOverTime) {
			return false;
		}
		
		return true;
	}
}
