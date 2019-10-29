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
<script src="<%=request.getContextPath()%>/res/cookie.js"></script>
<script src="<%=request.getContextPath()%>/res/token.js"></script>
</head>

<body>
	<br>
    <input type="hidden" id="userId" value="${user1.id}"/><%--取session  自己登陆时 只能看到自己的评论--%>
    <input type="button" value="返回首页"  style="color:red" onclick="exit()"/><br><br><br>
<h1></h1>

    <form id="fm">
    <fieldset  style="width:400px; height: 370px ">
        <legend > 商品详情</legend>
        名称:${productMsg.productName}       原价:  ￥${productMsg.priceShow}       <br> <br>
        折扣:  <c:if test="${productMsg.rateShow ==  0}"  > 无，按照原价</c:if> <c:if test="${productMsg.rateShow != 0}"  > ${productMsg.rateShow}%</c:if>
        邮费： <c:if test="${productMsg.freight == 0}"  > 包邮</c:if>           <c:if test="${productMsg.freight != 0}"  > ${productMsg.freight}元</c:if>   <br> <br>
        商品描述:${productMsg.productDescribe}               <br>   <br>
        点赞量: <span style="font-size: x-large; color: red">${productMsg.thumbNumber}</span>
        评论量:<span style="font-size: x-large; color: red">${total}</span>    <br>   <br>
        选择商品信息：
        <c:forEach items="${skuProductNamesById}" var="skuProduct">
            <input type="radio" id="radio"  onclick="selOnclick(${skuProduct.id})" name="skuAttrValueNames"  <c:if test="${skuProduct.id == productMsg.proSkuId }">checked</c:if>    value="${skuProduct.skuAttrValueNames}"/>${skuProduct.skuAttrValueNames}
        </c:forEach> <br>   <br>
        图片： <img src="http://${productMsg.picture}" style="width: 50px" height="50px"/><br> <br>

        数量
        <input type='button'  value='-' onclick='del()' />
        <input type='text'  name='num' id="num"  size='3px' value='1'/>
        <input type='button'  value='+' onclick='add()' /><br> <br>


        <input type="hidden" id="id" value="${productMsg.id}"/>
        <input type="hidden" name="productName"  value="${productMsg.productName}"/>
        <input type="hidden" name="oldPrice"  value="${productMsg.priceShow}"/>
        <input type="hidden" name="proRate"  value="${productMsg.rateShow}"/>
        <input type="hidden" name="proFreight"  value="${productMsg.freight}"/>
        <input type="hidden" name="proSku" id="proSku"  value="${skuProduct.skuAttrValueNames}"/>
        <input type="hidden" name="proSkuId" id="proSkuId"  value="${productMsg.proSkuId}" />
        <input type="hidden" id="token"  name="token" />

        <%--立即购买--%>
        <input type="hidden" name="freightMoney"  value="${productMsg.freight}"  />
        <input type="hidden" name="originalPrice" value="${productMsg.priceShow}"  />
        <input type="hidden" name="rate" value="${productMsg.rateShow}"  />
        <input type="hidden" id="skuAttrName" name="skuAttrName" value="${skuProduct.skuAttrValueNames}"  />
        <input type="hidden" name="orderSonStatus" value="1"  />


        <input type="button" onclick="jionCar()" value="加入购物车"/> ----- <input type="button" onclick="buyNow()" value="立即购买"/>
    </fieldset>

    <fieldset style="width:600px; height: 100px ">
        <legend > 商品评论</legend>
        好评率:<h1 style="color: red"><span  id="h1">${goodRate}%</span> </h1>
        <input  type="radio" value="1" name="score"  onclick="toScore()"  checked="checked"/>所有评论
        <input  type="radio" value="2" name="score" onclick="toScore()"  />好评
        <input  type="radio" value="3" name="score"  onclick="toScore()")/>中评
        <input  type="radio" value="4" name="score"  onclick="toScore()" />差评
    </fieldset>

        <div id="tb"></div>
        <div id="pagediv"></div>

        <input type="hidden" value="${productId}" id="productId" name="productId"/>
        <input type="hidden" value="1" id="pageNo" name="pageNo"/>
        <input type="hidden" name="search" onclick="search()"/>
    </form>
</body>
<script type="text/javascript">
    /*返回首页*/
    function exit(){
        layer.confirm('是否返回首页？', {
            btn: ['YES', 'NO'],
            btn1: function(index, layero){
                JavaScript:parent.window.location.href="<%=request.getContextPath() %>/user/toIndex";
            },
            btn2:function(index, layero){
                return;
            }
        })
    }

    /*勾选不同的sku  展示不同的价格*/
    function selOnclick(skuId) {
        var id = $("#id").val();
        window.location.href="<%=request.getContextPath() %>/user/toProductMessage?skuId="+skuId+"&id="+id;
    }

    /*加入购物车*/
    function jionCar() {
        if($("#num").val() < 1){
            layer.msg("只能等于1 或者大于0不能出现负数", {icon: 5,time:1000});
            return true;
        }
        if(isNaN($("#num").val())){
            layer.msg("请输入纯数字", {icon: 5,time:1000});
            return false;
        }
        $("#token").val(cookie.get("TOKEN"));
        $("#proSku").val(  $("#radio").val(   )  );
        var index1 = layer.load();
       token_post(
            "<%=request.getContextPath()%>/user/joinShoppingCar",
            $("#fm").serialize(),
            function(data){
                if (data.code == 200) {
                    layer.msg(data.msg, {icon: 6,time:1000},function(){
                      /*  var index = parent.layer.getFrameIndex(window.name);*/
                        window.layer.close(index1);
                        layer.confirm('是否进入购物车？', {
                            btn: ['YES', 'NO'],
                            btn1: function(index, layero){
                                window.location.href="<%=request.getContextPath() %>/user/myShoppingCar?token="+cookie.get("TOKEN")+"&proCarId="+data.data.proCarId;
                            },
                            btn2:function(index, layero){
                                return;
                            }
                        })

                    });
                    return;
                }
                layer.close(index1);
                layer.msg(data.msg, {icon: 5,time:1000});

            }
        );

    }


    /*立即购买    查看库存*/
    function buyNow() {
        if($("#num").val() < 1){
            layer.msg("只能等于1 或者大于0不能出现负数", {icon: 5,time:1000});
            return true;
        }
        if(isNaN($("#num").val())){
            layer.msg("请输入纯数字", {icon: 5,time:1000});
            return false;
        }
        if($("#num").val() == null){
            layer.msg("购买数量至少为1", {icon: 5,time:1000});
            return false;
        }

        if($("#num").val() < 1){
            layer.msg("购买数量至少为1", {icon: 5,time:1000});
            return false;
        }

        $("#token").val(cookie.get("TOKEN"));
        var  proSkuId = $("#proSkuId").val();
        var proId = $("#id").val();
        var num = $("#num").val();
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/order/buyNowCheckCount",
            {"proSkuId": proSkuId, "num":num},
            function (data){
                if(data.code == 200){
                    layer.msg(data.msg, {icon: 6},function () {
                        window.location.href="<%=request.getContextPath() %>/order/buyNowOrder?token="+cookie.get("TOKEN")+"&proId="+proId+"&proSkuId="+proSkuId+"&num="+num;

                    })
                    return
                }
                layer.close(loading);
                layer.msg(data.msg, {icon: 5})
            }
        )


    }





    /*数量减少到数据库修改*/
    function del(){
        if($("#num").val() < 2){
            layer.msg("购买数量至少为1", {icon: 5,time:1000});
        }
        if($("#num").val() > 1){
            $("#num").val($("#num").val() - 1);
        }


    }


    /*数量增加到数据库修改*/
    function add(){
        if($("#num").val() < 0){
            layer.msg("购买数量小于0", {icon: 5,time:1000});
        }
        if($("#num").val() >= 0){
            $("#num").val(parseInt($("#num").val())+1 );
        }
    }








    /*评论*/
    $(function () {
        search();
    });
    function search() {
        $("#token").val(cookie.get("TOKEN"));
        token_post(
            "<%=request.getContextPath()%>/order/commonList",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.list.length; i++) {
                    var com = data.data.list[i];
                    html += "<fieldset  style='width:600px; height: 300px*(com.replyList.length) '>";
                    html += " "+com.username+" ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    if(com.score == 1){
                        html += "<span style='font-size: xx-large; color: yellow'>★</span sty> <br><br>";
                    }
                    if(com.score == 2){
                        html += "<span style='font-size: xx-large; color: yellow'>★★</span sty> <br><br>";
                    }
                    if(com.score == 3){
                        html += "<span style='font-size: xx-large; color: yellow'>★★★</span sty> <br><br>";
                    }
                    if(com.score == 4){
                        html += "<span style='font-size: xx-large; color: yellow'>★★★★</span sty> <br><br>";
                    }
                    if(com.score == 5){
                        html += "<span style='font-size: xx-large; color: yellow'>★★★★★</span sty> <br><br>";
                    }

                    html += "<span style='color: grey'>"+com.createTimeShow+"</span>";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "<span style='font-size: x-large'>"+com.context+"</span>";
                    html += "<br>";
                    for (var j = 0; j < com.replyList.length ; j++) {
                        var rep = com.replyList[j];
                        html += "<span style='color: grey'>"+rep.createTimeShow+"</span>";
                        html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        if(rep.isUser == 1){
                            html += " "+com.username+": ";
                        } else {
                            html += "<span style='font-size: x-large; color: red'>商家回复：</span>";
                        }
                        html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        html += "<span style='font-size: x-large'>"+rep.context+"</span>";
                        html += "<br>";
                        html += "<hr>";

                    }
                    if($("#userId").val() == com.userId){
                        html += "<input type='button' value='回复' onclick='com("+com.id+")'>"
                    }
                    html += "<input type='hidden' id='product' value='"+com.productId+"' >"
                    html += "</fieldset>";

                }
                $("#tb").append(html);
                if(data.data.goodRate == 0){
                    $("#h1").html("暂无评论");
                } else {
                    $("#h1").html(data.data.goodRate);
                }


                var pagehtml = "";
                /* pagehtml += "当前"+(data.data.pageNo)+"页";*/
                pagehtml += "<input type='button' value='加载更多' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                /* pagehtml += "总"+(data.data.totalPage)+"页";*/
                $("#pagediv").html(pagehtml);
            }
        );
    }

    /*分页*/
    function page(pageNo,totalPage) {
        $("#pageNo").val(pageNo);
        if(pageNo < 1){
            layer.msg('已经是第一页了', {time: 500, icon:6});
            return;
        }
        if(pageNo > totalPage ){
            /*layer.msg('已经是第一页了', {time: 500, icon:5});*/
            $("#pagediv").html("没有更多数据了");
            return;
        }
        search();
    }

    function toScore() {
        $("#tb").html("");
        search();
    }


    /*去回复*/
    function com(id) {
        var productId = $("#productId").val();
        layer.open({
            type: 2,
            title: '回复',
            shadeClose: true,
            shade: 0.8,
            area: ['50%', '70%'],
            content:'<%=request.getContextPath()%>/order/toReply?id='+id+"&productId="+productId +"&token="+cookie.get("TOKEN"),
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });
    }






</script>



</html>