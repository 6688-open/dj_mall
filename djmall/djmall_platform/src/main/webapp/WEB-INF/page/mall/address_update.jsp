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
    <h2>修 改 地  址  </h2>
    <form id="fm">
        <p>
            <label for="addressName">收货人：</label>
            <input id="addressName" name="addressName" type="text" maxlength="8"  value="${address.addressName}"/>
            <input id="id" name="id" type="hidden" value="${address.id}" />
            <input id="proId" type="hidden" value="${address.proId}" />
            <input id="cityId" type="hidden" value="${address.cityId}"/>
            <input id="areaId" type="hidden" value="${address.areaId}"/>
        </p>
        <p>
            <label for="phone">手机号码：</label>
            <input id="phone" name="phone" type="text" maxlength="11" value="${address.phone}"  />
        </p>
       <p>
            <label for="phone">省市县：</label>
            <select id="pro" name="pro" onchange="changePro(this.value)" ></select>
            <select id="city" name="city" onchange="changeCity(this.value)"></select>
            <select id="area" name="area"></select>
        </p>
        <p>
            <label >街道小区详细位置：</label>
            <input id="address" name="address" type="text"  value="${address.address}" />
        </p>
         <p>
            <label >原详细位置：</label>
             ${oldAddress}
        </p>


        <p>
            <input type="submit" value="确认修改">

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
              var proId = $("#proId").val();
                var html = "";
                for (var i = 0; i < data.data.length; i++) {
                    var pro = data.data[i];
                    html += "<option      value='"+pro.id+"'>"+pro.areaName+"</option>";
                }
                $("#pro").html(html);

                if(proId != ""){
                    //获取所有省里option 的id 与后台回显 数据库里存的的id 比较  相等 被选中
                    $("#pro option").each(function(){
                        if($(this).val()==proId){
                            $(this).attr("selected",true)
                        }
                    })
                    //将省级的id 带给 下一级 市级
                    changePro(proId);
                }


              /*  changePro(data.data[0].id);*/
                if(proId == ""){
                    changePro(data.data[0].id);
                }

            });
    });

    function changePro(pId) {
        token_post(
            "<%=request.getContextPath()%>/address/getCity/"+pId,
            {},
            function(data){
                var cityId = $("#cityId").val();
                var html = "";
                for (var i = 0; i < data.data.length; i++) {
                    var city = data.data[i];
                    html += "<option value='"+city.id+"'>"+city.areaName+"</option>";
                }
                $("#city").html(html);
               /* changeCity(data.data[0].id);*/

                //获取所有市里option 的id 与后台回显 数据库里存的的id 比较  相等 被选中
                if(cityId != ""){
                    $("#city option").each(function(){
                        if($(this).val()==cityId){
                            $(this).attr("selected",true)
                        }
                    })
                    changeCity(cityId);
                }
                //等于“”  执行changeCity(data.data[0].id);
                if(cityId == ""){
                    changeCity(data.data[0].id);
                }

            });
    }



    function changeCity(pId) {
        token_post(
            "<%=request.getContextPath()%>/address/getCity/"+pId,
            {},
            function(data){
                var areaId = $("#areaId").val();
                var html = "";
                for (var i = 0; i < data.data.length; i++) {
                    var area = data.data[i];
                    html += "<option value='"+area.id+"'>"+area.areaName+"</option>";
                }
                $("#area").html(html);

                //获取所有县里option 的id 与后台回显 数据库里存的的id 比较  相等 被选中
                if(areaId != ""){
                    $("#area option").each(function(){
                        if($(this).val()==areaId){
                            $(this).attr("selected",true)
                        }
                    })
                }


                //首次加载 回显完成 就将值 ""
                $("#proId").val("");
                $("#cityId").val("");
                $("#areaId").val("");



            });












    }


























</script>
</html>
