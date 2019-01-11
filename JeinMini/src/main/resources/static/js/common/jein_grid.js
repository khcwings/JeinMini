/**
 * Jqgrid를 세팅한다. 
 */

var jgrid = {};

jgrid.createJqGrid = function(gridId, colModel, option) {
	var height 				= 200;		// 기본 그리드 높이(number, ??%, 'auto')
	var rowNum 				= 10;		// 기본 그리드 Row 수, -1 설정시 무제한
	var cellEdit 			= true;		// 셀 편집 활성화 여부
	var multiselect			= false;	// 최상단 Check Box 사용 여부
	var multiboxonly		= false;	// 다중 Row 선택 여부
	var multiselectWidth	= 50;		// 최상단 Check Box 넓이값
	var noDataMsg 			= "데이터가 존재하지 않습니다.";
	var defaultPager		= "jqGridDefaultPager";
	var customPager			= "jqGridCustomPager";
	var onSelectRow			= null;		// onSelectRow Event 재설정
	var onCellSelect		= null;		// onCellSelect Event 재설정

	if(!jut.isEmpty(option)) {			
		if(!jut.isEmpty(option.height))    { height 	= option.height; 	}
		if(!jut.isEmpty(option.rowNum))    { rowNum 	= option.rowNum; 	}		
		if(!jut.isEmpty(option.noDataMsg)) { noDataMsg 	= option.noDataMsg; }
		if(!jut.isEmpty(option.cellEdit))  { cellEdit 	= option.cellEdit; 	}
		if(!jut.isEmpty(option.multiselect))   		{ multiselect 		= option.multiselect; 		}		
		if(!jut.isEmpty(option.multiboxonly)) 		{ multiboxonly 		= option.multiboxonly; 		}
		if(!jut.isEmpty(option.multiselectWidth))  	{ multiselectWidth 	= option.multiselectWidth; 	}	
		if(!jut.isEmpty(option.defaultPager)) 		{ defaultPager 		= option.defaultPager; 		}
		if(!jut.isEmpty(option.customPager))  		{ customPager 		= option.customPager; 		}
		if(!jut.isEmpty(option.onSelectRow) && typeof(option.onSelectRow) === "function")  	{ 
			onSelectRow 	= option.onSelectRow; 	
		}
		if(!jut.isEmpty(option.onCellSelect) && typeof(option.onCellSelect) === "function")  	{ 
			onCellSelect 	= option.onCellSelect; 	
		}
	}	

	$("#" + gridId).jqGrid({
        datatype: "local",								// 입력 데이터 유형
        colModel: colModel,								// 그리드 셀 리스트
		gridveiw:true,									// 그리드 성능 향상 옵션
        autowidth:true,									// 그리드 셀 자동 넓이 설정
		height: height,									// 그리드 높이 설정
		cellEdit:cellEdit,								// 그리드 편집 가능 여부
		cellsubmit:'clientArray',						// 그리드 편집시 
		viewrecords: true,	
		rowNum: rowNum,									// 그리드에 보여줄 레코드 수
        shrinkToFit:true,								// 우측 스크롤바 위에 공강을 없애고 채움
        loadonce:true,									// 한번만 데이터롤 로드함.
		multiselect:multiselect,						// 최상단 Check Box 사용 여부
		multiselectWidth:multiselectWidth,				// 최상단 Check Box 넓이값
        multiboxonly:multiboxonly,						// 다중 Row 선택 여부
        pager: "#" + defaultPager,
        loadComplete:function(data) {
        	console.log("JQGRID LOAD COMPLETE =============> ");
        },
        beforeSelectRow: function (rowId, e) {			// 단일행 선택을 활성화한 경우 행선택시 다른행 선택을 초기한다. 
        	if(multiselect && multiboxonly) {
        		$(this).jqGrid("resetSelection");
                return true;
        	}
        	return true;
        },
        onCellSelect:function(rowid, iCol, cellcontent, e) {
        	if(typeof(onCellSelect) === "function") {
        		onCellSelect(rowid, status, e);
        	}
        },
        onSelectRow:function(rowid, status, e) {
        	if(typeof(onSelectRow) === "function") {
        		onSelectRow(rowid, status, e);
        	}
        }
    });
	// 체크박스를 활성화한 상태에서 단일행 선택모드를 선택한 경우 헤더의 체크박스를 제거한다. 
	if(multiselect && multiboxonly) {
		$("#" + gridId).parent().parent().parent().find("thead tr").eq(0).find("th").eq(0).find("input").remove();
	}	
	$("#" + gridId).parent().parent().parent().find(".ui-jqgrid-bdiv").prepend("<div class='ui-jqgrid-nodata d_none grid_nodata' id='jqGridNoData'>" + noDataMsg + "</div>");
	//$("#jqGridCustomPager").addClass("d_none");			// Paging 영역 숨기기
};
/**
 * 그리드에 행을 추가한다. 
 * gridId    : 대상 그리드 아이디
 * _rowData  : 추가 데이터
 * _position : 추가할 행의 위치
 * _editable : 추가할 행의 지정셀을 에디터 모드를 추가할 지 설정
 * _cellNum  : 에디터 모드 셀 인덱스
 */
jgrid.addRow = function(gridId, _rowData, _position, _editable, _cellNum) {
	var id 		 = "NEW_" + jut.getDate();
	var rowData  = {};
	var position = "first";
	var editable = false;
	var cellNum  = 1;
	
	if(!jut.isEmpty(_rowData) && typeof(_rowData) == "object") { rowData 	= _rowData; 	}
	if(!jut.isEmpty(_position)) { editable 	= _position; }		
	if(!jut.isEmpty(_editable)) { editable 	= _editable; }
	if(!jut.isEmpty(_cellNum))  { cellNum 	= _cellNum;  }
	
	$("#" + gridId).jqGrid('addRowData', id, rowData, position);
	if(editable) {
		$("#" + gridId).jqGrid('editCell', id, cellNum, true);
	}
	return id;
};
/**
 * 그리드에 조회된 데이터를 세팅한다. 
 * gridId : 대상 그리드 아이디
 * _data  : 조회된 원본 데이터
 * 
 */
jgrid.setLocalData = function(gridId, data) {
	// 그리드 데이트 초기화
	$("#" + gridId).jqGrid('clearGridData');

	// 그리드 데이터 세팅
	$("#" + gridId).jqGrid('setGridParam', {
		datatype: "local",	
		data:data
	}).trigger("reloadGrid");
};