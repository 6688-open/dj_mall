<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/css/layui.css"  media="all">
<script type="text/javascript" src = "<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src = "<%=request.getContextPath()%>/res/layer/layer.js"></script>
<script type="text/javascript" src = "<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/layui.js"></script>
</head>

<body>
	<br>

	<div align="right" style="border: 1px  ; width:900px ; height:700px; margin-left: 50px">
	<form id="fmt">
		<input type="text" name="lever"  id="lever" value="${user1.lever}"/>
		<input type="text" name="sellerId"  value="${user1.id}"/>

	</form>


	<h2 align="center">订单列表</h2>

		<div align="left">
			<input  type="button" value="导出订单" onclick="mysqlToExl()"/>
		</div>


	<br>
	<table border="1px" align="center" cellspacing="0px">
		<tr >
			<td>订单号</td>
			<td>商品名称</td>
			<td>购买数量</td>
			<td>折扣</td>
			<td>付款金额（包含邮费）</td>
			<td>支付方式</td>
			<td>邮费</td>
			<td>收货人信息</td>
			<td>下单人</td>
			<td>下单人电话</td>
			<td>下单时间</td>
			<td>支付时间</td>
			<%-- 买家才有的展示--%>
			<c:if test="${user1.lever == 1}">
				<td>买家消息</td>
			</c:if>
			<td>订单状态</td>
		</tr>
		<tbody id = "tbody"></tbody>
	</table>
	<div align="center" id = "pagediv"></div>
	</div>
</body>
<script type="text/javascript">


	$(function () {
		search();
	});
	
	function search() {
		$.post(
				"<%=request.getContextPath()%>/user/orderList",
				$("#fmt").serialize(),
				function (data) {
					var html = "";
					for (var i = 0; i < data.data.list.length; i++) {
						var order = data.data.list[i];
						html += "<tr>";
						html += "<td>"+order.orderSonNum+"</td>";
						html += "<td>"+order.productName+"</td>";
						html += "<td>"+order.proNum+"个</td>";
						if(order.rate == 0){
							html += "<td style='color: red'>暂无折扣</td>";
						}else{
							html += "<td>"+order.rate+"%</td>";
						}

						html += "<td>"+order.totalMoney+"元</td>";
						if(order.payStatus == null){
							html += "<td style='color: red'>暂无信息</td>";
						}else{
							html += "<td>"+order.payStatus+"</td>";
						}
						if(order.freightMoney == 0){
							html += "<td>包邮</td>";
						} else {
							html += "<td> 平邮 "+order.freightMoney+"元</td>";
						}
						if(order.address == null){
							html += "<td style='color: red'>暂无收货信息</td>";
						}else{
							html += "<td>"+order.address+"</td>";
						}
						html += "<td>"+order.username+"</td>";
						html += "<td>"+order.phone+"</td>";
						html += "<td>"+order.deliveryTime+"</td>";
						if(order.payTime == null){
							html += "<td style='color: red'>暂无支付信息</td>";
						}else{
							html += "<td>"+order.payTime+"</td>";
						}
						if(order.message == 2 && $("#lever").val() ==1 ){
							html += "<td>";
								html += "买家提醒赶紧发货了，";
								html += "<a href='javascript:iKonw(\""+order.orderSonNum+"\")' style='color: blue' >我知道了</a> ";
							html +=  "</td>";
						}
						if(order.message != 2 && $("#lever").val() ==1){
							html += "<td>无消息处理</td>";
						}


						if(order.orderSonStatus == 1){
							html += "<td>待支付</td>";
						}
						if(order.orderSonStatus == 2 && order.message != 3){
							html += "<td>";
							html += "待发货";
							if(  $("#lever").val() ==1){
								html += "<a href='javascript:fahuo(\""+order.orderSonNum+"\")' style='color: blue' >去发货</a> ";
							}
							html +=  "</td>";
						}
						if(order.orderSonStatus == 2 && order.message == 3){
							html += "<td>待收获</td>";
						}
						if(order.orderSonStatus == 3 && order.message == 3){
							html += "<td>已完成</td>";
						}
						if(order.orderSonStatus == 4){
							html += "<td>已取消</td>";
						}






						/*html += "<td>";
						html += "<a href='javascript:toPay(\""+order.parentNum+"\")' style='color: blue' >去支付</a> ";
						html += "<a href='javascript:removeNum(\""+order.parentNum+"\")' style='color: deeppink' >取消订单</a> ";
						html +=  "</td>";*/
						html += "</tr>";
						
					}
					$("#tbody").html(html);
					

                   /*  var pagehtml = "";
					 pagehtml += "当前"+(data.data.pageNo)+"页";
		    		 pagehtml += "<input type='button' value='上一页' onclick='page("+(eval(data.data.pageNo)-1)+" , "+data.data.totalPage+"  )' />";
		    		 pagehtml += "<input type='button' value='下一页' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
		    		 pagehtml += "总"+(data.data.totalPage)+"页";  
		    		 $("#pagediv").html(pagehtml);*/
				}
			);}
	/*分页*/
	function page(pageNo,totalPage) {
		$("#pageNo").val(pageNo);
		if(pageNo < 1){
			layer.msg('已经是第一页了', {time: 500, icon:6});
			return;
		}
		if(pageNo > totalPage ){
			layer.msg('已经是第一页了', {time: 500, icon:5});
			return;
		}
		search();
	}

	/*我知道了*/
	function iKonw(orderSonNum) {
			layer.confirm('确认发货？', {
				btn: ['确认', '取消'],
				btn1: function(index, layero){
					fahuo(orderSonNum);
				},
				btn2:function(index, layero){
					return;
				}
			})

	}



	/*卖家 去发货  3 */
	function fahuo(orderSonNum) {
		var loading=layer.load();
		$.post("<%=request.getContextPath()%>/user/fahuoMsg?orderSonNum="+orderSonNum,
				{"message":3},
				function (data){
					if(data.code == 200){
						layer.msg("发货成功", {icon: 6},function () {
							layer.close(loading);
							window.location.href = "<%=request.getContextPath()%>/user/toOrderList";

						})
						return
					}
					layer.close(loading);
					layer.msg(data.msg, {icon: 5});
				}
		)
	}



	/*导出订单*/
	function mysqlToExl() {
		window.location.href = "<%=request.getContextPath()%>/product/exportOrder";
	}















</script>
</html>