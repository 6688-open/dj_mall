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
<div align="left" style="border: 1px  ; width:900px ; height:700px; margin-left: 50px">
    <h2>修改</h2>
    <form id="fm">
        <input id="id" name="id" type="hidden" value="${product.id}"/>
        <p>
            <label for="productName">商品名称：</label>
            <input id="productName" name="productName"  type="text" value="${product.productName}" maxlength="6"/>
        </p>
        <p>
            <label for="freightId">邮费：</label>
            <select name="freightId" id="freightId">
                <c:forEach items="${freightAll}" var="fre">
                    <option value="${fre.id}" <c:if test="${product.freightId == fre.id}">selected</c:if>           >${fre.logName}--${fre.freight}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="productDescribe"></label>
            <textarea    name="productDescribe" id="productDescribe" rows="5" cols="30" >${product.productDescribe}</textarea>
        </p>
        <img src="http://${product.picture}" style="width: 50px" height="50px"/>
        <p>
            <label for="file">图片：</label>
            <input id="file" name="file" type="file" />
        </p>
        <p>
            <label for="dictionaryCode">分类：</label>
            <select name="dictionaryCode" id="dictionaryCode">
                <c:forEach items="${dictionaryList}" var="dic">
                    <c:if test="${product.dictionaryCode == dic.code}">
                        <option value="${dic.code}" <c:if test="${product.dictionaryCode == dic.code}">selected</c:if>  >${dic.dictionaryName}</option>
                    </c:if>

                </c:forEach>
            </select>
        </p>
        <p>
        <input type='button'  value='修改库存' onclick="upCount()"/>---
        <input type='button'  value='编辑'  onclick="upSku()"/>---
        <input type='button'  value='设为默认'  onclick="upDefault()"/>
        </p>
        <p>
        <table border="1px" align="left" cellspacing="0px">
            <tr>
                <td colspan="5"><h3>SKU列表</h3></td>

            </tr>
            <tr>
                <td>编号</td>
                <td>SKU属性</td>
                <td>库存</td>
                <td>价格(元)</td>
                <td>折扣(%)</td>
                <td>是否默认</td>
                <td>下架/上架</td>
                <td></td>
            </tr>
            <c:forEach items="${productSkuList}" var="pro">
                <tr>
                    <td><input type='checkbox' name='idcheck' value='${pro.id}'/> ${pro.id}</td>
                    <td>${pro.skuAttrValueNames}</td>
                    <td>${pro.skuCount}</td>
                    <td>${pro.skuPrice}</td>
                    <td>${pro.skuRate}</td>
                    <c:if test="${pro.isDefault == 0}">
                        <td>否</td>
                    </c:if>
                    <c:if test="${pro.isDefault == 1}">
                        <td>是</td>
                    </c:if>
                    <td>
                        <c:if test="${pro.skuStatus == 1}">
                            <input type='button'  value='下架'   onclick="upStatus(${pro.id},${pro.skuStatus})" />
                        </c:if>
                        <c:if test="${pro.skuStatus == 0}">
                            <input type='button'  value='上架'  style="color: red"   onclick="upStatus(${pro.id},${pro.skuStatus})" />
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table> <br>` <br>` <br>` <br>`
        </p>
       <div style="color: #00FF00;width:100px ; height:200px;  margin-left:150px; "  >
           <p>
           <input type="submit" value="确认修改">
           </p>
       </div>
    </form>
</div>

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
                productName: {
                    required: true,
                    minlength: 2,
                },

                productDescribe: {
                    required: true,
                    minlength: 15,
                },
            },
            messages:{
                productName: {
                    required: "请输入商品名",
                    minlength: "商品名至少由2个字母/数字/汉字组成"
                },
                productDescribe: {
                    required: "请输入产品描述",
                    minlength: "至少输入15个字"
                    // rangelength: "密码长度为 6-14 个字符",
                },
            }
        })
    })


    $.validator.setDefaults({
        submitHandler: function(form) {
            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#productName").val().match(pattern);

            if(result){
                layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }
            var loading = layer.load();
            var formData = new FormData($('#fm')[0]);
            $.ajax({
                type: 'post',
                url: "<%=request.getContextPath()%>/product/update",
                data: formData,
                cache: false,
                processData: false,
                contentType: false,
            }).success(function (data) {
                layer.msg(data.msg, {
                    icon: 1,shade: 0.01,time: 500
                }, function(){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    parent.location.href = "<%=request.getContextPath()%>/product/toList";
                });

            }).error(function () {
                layer.msg(data.msg, {icon: 5,time:1000});
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
                        parent.location.href = "<%=request.getContextPath()%>/product/toList";


    }


    /*下架*/
    function upStatus(id, skuStatus) {
        if(skuStatus == 0){
            var skuStatus = 1;
        } else {
            var skuStatus = 0;
        }
        var proId = $("#id").val();
        var index1 = layer.load();
        $.post(
            "<%=request.getContextPath()%>/product/updateStatus/"+id,
            {"skuStatus":skuStatus},
            function(data){
                if(data.code == 200){
                    layer.msg(data.msg, {
                        icon: 1,shade: 0.01,time: 500
                    }, function(){
                       /* var index = parent.layer.getFrameIndex(window.name);*/
                        window.layer.close(index1);
                        window.location.href = "<%=request.getContextPath()%>/product/toUpdate/"+proId;
                    });
                } else {
                    layer.close(index1);
                    layer.msg(data.msg,{icon: 5,time:500});
                }
            }
        );

    }

   /* 设置为默认*/
    function upDefault() {
        var obj = $("input[name='idcheck']:checked");
        if (obj.length <= 0) {
            layer.msg('至少选择一项', {time: 500, icon: 5});
            return;
        }
        if (obj.length > 1) {
            layer.msg('只能选择一项', {time: 500, icon: 5});
            return;
        }
        var id = $(obj[0]).val();
        var proId = $("#id").val();
        var index1 = layer.load();
        $.post(
            "<%=request.getContextPath()%>/product/updateDefault/"+id,
            {"skuDefault":1},
            function(data){
                if(data.code == 200){
                    layer.msg(data.msg, {
                        icon: 1,shade: 0.01,time: 500
                    }, function(){
                        /* var index = parent.layer.getFrameIndex(window.name);*/
                        window.layer.close(index1);
                        window.location.href = "<%=request.getContextPath()%>/product/toUpdate/"+proId;
                    });
                } else {
                    layer.close(index1);
                    layer.msg(data.msg,{icon: 5,time:500});
                }
            }
        );

    }



    /* 	去编辑 */
    function upSku() {
        var obj = $("input[name='idcheck']:checked");
        if (obj.length <= 0) {
            layer.msg('至少选择一项', {time: 500, icon: 5});
            return;
        }
        if (obj.length > 1) {
            layer.msg('只能选择一项', {time: 500, icon: 5});
            return;
        }
        var id = $(obj[0]).val();
        var proId = $("#id").val();
        layer.open({
            type: 2,
            title: '编辑',
            shadeClose: true,
            shade: 0.8,
            area: ['380px', '90%'],
            content:'<%=request.getContextPath()%>/product/toUpdateEditor/'+id+"/"+proId ,
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });
    }





    /* 	去修改库存 */
    function upCount() {
        var obj = $("input[name='idcheck']:checked");
        if (obj.length <= 0) {
            layer.msg('至少选择一项', {time: 500, icon: 5});
            return;
        }
        if (obj.length > 1) {
            layer.msg('只能选择一项', {time: 500, icon: 5});
            return;
        }
        var id = $(obj[0]).val();
        var proId = $("#id").val();
        layer.open({
            type: 2,
            title: '修改库存',
            shadeClose: true,
            shade: 0.8,
            area: ['380px', '90%'],
            content:'<%=request.getContextPath()%>/product/toUpdateCount/'+id+"/"+proId ,
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });
    }





























</script>
</html>
