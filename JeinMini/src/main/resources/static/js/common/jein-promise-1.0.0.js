'use strict';
function JeinPromise(){
	this.emptyCallback = function(){};
}
JeinPromise.prototype.ajax = function(opt){
	
	jcm.showLoadingBar();
	
	var url = (jut.isEmpty(opt.url)) ? '' : opt.url;
	var data = (jut.isEmpty(opt.data)) ? {} : opt.data;
	var method = (jut.isEmpty(opt.method)) ? 'GET' : opt.method;
	var async = (jut.isEmpty(opt.async)) ? true : opt.async;
	var headers = (jut.isEmpty(opt.headers)) ? '' : opt.headers;
	if(jut.isEmpty(headers)){
		headers = {
			'Accept': 'application/json'
			, 'Content-Type': 'application/json'
		};
	}
	var success = (jut.isEmpty(opt.success)) ? this.emptyCallback : opt.success;
	var error = (jut.isEmpty(opt.error)) ? this.emptyCallback : opt.error;
	
	var deferred = $.Deferred();
	try{
		$.ajax({
			url			: url
			, async		: async
			, method	: method
			, data		: JSON.stringify(data)
			, headers	: headers
			, success	: function(data) {
				if(!jut.isEmpty(data.redirectUrl)) {
					window.location =  data.redirectUrl;
				} else if(data.resultCode == jcm.constant.result_error) { 
					alert(data.resultMsg);
				} else {
					data.promiseResult = 'success';
					deferred.resolve(data);
					jcm.hideLoadingBar();
				}
			}
			, error		: function(err) {
				err.promiseResult = 'fail';
				deferred.reject(err);
				jcm.hideLoadingBar();
			}
		});
	}catch(exception){
		deferred.reject(exception);
		jcm.hideLoadingBar();
	}
	
	return deferred.promise();
}
JeinPromise.prototype.required = function(parentId){
	
	var deferred = $.Deferred();
	try{
		var that, title = '', flag = true, result = {rejectId : '', rejectTitle : '', rejectMessage : ''};
		$('#' + parentId).find('select, input, textarea').each(function(idx){
			that = $(this);
			if(that.prop('required')){
				if(!$.trim(that.val())){
					title = that.attr('title');
					if(title === ''){
						result.rejectId = that.attr('id');
						result.rejectTitle = '';
						result.rejectMessage = 'Rejected element has no title.';
					}else{
						result.rejectId = that.attr('id');
						result.rejectTitle = title;
						result.rejectMessage = '';
					}
					flag = false;
					return false;
				}
			}
		});
		
		if(flag){
			deferred.resolve();
		}else{
			deferred.reject(result.rejectId, result.rejectTitle, result.rejectMessage);
		}
	}catch(exception){
		deferred.reject();
	}
	
	return deferred.promise();
	
};
