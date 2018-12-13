$package('IQB.shouldReturnInfo');
IQB.shouldReturnInfo = function(){
	var _this = {
		cache: {},
		queryParam: function(key){
			var reg = new RegExp('(^|&)' + key + '=([^&]*)(&|$)', 'i');
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return null;
		},
		initPay: function(){
			var regId = _this.queryParam('regId');
			var orderId = _this.queryParam('orderId');
			IQB.post(urls.rootUrl + '/bill/repay4Tiqian.do', {regId: regId, orderId: orderId}, function(result){
				$('#sumAmt').val(result.repayAmt);
			});
		},
		pay: function(){
			var regId = _this.queryParam('regId');
			var openId = _this.queryParam('openId');
			var orderId = _this.queryParam('orderId');
			var merchantNo = _this.queryParam('merchantNo');
			var arr = [];
			var option = {};
			option.regId = regId;
			option.openId = openId;
			option.orderId = orderId;
			option.merchantNo = merchantNo;
			option.repayModel = 'all';
			option.sumAmt = $('#sumAmt').val();
			option.tradeNo = '123';
			option.repayDate = '2016';
			option.repayList = [];
			arr.push(option);
			IQB.postJson(urls.rootUrl + '/bill/go2payment/advance.do', arr, function(result){
				IQB.alert('处理成功！');
			});
		},
		init: function(){ 
			_this.initPay();
			$('#btn-pay').on('click', _this.pay);
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.shouldReturnInfo.init();
});		

//重写javaScript数组对象原型
Array.prototype.contain = function(e){  
	for(i = 0; i < this.length; i ++){  
		if(this[i] == e){
			return true;
		} 			  
	}  
	return false;  
}  