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

        <input  type="hidden"    name="pNum" value="${pNum}" />
        <input  type="hidden"    name="ids" value="${order.id}" />
    <input  type="hidden" id="token"  value="token" name="token" />



    <input type="hidden" name="freightMoney"  value="${productMsg.freight}"  />
    <input type="hidden" name="originalPrice" value="${productMsg.priceShow}"  />
    <input type="hidden" name="rate" value="${productMsg.rateShow}"  />
    <input type="hidden" id="skuAttrName" name="skuAttrName" value="${productMsg.skuAttrValueNames}"  />
    <input type="hidden" name="orderSonStatus" value="1"  />
    <input type="hidden" name="proSkuId" value="${proSkuId}"  />
    <input type="text" name="num" value="${num}"  />
    <input type="text" name="productName" value="${productMsg.productName}"  />


<br><br>
<a href="javascript:reback()"  style="color: blue"> 返回首页</a>
<br><br>
    收货人地址:
    <select name="address" id="address">
        <c:forEach items="${addressList}" var="address">
            <option value="${address.addressName}-${address.phone}-${address.address}">${address.addressName}-${address.phone}-${address.address}</option>
        </c:forEach>
    </select>
    <a href="javascript:addAddre()"  style="color: blue"> 没有地址？ 去添加</a>
<br><br>
    <fieldset  style="width:450px; height: 150px ">
        <legend > 商品详情</legend>
        名称:${productMsg.productName}       原价:  ￥${productMsg.priceShow}       <br> <br>
        折扣:  <c:if test="${productMsg.rateShow ==  0}"  > 无，按照原价</c:if> <c:if test="${productMsg.rateShow != 0}"  > ${productMsg.rateShow}%</c:if>
        邮费： <c:if test="${productMsg.freight == 0}"  > 包邮</c:if>           <c:if test="${productMsg.freight != 0}"  > ${productMsg.freight}元</c:if>    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    数量 ： X${num}     <br> <br>
        商品描述:${productMsg.productDescribe}               <br>   <br>
        点赞量: <span style="font-size: x-large; color: red">${productMsg.thumbNumber}</span>
        评论量:<span style="font-size: x-large; color: red">${total}</span>    <br>   <br>

    </fieldset>
    <br>
    商品金额 ： <c:if test="${productMsg.rateShow ==  0}" >￥${productMsg.priceShow*num} </c:if>  <c:if test="${productMsg.rateShow !=  0}" >￥${productMsg.priceShow*num*productMsg.rateShow/100} </c:if>
    邮费金额： <c:if test="${productMsg.freight == 0}"  > 包邮</c:if>           <c:if test="${productMsg.freight != 0}"  > ${productMsg.freight}元</c:if>
    实付金额 ：<c:if test="${productMsg.rateShow ==  0}" >￥${productMsg.priceShow*num+productMsg.freight} </c:if>      <c:if test="${productMsg.rateShow !=  0}" >￥${productMsg.priceShow*num*productMsg.rateShow/100 + productMsg.freight} </c:if>

    <div id="div"></div> <br><br>
<div id="moneyDiv" align="center"></div>
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
        var address = $("#address").val();
        if(address == null){
            layer.msg("请填写有效地址",{icon: 5,time:500});
            return;
        }
        $("#token").val(cookie.get("TOKEN"));
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/order/buyNow",
            $("#fm").serialize(),
            function (data){
                layer.close(loading);
                layer.msg(data,{icon: 6,time:500});
                $("#aliPayDiv").append(data);
               /* layer.msg(data, {
                    icon: 1,shade: 0.01,time: 500
                }, function(){
                    window.layer.close(loading);

                });*/
             /*   if(data.code == 200){
                    layer.msg(data.msg, {
                        icon: 1,shade: 0.01,time: 500
                    }, function(){
                        window.layer.close(loading);
                        window.location.href = "<%=request.getContextPath()%>/toIndex?token="+cookie.get("TOKEN");
                    });
                } else {
                    layer.close(loading);
                    layer.msg(data.msg,{icon: 5,time:500});
                }*/

                }
        )

    }

    /*没有地址？ 去添加*/
    function addAddre() {
        window.location.href = "<%=request.getContextPath()%>/address/addAddress?token="+cookie.get("TOKEN");
    }
    /*取消订单 回到首页*/
    function reback() {
        window.location.href = "<%=request.getContextPath()%>/user/toIndex?token="+cookie.get("TOKEN");
    }







</script>
</html>
