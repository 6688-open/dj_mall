<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/4/1
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
</head>
<body>
订单编号：${order.parentNum} <br><br>
收货信息：${order.address} <br>
商品信息：<br><br>
<table  border="1px" align="center" cellspacing="0px">
    <tr>
        <td>编号</td>
        <td>商品信息</td>
        <td>数量</td>
        <td>￥实际金额</td>
        <td>折扣%</td>
    </tr>
    <c:forEach items="${orderList}" var="o">

        <tr>
            <td>${o.bianhao}</td>
            <td>${o.productName}${o.skuAttrName}</td>
            <td>${o.proNum}</td>
            <td>￥${o.actualPrice}</td>
            <td>${o.rate}%</td>
        </tr>
    </c:forEach>
</table><br>
下单时间：${order.deliveryTimeShow}<span id="span"></span> <br>
支付方式：${order.payStatus} 支付<br>
<%--支付时间：${order.payTimeShow} <br>--%>
商品总金额：${order.allMoney} <br>
运费：￥${order.totalFreight} <br>
实付总金额：${order.totalMoney} <br>

</body>
<script>

</script>
</html>
