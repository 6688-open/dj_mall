<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/timecountdown.js"></script>
</head>
<body>

<style>
    *{padding: 0; margin: 0; font-family: "Lato", sans-serif}
    i{font-size: 14px}
    body{background: ; color: #921AFF}
    body{background: ; color: #ffffff}
    body{background: ; color: #921AFF}
    div{font-size: 20px; text-align: center; padding-top: 15px;}
    div span{display: inline-block; width: 50px; height: 50px;padding: 10px; border-radius: 5px}
    p{font-size: 18px}
    span.red{background: red}
    span.yg{background: yellowgreen}
    span.or{background: orange}
    span.pi{background: palevioletred}
    span.pk{background: #00a0e9}
    span.mk{background: #1FADC5}
    span p:first-child{font-size: 25px}
    span p:last-child{font-size: 14px}
</style>
	
</body>
<script type="text/javascript">

/**
 * time.day 和 time.dayZero 区别
 * time.day 如果是1天 返回 1
 * time.dayZero 如果是1天 则返回 01
 * 除了day拥有Zero外 hour,minute,second,msec都有Zero 即小于10的，都会在前面补0
 */
window.onload=function(){
    xcsoft.countdown('2019-11-07',function (time) {
        //time.days=总天数
        document.getElementById("y1").innerHTML=time.year
        document.getElementById("d1").innerHTML=time.day
        document.getElementById("h1").innerHTML=time.hourZero
        document.getElementById("i1").innerHTML=time.minuteZero
        document.getElementById("s1").innerHTML=time.secondZero
        document.getElementById("m1").innerHTML=time.msecZero
    },function (time) {
        //倒计时结束后的操作
    })
    xcsoft.countup('2018-11-01',function (time) {
        document.getElementById("y2").innerHTML=time.year
        document.getElementById("d2").innerHTML=time.day
        document.getElementById("h2").innerHTML=time.hourZero
        document.getElementById("i2").innerHTML=time.minuteZero
        document.getElementById("s2").innerHTML=time.secondZero
    })

    var nes=parseInt(new Date().getTime()/1000)+60;
    xcsoft.countdown(parseInt(nes)+'.3',function (time) {
        document.getElementById("s3").innerHTML=time.secondZero
        document.getElementById("m3").innerHTML=time.msecZero
    })
}



$(function(){
    //用户级别 经理
    var user = ${user1.lever};
    // alert(user)
    var userId = ${user1.id};
    var message = [1,2];
    var messages = 0;
    for(var i = 0; i < message.length; i++) {
        messages += "," + message[i];
    }
    // alert(ids.substring(2, 6));//截取
    if(user == 1){//卖家
        $.post(
            "<%=request.getContextPath() %>/product/findOrderAdmin",
            {"message":messages, "userId":userId, "orderSonStatus":2},//message 已支付 userId 等前登陆人ID
            function(data){
                if(data.code == 200){
                    layer.open({
                        content: "你还有未审核的订单，确认要去处理吗？？？"
                        , btn: ['确定','取消'],
                        style: 'width:80%',
                        yes: function(index, layero){
                            window.location.href="<%= request.getContextPath()%>/user/toOrderList"
                        },
                        cancel: function(index,layero){
                        },
                    });
                }else{

                }
            }
        )
    }
})
</script>

<div>
    <i>距2019年11月07日小宝贝生日还有</i>
    <div>
        <span class="red">
            <p id="y1"></p>
            <p>年</p>
        </span>
        <span class="yg">
            <p id="d1"></p>
            <p>天</p>
        </span>
        <span class="or">
            <p id="h1"></p>
            <p>时</p>
        </span>
        <span class="pi">
            <p id="i1"></p>
            <p>分</p>
        </span>
        <span class="pk">
            <p id="s1"></p>
            <p>秒</p>
        </span>
        <span class="mk">
            <p id="m1"></p>
            <p>毫秒</p>
        </span>
    </div>
</div>


<div>
    <i>从2018年11月01日至今我们在一起已经过去</i>
    <div>
        <span class="red">
            <p id="y2"></p>
            <p>年</p>
        </span>
        <span class="yg">
            <p id="d2"></p>
            <p>天</p>
        </span>
        <span class="or">
            <p id="h2"></p>
            <p>时</p>
        </span>
        <span class="pi">
            <p id="i2"></p>
            <p>分</p>
        </span>
        <span class="pk">
            <p id="s2"></p>
            <p>秒</p>
        </span>
    </div>
</div>


<div>
    <i>60秒倒计时</i>
    <div>
          <span class="pk">
            <p id="s3"></p>
            <p>秒</p>
        </span>
        <span class="mk">
            <p id="m3"></p>
            <p>毫秒</p>
        </span>
    </div>
</div>
</body>

</html>