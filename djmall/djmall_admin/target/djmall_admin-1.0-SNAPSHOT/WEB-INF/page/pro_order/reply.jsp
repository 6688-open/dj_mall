<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/4/13
  Time: 12:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/css/layui.css"  media="all">
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <script type="text/javascript" src = "<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/layui.js"></script>
</head>
<body>
    <form id="fm">
        <input type="hidden" name="commentId" value="${commentId}"/> <%--评论id--%>
        <input type="hidden" id="productId" value="${productId}"/> <%--商品--%>
        <input type="hidden" name="isUser" value="2"/> <%--是卖家--%>
        <textarea name="context" id="textContext" rows="10" cols="20">
        </textarea>
        <input type="button" value="提交" onclick="rep()"/>
    </form>
</body>
<script>

    function rep() {
        var reply = $("#textContext").val();
        if (reply.trim().split(" ").join("").length < 1) {
            layer.msg("请输入评论", {time: 600});
            // layer.close(loading);
            return;
        }
            var productId = $("#productId").val();
            var index1 = layer.load();
            $.post(
                "<%=request.getContextPath()%>/product/reply",
                $("#fm").serialize()
                ,function(data){
                    if(data.code == 200){
                        layer.msg('回复成功', {
                            icon: 1,shade: 0.01,time: 500
                        }, function(){
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            parent.location.href = "<%=request.getContextPath()%>/product/toCommon?id="+productId;
                        });
                    } else {
                        layer.close(index1);
                        layer.msg(data.msg,{icon: 5,time:500});
                    }
                }
            )

    }
</script>
</html>
