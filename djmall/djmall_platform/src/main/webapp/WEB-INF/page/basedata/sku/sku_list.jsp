<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<br>





    <form id = "fmt">
        <input type="hidden" name="pageNo" id = "pageNo" value="1" />
        <input type="hidden" value="search" onclick="search(page(1))" />
    </form>

    <h2 align="center">SKU信息展示</h2>
    <table border="1px" align="center" cellspacing="0px">
        <tr >
            <th>编号</th>
            <th>code</th>
            <th>商品类型</th>
            <th>属性名</th>
            <th>操作</th>
        </tr>
        <tbody id = "tbody"></tbody>
    </table>
    ${msg} <br/>
    <div align="center" id = "pagediv"></div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        $.ajaxSetup({
            layerIndex:-1,
            beforeSend: function () { //插件加载前
                this.layerIndex = layer.load(0, { shade: [0.5, '#fff'] });
                $('#submit').val("正在登录")
            },
            complete: function () { //完成加载后执行
                layer.close(this.layerIndex); //完成加载后关闭loading
            },
            error: function () { //报错时执行
                layer.alert('显示异常，请刷新后重试', {
                    skin: 'layui-layer-molv'
                    , closeBtn: 0
                    , shift: 4 //动画类型
                });
            }
        });
    });


    $(function () {
        search();
    });

    function search() {
        $.get(
            "<%=request.getContextPath()%>/sku/skuList",
            $("#fmt").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.skuList.length; i++) {
                    var sku = data.data.skuList[i];

                    html += "<tr>";
                    html += "<td>"+sku.id+"</td>";
                    html += "<td>"+sku.productType+"</td>";
                    html += "<td>"+sku.attrShow+"</td>";
                    html += "<td>"+sku.codeShow+"</td>";
                    html += "<td><input type='button' value='关联属性' onclick='up("+sku.id+",\""+sku.productType+"\")'/></td>";
                    html += "</tr>";

                }
                $("#tbody").html(html);
                /*var divhtml = "";
                divhtml += "<input type='button' value='新增' onclick='add()'/>";
                $("#div").html(divhtml);*/

                /*  var pagehtml = "";
                  pagehtml += "当前"+(data.data.pageNo)+"页";
                  pagehtml += "<input type='button' value='上一页' onclick='page("+(eval(data.data.pageNo)-1)+" , "+data.data.totalPage+"  )' />";
                  pagehtml += "<input type='button' value='下一页' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                  pagehtml += "总"+(data.data.totalPage)+"页";
                  $("#pagediv").html(pagehtml);*/
            }
        );

    }
    /*分页*/
    function page(pageNo,totalPage) {
        $("#pageNo").val(pageNo);
        if(pageNo < 1){
            layer.msg('已经是第一页了', {time: 500, icon:6});
            return;
        }
        if(pageNo > totalPage ){
            layer.msg('已经是第一页了', {time: 500, icon:5});
            return;
        }
        search();
    }


    function up(id, productType) {
        layer.open({
            type: 2,
            title: '关联属性',
            shadeClose: true,
            shade: 0.8,
            area: ['480px', '100%'],
            content:'<%=request.getContextPath()%>/sku/relationAttrSKU/'+productType,
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });
    }



    // 页面加载事件
   /* $(function () {
        // 在键盘按下并释放及提交后验证提交表单
        $("#fm").validate({
            errorPlacement: function(error,element) {
                //将错误信息展示在父元素的后面
                $(element).parent().after(error);
            },
            rules:{
                /!*对应input框的name值*!/
                attrName: {
                    required: true,
                    minlength: 1

                },

            },
            messages:{
                attrName: {
                    required: "请输入属性名",
                    minlength: "属性名称至少由2个字母/数字/汉字组成"
                },
            }
        })
    })
*/





    /*新增*/
  /*  $.validator.setDefaults({
        submitHandler: function(form) {

            var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%]");

            var result = $("#attrName").val().match(pattern);

            if(result){
                layer.msg("分类名称中含有特殊字符", {icon: 5,time:1000});
                return false;
            }


            var loading=layer.load();
            $.post(
                "<%=request.getContextPath()%>/sku/insert",
                $("#fm").serialize(),
                function (data) {
                    if(data.code == 200){
                        layer.msg(data.msg, {icon: 6},function () {
                            window.location.href = "<%=request.getContextPath()%>/sku/toList"

                        })
                        return
                    }
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 5})
                }

            );


        }
    })*/






</script>
</html>
