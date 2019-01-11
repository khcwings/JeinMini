var jcm = {
	contentPath     : '/mini'						// Content Root Path
	, fileUploadUrl : '/common/file/fileUpload'     // File Upload Url
	, excelReadUrl  : '/common/file/readExcelFile'  // File Upload Url
	, winPopupdUrl  : '/common/popup/windowPopup01' // Window Open Url
	, ajaxCallCount : 0
	, constant : {
		result_success : "S"		// 성공
		, result_error : "E"		// 실패
		, popup_width  : 400 		// 팝업 기본 넓이
		, popup_height : 300 		// 팝업 기본 높이
		, popup_effect : "blind"	// 팝업 이벤트 효과
		, popup_effect_time : 500	// 팝업 이벤트 효과 지속 시간
	}
};

// AJAX를 이용하여 파일 업로드
jcm.doAjaxFileForm = function(formId, _callbackFunc) {
	// 파일 저장 폼 정보
	var formData = new FormData($("#" + formId)[0]);
	// 로딩바 노출
	if(jcm.ajaxCallCount == 0) {
		jcm.showLoadingBar();
	}
	jcm.ajaxCallCount++;
	
	$.ajax({
		type : 'post',
		url  : jcm.contentPath + jcm.fileUploadUrl,
		data : formData,
		processData : false,
		contentType : false,
		success : function(res) {								// HTTP 성공
			// 로딩바 종료
			jcm.ajaxCallCount--;
			if(jcm.ajaxCallCount == 0) {
				jcm.hideLoadingBar();
			}
			
			if(jcm.constant.result_success == res.resultCode) {		// 응답코드가 성공인 경우
				if(jut.isObj(_callbackFunc)) {
					_callbackFunc(res);
				}
			} else {												// 응답코드가 실패인 경우
				alert(res.resultMsg);
			}
		},
		error : function(error) {								// HTTP 실패
			// 로딩바 종료
			jcm.ajaxCallCount--;
			if(jcm.ajaxCallCount == 0) {
				jcm.hideLoadingBar();
			}			
			alert("파일 업로드에 실패하였습니다.");
		}
	});
};

//AJAX를 이용하여 엑셀 파일 읽기
jcm.doAjaxExcelForm = function(formId, _url, _callbackFunc) {
	// 파일 저장 폼 정보
	var formData = new FormData($("#" + formId)[0]);
	// 로딩바 노출
	if(jcm.ajaxCallCount == 0) {
		jcm.showLoadingBar();
	}
	jcm.ajaxCallCount++;
	
	var url = jcm.contentPath + jcm.excelReadUrl;
	if(!jut.isEmpty(_url)) {
		url = jcm.contentPath + _url;
	}
	$.ajax({
		type : 'post',
		url  : url,
		data : formData,
		processData : false,
		contentType : false,
		success : function(res) {								// HTTP 성공
			// 로딩바 종료
			jcm.ajaxCallCount--;
			if(jcm.ajaxCallCount == 0) {
				jcm.hideLoadingBar();
			}
			
			if(jcm.constant.result_success == res.resultCode) {		// 응답코드가 성공인 경우
				if(jut.isObj(_callbackFunc)) {
					_callbackFunc(res);
				}
			} else {												// 응답코드가 실패인 경우
				alert(res.resultMsg);
			}
		},
		error : function(error) {								// HTTP 실패
			// 로딩바 종료
			jcm.ajaxCallCount--;
			if(jcm.ajaxCallCount == 0) {
				jcm.hideLoadingBar();
			}			
			alert("파일 업로드에 실패하였습니다.");
		}
	});
};

// AJAX를 이용하여 서비스를 요청한다. 
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
	if(jcm.ajaxCallCount == 0) {
		jcm.showLoadingBar();
	}
	jcm.ajaxCallCount++;
	$.ajax({
		url      : _url,
		async    : isAsync,
		method   : reqMethod,
		data     : JSON.stringify(inputParam),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
	    },
		success  : function(data) {
			console.log("#### doAjax result sussess ####");
			console.log(data);
			// 로딩바 종료
			jcm.ajaxCallCount--;
			if(jcm.ajaxCallCount == 0) {
				jcm.hideLoadingBar();
			}
			
			// 리턴값에 리다이렉트 URL이 존재한다면
			if(!jut.isEmpty(data.redirectUrl)) {
				window.location =  data.redirectUrl;
			} else if(data.resultCode == jcm.constant.result_success) { 
				if(jut.isObj(_callbackFunc)) {
					_callbackFunc(data, inputParam);
				} else {
					alert(data.resultMsg);
				}
			} else {
				alert(data.resultMsg);
			}
		},
		error    : function() {
			// 로딩바 종료
			jcm.ajaxCallCount--;
			if(jcm.ajaxCallCount == 0) {
				jcm.hideLoadingBar();
			}
			
			//showError("JS-AJAX", "error in javascript(doAjax).\nCheck server logs."); 
		}
	});
};

// 로딩바를 보여준다.
jcm.showLoadingBar = function() {
	console.log("##### showLoadingBar #####");		
	var layerPopupHtml = "<div id=\"loadingBar\" class=\"dim-layer\" style=\"display:block;\">";
	layerPopupHtml    += "	  <div class=\"dimBg\"></div>";
	layerPopupHtml    += "	  <img alt=\"loading\" class=\"loadingImg\" src=\"/mini/images/lodingBar.gif\">";
	layerPopupHtml    += "</div>";			
	$("body", top.document).append(layerPopupHtml);
};

// 로딩바를 종료한다.
jcm.hideLoadingBar = function(idx) {
	console.log("##### hideLoadingBar #####");	
	$("#loadingBar").remove();
};

// 레이어 팝업 오픈
jcm.openLayerPopup = function(popupId, _url, _params, _popupWidth, _popupHeight, _popupTop, _popupLeft) {
	if(jut.isEmpty(popupId)) {
		alert("팝업 ID가 존재하지 않습니다.");
		return "";
	}
	
	if(jut.isEmpty(_url)) {
		alert("팝업 URL이 존재하지 않습니다.");
		return "";
	}
	var url = jcm.contentPath + _url;
	
	// 페이지를 호출시 전달될 파라미터 정보
	var params = {};
	if((typeof _params) === "object") {
		params = _params;
	}
	
	// 팝업 넓이값
	var popupWidth = jcm.constant.popup_width;
	if(!jut.isEmpty(_popupWidth) && Number(_popupWidth) > 0) {
		popupWidth = Number(_popupWidth);
	}
	
	// 팝업 높이값
	var popupHeight = jcm.constant.popup_height;
	if(!jut.isEmpty(_popupHeight) && Number(_popupHeight) > 0) {
		popupHeight = Number(_popupHeight);
	}
	
	// 팝업 위치 Top
	var popupTop = 0;
	if(!jut.isEmpty(_popupTop) && Number(_popupTop) > 0) {
		popupTop = Number(_popupTop);
	}
	
	// 팝업 위치 Left
	var popupLeft = 0;
	if(!jut.isEmpty(_popupLeft) && Number(_popupLeft) > 0) {
		popupLeft = Number(_popupLeft);
	}
	
	// 기존에 존재하는 동일한 ID의 레이어 팝업을 삭제한다. 
	$("#" + popupId).remove();
	
	// 레이어 영역을 미리 생성한다. 
	var layerPopupHtml = "<div class=\"dim-layer\" id=" + popupId + ">";
	layerPopupHtml    += "	  <div class=\"dimBg\"></div>";
	layerPopupHtml    += "    <div class=\"pop-layer\" style=\"width:" + popupWidth + "px;height:" + popupHeight + "px;\"></div>";
	layerPopupHtml    += "</div>";			
	$("body", top.document).append(layerPopupHtml);
	
	// 팝업 화면을 호출한다. 
	$("#" + popupId + " .pop-layer").load(url, params, function(response, status, xhr) {
		// 성공적으로 팝업 페이지가 Load됨
		if(status == "success") {
			// 닫기 버튼 추가
			$("#" + popupId + " .pop-container").append("<div class=\"btn-r\"><a href=\"#\" class=\"btn-layerClose\">Close</a></div>");
			
			if(popupTop > 0 || popupLeft > 0) {
				$("#" + popupId + " .pop-layer").css({top: popupTop, left: popupLeft});
			} else {
				// 팝업의 위치를 설정하기 위한 작업
				var popupWidth  = $("#" + popupId + " .pop-layer").outerWidth();
				var popupHeight = $("#" + popupId + " .pop-layer").outerHeight();
	            var docWidth    = $(document).width();
	            var docHeight   = $(document).height();

		        // 화면의 중앙에 레이어를 띄운다.
		        if (popupHeight < docHeight || popupWidth < docWidth) {
		        	$("#" + popupId + " .pop-layer").css({
		                marginTop: -popupHeight /2,
		                marginLeft: -popupWidth/2
		            })
		        } else {
		        	$("#" + popupId + " .pop-layer").css({top: 0, left: 0});
		        }
			}
			
			// 레이어 팝업을 노출한다. 
			$("#" + popupId).show(jcm.constant.popup_effect, {}, jcm.constant.popup_effect_time, function() {});
			
			// 레이어 팝업 닫기 이벤트
			$("#" + popupId + " .pop-layer .btn-layerClose").click(function(){
				$("#" + popupId).hide(jcm.constant.popup_effect, {}, jcm.constant.popup_effect_time, function() {
					$("#" + popupId).remove();
				});
	            return false;
	        });
		}				
	});
};

// 윈도우 팝업 오픈
jcm.openWindowPopup = function(popupID, _url, _params, _popupWidth, _popupHeight, _popupTop, _popupLeft, _resizable) {	
	if(jut.isEmpty(popupId)) {
		alert("팝업 ID가 존재하지 않습니다.");
		return "";
	}
	
	if(jut.isEmpty(_url)) {
		alert("팝업 URL이 존재하지 않습니다.");
		return "";
	}
	

	// 페이지를 호출시 전달될 파라미터 정보
	var params = {};
	if((typeof _params) === "object") {
		params = _params;
	}
	
	// 페이지 오픈 URL
	var url = jcm.contentPath + jcm.winPopupdUrl + "?openUrl=" + encodeURIComponent(_url) + "&openParams=" + encodeURIComponent(JSON.stringify(params));
	
	// 팝업 넓이값
	var popupWidth = jcm.constant.popup_width;
	if(!jut.isEmpty(_popupWidth) && Number(_popupWidth) > 0) {
		popupWidth = Number(_popupWidth);
	}
	
	// 팝업 높이값
	var popupHeight = jcm.constant.popup_height;
	if(!jut.isEmpty(_popupHeight) && Number(_popupHeight) > 0) {
		popupHeight = Number(_popupHeight);
	}
	
	// 팝업 위치 Top
	var popupTop = (window.screen.height /2) - (popupHeight / 2); // 상하 크기 반환
	if(!jut.isEmpty(_popupTop) && Number(_popupTop) > 0) {
		popupTop = Number(_popupTop);
	}
	
	// 팝업 위치 Left
	var popupLeft =  (window.screen.width / 2) - (popupWidth / 2); //좌우 크기 반환
	if(!jut.isEmpty(_popupLeft) && Number(_popupLeft) > 0) {
		popupLeft = Number(_popupLeft);
	}
	
	// Resize 가능 여부(IE에서만 동작함)
	var resizable 	= "no";
	if(!jut.isEmpty(_resizable) && _resizable == "yes") {
		resizable = _resizable;
	}
	
	var option 	= "scrollbars=yes,toolbar=no";
	option 		+= ",resizable=" + resizable;
	option 		+= ",width="  + popupWidth;
	option 		+= ",height=" + popupHeight;
	if(popupTop > 0 || popupLeft > 0) {
		option 		+= ",top="  + popupTop;
		option 		+= ",left=" + popupLeft;		
	}
	
	var winPopup = window.open(url, popupID, option);
	winPopup.focus();
}

/**
 * 쿠키를 생성한다. 
 * @param name  		생성할 쿠키이름
 * @param value 		생성할 쿠키값
 * @param expiresDate	쿠키 저장 종료 날짜
 */
jcm.setCookieDate = function(name, value, expiresDate) {
	var date = new Date();
	if(jut.isEmpty(expiresDate) && jut.isDateCheck(expiresDate)) {
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
	if(jut.isEmpty(expiresDay)) {
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

// 달력을 세팅한다. 
jcm.setDatepicker = function(_objId, _format) {
	if(jut.isEmpty(_objId)) {
		alert("Object ID가 존재하지 않습니다.");
		return "";
	}
	var format = "yy-mm-dd";
	if(!jut.isEmpty(_format)) {
		format = _format;
	}
	
	$("#" + _objId).datepicker({
		autoSize: true
		, changeYear: true
		, changeMonth  : true
		, dateFormat : format
		, maxDate : '2019-01-20'
		, monthNamesShort  : [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ]
		, dayNamesMin: [ "일", "월", "화", "수", "목", "금", "토" ]
	});
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

$(document).ready(function() {
	console.log("################# JEIN COMMON START #################");
	// 키보드로 백스페이스 기능을 차단한다. 
	jcm.setDisableKeyDown();
});




