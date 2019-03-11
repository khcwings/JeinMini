/**
 * Jqgrid를 세팅한다. 
 */

var jgrid = {};

jgrid.createGrid = function(_gridId, _colModel, _option) {
	var bodyHeight 				= 350;		// 그리드 높이
	var bodyRowHeight 			= 35;		// 그리드 행 높이
	var scrollX 				= false;	// 그리드 가로 스크롤 생성 여부
	var scrollY 				= false;	// 그리드 세로 스크롤 생성 여부
	var multiCheck				= false;	// 그리드의 첫 Cell에 체크박스 생성 여부

	if(!jut.isEmpty(_option)) {			
		if(!jut.isEmpty(_option.bodyHeight))    	{ bodyHeight 		= _option.bodyHeight; 		}
		if(!jut.isEmpty(_option.bodyRowHeight))		{ bodyRowHeight 	= _option.bodyRowHeight; 	}
		if(!jut.isEmpty(_option.scrollX) && _option.scrollX)		{ scrollX 		= true; 	}
		if(!jut.isEmpty(_option.scrollY) && _option.scrollY)		{ scrollY 		= true; 	}
		if(!jut.isEmpty(_option.multiCheck) && _option.multiCheck)	{ multiCheck 	= true; 	}
	}	
	
	var gridOption = {
			el			: $("#" + _gridId),
			scrollX		: scrollX,
		    scrollY		: scrollY,
		    bodyHeight	: bodyHeight,		// Grid 높이
		    rowHeight	: bodyRowHeight,	// Row 높이
		    columns		: _colModel
	};
	
	if(multiCheck) {
		gridOption.rowHeaders = ['checkbox']
	}
		
	return new tui.Grid(gridOption);
};
