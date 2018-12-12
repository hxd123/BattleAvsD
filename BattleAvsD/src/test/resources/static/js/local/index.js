/**
 * 
 */
$(function(){
	
	initFun();
	
	var logoutUrl = '/shop/user/logout';

	//注销登录
	logoutSys = function(){
		 $.getJSON(logoutUrl,function(data){
			 if(data.success){
				 delCookie('USER_TOKEN');
				 layer.msg("注销成功！");
				 top.location = "/shop/page/login";
			 }else{
				 layer.msg("注销失败！");
			 }
		})
	}
	
	function openLayer(btn,cont,status){
		//示范一个公告层
		layer.open({
		  type: 1
		  ,title: false //不显示标题栏
		  ,closeBtn: false
		  ,area: '350px;'
		  ,shade: 0.8
		  ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		  ,resize: false
		  ,btn: btn
		  ,btnAlign: 'c'
		  ,moveType: 1 //拖拽模式，0或者1
		  ,content: cont
		  ,success: function(layero){
		    var btn = layero.find('.layui-layer-btn');
		    if(status == 1){
		    	btn.find('.layui-layer-btn0').attr({
			      href: '/shop/page/orderpage'
			      ,target: 'myFrameName'
			    });
		    }
		  }
		});
	}
	
	function initFun(){
		$.ajax({
			contentType: 'application/json;charset=UTF-8',
			url:'/shop/order/orderinfo',
			type: 'GET',
			dataType: 'JSON',
			success: function(data){
				handleOrderOrGoods(data);
			}
		})
	}
	
	function handleOrderOrGoods(data){
		if(data.success){
			var btnStr = new Array();
			btnStr.push('查看订单','关闭窗口');
			var contentStr = '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">'+
				'（1）存在订单信息，请检查!</div>';
			openLayer(btnStr,contentStr,1);
		}
	}
})
