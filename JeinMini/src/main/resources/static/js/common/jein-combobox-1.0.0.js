/**
 * var combo = new JeinComboBox({labelKey : 'LABEL', valueKey : 'VALUE'});
 * @param opt
 * @returns
 */
function JeinComboBox(){
	this.id = '';
	this.labelKey;
	this.valueKey;
	this.defaultShow = false;
	this.defaultValue = '';
	this.defaultLabel = '선택';
}
JeinComboBox.prototype.isInit = function(){
	return (jut.isEmpty(this.id)) ? false : true;
};
/**
 * 셀렉트박스에 옵션목록을 그린다
 */
JeinComboBox.prototype.draw = function(data, targetId){
	
	var target = document.getElementById(targetId);
	var option;
	
	//셀렉트박스를 초기화한다
	$(target).empty();
	
	if(this.defaultShow){
		option = document.createElement('option');
		option.value = this.defaultValue;
		option.innerText = this.defaultLabel;
		target.appendChild(option);
	}
	
	if(!jut.isEmpty(target)){
		for(var i=0; i<data.length; i++){
			option = document.createElement('option');
			option.value = data[i][this.valueKey];
			option.innerText = data[i][this.labelKey];
			target.appendChild(option);
		}
	}
	
	
	return this;
};
/**
 * 셀렉트박스에 그릴 데이터를 바인딩한다
 */
JeinComboBox.prototype.create = function(opt){
	
	var that = this, target, dataList, req;
	this.id = opt.id;
	this.labelKey = opt.labelKey;
	this.valueKey = opt.valueKey;
	if(!jut.isEmpty(opt.defaultShow)) this.defaultShow = opt.defaultShow;
	if(!jut.isEmpty(opt.defaultValue)) this.defaultValue = opt.defaultValue;
	if(!jut.isEmpty(opt.defaultLabel)) this.defaultLabel = opt.defaultLabel;
	
	//data value가 있으면 바로 그림
	if(!jut.isEmpty(opt.data)){
		this.draw(opt.data, opt.id);
	}
	//없으면 ajax로 데이터 받아옴8
	else{
		jcm.doAjax(
			true
			, opt.url
			, opt.requestData
			, function(response, request){
				if(response.resultCode === 'S'){
					that.draw(response[opt.dataKey], opt.id);
				}
			}
		);
	}
	return this;
};