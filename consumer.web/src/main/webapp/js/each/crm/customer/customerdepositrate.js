$package('IQB.customerdepositrate');
IQB.customerdepositrate = function(){
	var _box = null;
    var _list = null;
    var imgUrl = '';
	var _this = {
		cache:{},
		config: {
			action: {
				insert: urls['sysmanegeUrl'] + '/customerDeposit/insertCustomerDepositRate',
  				update: urls['sysmanegeUrl'] + '/customerDeposit/updateCustomerDepositRate',
  				getById: urls['sysmanegeUrl'] + '/customerDeposit/getCustomerDepositRateById',
  				remove: urls['sysmanegeUrl'] + '/customerDeposit/deleteCustomerDepositRate',
  				queryChannelListForSelect: urls['sysmanegeUrl'] + '/customerDeposit/queryChannelListForSelect',
  			},
			event: {
				reset: function(){//重写save	
					_box.handler.reset();
					$('#query-businessClass').val(null).trigger('change');
					$('#query-businessDetail').val(null).trigger('change');
				},
				update: function(){//重写update
					_this.extFunc.updateCustomerDepositInfo();
				},
				insert: function(){//重写insert
					$("#imgUl1").find("li").remove();
					$("#imgUl2").find("li").remove();
					$("#updateForm")[0].reset();  
					$('#select-businessClass').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "(无关)"});
					$('#select-businessDetail').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "(无关)"});
					$('#select-customerCode').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap', placeholder: "(无关)"});
					_box.handler.insert();
				},
				close:function(){
					_box.handler.close();
					$('.btn-upload-div').show();
					$('.btn-upload-remove').show();
				}
			},
  			dataGrid: {
  				url: urls['cfm'] + '/customerDeposit/queryCustomerDepositRateList',
  				singleCheck: true
			}
		},
		extFunc:{
			parseBusinessClass : function(val, dictType) {
				if(val == ''){
					return '(无关)';
				}
				var req_data = {'dictTypeCode': dictType};
				var ret = '';
				if(_this.cache.businessClassArr == undefined){
					IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
						var businessClassArr = result.iqbResult.result;
						_this.cache.businessClassArr = result.iqbResult.result;
					})
				}
				$.each(_this.cache.businessClassArr, function(key, retVal) {
					if(val == retVal.id){
						ret = retVal.text;
					}
				});
				return ret;
			},
			parseBusinessDetail : function(val, dictType) {
				if(val == ''){
					return '(无关)';
				}
				var req_data = {'dictTypeCode': dictType};
				var ret = '';
				if(_this.cache.businessDetailArr == undefined){
					IQB.postAsync(urls['rootUrl'] + '/sysDictRest/selectSysDictByType', req_data, function(result){
						var businessDetailArr = result.iqbResult.result;
						_this.cache.businessDetailArr = result.iqbResult.result;
					})
				}
				$.each(_this.cache.businessDetailArr, function(key, retVal) {
					if(val == retVal.id){
						ret = retVal.text;
					}
				});
				return ret;
			},
			parseCustomerName : function(val) {
				if(val == ''){
					return '(无关)';
				}
				return val;
			},
			businessClassSelect: function(){
				$('#query-businessDetail').empty();
				var req_data = {'businessClass': $("#query-businessClass").val()};
				IQB.postAsync(urls['rootUrl'] + '/customerDeposit/getBusinessDetailListByBusinessClass', req_data, function(result){
					$('#query-businessDetail').prepend("<option value=''>请选择</option>");
					$('#query-businessDetail').select2({theme: 'bootstrap', data: result.iqbResult.result});
					return result.iqbResult.result;
				})
			},
			selectBusinessClassSelect: function(){
				$('#select-businessDetail').empty();
				var req_data = {'businessClass': $("#select-businessClass").val()};
				IQB.postAsync(urls['rootUrl'] + '/customerDeposit/getBusinessDetailListByBusinessClass', req_data, function(result){
					$('#select-businessDetail').prepend("<option value=''>(无关)</option>");
					$('#select-businessDetail').select2({theme: 'bootstrap', data: result.iqbResult.result});
					return result.iqbResult.result;
				})
			},
			selectCustomerCodeSelect: function(){
				$('#select-customerCode').empty();
				var req_data = {};
				IQB.postAsync(urls['rootUrl'] + '/customerDeposit/queryChannelListForSelect', req_data, function(result){
					$('#select-customerCode').prepend("<option value=''>请选择</option>");
					$('#select-customerCode').select2({theme: 'bootstrap', data: result.iqbResult.result});
					return result.iqbResult.result;
				})
			},
			updateCustomerDepositInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['id'] = records[0].id;
			    	IQB.getById(_this.config.action.getById, option, function(result){	
			    		$("#updateForm")[0].reset();  
			    		$("#updateForm").form('load',result.iqbResult.result);
			    		
			    		$('#select-businessClass').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-businessDetail').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-customerCode').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$("#select-businessClass").trigger("change"); 
						$("#select-businessDetail").val(result.iqbResult.result.businessDetail).trigger('change');
						
			    		$("#updateForm").prop('action',_this.config.action.update);
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
			    		
					});
				}
			},
			checkCustomerDepositInfo: function(){
				var records = _box.util.getCheckedRows();
				if (_box.util.checkSelectOne(records)){
					var option = {};
			    	option['id'] = records[0].id;
			    	IQB.getById(_this.config.action.getById, option, function(result){		
			    		$("#updateForm")[0].reset(); 
			    		$("#updateForm").form('load',result.iqbResult.result);

			    		$('#select-businessClass').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-businessDetail').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$('#select-customerCode').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
						$("#select-businessClass").trigger("change"); 
						$("#select-businessDetail").val(result.iqbResult.result.businessDetail).trigger('change');
						
						$('.btn-upload-div').hide();
			    		$("#update-win").modal({backdrop: 'static', keyboard: false, show: true});
					});
			    	$("input").attr("disabled",true);
			    	$("select").attr("disabled",true);
			    	$("#btn-save").css({ "display": "none" });
			    	$("#btn-close").click(function(){
			    		$("input").removeAttr("disabled");
			    		$("select").removeAttr("disabled");
			    		$("#btn-save").css({ "display": "initial" });
			    	});
				}
			}
		},
		init: function(){
			_box = new DataGrid2(_this.config); 
            _list = new Tree(_this.config); 
			_box.init();
			this.initSelect();
			this.initBtnClick();
			this.initLevelSelect();
			this.initOthers();
		},
		initSelect: function(){
			IQB.getDictListByDictType('query-businessClass', 'COMM_BIZ_TYPE');
			IQB.getDictListByDictType('select-businessClass', 'COMM_BIZ_TYPE');
			$('#query-businessClass').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			$('#select-businessClass').select2({minimumResultsForSearch: 'Infinity', theme: 'bootstrap'});
			_this.extFunc.selectCustomerCodeSelect();
		},
		initBtnClick: function(){
			$('#btn-check').click(_this.extFunc.checkCustomerDepositInfo);
		},
		initOthers: function(){
		},
		initLevelSelect: function(){
			$('#query-businessClass').change(_this.extFunc.businessClassSelect);
			$('#select-businessClass').change(_this.extFunc.selectBusinessClassSelect);
		}
	}
	return _this;
}();

$(function(){
	/** 初始化表格  **/
	IQB.customerdepositrate.init();
	//为模态框添加滚动条
	$('#update-win').find('.modal-form').mCustomScrollbar({setHeight: 500});
});	


