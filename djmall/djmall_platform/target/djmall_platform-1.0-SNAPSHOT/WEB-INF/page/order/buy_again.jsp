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
<form id="fm" >

    <input  type="hidden"    name="orderSonNum" value="${orderSonNum}" />
    <input  type="hidden"    name="id" value="${order.id}" />
    <input  type="hidden"    name="orderSonStatus" value="2" />
    <input  type="hidden" id="token"  value="token" name="token" />

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
        <fieldset  style="width:450px; height: 100px ">
            <legend > 商品详情</legend>
            名称:${order.productName}       原价:  ￥${order.originalPrice}    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  数量：X${order.proNum}     <br> <br>
            折扣:  <c:if test="${order.rate ==  0}"  > 无，按照原价</c:if> <c:if test="${order.rate != 0}"  > ${order.rate}%</c:if>
            邮费： <c:if test="${order.freightMoney == 0}"  > 包邮</c:if>           <c:if test="${order.freightMoney != 0}"  > ${order.freightMoney}元</c:if>   <br> <br>

        </fieldset>
    <br>
    共计${size}件商品
    商品总金额 ：￥${order.allMoney}    邮费总金额： <c:if test="${order.totalFreight == 0}"  > 包邮</c:if>           <c:if test="${order.totalFreight != 0}"  > ${order.totalFreight}元</c:if>
    实付金额 ：￥ ${order.totalMoney}


    <br><br>
    <center>
        支付方式:
        <select name="payStatus">
            <option value="货到付款">货到付款</option>
            <option value="支付宝">支付宝</option>
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
        token_post("<%=request.getContextPath()%>/order/buyAgain",
            $("#fm").serialize(),
            function (data){
                if(data.code == 200){
                    layer.msg(data.msg, {
                        icon: 1,shade: 0.01,time: 500
                    }, function(){
                        window.layer.close(loading);
                        JavaScript:parent.window.location.href = "<%=request.getContextPath()%>/toIndex?token="+cookie.get("TOKEN");
                    });
                } else {
                    layer.close(loading);
                    layer.msg(data.msg,{icon: 5,time:500});
                }

            }
        )

    }

    /*没有地址？ 去添加*/
    function addAddre() {
        window.location.href = "<%=request.getContextPath()%>/address/addAddress?token="+cookie.get("TOKEN");
    }
    /*取消订单 回到我的订单*/
    function reback() {
        JavaScript:parent.window.location.href = "<%=request.getContextPath()%>/toIndex?token="+cookie.get("TOKEN");
    }







</script>
</html>
