/**
 * @author Van
 * @Version: 1.0
 * @DateTime: 2012-11-11
 */
var DataGrid2 = function(config){
	
		//工具函数
		var Util = {
			getCheckedRows: function(){	
				return Grid.datagrid2('getCheckedRows');		
			},
			checkSelect: function(rows){
				if(rows && rows.length > 0){
					return true;
				}else{
					IQB.alert('未选中行');
					return false;	
				}						
			},
			checkSelectOne: function(rows){
				if(!Util.checkSelect(rows)){
					return false;
				}
				if(rows.length == 1){
					return true;
				}
				IQB.alert('请选中单行');
				return false;
			}		
		};			
		
		//自定义函数
		var Handler = {
			//刷新
			refresh: function(callback){				
				if($.isFunction(callback)){
					callback();//回调
				}else{
					Grid.datagrid2(dataGrid);
				}				
			},
			//查询
			search: function(callback){				
				if($.isFunction(callback)){
					callback();//回调
				}else{
					$.each(Form.search.find('input[type = "text"]'), function(i, n){
						var value = $.trim($(n).val());
						$(n).val(value);
					});
					$.extend(dataGrid.queryParams, Form.search.serializeObject(), {pageNum: 1});
					Evt.refresh();
				}	
			},		
			//重置
			reset: function(callback){			
				if($.isFunction(callback)){
					callback();//回调
				}else{
					Form.search.form('reset');
				}
			},		
			//新增
			insert: function(callback){					
				if($.isFunction(callback)){
					callback();//回调
				}else{					
					Form.update.prop('action', Action.insert);
					Form.update.form('reset');
					Win.update.modal({backdrop: 'static', keyboard: false, show: true});
				}
			},
			//修改
			update: function(callback){
				var rows = Util.getCheckedRows();
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idKey = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idKey];
			    	//option[idKey] = parseInt(idValue, 10);
			    	option[idKey] = idValue;
			    	IQB.getById(Action.getById, option, function(result){							
						if($.isFunction(callback)){
							callback(result);//回调
						}else{
							Form.update.prop('action', Action.update);
							Form.update.form('load', result.iqbResult.result);							
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});
						}
					});
				}
			},
			updateForm:function(callback){
				var rows = Util.getCheckedRows();
				if(Util.checkSelectOne(rows)){
					var option = {};
			    	var idKey = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idKey];
			    	option[idKey] = idValue;
			    	IQB.getById(Action.getById, option, function(result){	
							Form.update.prop('action', Action.update);
							Form.update.form('load', result.iqbResult.result);	
							if($.isFunction(callback)){
								callback(result);//回调
							}
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});
					});
				}
			},
			updateAsset:function(callback){
				var rows = Util.getCheckedRows();
				if (Util.checkSelectOne(rows)){
					var option = {};
			    	var idKey = Grid.datagrid2('getIdField'); 
			    	var idValue = rows[0][idKey];
			    	option[idKey] = idValue;
			    	IQB.getById(Action.getById, option, function(result){
							Form.update.prop('action', Action.getById);
							Form.update.form('load', result.iqbResult.result);							
							Form.proForm.form('load', result.iqbResult.result);
							Form.orderForm.form('load', result.iqbResult.result);
							
							var tableResult = result.iqbResult.result;
							$('#preAmount').text(Formatter.money(tableResult.preAmount));
							$('#downPayment').text(Formatter.money(tableResult.downPayment));
							$('#margin').text(Formatter.money(tableResult.margin));
							$('#serviceFee').text(Formatter.money(tableResult.serviceFee));
							$('#feeAmount').text(Formatter.money(tableResult.feeAmount));
							$('#orderItems').text(tableResult.orderItems);
							$('#monthInterest').text(Formatter.money(tableResult.monthInterest));
							$('#orderTimes').val(Formatter.time(tableResult.orderTime));
							if($.isFunction(callback)){
								callback(result.iqbResult.result.orderId, result.iqbResult.result.leftitems);//回调
							}							
							Win.update.modal({backdrop: 'static', keyboard: false, show: true});						
					});
				}
			},
			//删除函数
			remove: function(callback){
				var rows = Util.getCheckedRows();				
				if (Util.checkSelectOne(rows)){
					IQB.confirm('确认要删除吗？', function(){
						var option = {};
						var idKey = Grid.datagrid2('getIdField'); 
				    	var idValue = rows[0][idKey];
				    	//option[idKey] = parseInt(idValue, 10);
				    	option[idKey] = idValue;
				   		IQB.remove(Action.remove, option, function(result){								
							if($.isFunction(callback)){
								callback(result);//回调
							}else{
								Evt.refresh();
							}
				   		});
					}, function(){});										
				}
			},
			//保存函数
			save: function(callback){
				IQB.save(Form.update, function(result){					
					if($.isFunction(callback)){
						callback(result);//回调
					}else{	
						//禁用表单验证,处理页面遗留toolTip
						Form.update.form('disableValidation');						
						Evt.close();
						Form.update.form('reset');
						//启用表单验证
					    Form.update.form('enableValidation');					    
					    Evt.refresh();					    
					}								    
				});
			},
			//关闭函数
			close: function (callback){					
				if($.isFunction(callback)){
					callback();//回调
				}else{
					Win.update.modal('hide');
				}			
			}
		};		
		
		//传入参数
		config = config || {};	
		//URL参数
		var actionUrl = config.action || {};
		//URL
		var Action = {
				'insert': actionUrl.insert || 'insert',
				'update': actionUrl.update || 'update',
				'getById': actionUrl.getById || 'getById',
				'remove': actionUrl.remove || 'remove'
		};
		//函数参数
		var evt = config.event || {};
		//函数
		var Evt = {
			refresh: evt.refresh || Handler.refresh,
			search: evt.search || Handler.search,
			reset: evt.reset || Handler.reset,
			insert: evt.insert || Handler.insert,
			update: evt.update || Handler.update,		
			remove: evt.remove || Handler.remove,
			save: evt.save || Handler.save,
			close: evt.close ||  Handler.close,
			updateForm:evt.updateForm || Handler.updateForm,
			updateAsset:evt.updateAsset || Handler.updateAsset
		};		
		//表格参数
		var dataGrid = config.dataGrid || {};	
		//表格
		var Grid = $('#datagrid');			
		//表单	
		var Form = {
			search: $('#searchForm'),
			list: $('#listForm'),
			update: $('#updateForm'),
			treeFrom: $('#treeFrom'),
			orderForm: $('#orderForm'),
			proForm: $('#proForm')
		};
		//窗口
		var Win = {
			update: $('#update-win')
		};	
		
		//初始化表格
		var initGrid = function(){	
			dataGrid.queryParams = dataGrid.queryParams || {};
			Grid.datagrid2(dataGrid);	
		}		
		
		//初始化表单
		var initForm = function(){			
			if(Form.search && Form.search[0]){
				Form.search.find('#btn-search').off('click').on('click', Evt.search);//查询
				Form.search.find('#btn-reset').off('click').on('click', Evt.reset);//重置
			}			
			if(Form.list && Form.list[0]){
				Form.list.find('#btn-insert').off('click').on('click', Evt.insert);//添加
				Form.list.find('#btn-update').off('click').on('click', Evt.update);//修改
				Form.list.find('#btn-remove').off('click').on('click', Evt.remove);//删除
			}
		};
		
		//初始化窗口
		var initWin = function(){
			if(Win.update && Win.update[0]){
				Win.update.find('#btn-save').off('click').on('click', Evt.save); //保存
				Win.update.find('#btn-close').off('click').on('click', Evt.close);//关闭
			}
		}
		
		//this返回属性		
		this.util = Util;
		this.handler = Handler;		
		this.grid = Grid;
		this.form = Form;
		this.win = Win;
		
		//初始化方法
		this.init = function(){
			initGrid();
			initForm();
			initWin();
		};
		
		return this;
};



//创建一个闭包    
(function($){    
  //表格插件的定义    
  $.fn.datagrid2 = function(){ 
	  //当前插件对象
	  var gird = $(this);
	  if(arguments.length > 0){
		  if(typeof(arguments[0]) == 'object'){
			  //合并插件参数
			  var options = $.extend({}, $.fn.datagrid2.defaults, arguments[0]); 
			  //设置插件分页参数			  
			  if(options.pagination){
				  options.queryParams.pageNum = options.queryParams.pageNum || 1;
				  options.queryParams.pageSize = options.queryParams.pageSize || 10;
			  }
			  //发送分页请求
			  IQB.post(options.url, options.queryParams, function(result){
				  //返回结果集
				  var list = result.iqbResult.result.list;
				  if(list!=null && list.length > 0){
					  var _html = '';		
					  //首行行号
					  var startRowNum = result.iqbResult.result.startRow || 1;
					  var columns = gird.find('thead').find('th');	
					  $.each(list, function(rowIndex, row){
						  _html = _html + '<tr>';
						  var rowNum = startRowNum + rowIndex;						  				
						  $.each(columns, function(colIndex, col){	
							  var html = '', hiddenClass = '';								
							  var isHidden = $(col).attr('hidden') || false;
							  if(isHidden){hiddenClass = 'hidden';}	
							  var property = $(col).attr('field');
							  if(property == 'rn'){
								  var val = rowNum;
								  html = val;
								  var formatter = $(col).attr('formatter');								
								  if(formatter){html = eval(formatter);}	
								  html = '<td class="' + hiddenClass + '">' + html + '</td>';
							  }else if(property == 'ck'){
								  var val = '<input class="datagrid-row-checkbox" type="checkbox" />';
								  html = val;
								  var formatter = $(col).attr('formatter');								
								  if(formatter){html = eval(formatter);}	
								  html = '<td class="' + hiddenClass + '">' + html + '</td>';
							  }else{								
								  var val = row[property];
								  if(val == null){val = '';}
								  html = val;
								  var formatter = $(col).attr('formatter');								
								  if(formatter){html = eval(formatter);}	
								  html = '<td field="' + property + '" class="' + hiddenClass + '"><font class="hidden">' + val + '</font>' + html + '</td>';											
							  }
							  _html = _html + html;
						  });
						  _html = _html + '</tr>';
				  	  });
					  gird.find('tbody').empty().append(_html);	
					  if(options.singleCheck){
						  gird.find('tbody').find('.datagrid-row-checkbox').each(function(i){
							  $(this).off('mousedown').on('mousedown', function(e){
								  if(e.which == 1){
									  gird.datagrid2('uncheckAll');
								  }									  
							  });								  
						  });								 
					  }		
					  //初始化前段分页插件
					  if(options.pagination){
						  var paginationOption = {						
								 totalPages: result.iqbResult.result.pages,
								 currentPage: result.iqbResult.result.pageNum,
								 numberOfPages: 5,
								 itemTexts: function(type, page, current){
									  if(type == 'first'){return '首页';}else if(type == 'prev'){return '上一页';}else if(type == 'next'){return '下一页';}else if(type == 'last'){return '尾页';}else{return page;}
								 },
								 itemContainerClass: function(type, page, current) {
									 return (page === current) ? 'active' : 'pointer-cursor';		            	
								 },
								 onPageClicked: function(e, originalEvent, type, page){
									 e.stopImmediatePropagation();
									 options.queryParams.pageNum = page;
									 gird.datagrid2(options);
									 if($.isFunction(options.onPageChanged)){
										 options.onPageChanged(page);
									 }
								 }		          								
						  };				  
						  $('#' + options.paginator).bootstrapPaginator(paginationOption);
						  $('#' + options.paginator).removeClass('pagination').find('ul').addClass('pagination pagination-sm');
					  }
				  }else{
					  if(result.iqbResult.result.total){
						  options.queryParams.pageNum = options.queryParams.pageNum - 1;
						  gird.datagrid2(options);
					  }else{
						  gird.find('tbody').empty();
						  $('#' + options.paginator).empty();
					  }
				  }
		  	  });			
		  }else if(typeof(arguments[0]) == 'string'){
			  if(arguments[0] == 'getIdField'){
				  var idKey = 'id';
				  gird.find('thead').find('tr').find('th').each(function(i){
					  var isIdField = $(this).attr('idField') || false;
					  if(isIdField){
						  idKey = $(this).attr('field');
					  }
				  });
				  return idKey;
			  }else if(arguments[0] == 'getRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  var row = {};
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('font').each(function(i){
						  var property = $(this).parent().attr('field');
						  var val = $(this).text();
						  row[property] = val;		
					  }); 
					  return row;
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'getCheckedRows'){
				  var arry = new Array();	
				  var trs = gird.find('tbody').find('tr');
				  $.each(trs, function(i, m){
					  if($(m).find('.datagrid-row-checkbox').prop('checked')){
						  var fonts = $(m).find('font');
						  var row = {};
				    	  $.each(fonts, function(j, n){
				    			var property = $(n).parent().attr('field');
				    			var val = $(n).text();
				    			row[property] = val;						
						  });		    		
				    	  arry.push(row);
					  }; 
				  });	
				  return arry;
			  }else if(arguments[0] == 'checkRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('.datagrid-row-checkbox').prop('checked', true); 
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'checkAll'){
				  gird.find('tbody').find('tr').each(function(i){
					  $(this).find('.datagrid-row-checkbox').prop('checked', true); 
				  }); 				 
			  }else if(arguments[0] == 'uncheckRow'){
				  if(typeof(arguments[1]) == 'number'){
					  var rowIndex = arguments[1];
					  rowIndex ++;
					  gird.find('tbody').find('tr:nth-child(' + rowIndex + ')').find('.datagrid-row-checkbox').prop('checked', false); 
				  }else{
					  console.error('参数类型错误');
				  }
			  }else if(arguments[0] == 'uncheckAll'){
				  gird.find('tbody').find('tr').each(function(i){
					  $(this).find('.datagrid-row-checkbox').prop('checked', false); 
				  }); 
			  }//继续扩展	  
		  }
	  }else{
		  console.error('参数类型错误');
	  }  
  };  
  //表格插件默认参数    
  $.fn.datagrid2.defaults = {    
		  queryParams: {},//分页参数
		  singleCheck: false,//是否单选,默认多选
		  pagination: true,//是否分页,默认是
		  paginator: 'paginator',//前端分页插件容器
		  onPageChanged: null
  };    
//闭包结束    
})(jQuery);     