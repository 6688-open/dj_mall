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
<center>
    <h2>修改库存</h2>
    <form id="fm">
        <p>
            <input id="proId" name="proId" value="${proId}" type="hidden" />

            <input id="" name="id" value="${productSku.id}" type="hidden" />
        </p>
        <p>
            <input type="button" value="-" onclick="del()">
            <input id="skuCount" name="skuCount" type="text" maxlength="6"  value="${productSku.skuCount}" size="4px" />
            <input type="button" value="+" onclick="add()">
        </p>
        <p>
            <input type="submit" value="确认">
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
                skuCount: {
                    required: true,

                },


            },
            messages:{
                skuCount: {
                    required: "请输入库存",
                },

            }
        })
    })

    function del(){
        if($("#skuCount").val() < 1){
            layer.msg("库存不能小于0", {icon: 5,time:1000});
        }
        if($("#skuCount").val() > 0){
            $("#skuCount").val($("#skuCount").val() - 1);
        }
    }

    function add(){
        if($("#skuCount").val() < 0){
            layer.msg("库存不能小于0", {icon: 5,time:1000});
        }
        if($("#skuCount").val() >= 0){
            $("#skuCount").val(parseInt($("#skuCount").val())+1 );
        }
    }




    $.validator.setDefaults({
        submitHandler: function(form) {

            if($("#skuCount").val() < 0){
                layer.msg("运费只能等于0 或者大于0不能出现负数", {icon: 5,time:1000});
                return true;
            }
            if(isNaN($("#skuCount").val())){
                layer.msg("请输入纯数字", {icon: 5,time:1000});
                return false;
            }
            var index1 = layer.load();
            var proId = $("#proId").val();
            $.post(
                "<%=request.getContextPath()%>/product/updateCount",
                $("#fm").serialize(),
                function(data){
                    if(data.code == 200){
                        layer.msg(data.msg, {
                            icon: 1,shade: 0.01,time: 500
                        }, function(){
                             var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            parent.location.href = "<%=request.getContextPath()%>/product/toUpdate/"+proId;
                        });
                    } else {
                        layer.close(index1);
                        layer.msg(data.msg,{icon: 5,time:500});
                    }
                }
            );
        }
    })






























</script>
</html>
