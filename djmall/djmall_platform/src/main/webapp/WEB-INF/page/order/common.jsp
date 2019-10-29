<%--
  Created by IntelliJ IDEA.
  User: 刘振国
  Date: 2019/4/11
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>评价</title>
    <%--<script src="//cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>--%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <%--token--%>
    <script src="<%=request.getContextPath()%>/res/cookie.js"></script>
    <script src="<%=request.getContextPath()%>/res/token.js"></script>
    <style>
        @font-face {
            font-family: 'iconfont'; /* project id 247957 */
            src: url('//at.alicdn.com/t/font_wkv6intmx8cnxw29.eot');
            src: url('//at.alicdn.com/t/font_wkv6intmx8cnxw29.eot?#iefix') format('embedded-opentype'),
            url('//at.alicdn.com/t/font_wkv6intmx8cnxw29.woff') format('woff'),
            url('//at.alicdn.com/t/font_wkv6intmx8cnxw29.ttf') format('truetype'),
            url('//at.alicdn.com/t/font_wkv6intmx8cnxw29.svg#iconfont') format('svg');
        }
        .iconfont {
            font-family:"iconfont" !important;
            font-size:16px;
            font-style:normal;
            -webkit-font-smoothing: antialiased;
            -webkit-text-stroke-width: 0.2px;
            -moz-osx-font-smoothing: grayscale;
        }
        body{
            font-family: "microsoft yahei";
        }
        ul,li{
            list-style: none;
            padding:0;
            margin:0;
        }
        a{
            text-decoration: none;
        }
        .order-evaluation {
            width: 827px;
            border: 2px solid #E84E40;
            background: #fff;
            z-index: 200;
            margin:50px auto;
        }
        .order-evaluation{
            padding:22px;
        }
        .order-evaluation h4{
            font-size:18px;
            color:#333333;
            padding-bottom:20px;
            border-bottom:1px dashed #dbdbdb;
        }
        .order-evaluation p{
            font-size:14px;
            color:#999;
            line-height:45px;
            margin-bottom:0;
        }
        .order-evaluation .order-evaluation-text{
            font-size:16px;
            color:#333;
            line-height:40px;
            width:809px;
            padding-left:15px;
            background:#f3f3f3;
            margin-bottom:25px;
            margin-top:20px;
        }
        .order-evaluation-checkbox ul li{
            width:142px;
            height:43px;
            border:1px solid #e8e8e8;
            text-align: center;
            background: #fff;
            font-size:14px;
            color:#333333;
            line-height:43px;
            margin-right:25px;
            margin-bottom:25px;
            float:left;
            cursor: pointer;
            overflow: hidden;
            position:relative;
        }
        .order-evaluation-checkbox ul li.checked i{
            display: block;
        }
        .order-evaluation-checkbox ul li.checked{
            border:1px solid #e84c3d;
        }
        .order-evaluation .order-evaluation-textarea{
            position:relative;
            width: 784px;
            height: 210px;
        }
        .order-evaluation .order-evaluation-textarea textarea{
            width:793px;
            height:178px;
            border:1px solid #e8e8e8;
            position:absolute;
            top:0;
            left:0;
            line-height:22px;
            padding:15px;
            color:#666;
        }
        .order-evaluation .order-evaluation-textarea span{
            position:absolute;
            bottom:10px;
            font-size:12px;
            color:#999;
            right:10px;
        }
        .order-evaluation .order-evaluation-textarea span em{
            color:#e84c3d;
        }
        .order-evaluation>a{
            width:154px;
            height:48px;
            border-radius: 6px;
            display: block;
            text-align: center;
            line-height:48px;
            background:#f36a5a;
            float:right;
            margin-top:20px;
            color:#fff;
            font-size:14px;
        }
        .order-evaluation-checkbox ul li i {
            display: none;
            color: #e84c3d;
            position: absolute;
            right: -4px;
            bottom: -14px;
            font-size: 20px;
        }
        /*.order-evaluation>a:hover{*/
            /*background: #e84c3d;*/
        /*}*/
        .block li label,.con span{ font-size: 18px; margin-right: 20px; line-height: 23px;}
        .block li span{display: inline-block; vertical-align: middle; cursor: pointer;}
        .block li span img{margin-right: -5px; }
        .level{color:#e84c3d;font-size:16px;margin-left:15px;position: relative;top: 3px;}
        .dmlei_tishi_info{
            height:70px;border-radius: 10px;background: rgba(0,0,0,0.5);font-size:18px;color:#fff;text-align: center;line-height: 70px;position:fixed;
            left: 48%;
            display: none;
            margin-left: -128px;
            top: 40%;
            margin-top: -35px;
            padding: 0 15px;
            z-index: 1000;
        }

        .myfloatleft{float:left;}
    </style>
</head>
<body bgcolor="#ffc0cb">
<form id="fmt">
    <input type="hidden" name="orderSonNum" value="${orderSonNum}"/>
    <input type="hidden" name="score" id="one"/><%--隐藏域  获取星星数--%>

    <div class="order-evaluation clearfix">
        <h4 style="color: #00a0e9">给本次购买商品评价</h4>
        <p>请严肃认真对待此次评价哦！您的评价对我们真的真的非常重要！</p>
        <div class="block">
            <ul>
                <li data-default-index="0">
            <span>
                <div id="showdiv">
                    <div id="1" class="myfloatleft" onmouseover="test('1')" onmouseout="myclear()"><h1>☆</h1></div>
                    <div id="2" class="myfloatleft" onmouseover="test('2')" onmouseout="myclear()"><h1>☆</h1></div>
                    <div id="3" class="myfloatleft" onmouseover="test('3')" onmouseout="myclear()"><h1>☆</h1></div>
                    <div id="4" class="myfloatleft" onmouseover="test('4')" onmouseout="myclear()"><h1>☆</h1></div>
                    <div id="5" class="myfloatleft" onmouseover="test('5')" onmouseout="myclear()"><h1>☆</h1></div>
                </div>&nbsp;&nbsp;
                <span id="mydiv" style="color: red"></span>

            </span>
                    <em class="level"></em>
                </li>
            </ul>
        </div>
        <div class="order-evaluation-text">
            本次交易评价，乖，摸摸头 给您留下了什么印象呢？
        </div>

        <div class="order-evaluation-textarea">
            <textarea name="context" id="TextArea1" onkeyup="words_deal();" ></textarea>
            <span>还可以输入<em id="textCount">140</em>个字</span>
        </div>
        <a href="javascript:publish();" rel="external nofollow" id="order_evaluation">评价完成</a>
    </div>
    <div id="order_evaluate_modal" class="dmlei_tishi_info"></div>
</form>
</body>
<script>
    //评价完成
    function publish() {
        var val1 = $("#one").val();//获取星数
        // alert(val1)
        var TextArea1 = $('#TextArea1').val();//评论
        // alert(TextArea1)
        var loading = layer.load();

            token_post("<%=request.getContextPath()%>/order/insertCommon",
            $("#fmt").serialize(),
            function (data) {
                if (data.code == 200) {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 6,time: 600}, function(){
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                        parent.location.href = "<%=request.getContextPath()%>/order/toOrderList?token="+cookie.get("TOKEN");
                        return;
                    });
                }else {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 5,time:1000});
                }
            }
        )
    }

    //获取星星数进行判断
    function test(myvalue){
        if (myvalue == 1) {
            $("#showdiv").append($("#mydiv").html("差评"));
        }
        if (myvalue == 2) {
            $("#showdiv").append($("#mydiv").html("较差"));
        }
        if (myvalue == 3) {
            $("#showdiv").append($("#mydiv").html("中等"));
        }
        if (myvalue == 4) {
            $("#showdiv").append($("#mydiv").html("一般"));
        }
        if (myvalue == 5) {
            $("#showdiv").append($("#mydiv").html("好评"));
        }
        for(var i=1;i<=5;i++){//将所有都变白星星
            document.getElementById(""+i).innerHTML="<h1>☆</h1>";
            document.getElementById(""+i).setAttribute("style","color:black");
        }
        for(var i=1;i<=myvalue;i++){//通过传入的id（也是一个数字）确定id以下的div都是橙色星星。
            document.getElementById(""+i).innerHTML="<h1>★</h1>";
            document.getElementById(""+i).setAttribute("style","color:orange");
        }
        $("#one").val(myvalue);//将星数放到隐藏域
    }


    //评价字数限制
    function words_deal()
    {
        var curLength=$("#TextArea1").val().length;
        if(curLength>140)
        {
            var num=$("#TextArea1").val().substr(0,140);
            $("#TextArea1").val(num);
            alert("超过字数限制，多出的字将被截断！" );
        }
        else
        {
            $("#textCount").text(140-$("#TextArea1").val().length);
        }
    }
    $("#order_evaluation").click(function(){
        $("#order_evaluate_modal").html("感谢您的评价！么么哒(* ￣3)(ε￣ *)").show().delay(3000).hide(500);
    })
</script>
</html>
