<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/3/28
  Time: 21:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/md5-min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jsencrypt/jsencrypt.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
    <script src="<%=request.getContextPath()%>/res/cookie.js"></script>
    <script src="<%=request.getContextPath()%>/res/token.js"></script>
    <script src="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/layui.js"></script>
    <link href="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/css/layui.css" rel="stylesheet" />
</head>
<body>
<div id="aliPayDiv"></div>
<form id="fm" >

    <input  type="text"    name="parentNum" value="${parentNum}" />
    <input  type="hidden" id="token"  value="token" name="token" />
    <input  type="hidden" id="orderSonStatus"  name="orderSonStatus" value="2" />
    <input  type="hidden"   name="message" value="1" />

    <br><br>
    <a href="javascript:reback()"  style="color: blue"> 返回首页</a>
    <br><br>
    收货人地址:
    <select name="address">
        <c:forEach items="${addressList}" var="address">
            <option value="${address.addressName}-${address.phone}-${address.address}">${address.addressName}-${address.phone}-${address.address}</option>
        </c:forEach>
    </select>
    <a href="javascript:addAddre()"  style="color: blue"> 没有地址？ 去添加</a>
    <br><br>
    <c:forEach items="${orderList}" var="o">
        <fieldset  style="width:450px; height: 100px ">
            <legend > 商品详情</legend>
            名称:${o.productName}       原价:  ￥${o.originalPrice}    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  数量：X${o.proNum}     <br> <br>
            折扣:  <c:if test="${o.rate ==  0}"  > 无，按照原价</c:if> <c:if test="${o.rate != 0}"  > ${o.rate}%</c:if>
            邮费： <c:if test="${o.freightMoney == 0}"  > 包邮</c:if>           <c:if test="${o.freightMoney != 0}"  > ${o.freightMoney}元</c:if>   <br> <br>

        </fieldset>
    </c:forEach>
    <br>
    共计${size}件商品
    商品总金额 ：￥${order.allMoney}    邮费总金额： <c:if test="${order.totalFreight == 0}"  > 包邮</c:if>           <c:if test="${order.totalFreight != 0}"  > ${order.totalFreight}元</c:if>
    实付金额 ：￥ ${order.totalMoney}


    <br><br>
    <center>
        支付方式:
        <select name="payStatus">
            <option value="货到付款">货到付款</option>
            <option value="支付宝" selected>支付宝</option>
            <option value="微信">微信</option>
        </select>
    </center>

</form>
<br><br>
<center>

    <input type="button" onclick="sub()" value="确认订单" />
    <input type="button" onclick="reback()" value="取消订单" />
</center>

</form>
</body>
<script>






    function sub() {
        $("#token").val(cookie.get("TOKEN"));
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/order/payOrder",
            $("#fm").serialize(),
            function (data){
                layer.close(loading);
                layer.msg(data,{icon: 6,time:500});
                $("#aliPayDiv").append(data);


            }
        )

    }

    /*没有地址？ 去添加*/
    function addAddre() {
        window.location.href = "<%=request.getContextPath()%>/address/addAddress?token="+cookie.get("TOKEN");
    }
    /*取消订单 回到首页*/
    function reback() {
        JavaScript:parent.window.location.href = "<%=request.getContextPath()%>/user/toIndex?token="+cookie.get("TOKEN");
    }







</script>
</html>
