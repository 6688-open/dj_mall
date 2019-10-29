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
    <h2>编辑</h2>
    <form id="fm">
        <p>
            <input id="proId" name="proId" value="${proId}" type="hidden" />
            <input id="" name="id" value="${productSku.id}" type="hidden" />
        </p>
        <p>
            <label for="skuAttrValueNames">属性名：</label>
            <input id="skuAttrValueNames" name="skuAttrValueNames" type="text" value="${productSku.skuAttrValueNames}" maxlength="6" />
        </p>
        <p>
            <label for="skuCount">库存：</label>
            <input id="skuCount" name="skuCount" type="text"  value="${productSku.skuCount}" maxlength="6">
        </p>
        <p>
            <label for="skuPrice">价格：</label>
            <input id="skuPrice" name="skuPrice" type="text" value="${productSku.skuPrice}" maxlength="6" >
        </p>
        <p>
            <label for="skuRate">折扣：</label>
            <input id="skuRate" name="skuRate" type="text"  value="${productSku.skuRate}" maxlength="3" >
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
                skuAttrValueNames: {
                    required: true,
                },
                skuCount: {
                    required: true,
                    //rangelength:[6,14],
                },
                skuPrice: {
                    required: true,
                    //rangelength:[6,14],
                },
                skuRate: {
                    required: true,
                },


            },
            messages:{
                skuAttrValueNames: {
                    required: "请输入属性名"
                },
                skuCount: {
                    required: "请输入库存",
                    // rangelength: "密码长度为 6-14 个字符",
                },
                skuPrice: {
                    required: "请输入价格",
                    //rangelength: "密码长度为 6-14 个字符",
                },
                skuRate: {
                    required: "请输入折扣"
                },

            }
        })
    })


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
            if($("#skuPrice").val() < 0){
                layer.msg("运费只能等于0 或者大于0不能出现负数", {icon: 5,time:1000});
                return true;
            }
            if(isNaN($("#skuPrice").val())){
                layer.msg("请输入纯数字", {icon: 5,time:1000});
                return false;
            }
            if($("#skuRate").val() < 0){
                layer.msg("运费只能等于0 或者大于0不能出现负数", {icon: 5,time:1000});
                return true;
            }
            if(isNaN($("#skuRate").val())){
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
