/**
 * 
 */
$(function(){

	var checkingUrl = '/shop/output/getlistbytime';
	//页面一打开就执行，放入ready是为了layer所需配件（css、扩展模块）加载完毕
	$('#btnPrapre').on('click',function(){ 
		var createTime = $('#createTime').val();
		var income = $('#incomeMoney').val();
		if(createTime == null || createTime == ''){
			$('#createTime').focus();
			layer.msg("该选项不能为空！");
			return;
		}
		if(income == null || income == ''){
			$('#incomeMoney').focus();
			layer.msg("该选项不能为空！");
			return;
		}
		$.ajax({
			url: checkingUrl,
			type : 'POST',
			dataType: 'json',
			data : {
				createTime : createTime,
				income : income
			},
			success: function(data){
				if(data.success){
					layer.msg('账本无异常');
				}else{
					if(data.code == 0){
						layer.msg(data.errMsg, function(){
							showExceptionTable();
							$('#checkingShow').css('display','');
						});
					}else if(data.code == 400){
						layer.msg('不存在该天消费信息！');
					}
				}
			}
		})
	});
	
	function showExceptionTable(){
		layui.use(['table'], function(){
			  var table = layui.table;
			  
			//创建tables表
			  table.render({
				    elem: '#userTable'
				    ,height: 460
				    ,url: '/shop/output/getlist' //数据接口
				    ,page: true //开启分页
				    ,cols: [[ //表头
				       {field: 'outputId', title: '出库ID', width:100, sort: true}
				      ,{field: 'goods', title: '商品名', width:100,templet:'<div>{{d.goods.fname}}</div>'}
				      ,{field: 'goods', title: '条形码', width:180,templet:'<div>{{d.goods.barcode}}</div>'}
				      ,{field: 'quantity', title: '数量', width:80}
				      ,{field: 'amountPrice', title: '销售金额', width:80}
				      ,{field: 'salePrice', title: '实销金额', width:80}
				      ,{field: 'subtotals', title: '数量小计', width:100}
				      ,{field: 'grossProfit', title: '毛利', width:100}
				      ,{field: 'customerNum', title: '客单数', width:100}
				      ,{field: 'retNum', title: '退货数量', width:100}
				      ,{field: 'createTime', title: '创建时间', width:202,templet:'<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm")}}</div	>'}
				    ]]
			  		,where :{
			  			createTime: $('#createTime').val()
			  		}
			});
		})
	};

})
