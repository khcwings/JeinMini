var JeinModalFactory = (function(){
	'use strict';
	var modalList = [];
	return {
		addModal : function(modal, id){
			modalList.push({id : id, modal : modal});
		}
		, removeModal : function(id){
			for(var i=0; i<modalList.length; i++){
				if(modalList[i].id === id) modalList.slice(i, i+1);
			}
		}
		, getModal : function(id){
			var mod = null;
			for(var i=0; i<modalList.length; i++){
				if(modalList[i].id === id) mod = modalList[i].modal;
			}
			return mod;
		}
	};
})();
/**
 * Modal Object
 * @param id		: 모달을 생성하며 설정할 아이디
 * @param url		: 모달에 표시할 페이지 URL
 * @param width		: 모달 가로 사이즈
 * @param height	: 모달 세로 사이즈
 * @param data		: 모달에 전달할 데이터(private)
 * @returns
 */
function JeinModal(options){
	var modal		= null;
	var data		= (jut.isEmpty(options.data)) ? null : options.data;
	this.isShow		= false;
	this.id			= (jut.isEmpty(options.id)) ? '' : options.id;
	this.url		= (jut.isEmpty(options.url)) ? '' : options.url;
	this.width		= (jut.isEmpty(options.width)) ? jcm.constant.popup_width : options.width * 1;
	this.height		= (jut.isEmpty(options.height)) ? jcm.constant.popup_height : options.height * 1;
	this.bodyHeight	= this.height;
	this.target		= (jut.isEmpty(options.target)) ? null : options.target;
	this.header		= (jut.isEmpty(options.header)) ? false : options.header;
	
	this.title		= (jut.isEmpty(options.title)) ? '' : options.title;
	this.footer		= (jut.isEmpty(options.header)) ? false : options.header;
	
	this.okBtnFlag	= (jut.isEmpty(options.okButton)) ? false : true;
	this.cancelBtnFlag	= (jut.isEmpty(options.cancelButton)) ? false : true;
	this.okBtn		= (jut.isEmpty(options.okButton)) ? null : options.okButton;
	this.cancelBtn	= (jut.isEmpty(options.cancelButton)) ? null : options.cancelButton;
	this.customBtn	= (jut.isEmpty(options.customButtons)) ? null : options.customButtons;
	
	this.getData = function(){
		return this.data;
	};
	this.getModal = function(){
		return modal;
	};
	
	if(jut.isEmpty(this.id)){
		alert('팝업 ID가 없습니다.');
		return;
	}
	if(jut.isEmpty(this.url)){
		alert('팝업 URL이 없습니다.');
		return;
	}
	if(jut.isEmpty(this.target)){
		alert('이벤트를 동작시킬 버튼ID가 없습니다.');
		return;
	}
	
	if(this.header) this.height += 50;//헤더가 설정되었다면 헤더높이를 더한다
	if(this.footer) this.height += 50;//푸터가 설정되었다면 푸터높이를 더한다
	
	var that = this;
	this.url = jcm.contentPath + this.url;
	JeinModalFactory.removeModal(this.id);
	$('#' + that.id).remove();
	
	var modalParent = document.createElement('div');
	modalParent.id = this.id;
	modalParent.className = 'dim-layer';
	
	var modalBackground = document.createElement('div');
	modalBackground.className = 'dimBg';
	modalBackground.addEventListener('click', function(e){
		e.stopPropagation();
		that.hide();
	});
	modalParent.appendChild(modalBackground);
	
	var modalLayer = document.createElement('div');
	modalLayer.className = 'pop-layer';
	modalLayer.style.width = this.width + 'px';
	modalLayer.style.height = this.height + 'px';
	//헤더
	var popHeader = document.createElement('div');
	popHeader.className = 'pop-header';
	popHeader.style.height = '50px';
	popHeader.innerText = (jut.isEmpty(this.title)) ? '' : this.title;
	//바디
	var popBody = document.createElement('div');
	popBody.className = 'pop-body';
	popBody.style.height = this.bodyHeight + 'px';
	//푸터
	var popFooter = document.createElement('div');
	popFooter.className = 'pop-footer';
	popFooter.style.height = '50px';
	//확인버튼
	if(this.okBtnFlag){
		var okButton = document.createElement('button');
		okButton.innerText = (jut.isEmpty(this.okBtn.label)) ? 'Ok' : this.okBtn.label;
		okButton.className = (jut.isEmpty(this.okBtn.className)) ? 'ui-button ui-corner-all ui-widget' : this.okBtn.className;
		okButton.addEventListener('click', function(e){
			if(!jut.isEmpty(that.okBtn.callback)) that.okBtn.callback(that);
		});
		popFooter.appendChild(okButton);
	}
	//취소버튼
	if(this.cancelBtnFlag){
		var cancelButton = document.createElement('button');
		cancelButton.innerText = (jut.isEmpty(this.cancelBtn.label)) ? 'Cancel' : this.cancelBtn.label;
		cancelButton.className = (jut.isEmpty(this.cancelBtn.className)) ? 'ui-button ui-corner-all ui-widget' : this.cancelBtn.className;
		cancelButton.addEventListener('click', function(e){
			if(!jut.isEmpty(that.cancelBtn.callback)) that.cancelBtn.callback(that);
		});
		popFooter.appendChild(cancelButton);
	}
	//커스텀버튼
	if(!jut.isEmpty(this.customBtn) && !jut.isEmpty(this.customBtn.length)){
		for(var i=0; i<this.customBtn.length; i++){
			var currentCustomButton = document.createElement('button');
			currentCustomButton.innerText = (jut.isEmpty(this.customBtn[i].label)) ? '' : this.customBtn[i].label;
			currentCustomButton.className = (jut.isEmpty(this.customBtn[i].className)) ? 'ui-button ui-corner-all ui-widget' : this.customBtn[i].className;
			currentCustomButton.setAttribute('data-index', i);
			currentCustomButton.addEventListener('click', function(e){
				if(!jut.isEmpty(that.customBtn[e.target.getAttribute('data-index') * 1].callback)){
					that.customBtn[e.target.getAttribute('data-index') * 1].callback(that);
				}
			});
			popFooter.appendChild(currentCustomButton);
		}
	}
	
	modalLayer.appendChild(popHeader);
	modalLayer.appendChild(popBody);
	modalLayer.appendChild(popFooter);
	modalParent.appendChild(modalLayer);
	
	//dom을 jquery object로 파싱
	modal = $(modalParent);
	JeinModalFactory.addModal(this, this.id);
	
	//target에 이벤트를 부여한다
	$('#' + that.target).on('click', function(){
		that.show();
	});
}
/**
 * 모달을 표시한다
 */
JeinModal.prototype.show = function(){
	console.log('show');
	if(!this.isShow){
		var that = this;
		$('body', top.document).append(that.getModal());
		$("#" + that.id + " .pop-body").load(that.url, {}, function(response, status, xhr) {
			if(status === 'success') {
				
				var popupWidth  = $("#" + that.id + ' .pop-layer').outerWidth();
				var popupHeight = $("#" + that.id + ' .pop-layer').outerHeight();
	            $("#" + that.id + " .pop-layer").css({
	                marginTop: (popupHeight / 2) * -1
	                , marginLeft: (popupWidth / 2) * -1
	                , position : 'absolute'
	            });
	            
	            $("#" + that.id).show(jcm.constant.popup_effect, {}, jcm.constant.popup_effect_time, function() {});
	            that.isShow = true;
			}				
		});
	}
	return this;
};
/**
 * 모달을 숨김처리 한다
 */
JeinModal.prototype.hide = function(){
	if(this.isShow){
		var that = this;
		$("#" + that.id).hide(jcm.constant.popup_effect, {}, jcm.constant.popup_effect_time, function() {
			$("#" + that.id).remove();
			that.isShow = false;
		});
	}
	return this;
};
/**
 * 모달을 표시하거나 숨김처리한 뒤 동기처리 콜백을 수행한다
 */
JeinModal.prototype.then = function(callback){
	callback(JeinModalFactory.getModal(this.id));
	return this;
};
/**
 * 모달이 표시중인지 체크한다
 */
JeinModal.prototype.isShow = function(){
	return this.isShow;
};
/**
 * 모달또는 여러개의 모달이 동시에 열려있는 경우 모달간 데이터를 주고받는다
 */
JeinModal.prototype.broadcast = function(id){
	var broadModal = JeinModalFactory.getModal(id);
	return broadModal.getData();
};