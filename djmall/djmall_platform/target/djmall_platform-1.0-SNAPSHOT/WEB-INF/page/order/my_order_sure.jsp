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

        <input type="hidden" id="orderIds"  name="orderIds" value="${ids}"/><%--购物车的ids--%>
        <input type="hidden" id="idsInteger"  name="idsInteger" value="${idsInteger}"/>


        <input  type="hidden"  value="search" onclick="search()" />
        <input  type="hidden" id="token"  value="token" name="token" />
        <input  type="hidden" id="ids"   name="ids" />
        <input  type="hidden"    name="pNum" value="${pNum}" />

<br><br>
<a href="javascript:reba()"  style="color: blue"> 返回首页</a>
<br><br>
    收货人地址:
    <select name="address" id="address">
        <c:forEach items="${addressList}" var="address">
            <option value="${address.addressName}-${address.phone}-${address.address}">${address.addressName}-${address.phone}-${address.address}</option>
        </c:forEach>
    </select>
    <a href="javascript:addAddre()"  style="color: blue"> 没有地址？ 去添加</a>
<br><br>
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

    /*返回主页*/
    function reba() {
        window.location.href="<%=request.getContextPath() %>/user/toIndex";
    }


    $(function () {
        search();
    })

    function search() {
        $("#token").val(cookie.get("TOKEN"));
        var ids = $("#orderIds").val();
        $("#ids").val(ids);
        token_post(
            "<%=request.getContextPath()%>/user/myOrderSureList",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i <data.data.list.length ;   i++) {
                    var pro = data.data.list[i];
                    html += "<fieldset  style='width:600px; height: 150px '>";
                    html += "<legend > 商品详情</legend>";
                    html += "Id ： " + pro.id + "  ";
                    html += "<br>"
                    html += "名称 ： "+pro.productName+"  ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "原价 ：￥ "+pro.oldPrice+"  ";
                    html += "<br><br>"
                    html += "SKU ： "+pro.proSku+"  ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        if(pro.proRate == 0){
                         html += "折扣 ： 无， 按照原价  ";
                        } else {
                          html += "折扣 ： "+pro.proRate+" % ";
                        }
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "数量 X"+pro.num+"";
                    html += "<br><br>";
                     if(pro.proFreight == 0){
                         html += "邮费 ： 包邮  ";
                     } else {
                          html += "邮费 ：平邮 "+pro.proFreight+" 元 ";
                     }
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "现价 ：￥ "+pro.newPrice+"  ";


                    html += "</fieldset>";

                }
                $("#div").html(html);

            }
        );
    }





    /*检查库存  价格*/
    $(function () {
        var ids = $("#orderIds").val();
        $("#ids").val(ids);
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/user/checkCountAndMoney",
            $("#fm").serialize(),
            function (data){
                if(data.code == 200){
                    /* layer.msg(data.msg, {icon: 6},function () {*/
                    var html = "已选择 "+data.data.number+" 件商品， 总商品金额：￥"+data.data.totalMoney+" ,  运费：￥"+data.data.totalFreight+"  ";
                    html += "<br>";
                    html += "应付金额：￥"+data.data.finalMoney+" ";
                    $("#moneyDiv").html(html);


                    layer.close(loading);

                } else {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 5})
                }

            }
        )
    })




    /*确认订单*/
    function sub() {
        var address = $("#address").val();
        if(address == null){
            layer.msg("请填写有效地址",{icon: 5,time:500});
            return;
        }
        $("#token").val(cookie.get("TOKEN"));
        var ids = $("#orderIds").val();
        $("#ids").val(ids);
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/order/sureOrder",
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
        window.location.href = "<%=request.getContextPath()%>/user/toIndex?token="+cookie.get("TOKEN");
    }







</script>
</html>
