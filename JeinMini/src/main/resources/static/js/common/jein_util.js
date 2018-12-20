var jut = {};

/**
 * Undefined 판단
 * @method	isUndefined
 * @param	{Object|String}[_obj]
 * @return	{boolean} undefined 이면 true, 아니면 false
 */
jut.isUndefined	= function(_obj){
	if(_obj === undefined || _obj === "undefined"){
		return true;
	}else{
		return false;
	}
};

/**
 * null 판단
 * @method	isNull
 * @param	{String|Object}[_obj]null인지 판단할 객체
 * @return	{boolean}_obj가 null 이면 true, 아니면 false
 */
jut.isNull = function(_obj){
	if(_obj === null){
		return true;
	}else{
		return false;
	}
};

/**
 * Object가 undefined 또는 null 인지 판단
 * @method	isObj
 * @param	{Object}[_obj] 판별할 객체
 * @return	{boolean}_obj가 null이 아니거나 undefined 이면 false, 아니면 true
 */
jut.isObj = function(_obj){
	if( !jut.isUndefined(_obj) && !jut.isNull(_obj) ){
		return true;
	}else{
		return false;
	}
};

/**
 * Object가 빈 값인지 판단
 */
jut.isEmpty = function(_obj){
	if( jut.isUndefined(_obj) || jut.isNull(_obj) || _obj == "" ){
		return true;
	}else{
		return false;
	}
};

// 특정 html 문자열을 변환한다. 
jut.htmlEscape = function(_obj) {
	return _obj.replace(/&/g, '&amp;')
		.replace(/</g, '&lt;')
		.replace(/>/g, '&gt;')
		.replace(/'/g, '&#039;')
		.replace(/"/g, '&quot;');
};

// 변환된 특정 html 문자열을 되돌린다. 
jut.htmlUnEscape = function(_obj) {
	return _obj.replace(/&amp;/g, '&')
		.replace(/&lt;/g, '<')
		.replace(/&gt;/g, '>')
		.replace(/&#039;/g, '\'')
		.replace(/&quot;/g, '\"');
};


/**
 * 입력받은 날짜가 유효한 날짜인지 체크
 * @method	isDateCheck
 * @param	{String}[_cal] 날짜
 * @return	{boolean}동일한 날짜면 true 아니면 false
 */
jut.isDateCheck = function(_cal) {
	if(jut.isEmpty(_cal) || _cal.length < 8) {
		return false;
	}
	var year  = parseInt(_cal.substring(0, 4));
	var month = parseInt(_cal.substring(4, 6)) -1;
	var day   = parseInt(_cal.substring(6, 8));	
	
	var cDate = new Date(year, month, day);
	if(cDate.getFullYear() != year || cDate.getMonth() != month || cDate.getDate() != day) {
		return false;
	}
	return true;
};

/**
 * 이메일 유효성 체크(한글이메일 포함)
 * @method	isCheckEmail
 * @param	{Object}[_email] 이메일
 * @return	{boolean} 정규식과 일치하면 true, 아니라면 false
 */
jut.isCheckEmail = function(_email) {    
    var regexEmail =/^([가-힣\w-]+(?:\.[가-힣\w-]+)*)@((?:[가-힣\w-]+\.)*\w[가-힣\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    if(regexEmail.test(_email)) {
        return true;
    } else{
    	return false;
    }
}



/**
 * yyyyMMddHHmmssSSS 형식의 날짜를 리턴한다. 
 * _dateString 을 입력한 경우 입력된 날짜가 유효하고 8, 14, 17 인 경우 해당 날짜로 세팅한다. 
 * _dateFormat 을 입력한 경우 해당 포멧으로 날짜를 리턴한다. 
 * @method	getDate
 * @param	{Object}[_dateString] 지정 날짜
 * @param	{Object}[_dateFormat] 지정 포멧
 * @return	{String} 지정된 포멧 또는 기본 포멧
 */
jut.getDate = function(_dateString, _dateFormat) {
	var date = new Date();
	if(jut.isObj(_dateString)) {
		if(_dateString.length == 17) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8), 
					_dateString.substring(8, 10), _dateString.substring(10, 12), _dateString.substring(12, 14), _dateString.substring(14, 17));
		} else if(_dateString.length == 14) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8), 
					_dateString.substring(8, 10), _dateString.substring(10, 12), _dateString.substring(12, 14));
		} else if(_dateString.length == 8) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8));
		}
	}
	
	var year    	 = date.getFullYear();
	var month   	 = parseInt(date.getMonth()) + 1;
	month 			 = month.toString().length == 1 ?  "0" + month.toString() : month;
	var day     	 = date.getDate().toString().length == 1 ?  "0" + date.getDate().toString() : date.getDate();
	var hours   	 = date.getHours().toString().length == 1 ?  "0" + date.getHours().toString() : date.getHours();
	var minutes 	 = date.getMinutes().toString().length == 1 ?  "0" + date.getMinutes().toString() : date.getMinutes();
	var seconds 	 = date.getSeconds().toString().length == 1 ?  "0" + date.getSeconds().toString() : date.getSeconds();
	var milliseconds = date.getMilliseconds().toString().length == 1 ?  "00" + date.getMilliseconds().toString() : (date.getMilliseconds().toString().length == 2 ? "0" + date.getMilliseconds().toString() : date.getMilliseconds());
	
	var retValue = "" + year + month + day + hours + minutes + seconds + milliseconds;
	if(!jut.isObj(_dateFormat)) {
		return retValue;
	} else {
		return _dateFormat.replace(/(yyyy|yy|MM|dd|HH|mm|SSS|ss)/gi, function($1) {
			switch($1) {
				case 'yyyy': return (retValue.length >= 4 ? retValue.substring(0,4) : "");
				case 'yy':   return (retValue.length >= 4 ? retValue.substring(2,4) : "");
				case 'MM':   return (retValue.length >= 6 ? retValue.substring(4,6) : "");
				case 'dd':   return (retValue.length >= 8 ? retValue.substring(6,8) : ""); 
				case 'HH':   return (retValue.length >= 10 ? retValue.substring(8,10) : ""); 
				case 'mm':   return (retValue.length >= 12 ? retValue.substring(10,12) : ""); 
				case 'ss':   return (retValue.length >= 14 ? retValue.substring(12,14) : ""); 
				case 'SSS':   {
					return (retValue.length >= 17 ? retValue.substring(14,17) : ""); 
				}
			}
		});
	}
}

/**
 * _addDate 만큼의 날짜를 더한 yyyyMMddHHmmss 형식의 날짜를 리턴한다. 
 * _addDate 을 입력한 경우 오늘 날짜에 해당 날짜만큼을 추가한다.  
 * _dateString 을 입력한 경우 입력된 날짜가 유효하고 8, 14, 17 인 경우 해당 날짜로 세팅한다. 
 * @method	getDate
 * @param	{Object}[_addDate] 추가할 날짜
 * @param	{Object}[_dateString] 지정 날짜
 * @return	{String} yyyyMMddHHmmss 형식의 날짜
 */
jut.getAddDate = function(_addDate, _dateString) {
	
	var date = new Date();
	if(jut.isObj(_dateString)) {
		if(_dateString.length == 14) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8), 
							_dateString.substring(8, 10), _dateString.substring(10, 12), _dateString.substring(12, 14));
		} else if(dateString.length == 8) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8));
		}
	}

	if(!jut.isObj(_addDate)) {
		_addDate = "0";
	}
	date.setDate(date.getDate() + parseInt(_addDate));
	
	var year    	 = date.getFullYear();
	var month   	 = parseInt(date.getMonth()) + 1;
	month 			 = month.toString().length == 1 ?  "0" + month.toString() : month;
	var day     	 = date.getDate().toString().length == 1 ?  "0" + date.getDate().toString() : date.getDate();
	var hours   	 = date.getHours().toString().length == 1 ?  "0" + date.getHours().toString() : date.getHours();
	var minutes 	 = date.getMinutes().toString().length == 1 ?  "0" + date.getMinutes().toString() : date.getMinutes();
	var seconds 	 = date.getSeconds().toString().length == 1 ?  "0" + date.getSeconds().toString() : date.getSeconds();
	
	return "" + year + month + day + hours + minutes + seconds;
}

/**
 * _addMonth 만큼의 월를 더한 yyyyMMddHHmmss 형식의 날짜를 리턴한다. 
 * _addMonth 을 입력한 경우 오늘 날짜에 해당 월만큼을 추가한다.  
 * _dateString 을 입력한 경우 입력된 날짜가 유효하고 8, 14, 17 인 경우 해당 날짜로 세팅한다. 
 * @method	getDate
 * @param	{Object}[_addMonth] 추가할 달
 * @param	{Object}[_dateString] 지정 날짜
 * @return	{String} yyyyMMddHHmmss 형식의 날짜
 */
jut.getAddDateByMonth = function(_addMonth, _dateString) {
	
	var date = new Date();
	if(jut.isObj(_dateString)) {
		if(_dateString.length == 14) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8), 
							_dateString.substring(8, 10), _dateString.substring(10, 12), _dateString.substring(12, 14));
		} else if(dateString.length == 8) {
			date = new Date(_dateString.substring(0, 4), parseInt(_dateString.substring(4, 6) - 1), _dateString.substring(6, 8));
		}
	}

	if(!jut.isObj(_addMonth)) {
		_addMonth = "0";
	}
	date.setMonth(date.getMonth() + parseInt(_addMonth));
	
	var year    	 = date.getFullYear();
	var month   	 = parseInt(date.getMonth()) + 1;
	month 			 = month.toString().length == 1 ?  "0" + month.toString() : month;
	var day     	 = date.getDate().toString().length == 1 ?  "0" + date.getDate().toString() : date.getDate();
	var hours   	 = date.getHours().toString().length == 1 ?  "0" + date.getHours().toString() : date.getHours();
	var minutes 	 = date.getMinutes().toString().length == 1 ?  "0" + date.getMinutes().toString() : date.getMinutes();
	var seconds 	 = date.getSeconds().toString().length == 1 ?  "0" + date.getSeconds().toString() : date.getSeconds();
	
	return "" + year + month + day + hours + minutes + seconds;
};

/**
 * _val1과 _val2의 날짜 사이의 일수를 구한다.  
 * _addMonth 을 입력한 경우 오늘 날짜에 해당 월만큼을 추가한다.  
 * _dateString 을 입력한 경우 입력된 날짜가 유효하고 8, 14, 17 인 경우 해당 날짜로 세팅한다. 
 * @method	getDate
 * @param	{Object}[_val1] 날짜1
 * @param	{Object}[_val2] 날짜2
 * @return	{String} 차이나는 날짜 
 */
jut.daysBetween = function(_val1 , _val2) {
	if(!jut.isObj(_val1) || !jut.isObj(_val2) || _val1.length < 8 || _val2.length < 8) {
		return "";
	}
	var from_dt = new Date(_val1.substring(0, 4), parseInt(_val1.substring(4, 6) - 1), _val1.substring(6, 8));
	var to_dt   = new Date(_val2.substring(0, 4), parseInt(_val2.substring(4, 6) - 1), _val2.substring(6, 8));

	return ((to_dt.getTime() - from_dt.getTime()) / 1000 / 60 / 60 / 24);
};
