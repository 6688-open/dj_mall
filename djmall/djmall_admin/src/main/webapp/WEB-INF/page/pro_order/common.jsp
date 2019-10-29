<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/css/layui.css"  media="all">
<script type="text/javascript" src = "<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src = "<%=request.getContextPath()%>/res/layer/layer.js"></script>
<script type="text/javascript" src = "<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/layui.js"></script>
</head>

<body>
	<br>
    <input type="button" value="返回首页"  style="color:red" onclick="exit()"/><br/>
<h1></h1>
	
    <form id="fm">

    <fieldset style="width:600px; height: 100px ">
        <legend > 商品评论</legend>
        好评率:<h1 style="color: red"><span  id="h1">${goodRate}%</span> </h1>
        <input  type="radio" value="1" name="score"  onclick="toScore()"  checked="checked"/>所有评论
        <input  type="radio" value="2" name="score" onclick="toScore()"  />好评
        <input  type="radio" value="3" name="score"  onclick="toScore()")/>中评
        <input  type="radio" value="4" name="score"  onclick="toScore()" />差评
    </fieldset>

        <div id="tb"></div>
        <div id="pagediv"></div>

        <input type="hidden" value="${id}" id="productId" name="productId"/>
        <input type="hidden" value="1" id="pageNo" name="pageNo"/>
        <input type="hidden" name="search" onclick="search()"/>
    </form>
</body>
<script type="text/javascript">
    /*返回首页*/
 /*   function exit(){
        layer.confirm('是否返回首页？', {
            btn: ['YES', 'NO'],
            btn1: function(index, layero){
                JavaScript:parent.window.location.href="<%=request.getContextPath() %>/user/toIndex";
            },
            btn2:function(index, layero){
                return;
            }
        })
    }*/



    function exit(){
        window.location.href="<%=request.getContextPath() %>/product/toList";
    }






    /*评论*/
    $(function () {
        search();
    });
    function search() {
        $.post(
            "<%=request.getContextPath()%>/product/commonList",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.list.length; i++) {
                    var com = data.data.list[i];
                    html += "<fieldset  style='width:600px; height: 300px*(com.replyList.length) '>";
                    html += " "+com.username+" ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    if(com.score == 1){
                        html += "<span style='font-size: xx-large; color: yellow'>★</span sty> <br><br>";
                    }
                    if(com.score == 2){
                        html += "<span style='font-size: xx-large; color: yellow'>★★</span sty> <br><br>";
                    }
                    if(com.score == 3){
                        html += "<span style='font-size: xx-large; color: yellow'>★★★</span sty> <br><br>";
                    }
                    if(com.score == 4){
                        html += "<span style='font-size: xx-large; color: yellow'>★★★★</span sty> <br><br>";
                    }
                    if(com.score == 5){
                        html += "<span style='font-size: xx-large; color: yellow'>★★★★★</span sty> <br><br>";
                    }

                    html += "<span style='color: grey'>"+com.createTimeShow+"</span>";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "<span style='font-size: x-large'>"+com.context+"</span>";
                    html += "<br>";
                    for (var j = 0; j < com.replyList.length ; j++) {
                        var rep = com.replyList[j];
                        html += "<span style='color: grey'>"+rep.createTimeShow+"</span>";
                        html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        if(rep.isUser == 1){
                            html += " "+com.username+": ";
                        } else {
                            html += "<span style='font-size: x-large; color: red'>商家回复：</span>";
                        }
                        html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        html += "<span style='font-size: x-large'>"+rep.context+"</span>";
                        html += "<br>";
                        html += "<hr>";
                    }
                        html += "<input type='button' value='回复' onclick='com("+com.id+")'>"
                    html += "</fieldset>";

                }
                $("#tb").append(html);
                if(data.data.goodRate == 0){
                    $("#h1").html("暂无评论");
                } else {
                    $("#h1").html(data.data.goodRate);
                }


                var pagehtml = "";
                /* pagehtml += "当前"+(data.data.pageNo)+"页";*/
                pagehtml += "<input type='button' value='加载更多' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                /* pagehtml += "总"+(data.data.totalPage)+"页";*/
                $("#pagediv").html(pagehtml);
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
            /*layer.msg('已经是第一页了', {time: 500, icon:5});*/
            $("#pagediv").html("没有更多数据了");
            return;
        }
        search();
    }

   function toScore() {
       $("#tb").html("");
       search();
   }


   /*去回复*/
    function com(id) {
        var productId = $("#productId").val();
        layer.open({
            type: 2,
            title: '回复',
            shadeClose: true,
            shade: 0.8,
            area: ['50%', '70%'],
            content:'<%=request.getContextPath()%>/product/toReply/'+id+"/"+productId,
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });
    }


</script>



</html>