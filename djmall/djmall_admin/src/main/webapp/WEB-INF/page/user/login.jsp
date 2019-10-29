<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/2/28
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/md5-min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jsencrypt/jsencrypt.min.js"></script>
    <style>
        .error{
            color:red;
        }
    </style>
</head>
<body>
<br><br>
<center>
    <h2>用  户  登  录</h2>
    <form id="fm">
        <p>
            <label for="username">用户:</label>
            <input id="username" name="username" type="text" placeholder="用户名/手机号/邮箱" maxlength="10"/>
        </p>
        <p>
            <label for="password">密码:</label>
            <input id="password" name="password" type="password" placeholder="密码" maxlength="8">
        </p>
        <p>
            <input type="submit" value="login">
        </p>
    </form>

    <a href="javascript:reg()">还没有账号?点这里注册</a> --  <a href="javascript:findPassword()">忘记密码?</a>
    <h2><span style="color: red">${success}</span></h2>
</center>



</body>
<script>
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
                username: {
                    required: true,
                    minlength: 2
                },
                password: {
                    required: true,
                    //rangelength:[6,14],
                },

            },
            messages:{
                username: {
                    required: "请输入用户名",
                    minlength: "用户名至少由2个字母/数字/汉字组成"
                },
                password: {
                    required: "请输入密码",
                    // rangelength: "密码长度为 6-14 个字符",
                },
            }
        })
    })


    $.validator.setDefaults({
        submitHandler: function(form) {
            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#username").val().match(pattern);

            if(result){
                layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            var loading=layer.load();
            var password = md5($("#password").val());
            $("#password").val(password);
            $.post("<%=request.getContextPath()%>/user/login",
                $("#fm").serialize(),
                function (data){
                    if(data.code == 200){
                        layer.msg(data.msg, {icon: 6},function () {
                            window.location.href = "<%=request.getContextPath()%>/user/toLoginImg"
                        })
                        return
                    }
                    if(data.code == 300){

                       layer.open({
                            type: 2,
                            title: '重置密码',
                            shadeClose: true,
                            shade: 0.8,
                            area: ['360px', '50%'],
                            content:'<%=request.getContextPath()%>/user/reset/'+data.data.id ,
                            end: function(){              //修改完刷新页面
                                location.reload();
                            }
                        });
                        layer.close(loading);
                        layer.msg(data.msg, {icon: 6})



                    }
                    if(data.code == -1){
                        $("#password").val("");
                        layer.close(loading);
                        layer.msg(data.msg, {icon: 5})
                    }

                }
            )
        }
    })





























    function getKey() {
        var code = $("#code");
        code.attr("disabled","disabled");
        setTimeout(function(){
            code.css("opacity","0.8");
        },1000)
        var time = 60;
        var set=setInterval(function(){
            code.val("("+--time+")秒后重新获取");
        }, 1000);
        setTimeout(function(){
            code.attr("disabled",false).val("重新获取验证码");
            clearInterval(set);
        }, 60000);
        $.post("${ctx}/user/messageVerification",
            {"phone":$("#phone").val(),"tplId":"131221"},
            function (data) {
                if (data.code == 200){
                    layer.msg('发送成功', {
                        icon: 1,
                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                    })
                }
            })
    }







    function reg(){
        window.location.href = "<%=request.getContextPath()%>/user/toRegister";
    }
    function findPassword(){
        window.location.href = "<%=request.getContextPath()%>/user/toFindPassword";
    }
</script>
</html>
