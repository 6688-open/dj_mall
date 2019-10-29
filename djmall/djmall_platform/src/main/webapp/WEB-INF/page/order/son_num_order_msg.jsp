<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/4/1
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
订单编号：${order.orderSonNum} <br><br>
收货信息：<c:if test="${order.address != null}">${order.address}</c:if>  <c:if test="${order.address == null}">暂无信息</c:if> <br><br>
商品信息：
<table  border="1px" align="center" cellspacing="0px">
    <tr>
        <td>编号</td>
        <td>商品信息</td>
        <td>数量</td>
        <td>￥实际金额</td>
        <td>折扣%</td>
    </tr>
        <tr>
            <td>1</td>
            <td>${order.productName}${order.skuAttrName}</td>
            <td>${order.proNum}</td>
            <td>￥${order.actualPrice}</td>
            <td>${order.rate}%</td>
        </tr>
</table><br>
下单时间：${order.deliveryTimeShow}<span id="span"></span> <br><br>
支付方式：<c:if test="${order.payStatus != null}">${order.payStatus}</c:if>  <c:if test="${order.payStatus == null}">暂无信息</c:if><br>
支付时间：<c:if test="${order.payTimeShow != null}">${order.payTimeShow}</c:if>  <c:if test="${order.payTimeShow == null}">暂无信息</c:if><br>
商品总金额：${order.actualPrice} <br>
运费：￥${order.freightMoney} <br>
实付总金额：${order.totalMoney} <br>
</body>
</html>
