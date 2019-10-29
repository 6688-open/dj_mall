<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>支付完成倒计时页面</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <%--token--%>
    <script src="<%=request.getContextPath()%>/res/cookie.js"></script>
    <script src="<%=request.getContextPath()%>/res/token.js"></script>
</head>
<style>
    body {
        margin: 0;
        padding: 0;
        background: #E6EAEB;
        font-family: Arial, '微软雅黑', '宋体', sans-serif
    }
    .alert-box {
        display: none;
        position: relative;
        margin: 96px auto 0;
        padding: 180px 85px 22px;
        border-radius: 10px 10px 0 0;
        background: #FFF;
        box-shadow: 5px 9px 17px rgba(102,102,102,0.75);
        width: 286px;
        color: #FFF;
        text-align: center
    }
    .alert-box p {
        margin: 0
    }
    .alert-circle {
        position: absolute;
        top: -50px;
        left: 111px
    }
    .alert-sec-circle {
        stroke-dashoffset: 0;
        stroke-dasharray: 735;
        transition: stroke-dashoffset 1s linear
    }
    .alert-sec-text {
        position: absolute;
        top: 11px;
        left: 190px;
        width: 76px;
        color: #000;
        font-size: 68px
    }
    .alert-sec-unit {
        font-size: 34px
    }
    .alert-body {
        margin: 35px 0
    }
    .alert-head {
        color: #242424;
        font-size: 28px
    }
    .alert-concent {
        margin: 25px 0 14px;
        color: #7B7B7B;
        font-size: 18px
    }
    .alert-concent p {
        line-height: 27px
    }
    .alert-btn {
        display: block;
        border-radius: 10px;
        background-color: #4AB0F7;
        height: 55px;
        line-height: 55px;
        width: 286px;
        color: #FFF;
        font-size: 20px;
        text-decoration: none;
        letter-spacing: 2px
    }
    .alert-btn:hover {
        background-color: #6BC2FF
    }
    .alert-footer {
        margin: 0 auto;
        height: 42px;
        width: 120px
    }
    .alert-footer-icon {
        float: left
    }
    .alert-footer-text {
        float: left;
        border-left: 2px solid #EEE;
        padding: 3px 0 0 5px;
        height: 40px;
        color: #0B85CC;
        font-size: 12px;
        text-align: left
    }
    .alert-footer-text p {
        color: #7A7A7A;
        font-size: 22px;
        line-height: 18px
    }
</style>
<script type="text/javascript">
    // alert(cookie.get("TOKEN"))
    function alertSet(e) {
        // alert(cookie.get("TOKEN"))
        document.getElementById("js-alert-box").style.display = "block",
            document.getElementById("js-alert-head").innerHTML = e;
        var t = 6,
            n = document.getElementById("js-sec-circle");
        document.getElementById("js-sec-text").innerHTML = t,
            setInterval(function() {
                    if (0 == t){
                        JavaScript:parent.window.location.href="<%=request.getContextPath()%>/toIndex?token="+cookie.get("TOKEN");//#时间到后跳转地址
                    }else {
                        t -= 1,
                            document.getElementById("js-sec-text").innerHTML = t;
                        var e = Math.round(t / 6 * 735);
                        n.style.strokeDashoffset = e - 735
                    }
                },
                970);
    }
</script>
<body bgcolor="#ffc0cb">
    <div id="js-alert-box" class="alert-box">
        <svg class="alert-circle" width="234" height="234">
            <circle cx="117" cy="117" r="108" fill="#FFF" stroke="#43AEFA" stroke-width="17"></circle>
            <circle id="js-sec-circle" class="alert-sec-circle" cx="117" cy="117" r="108" fill="transparent" stroke="#F4F1F1" stroke-width="18" transform="rotate(-90 117 117)"></circle>
            <text class="alert-sec-unit" x="82" y="172" fill="#BDBDBD">&nbsp;&nbsp;秒</text>
        </svg>
        <div id="js-sec-text" class="alert-sec-text"></div>
        <div class="alert-body">
            <div id="js-alert-head" class="alert-head"></div>
        </div>
    </div>
</body>
</html>
<script>alertSet('支付确认中,请稍后...');</script>