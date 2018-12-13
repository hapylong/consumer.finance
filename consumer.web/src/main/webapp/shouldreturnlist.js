$package('IQB.shouldReturnList');
IQB.shouldReturnList = function(){
	var _this = {
		cache: {},
		queryParam: function(key){
			var reg = new RegExp('(^|&)' + key + '=([^&]*)(&|$)', 'i');
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return null;
		},
		statusFormatter: function(val){
			if(val == '1'){
				return '未还款';
			}
		},
		link: function(orderId, merchantNo){
			var regId = _this.queryParam('regId');
			var openId = _this.queryParam('openId');
			var href = $('#btn-link').prop('href');
			href += '?regId=' + regId + '&openId=' + openId + '&orderId=' + orderId + '&merchantNo=' + merchantNo;
			$('#btn-link').prop('href', href);
			$('#btn-link').find('span').first().click();
		},
		initList: function(){
			var regId = _this.queryParam('regId');
			var openId = _this.queryParam('openId');
			IQB.post(urls.rootUrl + '/bill/queryCurrBills.do', {regId: regId, openId: openId}, function(result){
				var resultInfo = result.result;
				var html = '';
				$.each(resultInfo, function(i, m){
					var merchantNo;
					html += '<div class="panel panel-primary" style="margin-bottom: 10px;">';
					html += '<div class="panel-heading"><h3 class="panel-title">订单号(orderId)：' + m.orderId + '</h3></div>' 
					html += '<table class="table table-hover table-condensed"><thead><tr class="warning"><th></th><th>期数(repayNo)</th><th>应还金额(curRepayAmt)[保留2位小数]</th><th>状态(status)</th><th>最后还款日(lastRepayDate)</th><th>商户号(merchantNo)</th></tr></thead><tbody>'
					$.each(m.billList, function(j, n){
						merchantNo = n.merchantNo;
						html += '<tr>';
						html += '<td><input type="checkbox" orderId="' + m.orderId + '" merchantNo="' + merchantNo + '" repayNo="' + n.repayNo + '" curRepayAmt="' + n.curRepayAmt + '"  /></td>';
						html += '<td>' + n.repayNo + '</td>';
						html += '<td>' + (parseFloat(n.curRepayAmt)).toFixed(2) + '</td>';
						html += '<td>' + _this.statusFormatter(n.status) + '</td>';
						html += '<td>' + Formatter.time2(n.lastRepayDate) + '</td>';
						html += '<td>' + n.merchantNo + '</td>';
						html += '</tr>';
					});
					html += '</tbody></table><div class="panel-body">' + '<button id="btn-query" type="button" class="btn btn-primary pull-right" onclick="IQB.shouldReturnList.link(\'' + m.orderId + '\',\'' + merchantNo + '\')">提前还款</button>' + '</div>'
					html += '</div>';
				});
				$('.container').first().html(html);
			});
		},
		pay: function(){
			var c = $('input[type="checkbox"]:checked');
			if(c.length > 0){
				var arr = [];
				var regId = _this.queryParam('regId');
				var openId = _this.queryParam('openId');
				$.each(c, function(i, m){
					var o = {};
					o.regId = regId;
					o.openId = openId;
					o.orderId = $(m).attr('orderId');
					o.merchantNo = $(m).attr('merchantNo');					
					o.repayNo = $(m).attr('repayNo');
					o.amt = $(m).attr('curRepayAmt');
					arr.push(o);
				});
				var arr2 = [];
				$.each(arr, function(i, m){
					if(!arr2.contain(m.orderId)){
						arr2.push(m.orderId);
					}
				});
				var arr3 = [];
				$.each(arr2, function(i, m){
					var o = {};
					o.orderId = m;
					o.sumAmt = 0;
					o.repayList = [];
					$.each(arr, function(j, n){
						if(m == n.orderId){
							o.regId = n.regId;
							o.openId = n.openId;
							o.merchantNo = n.merchantNo;
							o.repayModel = 'normal';
							o.sumAmt += parseFloat(n.amt);
							o.tradeNo = '123';
							o.repayDate = '2016';
							o.repayList.push(n);
						}
					});
					o.sumAmt = o.sumAmt.toFixed(4) + '';
					arr3.push(o);
				});
				IQB.postJson(urls.rootUrl + '/bill/go2payment/normal.do', arr3, function(result){
					var msg = result.retMsg || result.msg;
					IQB.alert(msg);
				});
			}else{
				IQB.alert('未选中');
			}
		},
		init: function(){ 
			_this.initList();
			$('#btn-pay').on('click', _this.pay);
		}
	}
	return _this;
}();

$(function(){
	//页面初始化
	IQB.shouldReturnList.init();
});		