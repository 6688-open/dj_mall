<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/res/jquery-1.12.4.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/res/zTree_v3/css/demo.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/res/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/res/zTree_v3/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
<script src="<%=request.getContextPath()%>/res/cookie.js"></script>
<script src="<%=request.getContextPath()%>/res/token.js"></script>
	<script type="text/javascript">
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			}
		};

		var zNodes =[
           /* { id:0, pId:0, name:"djmall", open:true},
			{ id:1, pId:0, name:"运营平台", open:true},
			{ id:121, pId:1, name:"用户管理", url:"<%=request.getContextPath()%>/user/toList", target:"right"},
			{ id:111, pId:1, name:"首页", open:true},
			{ id:1111, pId:111, name:"基础管理" , open:true},
			{ id:11111, pId:1111, name:"运费", url:"<%=request.getContextPath()%>/log/toList", target:"right"},
			{ id:11111, pId:1111, name:"字典数据", url:"<%=request.getContextPath()%>/dictionary/toList", target:"right"},
			{ id:11112, pId:1111, name:"商品属性维护", url:"<%=request.getContextPath()%>/sku/toList", target:"right" },
			{ id:11113, pId:1111, name:"通用SKU维护",  url:"<%=request.getContextPath()%>/sku/toSkuList", target:"right" },
			{ id:1112, pId:111, name:"订单展示"},
			{ id:1112, pId:111, name:"商品展示页面", url:"<%=request.getContextPath()%>/product/toList", target:"right" },*/



           { id:2, pId:0, name:"商城", open:true},
            { id:22, pId:2, name:"首页",  open:true},
            { id:333, pId:22, name:"商品详情"},
            { id:333, pId:22, name:"购物车"},
            { id:333, pId:22, name:"确认订单"},
            { id:3333, pId:222, name:"个人信息" , open:true},
            { id:33334, pId:222, name:"收货地址"},
            { id:4444, pId:33334, name:"添加收货地址"},
            { id:33335, pId:222, name:"我的订单"},
            { id:55555, pId:33335, name:"去评价"},
            { id:55555, pId:33335, name:"订单详情"},
			{ id:222, pId:2, name:"个人中心" , open:true},
		];

		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});









	</script>
</head>
<body>
	<%--<div id="treeDemo" class="ztree" ></div>--%>

	<input type="hidden" id="token" value="${token}"/>
	<center>
		<br><br>
		<h1>个人信息</h1>
		<table  border="1px" align="center" cellspacing="0px">
			<tr>
				<td>
                    <a href="<%=request.getContextPath()%>/user/toUpdateMsg?token=${token}" target="right" >个人信息</a>
				</td>
			</tr>
			<tr>
				<td>
                    <a href="<%=request.getContextPath()%>/address/toList?token=${token}" target="right" >收货地址</a>
				</td>
			</tr>
			<tr>
				<td>
                    <a href="<%=request.getContextPath()%>/order/toOrderList?token=${token}" target="right" >我的订单</a>
				</td>
			</tr>
		</table>
	</center>
</body>

</html>   




		