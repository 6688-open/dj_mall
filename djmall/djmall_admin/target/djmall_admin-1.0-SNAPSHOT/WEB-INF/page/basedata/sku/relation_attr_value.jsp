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
    <style>
        .error{
            color:red;
        }
    </style>
</head>
<body>
<input type="button"  onclick="back()" value="返回" style="color: red"  align="right">
<center>
    <h4> 关联关系</h4>
     属性名： ${attrName}
    <form id="fm">
        <p>
            <label for="attrValue">属性名：</label>
            <input id="attrValue" name="attrValue" type="text" placeholder="属性名" maxlength="6"/><br>
            <input id="attrId" name="attrId" type="hidden" value="${id}" >
            <input id="attrName" name="attrName" type="hidden" value="${attrName}" >
        </p>
        <p>
            <input type="submit" value="新增">

        </p>
    </form>

    <h3>已关联值</h3>
    <table border="1px" align="center" cellspacing="0px">
        <tr>
            <th>编号</th>
            <th>属性值</th>
            <th></th>
        </tr>
        <c:forEach items="${attrValueList}" var="attrValue">
            <tr>
                <td>${attrValue.id}</td>
                <td>${attrValue.attrValue}</td>
                <td><input type='button' value=' 移除' onclick='remve(${attrValue.id})'/></td>
            </tr>
        </c:forEach>
    </table>




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
                attrValue: {
                    required: true,
                    minlength: 1

                },
            },
            messages:{
                attrValue: {
                    required: "请输入属性值",
                    minlength: "属性值至少由2个字母/数字/汉字组成"
                },
            }
        })
    })


    $.validator.setDefaults({
        submitHandler: function(form) {
            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#attrValue").val().match(pattern);

            if(result){
                layer.msg("名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            var valu = $("#attrName").val();
            var index1 = layer.load();
            $.post(
                "<%=request.getContextPath()%>/sku/insertAttrValue",
                $("#fm").serialize(),
                function(data){
                    if(data.code == 200){
                        layer.msg(data.msg, {
                            icon: 1,shade: 0.01,time: 500
                        }, function(){
                            var index = parent.layer.getFrameIndex(window.name);
                           // parent.layer.close(index);
                            window.location.href = "<%=request.getContextPath()%>/sku/relationAttrValue/"+data.data.attrId+"/"+valu;
                        });
                    } else {
                        layer.close(index1);
                        layer.msg(data.msg,{icon: 5,time:500});
                    }
                }
            );
        }
    })

    function remve(id) {
            layer.confirm('确认移出？', {
                btn: ['YES', 'NO'],
                btn1: function (index, layero) {
                    var valu = $("#attrName").val();
                    var attrId = $("#attrId").val();
                    var loading=layer.load();
                    $.post("<%=request.getContextPath()%>/sku/remve/"+id,
                        {},
                        function (data){
                            if(data.code == 200){
                                layer.msg(data.msg, {icon: 6},function () {
                                    window.location.href = "<%=request.getContextPath()%>/sku/relationAttrValue/"+attrId+"/"+valu;

                                })
                                return
                            }
                            layer.close(loading);
                            layer.msg(data.msg, {icon: 5})
                        }
                    )
                },
                btn2: function (index, layero) {
                    return;
                }
            })



    }

    /**
     *  关闭layer  跳转toList
     */
    function back() {
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                        parent.location.href = "<%=request.getContextPath()%>/sku/toList";


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
