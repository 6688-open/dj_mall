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
    <h2>用  户  修  改</h2>
    <form id="fm">
        <p>
            <input id="id" name="id" type="hidden" value="${user.id}"/>
            <label for="nickName">昵称：</label>
            <input id="nickName" name="nickName" type="text" maxlength="8" value="${user.nickName}"/>
        </p>
        <img src="http://${user.headImg}" style="width: 50px" height="50px"/>
        <p>
            <label for="file">图片：</label>
            <input id="file" name="file" type="file" />
        </p>
        <p>
            <label for="sex">性别：</label>
            <input name="sex" type="radio" value="1"  <c:if test="${user.sex == 1}">checked</c:if>   >     男
            <input name="sex" type="radio" value="2"  <c:if test="${user.sex == 2}">checked</c:if>  >     女
        </p>
        <p>
            <label for="email">填写邮箱：</label>
            <input id="email" name="email" type="text" maxlength="20"  value="${user.email}"/>
        </p>
        <p>
            <input type="submit" value="确认修改">

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
                /*对应input框的name值*/
                nickName: {
                    required: true,
                    minlength: 2,
                    remote:{
                        url:"<%=request.getContextPath()%>/user/uniq",
                        type:"post",
                        data:{userName:function(){return $("#nickName").val()}}
                    }
                },
                email: {
                    required: true,
                    email: true //必须输入正确格式的电子邮件。
                },
                sex:"required",
            },
            messages:{
                nickName: {
                    required: "请输入昵称名",
                    minlength: "昵称名至少由2个字母/数字/汉字组成",
                    remote:"昵称已存在,请重新填写"
                },
                email: {
                    required: "请输入邮箱",
                    rangelength: "请输入正确的邮箱格式"
                },
                sex:"请选择性别",
            }
        })
    })


    $.validator.setDefaults({
        submitHandler: function(form) {
            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#nickName").val().match(pattern);

            if(result){
                layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            var loading = layer.load();
            var formData = new FormData($('#fm')[0]);
            $.ajax({
                type: 'post',
                url: "<%=request.getContextPath()%>/user/updateUserMsg",
                data: formData,
                cache: false,
                processData: false,
                contentType: false,
                beforeSend: function (request) {
                    request.setRequestHeader("TOKEN", cookie.get("TOKEN"));
                },
            }).success(function (data) {
                layer.msg(data.msg, {
                    icon: 1,shade: 0.01,time: 500
                }, function(){
                   /* var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);*/
                    window.layer.close(loading);
                    window.location.href = "<%=request.getContextPath()%>/user/toUpdateMsg?token="+cookie.get("TOKEN");
                });

            }).error(function () {
                layer.msg("异常", {icon: 5,time:1000});
                layer.close(loading);
            });
        }
    })


    /**
     *  关闭layer  跳转toList
     */
    function back() {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
        parent.location.href = "<%=request.getContextPath()%>/user/toList";


    }























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
