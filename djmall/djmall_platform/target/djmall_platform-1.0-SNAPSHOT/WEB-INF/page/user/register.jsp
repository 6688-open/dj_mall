<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/1/21
  Time: 15:45
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
    <h2>用  户  注  册</h2>
    <form id="fm">
        <p>
            <label for="username">用户姓名：</label>
            <input id="username" name="username" type="text" placeholder="用户名"/>
        </p>
        <p>
            <label for="password">输入密码：</label>
            <input id="password" name="password" type="password" placeholder="密码">
        </p>
        <p>
            <label for="password1">确认密码：</label>
            <input id="password1" name="password1" type="password" placeholder="确认密码">
        </p>
        <p>
            <label for="phone">手机号码：</label>
            <input id="phone" name="phone" type="text"  placeholder="手机号" maxlength="11" />
            <%--<input type="button" value="获取短信验证码" onclick="getKey()" id="code"/>--%>
        </p>
        <p>
            <label for="email">填写邮箱：</label>
            <input id="email" name="email" type="text" placeholder="邮箱"/>
        </p>
        <p>
            <label for="sex">性别：</label>
            <input name="sex" type="radio" value="1">     男
            <input name="sex" type="radio" value="2">     女
        </p>
       <%-- <p>
            <label for="lever">级别：</label>
            <input name="lever" type="radio" value="1">     商户
            <input name="lever" type="radio" value="2">     管理员
        </p>--%>
        <input name="lever" type="hidden" value="3">
        <input name="status" type="hidden" value="1">
        <p>
            <input type="button" onclick="reg()" value="确认注册">
            <a href="<%=request.getContextPath()%>/user/toIndex">以有账号?前去登录</a>
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



    //文档加载完成后也进行表单校验，上面仅仅在单击事件触发后才进行
    //如果是普通的表单提交，直接在文档加载完成后进行表单校验就可以了
    $(function () {
        validform();
    })

    //声明一个函数  自定义函数
    function validform() {
        return $("#fm").validate({
            errorPlacement: function(error,element) {
                //将错误信息展示在父元素的后面
                $(element).parent().after(error);
            },
            rules:{
                /*对应input框的name值*/
                username: {
                    required: true,
                    minlength: 2,
                    remote:{
                        url:"<%=request.getContextPath()%>/user/uniq",
                        type:"post",
                        data:{userName:function(){return $("#username").val()}}
                    }
                },
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
                    rangelength:[11,11],// 手机号必须是11位
                    remote:{
                        url:"<%=request.getContextPath()%>/user/uniq",
                        type:"post",
                        data:{userName:function(){return $("#phone").val()}}
                    }
                },
                email: {
                    required: true,
                    email: true ,//必须输入正确格式的电子邮件。
                    remote:{
                        url:"<%=request.getContextPath()%>/user/uniq",
                        type:"post",
                        data:{userName:function(){return $("#email").val()}}
                    }
                },
                sex:"required",

            },
            messages:{
                username: {
                    required: "请输入用户名",
                    minlength: "用户名至少由2个字母/数字/汉字组成",
                    remote:"用户名已存在,请重新注册"
                },
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
                    rangelength: "手机号必须是11位的整数",
                    remote:"手机号密码已存在,请重新注册"
                },
                email: {
                    required: "请输入邮箱",
                    rangelength: "请输入正确的邮箱格式",
                    remote:"邮箱已存在,请重新注册"
                },
                sex:"请选择性别",
            }



        })
    }



    //onclick事件
    //点击表单提交按钮触发单击事件，进行表单验证，验证通过发起ajax请求，验证不通过在表单里提示
    function reg(){
        if (validform().form()) {
            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#username").val().match(pattern);

            if(result){
                layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            var loading = layer.load();
            var password = md5($("#password").val());
            $("#password").val(password);
            $.post(
                "<%=request.getContextPath()%>/user/register",
                $("#fm").serialize(),
                function(data){
                    var index1 = layer.load();
                    if(data.code == 200){
                        layer.msg(data.msg, {icon: 6},function () {
                            layer.close(index1);
                            window.location.href = "<%=request.getContextPath()%>/user/toIndex"
                        })
                        return
                    } else {
                        layer.close(loading);
                        layer.msg("失败网络异常", {icon: 5,time:1000});
                    }
                }
            );
        }
    }






   /* $.validator.setDefaults({
        submitHandler: function(form) {

            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#username").val().match(pattern);

            if(result){
                layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            var loading = layer.load();
            var password = md5($("#password").val());
            $("#password").val(password);
            $.post(
                "<%=request.getContextPath()%>/user/register",
                $("#fm").serialize(),
                function(data){
                    var index1 = layer.load();
                    if(data.code == 200){
                        layer.msg(data.msg, {icon: 6},function () {
                            layer.close(index1);
                            window.location.href = "<%=request.getContextPath()%>/user/toIndex"
                        })
                        return
                    } else {
                        layer.close(loading);
                        layer.msg("失败网络异常", {icon: 5,time:1000});
                    }
                }
            );
        }
    })
*/


























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



</script>
</html>
