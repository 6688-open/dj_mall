<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fujl
  Date: 2019/1/21
  Time: 21:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登陆</title>
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
    <style>
        .like{ font-size:20px; color:#ccc; cursor:pointer;}
        .cs{color:#f00;}
        .likes{color:#f00;}
    </style>
</head>
 <body>
    <center>
        <h1>点金商城</h1>
        <a href="javascript:exit()" style="color: red" hidden  id="exit">退出登录</a>

    </center>
    <h2>
        <a href="javascript:first()" style="color: red"  id="loginFirst">首页</a>
        <a href="javascript:loginClick()" style="color: red"    id="loginUser">登陆</a>
        <%--<a href="<%=request.getContextPath()%>/toIndex?token=${token}" style="color: red"  id="user">${username}</a>--%>
        <a href="javascript:reg()" style="color: red"  id="loginRegister">注册</a>
        <a href="javascript:myShpoCar()" style="color: red" hidden  id="myShpoCar">我的购物车</a>

    </h2>


        <%--支付宝账号--%>
    支付宝账号<input type="text" value="brtfoy3827@sandbox.com"><br>
    域名<input type="text" value="2t45i18784.qicp.vip:16916">




    <form id="fm" align = "center">
        <input type="hidden" value="1" name="pageNo" id="pageNo">
        <p><input  name="productName" id="productName" onblur="productNameClick()" maxlength="8"  type="text"/>
        <input  type="button"  value="search" onclick="search(page(1))" /><br><br>
        </p>
        <p>
        价格: <input id="startPrice"  name="startPrice" onblur="startPriceClick()"  maxlength="8" type="text"/>  ～ <input id="endPrice"  name="endPrice" onblur="endPriceClick()" maxlength="8"  type="text"/>  <br><br><br>
        </p>
        <p>
            分类:
            <c:forEach items="${dictionaryList}" var="dicPro">
                <input type="checkbox" name="dictionaryCodeArr" value="${dicPro.code}" > ${dicPro.dictionaryName}
            </c:forEach> <br><br>

        </p>
    </form>

        <table border="1px" cellspacing="0px" cellpadding="0px" align="center">
            <thead>
            <tr>
                <td>ID</td>
                <td>名称</td>
                <td>价格</td>
                <td>库存</td>
                <td>SKU</td>
                <td>折扣</td>
                <td>分类</td>
                <td>邮费</td>
                <td>图片</td>
                <td>描述</td>
                <td>点赞</td>

            </tr>
            </thead>
            <tbody id="tb">

            </tbody>
        </table><br><br>
<div id="pagediv" align="center"></div>
</body>
<script type="text/javascript">
    $(function () {
       /* debugger;
        alert(cookie.get("TOKEN"))
        alert(cookie.get("NICK_NAME"))*/
        if(check_login()){
            /*登陆成功 在回调函数set cookie set TOKEN  NICK_NAME    cookie.get("NICK_NAME")  直接取 */
            $("#loginUser").html(cookie.get("NICK_NAME"))
            $("#loginUser").attr('href',"<%=request.getContextPath()%>/toIndex?token="+cookie.get("TOKEN")  );
            $("#exit").show();
            /*登录才能展示购物车*/
            $("#myShpoCar").show();
        }

    })

    /**
     * 登录
     */
    function loginClick() {
        layer.open({
            type: 2,
            area: ['500px', '400px'],
            maxmin:true,
            content: ['<%=request.getContextPath()%>/user/toLogin', 'no'],
            end: function(){
                location.reload();
            }
        });
    }


    function reg(){
        window.location.href = "<%=request.getContextPath()%>/user/toRegister";
    }
    function findPassword(){
        window.location.href = "<%=request.getContextPath()%>/user/toFindPassword";
    }
    function phoneLogin(){
        window.location.href = "<%=request.getContextPath()%>/user/phoneLogin";
    }

    /*首页*/
    function first(){
        window.location.href = "<%=request.getContextPath()%>/user/toIndex";
    }
    /*退出登录   清空cookio*/
    function exit(){
        cookie.delete("TOKEN");
        window.location.href = "<%=request.getContextPath()%>/user/toIndex";
    }


    $(function () {
        search();
    })

    function search() {
        $.post(
            "<%=request.getContextPath()%>/user/productAndSkuShow",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i <data.data.list.length ;   i++) {
                    var pro = data.data.list[i];
                    html += "<tr>";
                    html += "<td>"+pro.id+"</td>";
                    /*html += "<td>"+pro.productName+"</td>";*/
                    html += "<td>";
                    html += "<a href='javascript:productMsg("+pro.id+")' style='color: blue' >"+pro.productName+"</a> ";
                    html +=  "</td>";


                    html += "<td>"+pro.priceShow+"</td>";
                    html += "<td>"+pro.countShow+"</td>";
                    html += "<td>"+pro.skuAttrValuesNames+"</td>";
                    if(pro.rateShow == 0){
                        html += "<td>暂无折扣</td>";
                    } else {
                        html += "<td>"+pro.rateShow+"%</td>";
                    }
                    html += "<td>"+pro.dictionaryNameShow+"</td>";
                    if(pro.freight == 0){
                        html += "<td>包邮</td>";
                    } else {
                        html += "<td> 平邮"+pro.freight+" 元</td>";
                    }
                    html += "<td>"+' <img src="http://'+pro.picture+'" style="width: 50px" height="50px"/>'+"</td>";
                    html += "<td>"+pro.productDescribe+"</td>";
                    html += "<input type = 'hidden' id='" + pro.id + "' value = '" + pro.thumbNumber + "'/>";
                    html += "<input type = 'hidden' class='" + pro.id + "' value = '" + pro.proSkuId + "'/>";
                    html += "<td style='color: red'><a href='javascript:xin("+(pro.id)+")' class='like'>&#10084;<p class='likes' class='p'></p></a>"
                    html += "+" +pro.thumbNumber + "</td>";
                    html += "</tr>";

                }
                $("#tb").html(html);


                var pagehtml = "";
                pagehtml += "当前"+(data.data.pageNo)+"页";
                pagehtml += "<input type='button' value='上一页' onclick='page("+(eval(data.data.pageNo)-1)+" , "+data.data.totalPage+"  )' />";
                pagehtml += "<input type='button' value='下一页' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                pagehtml += "总"+(data.data.totalPage)+"页";
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
            layer.msg('已经是第一页了', {time: 500, icon:5});
            return;
        }
        search();
    }



    /* 模糊查  输入格式 限制*/
    function productNameClick() {
        var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

        var result = $("#productName").val().match(pattern);

        if(result){
            layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
            return false;
        }

    }


    /*价格输入格式  限制*/
    function startPriceClick() {
        if($("#startPrice").val() < 0){
            layer.msg("运费只能等于0 或者大于0不能出现负数", {icon: 5,time:1000});
            return true;
        }
        if(isNaN($("#startPrice").val())){
            layer.msg("请输入纯数字", {icon: 5,time:1000});
            return false;
        }
    }
    /* 结束价格  输入格式 限制*/
    function endPriceClick() {
        if($("#endPrice").val() < 0){
            layer.msg("运费只能等于0 或者大于0不能出现负数", {icon: 5,time:1000});
            return true;
        }
        if(isNaN($("#endPrice").val())){
            layer.msg("请输入纯数字", {icon: 5,time:1000});
            return false;
        }

    }


    /*商品详情*/
    function productMsg(id) {
       /* window.location.href = "<%=request.getContextPath()%>/user/toProductMessage?id="+id;*/
        window.open("<%=request.getContextPath()%>/user/toProductMessage?id="+id);
    }
    /*去我的购物车*/
    function myShpoCar(id) {
       /* window.location.href = "<%=request.getContextPath()%>/user/toProductMessage?id="+id;*/
        window.open("<%=request.getContextPath()%>/user/myShoppingCar?token="+cookie.get("TOKEN"));
    }










    function xin(id) {
        // 根据id获取他的点赞量
        var states = $("#"+id).val();
        // 查看是否登录
        if (false == check_login()) {
            layer.msg("请前去登录", {icon: 5, time:1000});
            login();
            return false;
        }
        $(".like").click(function () {
            $(this).toggleClass('cs');
            var thumbNumber = Math.floor(Math.abs(states) + 1);
            // $('.likes').text(ran);
            var proSkuId = $("."+id).val();
            var loading = layer.load();
            token_post(
                "<%=request.getContextPath()%>/user/updateProduct?token="+cookie.get("TOKEN")+"&id="+id,
                {"proSkuId" : proSkuId, "thumbNumber": thumbNumber},
                function(data){
                    if(data.code == 200){
                        layer.msg(data.msg, {icon: 6},function () {
                            layer.close(loading);
                            window.location.href = "<%=request.getContextPath()%>/user/toIndex";

                        })
                        return
                    }
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 5});
                }
            );
        })
    }

</script>
</html>
