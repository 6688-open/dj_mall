<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/1/21
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/md5-min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
    <script src="<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
    <script src="<%=request.getContextPath()%>/res/cookie.js"></script>
    <script src="<%=request.getContextPath()%>/res/token.js"></script>
    <style>
        .error{
            color:red;
        }
    </style>
</head>
<body>
<center>
    <h2>新 增 地  址  </h2>
    <form id="fm">
        <p>
            <label for="addressName">收货人：</label>
            <input id="addressName" name="addressName" type="text" maxlength="8" />
            <%--通过token   redis  获取user的 id--%>
            <input id="token" name="token" type="hidden" />
        </p>
        <p>
            <label for="phone">手机号码：</label>
            <input id="phone" name="phone" type="text" maxlength="11"  />
        </p>
        <p>
            <label for="phone">省市县：</label>
            <select id="pro" name="pro" onchange="changePro(this.value)"></select>
            <select id="city" name="city" onchange="changeCity(this.value)"></select>
            <select id="area" name="area"></select>
        </p>
        <p>
            <label >街道小区详细位置：</label>
            <input id="address" name="address" type="text" maxlength="15"  />
        </p>

        <p>
            <input type="submit" value="确认新增">

        </p>
    </form>
</center>
</body>
<script type="text/javascript">

    // 页面加载事件
    $(function () {
        // 在键盘按下并释放及提交后验证提交表单
        $("#fm").validate({
            errorPlacement: function(error,element) {
                //将错误信息展示在父元素的后面
                $(element).parent().after(error);
            },
            rules:{
                /*对应input框的name值*/
                addressName: {
                    required: true,
                    minlength: 2
                },
                address: {
                    required: true,
                    minlength: 2
                },
                phone: {
                    required: true,
                    rangelength:[11,11],// 手机号必须是11位
                },
            },
            messages:{
                addressName: {
                    required: "请输入昵称名",
                    minlength: "昵称名至少由2个字母/数字/汉字组成"
                },
                phone: {
                    required: "请输入手机号码",
                    rangelength: "手机号必须是11位的整数",
                },
                address: {
                    required: "请输入地址",
                    minlength: "地址至少由2个字母/数字/汉字组成"
                },
            }
        })
    })


    $.validator.setDefaults({
        submitHandler: function(form) {
            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#addressName").val().match(pattern);
            var result1 = $("#address").val().match(pattern);

            if(result){
                layer.msg("名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            if(result1){
                layer.msg("地址名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            if($("#phone").val() < 0){
                layer.msg("手机号码只能等于0 或者大于0不能出现负数", {time:1000});
                return true;
            }
            if(isNaN($("#phone").val())){
                layer.msg("请输入纯数字", {time:1000});
                return false;
            }
            $("#token").val(cookie.get("TOKEN"))
            var index1 = layer.load();
            token_post(
                "<%=request.getContextPath()%>/address/add",
                $("#fm").serialize(),
                function(data){
                    if (data.code == 200) {
                        layer.msg(data.msg, {icon: 6,time:1000},function(){
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            parent.location.href = "<%=request.getContextPath()%>/address/toList?token="+cookie.get("TOKEN");
                        });
                        return;
                    }
                    layer.close(index1);
                    layer.msg(data.msg, {icon: 5,time:1000});

                }
            );
        }
    })











    $(function(){
        token_post(
            "<%=request.getContextPath()%>/address/getCity/0",
            {},
            function(data){
                var html = "";
                for (var i = 0; i < data.data.length; i++) {
                    var pro = data.data[i];
                    html += "<option value='"+pro.id+"'>"+pro.areaName+"</option>";
                }
                $("#pro").html(html);
                changePro(data.data[0].id);
            });
    });

    function changePro(pId) {
        token_post(
            "<%=request.getContextPath()%>/address/getCity/"+pId,
            {},
            function(data){
                var html = "";
                for (var i = 0; i < data.data.length; i++) {
                    var city = data.data[i];
                    html += "<option value='"+city.id+"'>"+city.areaName+"</option>";
                }
                $("#city").html(html);
                changeCity(data.data[0].id);
            });
    }



    function changeCity(pId) {
        token_post(
            "<%=request.getContextPath()%>/address/getCity/"+pId,
            {},
            function(data){
                var html = "";
                for (var i = 0; i < data.data.length; i++) {
                    var area = data.data[i];
                    html += "<option value='"+area.id+"'>"+area.areaName+"</option>";
                }
                $("#area").html(html);
            });
    }


























</script>
</html>
