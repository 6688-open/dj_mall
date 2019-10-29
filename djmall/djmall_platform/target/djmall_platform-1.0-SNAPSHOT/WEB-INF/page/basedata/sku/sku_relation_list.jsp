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
    <form id="fmt">
    <h4> 关联关系</h4>

    <button onclick="save()">保存</button>
    <input type="hidden" id="productType" name="productType" value="${productType}"/>
    <table border="1px" align="center" cellspacing="0px">
        <tr>
            <th></th>
            <th>编号</th>
            <th>id</th>
            <th>属性值</th>
            <th></th>
        </tr>
        <c:forEach items="${productAttrAllList}" var="productAttr">
            <tr>
                <td>
                    <input type='checkbox'  name="productAttrIds"
                            <c:forEach var="pro" items="${productAttrList}">
                                <c:if test="${productAttr.attrName == pro.attrName}">checked </c:if>
                            </c:forEach>
                           value="${productAttr.id}"/>


                </td>



                <td>${productAttr.id}</td>
                <td>${productAttr.attrName}</td>
                <td>${productAttr.attrValueShow}</td>
            </tr>
        </c:forEach>
    </table>

    </form>




</center>
</body>
<script type="text/javascript">

    function save(){
        var productType = $("#productType").val();
        var index1 = layer.load();
        $.post(
            "<%=request.getContextPath()%>/sku/savaAndDelRela/"+productType,
            $("#fmt").serialize()
            ,function(data){
                if(data.code == 200){
                    layer.msg(data.msg,{icon: 6,time:500});
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    parent.location.href = "<%=request.getContextPath()%>/sku/toSkuList";
                  /*  layer.msg(data.msg, {
                        icon: 1,shade: 0.01,time: 500
                    }, function(){
                        var index = parent.layer.getFrameIndex(window.name);
                         parent.layer.close(index);
                         parent.location.href = "<%=request.getContextPath()%>/sku/toSkuList";
                    });*/
                } else {
                    layer.close(index1);
                    layer.msg(data.msg,{icon: 5,time:500});
                }

                if(data.code == 200){

                }






                /* if(data.code == 200){
                     layer.msg(data.msg, {
                         icon: 1,shade: 0.01,time: 500
                     }, function(){
                         alert();
                         var index = parent.layer.getFrameIndex(window.name);
                         // parent.layer.close(index);
                         window.location.href = "<%=request.getContextPath()%>/sku/relationAttrValue/"+data.data.attrId+"/"+valu;
                    });
                } else {
                    layer.close(index1);
                    layer.msg(data.msg,{icon: 5,time:500});
                }*/
            }
        )
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
