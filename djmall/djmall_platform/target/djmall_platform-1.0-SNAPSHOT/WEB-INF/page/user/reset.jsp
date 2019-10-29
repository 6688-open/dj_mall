<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/2/21
  Time: 14:07
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
    <%--    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jsencrypt/jsencrypt.min.js"></script>--%>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
</head>
<body>

<center>
    <h2>重置密码</h2>
    <form id="fm">

        <input type="hidden" name="id" value="${id}">
        <p>
            <label for="password"></label>
            <input id="password" name="password" type="password" placeholder="新密码">
        </p><br>
        <p>
            <label for="password1"></label>
            <input id="password1" name="password1" type="password"  placeholder="确认密码">
        </p><br>

        <input type="submit" value="确认修改">
    </form>
</center>

</body>
<style>
    .error{
        color: red;
    }
</style>

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
                    // rangelength:[6,14],
                },
                password1: {
                    required: true,
                    equalTo:"#password"
                    // rangelength:[6,14],
                },


            },
            messages:{

                password: {
                    required: "请输入密码",
                    // rangelength: "密码长度为 6-14 个字符",
                },
                password1: {
                    required: "请输入密码",
                    equalTo:"两次输入的密码不一致"
                    // rangelength: "密码长度为 6-14 个字符",

                },

            }
        })
    })







    $.validator.setDefaults({
        submitHandler: function(form) {
            var index1 = layer.load();
            var password = md5($("#password").val());
            $("#password").val(password);
            $.post(
                "<%=request.getContextPath()%>/user/upRestPassword",
                $("#fm").serialize(),
                function(data){
                    if (data.code == 200) {
                        layer.msg(data.msg, {icon: 6,time:1000},function(){
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            parent.location.href = "<%=request.getContextPath()%>/toIndex";
                        });
                        return;
                    }
                    layer.close(index1);
                    layer.msg(data.msg, {icon: 5,time:1000});

                }
            );
        }
    })









</script>
</html>
