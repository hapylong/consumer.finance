/**
 * @author
 * @Version
 * @DateTime
 */
$package('Formatter');
var Formatter = {
	//审核结果格式化
	approve: function(val){
		if(val == '1'){
			return '通过';
		}else if (val == '0'){
			return '拒绝';
		}else{
			return '';
		}
	},
	//时间戳转时间字符串
	time: function(val){
		if(val){
			val = parseInt(val, 10) * 1000;
			var date = new Date(val);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	},
	//时间戳转时间字符串
	time2: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	},
	//时间戳转日期字符串
	date: function(val){
		if(val){
			val = parseInt(val, 10) * 1000;
			var date = new Date(val);
			return date.format('yyyy年M月d日');
		}
		return '';
	},
	//时间转换 for requestFund
	dateFund: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日');
		}
		return '';
	},
	//是否可用
	isEnable: function(val){
		if(val != null){
			if(val == 1){
				return '启用';
			}else{
				return '停用';
			}
		}
		return '';
	},
	//是否可用(标签格式)
	isEnableHtml: function(val){
		if(val != null) {
			if(val == 1){
				return '<span class="label label-primary">启用</span>';
			}else{
				return '<span class="label label-default">停用</span>';
			}
		}
		return '';
	},
	//是否通过
	ispass: function(val){
		if(val != null){
			if(val == 1){
				return '通过';
			}else{
				return '不通过';
			}
		}
		return '';
	},
	//是否通过(标签格式)
	ispassHtml: function(val){
		if(val != null){
			if(val == 1){
				return '<span class="label label-primary">通过</span>';
			}else{
				return '<span class="label label-default">不通过</span>';
			}
		}
		return '';
	},
	//是否只读
	readonly: function(val){
		if(val != null){
			if(val == 1){
				return '只读';
			}else{
				return '可写';
			}
		}
		return '';
	},
	//是否只读(标签格式)
	readonlyHtml: function(val){
		if(val != null){
			if(val == 1){
				return '<span class="label label-default">只读</span>';
			}else{
				return '<span class="label label-primary">可写</span>';
			}
		}
		return '';
	},
	//是否是管理员
	isSuperadmin: function(val){
		if(val != null){
			if(val == 1){
				return '是';
			}else{
				return '否';
			}
		}
		return '';
	},
	//角色状态格式化
	stationStatus: function(val) {
		if(val != null){
			if(val == 1){
				return '<span class="label label-primary">激活</span>';
			}else{
				return '<span class="label label-default">非激活</span>';
			}
		}
		return '';
	},
	//用户状态格式化
	UserStatus: function(val){
		if(val != null){
			if(val == 1){
				return '<span class="label label-primary">正常</span>';
			}else{
				return '<span class="label label-default">冻结</span>';
			}
		}
		return '';
	},
	//业务类型格式化
	groupName: function(val){
		if(val){
			if(val == 'new'){
				return '新车';
			}else{
				return '二手车';
			}
		}
		return '';
	},
	/*CFM*/
	//是否上收月供
	upPayment: function(val){
		if(val != null){
			if(val == 1){
				return '是';
			}else{
				return '否';
			}
		}
		return '';
	},
	//时间戳转时间字符串
	timeCfm: function(val){
		if(val){
			val = parseInt(val, 10);
			var date = new Date(val);
			return date.format('yyyy年M月d日 hh:mm:ss');
		}
		return '';
	},
	//收取方式格式化
	chargeWay: function(val){	
		if(val != null){
			if(val == 1){
				return '线下收取';
			}else{
				return '线上收取';
			}
		}
		return '';
	},
	//货币格式化(¥200,500.00)
	money: function(val){
		if(val != null && val != '' && val > 0){
			var n = 2;
			val = parseFloat((val + '').replace(/[^\d\.-]/g, '')).toFixed(n)
					+ '';
			var l = val.split('.')[0].split('').reverse();
			var r = val.split('.')[1];
			var t = '';
			for(var i = 0; i < l.length; i++){
				t += l[i]
						+ ((i + 1) % 3 == 0 && (i + 1) != l.length ? ',' : '');
			}
			return '¥' + t.split('').reverse().join('') + '.' + r;
		}
		return 0;
	},
	//比例
	percent: function(val){
		if(val != null && val != '' && val > 0){
			return val;
		}
		return 0;
	},
	//还原货币格式(200500.00)
	removeMoneyPrefix: function(val){
		return val.replace('¥', '').replace(',', '').replace(',', '').replace(',', '');
	},
	//还原收取方式
	chargeWayReverse: function(val){
		if(val){
			if(val == '线上收取'){
				return 0;
			}else{
				return 1;
			}
		}
		return '';
	},
	//支付结果格式化
	preAmountStatus: function(val){
		if(val != null){
			if(val == 1){
				return '已支付';
			}else{
				return '未支付';
			}
		}
		return '';
	},
	//订单状态格式化
	orderStatu: function(val){	
		if(val != null){
			if(val == 1){
				return '审核拒绝';
			}else if(val == 2){
				return '审核中';
			}else if(val == 3){
				return '已分期';
			}else if(val == 4){
				return '待支付';
			}else{
				return '审核通过';
			}
		}		
		return '';
	},
	//工作流格式化
	wfStatus: function(val){	
		if(val != null){   
			if(val == 5){
				return '人工风控';
			}else if(val == 6){
				return '业务门店审核中';
			}else if(val == 7){
				return '内审审核中';
			}else if(val == 8){
				return '财务审核中';
			}else if(val == 9){
				return '信贷';
			}
		}		
		return '';
	},
	//账单状态格式化
	status: function(val){
		if(val != null){
			if(val == 1){
				return '已还款';
			}else if(val == 2){
				return '待还款';
			}else if(val == 3){
				return '已逾期';
			}
		}		
		return '';
	},
	//还款按钮格式化
	option: function(val, row, rowIndex){
		return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.toPayment.turnTo(' + rowIndex + ')">代偿</button>';
	},
	//还款按钮格式化2
	option2: function(val, row, rowIndex){
		if(row.status == 1){
			return '<button type="button" disabled class="btn btn-default btn-sm btn-success" onclick="IQB.overdue.turnTo(' + rowIndex + ')">代偿</button>';
		}else{
			return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.overdue.turnTo(' + rowIndex + ')">代偿</button>';
		}
	},
	//请款按钮格式化
	option3: function(val, row, rowIndex){
		return '<button type="button" class="btn btn-default btn-sm btn-success" onclick="IQB.requestFunds.turnTo(' + rowIndex + ')">请款</button>';
	},
	//复选框格式化
	checkbox: function(val, row, rowIndex){
		if(row.status == 1){
			return '<input type="checkbox" class="datagrid-row-checkbox" disabled>';
		}else{
			return '<input type="checkbox" class="datagrid-row-checkbox">';
		}
	},
	//暂时无效
	isPay: function(val){
		if(val < 4){
			 return true;
		}else{
			 return false;
		}
		return '';
	},
	//***
	fundSource: function(val){
		if(val != null){
			if(val == 1){
				return '爱钱帮';
			}else if(val == 2){
				return '饭饭金服';
			}
		}
		return '';
	},
	//订单状态格式化
	riskStatus: function(val){
		if(val != null){
			if(val == 0){
				return '审核通过';
			}else if(val == 1){
				return '审核拒绝';
			}else if(val == 2){
				return '审核中';
			}else if(val == 3){
				return '已分期';
			}else if(val == 4){
				return '人工风控';
			}else if(val == 5){
				return '业务门店审核中';
			}else if(val == 6){
				return '内审审核中';
			}else if(val == 7){
				return '财务审核中';
			}else{
				return '信贷';
			}
		}
		return '';
	},
	//忽略空值
	ifNull: function(val){	
		if(val != null && val != ''){
			return val;
		}
		return '';
	},
	//页面高度
	fitHeight: function(percent){
		percent = percent || 1;
		return document.body.scrollHeight * percent;
	},
	//页面宽度
	fitWidth: function(percent){
		percent = percent || 1;
		return document.body.scrollWidth * percent;
	},
	//媒体类型及其支持扩展名
	extensionName: {
		pic: ['jpg', 'jepg', 'png'],
		doc: ['doc', 'docx', 'txt', 'pdf', 'xls', 'xlsx']
	},
	//根据图片路径获取图片名
	getImgName: function(val){	
		var arry = val.split('\\').reverse();
		return arry[0];
	},
	//获取文件名
	getFileName: function(val){	
		if(val.indexOf('\\') == -1){
			return val;
		}
		return val.substring(val.lastIndexOf('\\') + 1) ;
	},
	//获取文件扩展名
	getExtensionName: function(val){	
		return val.substring(val.lastIndexOf('.') + 1) ;
	},
	parseCustomerType: function(val, dictType){
		var req_data = {'dictTypeCode': dictType};
		var ret = '';
		IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
			var customerTypeArr = result.iqbResult.result;
			$.each(customerTypeArr, function(key, retVal) {
				if(val == retVal.id){
					ret = retVal.text;
				}
			});
		})
		return ret;
	},
	review: function(val){	
		if(val != null && val != ''){
			if(val == 1){
				return '是';
			}else{
				return '否';
			}
		}
		return '';
	},
};
//重写javaScript日期对象原型
Date.prototype.format = function(fmt){
	var o = {
		'M+': this.getMonth() + 1,//月份
		'd+': this.getDate(),//日
		'h+': this.getHours(),//小时
		'm+': this.getMinutes(),//分
		's+': this.getSeconds(),//秒
		'q+': Math.floor((this.getMonth() + 3) / 3),//季度
		'S': this.getMilliseconds()//毫秒
	};
	if(/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	for( var k in o)
		if(new RegExp('(' + k + ')').test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
	return fmt;
}

//重写javaScript数组对象原型
Array.prototype.contain = function(e){  
	for(i = 0; i < this.length; i ++){  
		if(this[i] == e){
			return true;
		} 			  
	}  
	return false;  
}  