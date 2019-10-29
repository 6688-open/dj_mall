<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/3/2
  Time: 17:13
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
    <script src="<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
    <style>
        .error{
            color:red;
        }
    </style>
</head>
<body>
<br><br>
<center>
    <h2>找 回 密 码</h2>
    <form id="fm">

        <p>
            <label for="phone">手机号码：</label>
            <input id="phone" name="phone" type="text"  placeholder="手机号" maxlength="11" />
            <%--<input type="button" value="获取短信验证码" onclick="getKey()" id="code"/>--%>
        </p>
        <p>
            <label for="verifCode">图形验证码：</label>
            <input type="text" id="verifCode" name="verifCode" size="2px" onblur="onblu()"    placeholder="验证码"/>                 <!-- 拦截器放过/user/getVerifCode  -->
            <img id="img"  onclick="flush()" alt="验证码" src="<%=request.getContextPath()%>/user/getVerifCode">
            <a href="#" onclick="reImg();">看不清，换一张</a> <br>


        </p>
        <p>
            <label for="code">短信验证码：</label>
            <input id="code" name="code" type="text" placeholder="验证码" size="3px" maxlength="6">
            <input type="button" value="获取手机验证码" id="codeMsg" onclick="getCode()">
        </p>
        <p>
            <label for="password">新密码：</label>
            <input id="password" name="password" type="password" placeholder="密码" maxlength="8">
            <input id="pwd" name="pwd" type="hidden" >
        </p>
        <p>
            <label for="password1">确认密码：</label>
            <input id="password1" name="password1" type="password" placeholder="确认密码" maxlength="8">
        </p>
        <p>
            <input type="submit" value="确认修改"> &nbsp; &nbsp; &nbsp; &nbsp;
            <input type="button" value="取消" onclick="reback()">
        </p>
    </form>
</center>
</body>
<script type="text/javascript">
    var hash = {
        'qq.com': 'http://mail.qq.com',
        'gmail.com': 'http://mail.google.com',
        'sina.com': 'http://mail.sina.com.cn',
        '163.com': 'http://mail.163.com',
        '126.com': 'http://mail.126.com',
        'yeah.net': 'http://www.yeah.net/',
        'sohu.com': 'http://mail.sohu.com/',
        'tom.com': 'http://mail.tom.com/',
        'sogou.com': 'http://mail.sogou.com/',
        '139.com': 'http://mail.10086.cn/',
        'hotmail.com': 'http://www.hotmail.com',
        'live.com': 'http://login.live.com/',
        'live.cn': 'http://login.live.cn/',
        'live.com.cn': 'http://login.live.com.cn',
        '189.com': 'http://webmail16.189.cn/webmail/',
        'yahoo.com.cn': 'http://mail.cn.yahoo.com/',
        'yahoo.cn': 'http://mail.cn.yahoo.com/',
        'eyou.com': 'http://www.eyou.com/',
        '21cn.com': 'http://mail.21cn.com/',
        '188.com': 'http://www.188.com/',
        'foxmail.com': 'http://www.foxmail.com',
        'outlook.com': 'http://www.outlook.com'
    }

    // 页面加载事件
    $(function () {
        // 在键盘按下并释放及提交后验证提交表单
        $("#fm").validate({
            errorPlacement: function(error,element) {
                //将错误信息展示在父元素的后面
                $(element).parent().after(error);
            },
            rules:{
                password: {
                    required: true,
                    //rangelength:[6,14],
                },
                password1: {
                    required: true,
                    //rangelength:[6,14],
                    equalTo: "#password" //输入值必须和 #passWord相同。
                },
                phone: {
                    required: true,
                    rangelength:[11,11]// 手机号必须是11位
                },
                verifCode:"required",
                code:"required",

            },
            messages:{
                password: {
                    required: "请输入密码",
                    // rangelength: "密码长度为 6-14 个字符",
                },
                password1: {
                    required: "请输入确认密码",
                    //rangelength: "密码长度为 6-14 个字符",
                    equalTo: "两次密码输入不一致"
                },
                phone: {
                    required: "请输入手机号密码",
                    rangelength: "手机号必须是11位的整数"
                },
                verifCode:"请填写验证码",
                code:"请输入验证码",
            }
        })
    })
    /*获取手机验证码前  必须 图形验证码正确*/
    $(function () {
        var code = $("#codeMsg");
        code.attr("disabled","disabled");
        code.val("获取手机验证码");
    })

    function onblu() {
        var loading = layer.load();
        $.post(
            "<%=request.getContextPath()%>/user/verifCode",
            $("#fm").serialize(),
            function(data){
                if (data.code == 200) {
                    layer.close(loading);
                    layer.msg("图形验证码验证成功", {icon: 6,time:1000});
                    /*获取手机验证码  必须 图形验证码正确*/
                    var code = $("#codeMsg");
                    code.attr("disabled",false).val("获取手机验证码");
                } else{
                    layer.close(loading);
                    layer.msg("重新输入验证码", {icon: 5,time:1000});
                    flush();
                }


            }

        );
    }

    function flush(){
        var img = document.getElementById("img");
        img.src = "<%=request.getContextPath()%>/user/getVerifCode?now="+new Date();
    }
    function reImg(){
        /* 点击图片刷新  点击按钮刷新   登陆失败 再调方法刷新验证码  */
        flush();
    }



    function reback() {
        window.location.href = "<%=request.getContextPath()%>/user/toLogin";
    }

    function getCode(){
        var code = $("#codeMsg");
        code.attr("disabled","disabled");
        setTimeout(function(){
            code.css("opacity","0.8");
        },1000)
        var time = 60;
        var set=setInterval(function(){
            code.val("("+--time+")秒后重新获取");
        }, 1000);
        setTimeout(function(){
            code.attr("disabled",false).val("重新获取手机验证码");
            clearInterval(set);
        }, 60000);
        var loading = layer.load();
        $.post(
            "<%=request.getContextPath()%>/user/getPhoneMsg",
            $("#fm").serialize(),
            function(data){
                if (data.code == 200) {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 6,time:1000})
                    /* layer.msg(data.msg, {icon: 6,time:1000},function(){
                         window.location.href = "<%=request.getContextPath()%>/user/toPhoneLogin";
                    });*/
                    return;
                }
                layer.msg(data.msg, {icon: 5,time:1000})
                layer.close(loading);
            }
        );
    }


    $.validator.setDefaults({
        submitHandler: function(form) {
            var loading = layer.load();
            /*另起一个参数  数字 用来发邮箱*/
            $("#pwd").val($("#password").val());
            /*加密*/
            var password = md5($("#password").val());
            $("#password").val(password);
            $.post(
                "<%=request.getContextPath()%>/user/resetPsaaword",
                $("#fm").serialize(),
                function(data) {
                    if (data.code == 200) {
                        layer.close(loading);
                        window.location.href = "<%=request.getContextPath()%>/user/toLogin";
                        layer.msg("修改成功", {icon: 6,time:1000});
                    } else{
                        layer.close(loading);
                        layer.msg(data.msg, {icon: 5,time:1000});
                        flush();
                    }
                }
            );
        }
    })



























   /* function getKey() {
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
    }*/



</script>
</html>
