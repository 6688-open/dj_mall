<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/2/19
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/res/imgver/style.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/md5-min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <%--    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jsencrypt/jsencrypt.min.js"></script>--%>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/imgver/img_ver.js"></script>

    <%--导航框--%>
    <script src="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/layui.js"></script>
    <link href="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/css/layui.css" rel="stylesheet" />
</head>
<body>
<style>
    .error{
        color: red;
    }
</style>
<%--导航框--%>
<div class="layui-tab layui-tab-card">
    <ul class="layui-tab-title">
        <li><a href="javascript:toLogin()">登陆</a></li>
        <li><a href="javascript:phoneLogin()">手机登陆</a></li>
    </ul>
</div>

<center><br><br>
        <h2>手机号码登录</h2><br>
    <form id="fm">

        <p>
            <label for="phone">手机号：</label>
            <input id="phone" name="phone" type="text"/>
            <input type="hidden" value="2" name="codeType"/>
        </p>
        <p>
            <label for="verifCode">图形验证码：</label>
            <input type="text" id="verifCode" name="verifCode" size="2px" onblur="onblu()"    placeholder="验证码"/>                 <!-- 拦截器放过/user/getVerifCode  -->
            <img id="img"  onclick="flush()" alt="验证码" src="<%=request.getContextPath()%>/user/getVerifCode">
            <a href="#" onclick="reImg();">看不清，换一张</a> <br>


        </p>
        <p>
            <label for="phoneCode">验证码：</label>
            <input id="phoneCode" name="phoneCode" type="text" size="3px"/> <input type="button" value="获取手机验证码" id="codeMsg" onclick="getCode()">
        </p>
        <input type="submit"  value="登录" >  --- <a href="javascript:reg()">还没有账号?点这里注册</a>


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

    // 页面加载事件 合法判断
    $(function () {
        // 在键盘按下并释放及提交后验证提交表单
        $("#fm").validate({
            errorPlacement: function(error,element) {
                //将错误信息展示在父元素的后面
                $(element).parent().after(error);
            },
            rules:{

                phone: {
                    required: true,
                    rangelength:[11,11],// 手机号必须是11位
                },
                phoneCode:"required",
                verifCode:"required",

            },
            messages:{

                phone: {
                    required: "请输入手机号密码",
                    rangelength: "手机号必须是11位的整数"
                },
                phoneCode:"请输入验证码",
                verifCode:"请填写验证码",
            }
        })


        $("#sub1").hide();
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
                if(data.code == 200){
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 6},function () {

                    })
                    return
                }
                layer.msg(data.msg, {icon: 5,time:1000})
                layer.close(loading);
            }
        );
    }





    $.validator.setDefaults({
        submitHandler: function(form) {
            var loading = layer.load();
            $.post(
                "<%=request.getContextPath()%>/user/loginPhone",
                $("#fm").serialize(),
                function(data){
                    if (data.code == 200) {
                        layer.msg("手机号 验证成功", {icon: 6,time:1000},function(){
                            window.location.href = "<%=request.getContextPath()%>/toIndex";
                        });
                        return;
                    } else if(data.code == 400){
                        layer.msg("验证码失效", {icon: 5,time:1000},function(){
                            window.location.href = "<%=request.getContextPath()%>/user/toError";
                        });
                    }
                    layer.close(loading);
                    layer.msg("手机号 验证码 失败", {icon: 5,time:1000});
                }
            );
        }
    })



    function reg(){
        window.location.href = "<%=request.getContextPath()%>/user/toRegister";
    }











    //导航
    function phoneLogin(){
        window.location.href = "<%=request.getContextPath()%>/user/phoneLogin";
    }
    /** 登陆*/
    function toLogin(){
        window.location.href="<%=request.getContextPath()%>/user/toLogin";
    }




</script>
</html>
