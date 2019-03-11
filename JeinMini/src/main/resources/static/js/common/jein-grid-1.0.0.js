'use strict';
var JeinGridFactory = (function(){
	return{
		getDefaultOptions : function(){
			return {
				el : null
				, data : []
				, scrollX : false
				, scrollY : false
				, header : []
				, columns : []
			}
		}
	};
})();

/**
 * 그리드 객체
 */
function JeinGrid(){
	var grid = null;
	this.getGrid = function(){
		return grid;
	};
	this.setGrid = function(obj){
		grid = obj;
	};
	
	this.prevRowIndex = null;
	this.prevColumnName = null;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Object return functions
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * 새로운 그리드를 생성한다.
 * @param	opt		그리드를 생성하며 초기화할 옵션
 * 
 * id : 요소 아이디
 * scrollX : x축 스크롤 여부
 * scrollY : y축 스크롤 여부
 * header : 헤더 옵션
 * columns : 컬럼 옵션
 * 
 */
JeinGrid.prototype.create = function(opt){
	//그리드의 기본옵션을 복사한다.
	//var options = jut.copy(JeinGridFactory.getDefaultOptions());
	var options = {};
	
	//그리드의 기본옵션을 변경처리한다
	if(!jut.isEmpty(opt.id))			options.el = $('#' + opt.id.replace(/\#/g, ''));
	if(!jut.isEmpty(opt.rowHeaders))	options.rowHeaders = opt.rowHeaders;	
	if(!jut.isEmpty(opt.scrollX))		options.scrollX = opt.scrollX;
	if(!jut.isEmpty(opt.scrollY))		options.scrollY = opt.scrollY;
	if(!jut.isEmpty(opt.header))		options.header = opt.header;
	if(!jut.isEmpty(opt.pagination))	options.pagination = opt.pagination;
	if(!jut.isEmpty(opt.columns))		options.columns = opt.columns;
	if(!jut.isEmpty(opt.height))		options.bodyHeight = opt.height;
	if(!jut.isEmpty(opt.virtualScrolling))		options.virtualScrolling = opt.virtualScrolling;
	if(!jut.isEmpty(opt.treeOptions))		options.treeColumnOptions = opt.treeOptions;
	
	//현재 객체에 그리드를 새로 생성하고 반영한다
	this.setGrid(new tui.Grid(options));
	
	//초기 데이터가 있다면 반영한다
	if(typeof(opt.data) === 'object' && !jut.isEmpty(opt.data.length)){
		this.getGrid().setData(opt.data);
	}
	
	//헤더선택시 선택되는 default row를 해제한다
	if(!jut.isEmpty(opt.focusDefault) && !opt.focusDefault){
		this.getGrid().on('mousedown', function(ev){
			if(ev.targetType === 'columnHead') ev.stop();
		});
	}
	
	//객체를 반환한다
	return this;
};

/**
 * 그리드에 컬럼을 추가한다
 * @param	cols	컬럼 옵션
 */
JeinGrid.prototype.addColumns = function(cols){
	//참조중인 객체의 그리드를 가져온다
	var grid = this.getGrid();
	//그리드가 생성된 상태라면 컬럼을 추가한다
	if(!jut.isEmpty(grid)){
		var gridCols = grid.getColumns();
		var newCols = gridCols.concat(cols);
		grid.setColumns(newCols);
	}
	//객체를 반환한다
	return this;
};

/**
 * 그리드 스타일을 변경한다
 */
JeinGrid.prototype.setGridStyle = function(opt){
	
	tui.Grid.applyTheme('default', opt);
	
};

/**
 * 그리드에 데이터를 바인딩 한다
 */
JeinGrid.prototype.setData = function(data){
	this.getGrid().setData(data);
	return this;
};
JeinGrid.prototype.setAjaxData = function(opt){
	var that = this;
	jcm.doAjax(true, opt.url, opt.requestData, function(response, request){
		if(jut.isEmpty(opt.dataKey)){
			that.setData(response);
		}else{
			that.setData(response[opt.dataKey]);
		}
	});
	return this;
};
JeinGrid.prototype.setTreeData = function(data, opt){
	
	var dataList = data					//데이터
		, sortKey = opt.sortKey			//정렬키
		, parentKey = opt.parentKey		//부모키
		, childKey = opt.childrenKey	//자식키
		, resultTree = [];				//결과트리
	
	var expand = (jut.isEmpty(opt.expand)) ? false : opt.expand;
	
	//1. 옵션으로 전달받은 정렬키를 이용하여 오름차순 정렬한다
	var sortList = jut.ascendingSort(dataList, sortKey);
	var tree = {};
	tree[parentKey] = '';
	
	//2. 오름차순 정렬된 데이터를 재귀호출하며 자식노드를 탐색한다 
	var addChildrenToNode = function(node){
	    var currentNodeId = node[parentKey];
	    node._children = [];//호출시 전달된 노드에 tui grid tree구조를 적용한다
	    for(var i=0; i<sortList.length; i++){
	    	//자식키가 호출된 노드의 부모키와 같으면 부모노드에 자식노드를 추가하고 재귀호출한다
	    	if(sortList[i][childKey] === currentNodeId){
	    		sortList[i] = addChildrenToNode(sortList[i]);
	    		//펼치기 설정을 추가한다
	    		if(expand) sortList[i]._extraData = {treeState : 'EXPAND'};
	            node._children.push(sortList[i]);
	        }
	    }
	    //자식노드를 오름차순 정렬한다
	    node._children = jut.ascendingSort(node._children, sortKey);
	    return node;
	};
	resultTree = addChildrenToNode(tree);
	
	//최상위 껍데기 노드를 삭제하고 2depth 노드부터 실제 데이터로 반영한다
	var temp = [];
	for(var i=0; i<resultTree._children.length; i++){
		
		temp.push(resultTree._children[i]);
	}
	//그리드에 데이터를 반영한다
	this.setData(temp);
	
	return this;
};
JeinGrid.prototype.setAjaxTreeData = function(opt){
	var that = this;
	jcm.doAjax(true, opt.url, opt.requestData, function(response, request){
		if(jut.isEmpty(opt.dataKey)){
			that.setTreeData(response, opt);
		}else{
			that.setTreeData(response[opt.dataKey], opt);
		}
	});
	return this;
};

/**
 * 그리드 로우의 포커싱이 변할때 콜백함수를 호출한다
 */
JeinGrid.prototype.focusChangeRow = function(callback){
	var that = this;
	this.getGrid().on('focusChange', function(ev){
		if(that.prevRowIndex !== ev.rowKey){
			callback(ev.rowKey, ev.columnName, ev.prevRowKey, ev.prevColumnName);
			that.prevRowIndex = ev.rowKey;
			that.prevColumnName = ev.columnName;
		}
	});
	return this;v
};

/**
 * 그리드 셀의 포커싱이 변할때 콜백함수를 호출한다
 */
JeinGrid.prototype.focusChangeCell = function(callback){
	var that = this;
	this.getGrid().on('focusChange', function(ev){
		callback(ev.rowKey, ev.columnName, ev.prevRowKey, ev.prevColumnName);
	});
	return this;
};

/**
 * 그리드에 로우를 추가한다
 */
JeinGrid.prototype.addRow = function(index, defaultData){
	var header = {};
	var cols = this.getGrid().getColumns();
	if(jut.isEmpty(defaultData)){
		for(var i=0; i<cols.length; i++){
			header[cols[i].name] = '';
		}
	}else{
		for(var i=0; i<cols.length; i++){
			for(var key in defaultData){
				if(key === cols[i].name){
					header[cols[i].name] = defaultData[key];
				}else{
					header[cols[i].name] = '';
				}
			}
		}
	}
	
	this.getGrid().appendRow(header, {at : index});
	return this;
};

/**
 * 지정된 로우를 disabled 처리한다
 */
JeinGrid.prototype.setRowDisabled = function(opt){
	var that = this;
	this.getGrid().on('mousedown', function(ev){
		var target = ev.nativeEvent.target;
		var rowKey = ev.rowKey;
		if(that.getCellData(rowKey, opt.name) === opt.value){
			that.getGrid().disableRow(rowKey);
		}
	});
	return this;
};

/**
 * 지정된 셀의 데이터를 변경한다
 */
JeinGrid.prototype.setCellData = function(rowIndex, colName, val){
	this.getGrid().setValue(rowIndex, colName, val);
	return this;
};

/**
 * 체크된 행의 셀 데이터를 일괄적으로 변경한다
 */
JeinGrid.prototype.setCheckedRowCellData = function(colName, val){
	var grid = this.getGrid();
	var checkedRows = grid.getCheckedRowKeys(false);
	for(var i=0; i<checkedRows.length; i++){
		grid.setValue(checkedRows[i], colName, val);
	}
	return this;
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Value return functions
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * 행번호와 컬럼명에 해당하는 셀의 데이터를 리턴한다
 */
JeinGrid.prototype.getCellData = function(rowIndex, columnName){
	return this.getGrid().getValue(rowIndex, columnName, false);
};

/**
 * 포커싱된 셀의 데이터를 리턴한다
 */
JeinGrid.prototype.getFocusedCellData = function(colName){
	var focusedRowIndex = this.getGrid().getFocusedCell();
	return this.getGrid().getValue(focusedRowIndex, colName, false);
};

/**
 * 그리드 전체 데이터를 리턴한다
 */
JeinGrid.prototype.getData = function(){
	return this.getGrid().getRows();
};

/**
 * 변경된 데이터를 리턴한다
 */
JeinGrid.prototype.getChangedIgnoreData = function(arr){
	var ignores = [];
	if(!jut.isEmpty(arr) && !jut.isEmpty(arr.length)){
		ignores = arr;
	}
	var changedData = this.getGrid().getModifiedRows({
		checkedOnly : false
		, withRawData : false
		, rowKeyOnly : false
		, ignoredColumns : ignores
	});
	
	var createdRows = changedData.createdRows
		, updatedRows = changedData.updatedRows
		, deletedRows = changedData.deletedRows;
	var resultData = [];
	//생성된 데이터
	for(var i=0; i<createdRows.length; i++){
		createdRows[i].ROW_STATUS = 'C';
		resultData.push(createdRows[i]);
	}
	//수정된 데이터
	for(var i=0; i<updatedRows.length; i++){
		updatedRows[i].ROW_STATUS = 'U';
		resultData.push(updatedRows[i]);
	}
	//삭제된데이터
	for(var i=0; i<deletedRows.length; i++){
		deletedRows[i].ROW_STATUS = 'D';
		resultData.push(deletedRows[i]);
	}
	return resultData;
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Not return functions
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 체크 된 행을 삭제한다
 */
JeinGrid.prototype.removeCheckedRows = function(){
	this.getGrid().removeCheckedRows(false);
};

/*
 * 체크 된 행의 인덱스 목록을 반환한다
 */
JeinGrid.prototype.getCheckedRowIndex = function(){
	
};