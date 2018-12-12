/**
 * 
 */

var jcm = {};

jcm.doAjaxFileForm = function(formId, _callbackFunc) {
	// 파일 저장 폼 정보
	var formData = new FormData($("#" + formId)[0]);
	
	// 로딩바 노출
	jcm.showLoadingBar();
	
	$.ajax({
		type : 'post',
		url : '/mini/file/common/fileUpload',
		data : formData,
		processData : false,
		contentType : false,
		success : function(res) {
			// 로딩바 종료
			jcm.hideLoadingBar();
			if(jut.isObj(_callbackFunc)) {
				_callbackFunc(res);
			}			
			alert("파일 업로드하였습니다.");
		},
		error : function(error) {
			// 로딩바 종료
			jcm.hideLoadingBar();
			
			alert("파일 업로드에 실패하였습니다.");
			
			console.log(error);
			console.log(error.status);
		}
	});
};

/**
 * 로딩바를 보여준다. 
 */
jcm.showLoadingBar = function() {
	console.log("##### showLoadingBar #####");
};

/**
 * 로딩바를 종료한다. 
 */	
jcm.hideLoadingBar = function() {
	console.log("##### hideLoadingBar #####");
};


/**
 * 입력받은 날짜가 유효한 날짜인지 체크
 * @method	isDateCheck
 * @param	{String}[_cal] 날짜
 * @return	{boolean}동일한 날짜면 true 아니면 false
 */
jcm.isDateCheck = function(_cal) {
	if(!jcm.isObj(_cal) || _cal.length < 8) {
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

jcm.doAjax = function(_isAsync, _url, _param, _callbackFunc, _option) {
	var isLoadingBar = true; 	// 로딩바는 디스플레이가 기본 옵션
	var reqMethod    = "POST"; 	// Request에 사용될 HTTP method
	
	// 전송 유형 체크
	var isAsync = (_isAsync == true)?true:false;
	

	// 전송 파라미터 데이터 체크
	var param = (!jut.isObj(_param))?{}:_param;

	// 입력 데이터 체크
	var inputParam = {};
	$.each(param , function(key, val) {
		inputParam[key] = val;
	});
	
	// 전송 옵션이 존재하는 경우 세팅한다. 
	if(jut.isObj(_option)) {
		// 로딩바 노출에 대한 옵션을 설정한 경우 입력한 내용으로 변경한다. 
		if(jut.isObj(_option.isLoadingBar)) {
			isLoadingBar = (_option.isLoadingBar == true)?true:false;
		}
		
		// 로딩바 노출에 대한 옵션을 설정한 경우 입력한 내용으로 변경한다. 
		if(jut.isObj(_option.reqMethod)) {
			reqMethod = _option.reqMethod;
		}
	}
	
	// 로딩바 노출
	jcm.showLoadingBar(isLoadingBar);

	$.ajax({
		url      : _url,
		async    : isAsync,
		method   : reqMethod,
		dataType : "json",
		data     : inputParam,
		success  : function(data) {
			console.log("#### doAjax result sussess ####");
			console.log(data);
			// 로딩바 종료
			jcm.hideLoadingBar(isLoadingBar);
			if(jut.isObj(_callbackFunc)) {
				_callbackFunc(data);
			}
		},
		error    : function() {
			// 로딩바 종료
			jcm.hideLoadingBar(isLoadingBar);
			
			//showError("JS-AJAX", "error in javascript(doAjax).\nCheck server logs."); 
		}
	});
};
	


/**
 * 쿠키를 생성한다. 
 * @param name  		생성할 쿠키이름
 * @param value 		생성할 쿠키값
 * @param expiresDate	쿠키 저장 종료 날짜
 */
jcm.setCookieDate = function(name, value, expiresDate) {
	var date = new Date();
	if(jcm.isObj(expiresDate) && jcm.isDateCheck(expiresDate)) {
		date = new Date(expiresDate.substring(0, 4), parseInt(expiresDate.substring(4, 6) - 1), expiresDate.substring(6, 8));
	}
	date.setHours(23, 59, 59, 999);
	document.cookie = name + "=" + escape(value) + ";expires=" + date.toUTCString() + ";path=/";
};

/**
 * 쿠키를 생성한다. 
 * @param name  		생성할 쿠키이름
 * @param value 		생성할 쿠키값
 * @param expiresDay	쿠키 저장 기간(예:90[90일])
 */
jcm.setCookieDay = function(name, value, expiresDay) {
	if(!jcm.isObj(expiresDay)) {
		expiresDay = 0;
	}
	var date = new Date();
	date.setDate(date.getDate() + parseInt(expiresDay));
	date.setHours(23, 59, 59, 999);
	document.cookie = name + "=" + escape(value) + ";expires=" + date.toUTCString() + ";path=/";
};

/**
 * 쿠키이름으로 찾은 값을 리턴한다. 
 * @method	getCookie
 * @param	{Object|String}[_isFlag]
 * @return	{boolean} undefined 이면 true, 아니면 false
 */
jcm.getCookie = function(_name) {
	var cookieObjString = document.cookie;
	var cookieList = cookieObjString.split(';');
	for(var i = 0; i < cookieList.length; i++) {
		var cookie = cookieList[i];
		while(cookie.charAt(0) == ' ') {
			cookie = cookie.substring(1);
		}
		if(cookie.indexOf(_name) == 0) {
			return unescape(cookie.substring(_name.length+1, cookie.length));
		}
	}
	return "";
};

/**
 * Back Space의 기능을 차단한다. 
 * @method	setDisableKeyDown
 */
jcm.setDisableKeyDown = function() {	
	$("body").keydown( function(event) {
		var targetObj = event.target;
		var targetReadonly = $(targetObj).attr("readonly") == "readonly" || $(targetObj).attr("disabled") == "disabled";

		if(event.keyCode == "8" && ((targetObj.nodeName != "INPUT" && targetObj.nodeName != "TEXTAREA") || targetReadonly)) {
			return false;
		}
	});
}


jcm.openPopup = function(_url, _mode, _popupID, _param) {
	 
	var x			= "";
	var y			= "";
	var width		= "";
	var height		= "";
	var position	= "left";
	var resizable 	= "no";
	
	if(jcm.isObj(_param)) {
		x 			= !jcm.isUndefined(_param.x)?_param.x:x;
		y 			= !jcm.isUndefined(_param.y)?_param.y:y;
		width 		= !jcm.isUndefined(_param.width)?_param.width:width;
		height		= !jcm.isUndefined(_param.height)?_param.height:height;
		position 	= !jcm.isUndefined(_param.position)?_param.position:position;
		resizable 	= !jcm.isUndefined(_param.resizable)?_param.resizable:resizable;
	}
	
	
	if(jcm.isObj(_mode) && _mode == "POPUP_WINDOW") {
		if(!jcm.isObj(_popupID)){
			_popupID = _url.substring( _url.lastIndexOf("/") != -1 ? 0 : _url.lastIndexOf("/") ).replace(/[ \{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi, "");
		}

		window.open(
			_url,
			_popupID,
			"scrollbars=yes"+
			",toolbar=no" +
			",resizable=" + resizable +
			(!jcm.isObj(x) ? "":",left=" + x) +
			(!jcm.isObj(y) ? "":",top=" + y) +
			",width="  + (!jcm.isObj(width) ? 640 : width) + 
			",height=" + (!jcm.isObj(height) ? 480 : height)
		);
		
	} else if(jcm.isObj(_mode) && _mode == "POPUP_LAYER") {		
		var realX            = (!jcm.isObj(x) ? 10  : x );
		var realY            = (!jcm.isObj(y) ? 10  : y );
		var readWidth        = (!jcm.isObj(width) ? 640 : width);
		
		var layerPopupModal  = $("<div class=\"layer_popup_modal\"></div>");
		var layerPopup       = $("<div class=\"layer_popup\"></div>");
		var layerPopupFrame  = $("<iframe frameborder=\"0\" allowTransparency='true'></iframe>");
		
		layerPopupFrame.css("width",  readWidth);
		layerPopupFrame.attr("src", _url);
		
		layerPopup.append(layerPopupFrame);
		
		layerPopup.css("left", realX);
		layerPopup.css("top",    realY);
		layerPopup.css("width",  readWidth);
		
		$("body", top.document).append(layerPopupModal);
		$("body", top.document).append(layerPopup);
		
	}
}


