$package('IQB.shouldReturn');
IQB.shouldReturn = function(){
	var _this = {
		cache: {},
		init: function(){ 
			$('#btn-query').on('click', function(){
				var regId = $.trim($('#regId').val());
				var openId = $.trim($('#openId').val());
				if(regId && openId){
					var href = $('#btn-link').prop('href');
					href += '?regId=' + regId + '&openId=' + openId;
					$('#btn-link').prop('href', href);
					$('#btn-link').find('span').first().click();
				}else{
					IQB.alert('请输入必要信息！');
				}
			});
		}
	}
	return _this;
}();

$(function(){
	$('#regId').val(18911908439);
	$('#openId').val(10101);
	//页面初始化
	IQB.shouldReturn.init();
});		