<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/3/31
  Time: 14:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/md5-min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/res/jsencrypt/jsencrypt.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
    <script src="<%=request.getContextPath()%>/res/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
    <script src="<%=request.getContextPath()%>/res/cookie.js"></script>
    <script src="<%=request.getContextPath()%>/res/token.js"></script>
    <script src="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/layui.js"></script>
    <link href="<%=request.getContextPath()%>/res/layui-v2.4.5/layui/css/layui.css" rel="stylesheet" />
</head>
<body>
<form id="fm">
    <input type="hidden" name="search" onclick="search()"/>
    <input type="hidden" name="search1" onclick="search1()"/>
    <input type="hidden" name="search2" onclick="search2()"/>
    <input type="hidden" name="search3" onclick="search3()"/>
    <input type="hidden" id="pageNo" name="pageNo" value="1"/>
    <input type="hidden" id="token" name="token" />
    <input type="hidden" id="orderSonStatus" name="orderSonStatus" />
</form>



<div class="layui-tab">
    <ul class="layui-tab-title">
        <li>待付款</li>
        <li >待收货</li>
        <li >已完成</li>
        <li >已取消</li>
    </ul>
    <div class="layui-tab-content">
        <div class="layui-tab-item layui-show" >
            <table border="1px" cellspacing="0px" cellpadding="0px" align="center">
                <thead>
                <tr>
                    <td>订单号</td>
                    <td>商品名称</td>
                    <td>购买数量</td>
                    <td>付款金额（包含邮费）</td>
                    <td>支付方式</td>
                    <td>邮费</td>
                    <td>下单时间</td>
                    <td>订单状态</td>
                </tr>
                </thead>
                <tbody id="tb"></tbody>
            </table><br><br>
            <div id="pagediv"></div>
        </div>
        <div class="layui-tab-item">
            <table border="1px" cellspacing="0px" cellpadding="0px" align="center">
                <thead>
                <tr>
                    <td>订单号</td>
                    <td>商品信息</td>
                    <td>购买数量</td>
                    <td>折扣</td>
                    <td>付款金额（包含邮费）</td>
                    <td>支付方式</td>
                    <td>邮费</td>
                    <td>下单时间</td>
                    <td>支付时间</td>
                    <td>订单状态</td>
                </tr>
                </thead>
                <tbody id="tb2"></tbody>
            </table><br><br>
            <div id="pagediv2"></div>
        </div>
        <div class="layui-tab-item">
            <table border="1px" cellspacing="0px" cellpadding="0px" align="center">
                <thead>
                <tr>
                    <td>订单号</td>
                    <td>商品信息</td>
                    <td>购买数量</td>
                    <td>折扣</td>
                    <td>付款金额（包含邮费）</td>
                    <td>支付方式</td>
                    <td>邮费</td>
                    <td>下单时间</td>
                    <td>支付时间</td>
                    <td>订单状态</td>
                </tr>
                </thead>
                <tbody id="tb3"></tbody>
            </table><br><br>
            <div id="pagediv3"></div>
        </div>
        <div class="layui-tab-item">
            <table border="1px" cellspacing="0px" cellpadding="0px" align="center">
                <thead>
                <tr>
                    <td>订单号</td>
                    <td>商品信息</td>
                    <td>购买数量</td>
                    <td>折扣</td>
                    <td>付款金额（包含邮费）</td>
                    <td>支付方式</td>
                    <td>邮费</td>
                    <td>下单时间</td>
                    <td>取消时间</td>
                    <td>订单状态</td>
                </tr>
                </thead>
                <tbody id="tb4"></tbody>
            </table><br><br>
            <div id="pagediv4"></div>
        </div>
    </div>
</div>

</body>
<script>
    //注意：选项卡 依赖 element 模块，否则无法进行功能性操作
    layui.use('element', function(){
        var element = layui.element;

        //…
    });



    /*待支付的展示*/
    $(function () {
        search();
    });
    function search() {
        $("#token").val(cookie.get("TOKEN"));
        token_post(
            "<%=request.getContextPath()%>/order/waitPayList",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.list.length; i++) {
                    var order = data.data.list[i];

                    html += "<tr>";
                    html += "<td>";
                    html += "<a href='javascript:proParentNumClick(\""+order.parentNum+"\")' style='color: blue' >"+order.parentNum+"</a> ";
                    html +=  "</td>";


                    html += "<td>"+order.productNameShow+"</td>";
                    html += "<td>"+order.number+"个</td>";
                    html += "<td>"+order.totalMoney+"元</td>";
                    html += "<td>"+order.payStatus+"</td>";
                    if(order.totalFreight == 0){
                        html += "<td>包邮</td>";
                    } else {
                        html += "<td> 平邮 "+order.totalFreight+"元</td>";
                    }
                    html += "<td>"+order.deliveryTime+"</td>";
                    html += "<td>";
                    html += "待支付 ";
                    html += "<a href='javascript:toPay(\""+order.parentNum+"\")' style='color: blue' >去支付</a> ";
                    html += "<a href='javascript:removeNum(\""+order.parentNum+"\")' style='color: deeppink' >取消订单</a> ";
                    html +=  "</td>";
                    html += "</tr>";
                }
                $("#tb").append(html);

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
    /*待收货*/
    $(function () {
        search1();
    });

    /*待收货*/
    function  search1() {
       /* $("#pageNo").val(1);*/
        $("#orderSonStatus").val(2);
        $("#token").val(cookie.get("TOKEN"));
        token_post(
            "<%=request.getContextPath()%>/order/orderStatus",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.list.length; i++) {
                    var order = data.data.list[i];

                    html += "<tr>";
                    html += "<td>";
                    html += "<a href='javascript:proSonNumClick(\""+order.orderSonNum+"\")' style='color: blue' >"+order.orderSonNum+"</a> ";
                    html +=  "</td>";
                    html += "<td>"+order.productName+"-"+order.skuAttrName+"</td>";
                    html += "<td>"+order.proNum+"个</td>";
                    if(order.rate == 0){
                        html += "<td>暂无折扣</td>";
                    } else {
                        html += "<td>"+order.rate+"%</td>";
                    }
                    html += "<td>"+order.totalMoney+"元</td>";
                    html += "<td>"+order.payStatus+"</td>";
                    if(order.freightMoney == 0){
                        html += "<td>包邮</td>";
                    } else {
                        html += "<td> 平邮 "+order.freightMoney+"元</td>";
                    }
                    html += "<td>"+order.deliveryTime+"</td>";
                    html += "<td>"+order.payTime+"</td>";
                    html += "<td>";
                    if(order.message == 1){
                        html += "<a href='javascript:tofahuo(\""+order.orderSonNum+"\")' style='color: blue' >提醒卖家发货</a> ";
                    }
                    if(order.message == 2){
                        html += "已提醒";
                    }
                    if(order.message == 3){
                        html += "<a href='javascript:sureShpuhuo(\""+order.orderSonNum+"\")' style='color: blue' >确认收货</a> ";
                    }

                    html +=  "</td>";
                    html += "</tr>";
                }
                $("#tb2").append(html);

                var pagehtml = "";
                /* pagehtml += "当前"+(data.data.pageNo)+"页";*/
                pagehtml += "<input type='button' value='加载更多' onclick='page2("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                /* pagehtml += "总"+(data.data.totalPage)+"页";*/
                $("#pagediv2").html(pagehtml);
            }
        );
    }

    /*分页*/
    function page2(pageNo,totalPage) {
        $("#pageNo").val(pageNo);
        if(pageNo < 1){
            layer.msg('已经是第一页了', {time: 500, icon:6});
            return;
        }
        if(pageNo > totalPage ){
            /*layer.msg('已经是第一页了', {time: 500, icon:5});*/
            $("#pagediv2").html("没有更多数据了");
            return;
        }
        search1();
    }


    /*已完成*/
    $(function () {
        search2();
    });
    /*已完成*/
    function search2() {
       /* $("#pageNo").val(1);*/
        $("#orderSonStatus").val(3);
        $("#token").val(cookie.get("TOKEN"));
        token_post(
            "<%=request.getContextPath()%>/order/orderStatus",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.list.length; i++) {
                    var order = data.data.list[i];

                    html += "<tr>";
                    html += "<td>";
                    html += "<a href='javascript:proSonNumClick(\""+order.orderSonNum+"\")' style='color: blue' >"+order.orderSonNum+"</a> ";
                    html +=  "</td>";
                    html += "<td>"+order.productName+"-"+order.skuAttrName+"</td>";
                    html += "<td>"+order.proNum+"个</td>";
                    if(order.rate == 0){
                        html += "<td>暂无折扣</td>";
                    } else {
                        html += "<td>"+order.rate+"%</td>";
                    }
                    html += "<td>"+order.totalMoney+"元</td>";
                    html += "<td>"+order.payStatus+"</td>";
                    if(order.freightMoney == 0){
                        html += "<td>包邮</td>";
                    } else {
                        html += "<td> 平邮 "+order.freightMoney+"元</td>";
                    }
                    html += "<td>"+order.deliveryTime+"</td>";
                    html += "<td>"+order.payTime+"</td>";
                    html += "<td>";
                    html += "<a href='javascript:appraise(\""+order.orderSonNum+"\")' style='color: blue' >评价晒单-- </a> ";
                    html += "<a href='javascript:buyAgain(\""+order.orderSonNum+"\")' style='color: red' > 再次购买</a> ";
                    html +=  "</td>";
                    html += "</tr>";
                }
                $("#tb3").append(html);

                var pagehtml = "";
                /* pagehtml += "当前"+(data.data.pageNo)+"页";*/
                pagehtml += "<input type='button' value='加载更多' onclick='page3("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                /* pagehtml += "总"+(data.data.totalPage)+"页";*/
                $("#pagediv3").html(pagehtml);
            }
        );
    }
    /*分页*/
    function page3(pageNo,totalPage) {
        $("#pageNo").val(pageNo);
        if(pageNo < 1){
            layer.msg('已经是第一页了', {time: 500, icon:6});
            return;
        }
        if(pageNo > totalPage ){
            /*layer.msg('已经是第一页了', {time: 500, icon:5});*/
            $("#pagediv3").html("没有更多数据了");
            return;
        }
        search2();
    }


    /*已取消*/
    $(function () {
        search3();
    });
    /*已取消*/
    function search3() {
       /* $("#pageNo").val(1);*/
        $("#orderSonStatus").val(4);
        $("#token").val(cookie.get("TOKEN"));
        token_post(
            "<%=request.getContextPath()%>/order/orderStatus",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i < data.data.list.length; i++) {
                    var order = data.data.list[i];

                    html += "<tr>";
                    html += "<td>";
                    html += "<a href='javascript:proSonNumClick(\""+order.orderSonNum+"\")' style='color: blue' >"+order.orderSonNum+"</a> ";
                    html +=  "</td>";
                    html += "<td>"+order.productName+"-"+order.skuAttrName+"</td>";
                    html += "<td>"+order.proNum+"个</td>";
                    if(order.rate == 0){
                        html += "<td>暂无折扣</td>";
                    } else {
                        html += "<td>"+order.rate+"%</td>";
                    }
                    html += "<td>"+order.totalMoney+"元</td>";
                    html += "<td>"+order.payStatus+"</td>";
                    if(order.freightMoney == 0){
                        html += "<td>包邮</td>";
                    } else {
                        html += "<td> 平邮 "+order.freightMoney+"元</td>";
                    }
                    html += "<td>"+order.deliveryTime+"</td>";
                    html += "<td>"+order.cancelTime+"</td>";
                    html += "<td>";
                    html += "<a href='javascript:buyAgain(\""+order.orderSonNum+"\")' style='color: blue' >再次购买</a> ";
                    html +=  "</td>";
                    html += "</tr>";
                }
                $("#tb4").append(html);

                var pagehtml = "";
              /*  pagehtml += "当前"+(data.data.pageNo)+"页";*/
                pagehtml += "<input type='button' value='加载更多' onclick='page4("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
                 /*pagehtml += "总"+(data.data.totalPage)+"页";*/
                $("#pagediv4").html(pagehtml);
            }
        );
    }

    /*分页*/
    function page4(pageNo,totalPage) {
        $("#pageNo").val(pageNo);
        if(pageNo < 1){
            layer.msg('已经是第一页了', {time: 500, icon:6});
            return;
        }
        if(pageNo > totalPage ){
            /*layer.msg('已经是第一页了', {time: 500, icon:5});*/
            $("#pagediv4").html("没有更多数据了");
            return;
        }
        search3();
    }


    /*父级订单*/
    function proParentNumClick(parentNum) {
        layer.open({
            type: 2,
            title: '添加',
            shadeClose: true,
            shade: 0.8,
            area: ['380px', '90%'],
            content:'<%=request.getContextPath()%>/order/toParentNumMsg?token='+cookie.get("TOKEN")+"&parentNum="+parentNum ,
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });

    }
    /*子集订单*/
    function proSonNumClick(sonNum) {
        layer.open({
            type: 2,
            title: '添加',
            shadeClose: true,
            shade: 0.8,
            area: ['380px', '90%'],
            content:'<%=request.getContextPath()%>/order/toSonNumMsg?token='+cookie.get("TOKEN")+"&sonNum="+sonNum ,
            end: function(){              //修改完刷新页面
                location.reload();
            }
        });

    }
    /*取消订单*/
    function removeNum(parentNum) {
        layer.confirm('确认取消订单？', {
            btn: ['YES', 'NO'],
            btn1: function (index, layero) {
                var loading=layer.load();
                token_post("<%=request.getContextPath()%>/order/delOrder?token="+cookie.get("TOKEN")+"&parentNum="+parentNum,
                    {},
                    function (data){
                        if(data.code == 200){
                            layer.msg(data.msg, {icon: 6},function () {
                                layer.close(loading);
                                window.location.href = "<%=request.getContextPath()%>/order/toOrderList?token="+cookie.get("TOKEN");

                            })
                            return
                        }
                        layer.close(loading);
                        layer.msg(data.msg, {icon: 5});
                    }
                )
            },
            btn2: function (index, layero) {
                return;
            }
        })
    }
    /*去支付*/
    function toPay(parentNum) {
        window.location.href = "<%=request.getContextPath()%>/order/toPay?token="+cookie.get("TOKEN")+"&parentNum="+parentNum;
    }

    /* 提醒卖家发货*/
    function tofahuo(orderSonNum) {
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/order/fahuoMsg?token="+cookie.get("TOKEN")+"&orderSonNum="+orderSonNum,
            {"message":2},
            function (data){
                if(data.code == 200){
                    layer.msg(data.msg, {icon: 6},function () {
                        layer.close(loading);
                        window.location.href = "<%=request.getContextPath()%>/order/toOrderList?token="+cookie.get("TOKEN");

                    })
                    return
                }
                layer.close(loading);
                layer.msg(data.msg, {icon: 5});
            }
        )
    }

    /* 确认收货*/
    function sureShpuhuo(orderSonNum) {
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/order/sureShouhuo?token="+cookie.get("TOKEN")+"&orderSonNum="+orderSonNum,
            {"orderSonStatus":3},
            function (data){
                if(data.code == 200){
                    layer.msg(data.msg, {icon: 6},function () {
                        layer.close(loading);
                        window.location.href = "<%=request.getContextPath()%>/order/toOrderList?token="+cookie.get("TOKEN");

                    })
                    return
                }
                layer.close(loading);
                layer.msg(data.msg, {icon: 5});
            }
        )
    }







    /* 去评价晒单*/
    function appraise(orderSonNum) {
            layer.open({
                type: 2,
                title: '评价',
                shadeClose: true,
                shade: 0.8,
                area: ['80%', '90%'],
                content:'<%=request.getContextPath()%>/order/toCommon?token='+cookie.get("TOKEN")+"&orderSonNum="+orderSonNum
            });
    }

    /*再次购买  加入购物车*/
    function buyAgain(orderSonNum) {
        window.location.href = "<%=request.getContextPath()%>/order/toBuyAgain?token="+cookie.get("TOKEN")+"&orderSonNum="+orderSonNum;
    }



</script>
</html>
