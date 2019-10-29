<%--
  Created by IntelliJ IDEA.
  User: dj
  Date: 2019/3/28
  Time: 21:18
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
<form id="fm" >
        <input  type="hidden"  value="search" onclick="search()" />
        <input  type="hidden" id="token"  value="token" name="token" />
        <input  type="hidden" id="ids"   name="ids" />
        <%--再次购买回显选中  和加入购物车回显选中--%>
        <input  type="text" id="proCarId" value="${proCarId}"   name="proCarId" />

<br><br>
<input type="checkbox" onclick="selectQuanOrFan()" id="checkAll"/> 全选      -------------
<a href="javascript:delIds()"  style="color: blue"> 删除选中的商品</a>-------------
<a href="javascript:reba()"  style="color: blue"> 返回首页</a>
<br><br>
<div id="div"></div> <br><br>
<div id="moneyDiv" align="center"></div>

</form>
<br><br>
<center>

   <%-- <input type="button" onclick="sub()" value="计算总金额" />--%>
    <input type="button" onclick="submi()" value="去结算" />
</center>

</form>
</body>
<script>

    /*勾选不同的sku  展示不同的价格*/
    function reba() {
        window.location.href="<%=request.getContextPath() %>/user/toIndex";
    }


    $(function () {
        search();
    })

    function search() {
        $("#token").val(cookie.get("TOKEN"));
        token_post(
            "<%=request.getContextPath()%>/user/shoppingCarList",
            $("#fm").serialize(),
            function (data) {
                var html = "";
                for (var i = 0; i <data.data.list.length ;   i++) {
                    var pro = data.data.list[i];
                    html += "<fieldset  style='width:600px; height: 150px '>";
                    html += "<legend > 商品详情</legend>";

                    if(pro.id == $("#proCarId").val()){
                        html += "<input type='checkbox' name='idcheck' onclick='check()' checked   value='"+pro.id+"'/>";
                    } else {
                        html += "<input type='checkbox' name='idcheck'  onclick='check()'   value='"+pro.id+"'/>"
                    }

                    html += "Id ： " + pro.id + "  ";
                    html += "<br>"
                    html += "名称 ： "+pro.productName+"  ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "原价 ：￥ "+pro.oldPrice+"  ";
                    html += "<br><br>"
                    html += "SKU ： "+pro.proSku+"  ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        if(pro.proRate == 0){
                         html += "折扣 ： 无， 按照原价  ";
                        } else {
                          html += "折扣 ： "+pro.proRate+" % ";
                        }
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "数量";
                    html += "<input type='button'  value='-' onclick='del("+pro.id+")' />";
                    html += "<input type='text' onchange='chan("+pro.id+")' id='"+pro.id +"' name='num'  size='3px' value=' "+pro.num+" '/>";
                    html += "<input type='button'  value='+' onclick='add("+pro.id+")' />";
                    html += "<span id='span"+pro.id+"'></span>"
                    html += "<br><br>";
                     if(pro.proFreight == 0){
                         html += "邮费 ： 包邮  ";
                     } else {
                          html += "邮费 ： "+pro.proFreight+" 元 ";
                     }
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += "现价 ：￥ "+pro.newPrice+"  ";
                    html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    html += " <a href='javascript:rem("+pro.id+")'   style='color: red'> 后悔了 ， 不买了</a>  ";


                    html += "</fieldset>";

                }
                $("#div").html(html);

            }
        );


    }




    /*		全选反选											*/
    function selectQuanOrFan(){
        var productBox = document.getElementsByName("idcheck");
        var checkBox = document.getElementById("checkAll");
        for(var i = 0; i < productBox.length; i++){
            if(productBox[i].checked == true){

                if(checkBox.checked == true){
                    productBox[i].checked = false;
                }else{
                    productBox[i].checked = true;
                }
            }else{
                productBox[i].checked = checkBox.checked;
            }
        }
    }

    /*批量删除*/
    function delIds() {
        var userIds = $('input[name="idcheck"]:checked');
        if (userIds.length <= 0) {
            layer.msg('至少选择一项', {time: 500, icon: 5});
            return false;
        }
        layer.confirm('确认删除？', {
            btn: ['YES', 'NO'],
            btn1: function (index, layero) {
                var idsArr = "";
                for (var i = 0; i < userIds.length; i++) {
                    idsArr += "," + $(userIds[i]).val();
                }
                var ids = idsArr.substring(1, idsArr.length);
                var loading=layer.load();
                token_post("<%=request.getContextPath()%>/user/deleteShoppingCarByids",
							{"ids": ids},
							function (data){
								if(data.code == 200){
									layer.msg(data.msg, {icon: 6},function () {
									window.location.href = "<%=request.getContextPath()%>/user/myShoppingCar?token="+cookie.get("TOKEN")

									})
									return
								}
								layer.close(loading);
								layer.msg(data.msg, {icon: 5})
							}
					)
				},
				btn2: function (index, layero) {
					return;
				}
			})


		}
		/*后悔了 不要了  移出*/
		function rem(ids) {
            layer.confirm('确认不要了？', {
                btn: ['YES', 'NO'],
                btn1: function (index, layero) {
                    var loading=layer.load();
                    token_post("<%=request.getContextPath()%>/user/deleteShoppingCarByids",
                        {"ids": ids},
                        function (data){
                            if(data.code == 200){
                                layer.msg(data.msg, {icon: 6},function () {
                                    window.location.href = "<%=request.getContextPath()%>/user/myShoppingCar?token="+cookie.get("TOKEN")

                                })
                                return
                            }
                            layer.close(loading);
                            layer.msg(data.msg, {icon: 5})
                        }
                    )
                },
                btn2: function (index, layero) {
                    return;
                }
            })
        }

      function chan(id) {
          add(id);
          del(id);
      }





    /*数量减少到数据库修改*/
    function del(id){
        if(isNaN($("#"+id).val())){
            layer.msg("请输入纯数字", {icon: 5,time:1000});
            return false;
        }
        if($("#"+id).val() < 2){
            layer.msg("购买数量至少为1", {icon: 5,time:1000});
        }
        if($("#"+id).val() > 1){
            $("#"+id).val($("#"+id).val() - 1);
        }
        var num = $("#"+id).val();
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/user/updateNum",
            {"id": id, "num":num},
            function (data){
                if(data.code == 200){
                    /*layer.msg(data.msg, {icon: 6},function () {
                        $("#span"+id).html(data.msg);
                        window.location.href = "<%=request.getContextPath()%>/user/myShoppingCar?token="+cookie.get("TOKEN")

                    })*/
                    layer.close(loading);
                    $("#span"+id).html(data.msg);
                    $("#"+id).val(data.data.num);
                    return
                }
                layer.close(loading);
                layer.msg(data.msg, {icon: 5})
            }
        )

    }




    
    
    
    /*数量增加到数据库修改*/
    function add(id){
        if(isNaN($("#"+id).val())){
            layer.msg("请输入纯数字", {icon: 5,time:1000});
            return false;
        }
        if($("#"+id).val() < 0){
            layer.msg("购买数量小于0", {icon: 5,time:1000});
        }
        if($("#"+id).val() >= 0){
            $("#"+id).val(parseInt($("#"+id).val())+1 );
        }

        var num = $("#"+id).val();
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/user/updateNum",
            {"id": id, "num":num},
            function (data){
                if(data.code == 200){
                   /* layer.msg(data.msg, {icon: 6},function () {
                        $("#span").html(data.msg);
                        window.location.href = "<%=request.getContextPath()%>/user/myShoppingCar?token="+cookie.get("TOKEN")

                    })
                    return*/
                    layer.close(loading);
                    $("#span"+id).html(data.msg);
                    $("#"+id).val(data.data.num);
                    return
                }
                layer.close(loading);
                layer.msg(data.msg, {icon: 5})
            }
        )

    }

    /*页面加载   默认选中的计算出价格*/
   $(function () {
       var ids =$("#proCarId").val() ;
       $("#ids").val(ids);
       if(ids == ""){
           return;
       }
       var loading=layer.load();
       token_post("<%=request.getContextPath()%>/user/checkCountAndMoney",
           $("#fm").serialize(),
           function (data){
               if(data.code == 200){
                   /* layer.msg(data.msg, {icon: 6},function () {*/
                   var html = "已选择 "+data.data.number+" 件商品， 总商品金额：￥"+data.data.totalMoney+" ,  运费：￥"+data.data.totalFreight+"  ";
                   html += "<br>";
                   html += "应付金额：￥"+data.data.finalMoney+" ";
                   $("#moneyDiv").html(html);


                   layer.close(loading);
                   layer.msg("现在去结算", {icon: 6})

               } else {
                   layer.close(loading);
                   layer.msg(data.msg, {icon: 5})
               }

           }
       )
   })



    /* 已选中多少 件   价格  */
    function check() {
        var userIds = $('input[name="idcheck"]:checked');
        if (userIds.length <= 0) {
            layer.msg('至少选择一项结算', {time: 500, icon: 5});
            return false;
        }
        var idsArr = "";
        for (var i = 0; i < userIds.length; i++) {
            idsArr += "," + $(userIds[i]).val();
        }
        var ids = idsArr.substring(1, idsArr.length);
        $("#ids").val(ids);
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/user/checkCountAndMoney",
            $("#fm").serialize(),
            function (data){
                if(data.code == 200){
                   /* layer.msg(data.msg, {icon: 6},function () {*/
                       var html = "已选择 "+data.data.number+" 件商品， 总商品金额：￥"+data.data.totalMoney+" ,  运费：￥"+data.data.totalFreight+"  ";
                        html += "<br>";
                        html += "应付金额：￥"+data.data.finalMoney+" ";
                        $("#moneyDiv").html(html);


                    layer.close(loading);
                    layer.msg("现在去结算", {icon: 6})

                } else {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 5})
                }

            }
        )

    }


    /* 去结算   选中的去结算的时候  购物车的要删除  */
    function submi() {
        var userIds = $('input[name="idcheck"]:checked');
        if (userIds.length <= 0) {
            layer.msg('至少选择一项结算', {time: 500, icon: 5});
            return false;
        }
        var idsArr = "";
        for (var i = 0; i < userIds.length; i++) {
            idsArr += "," + $(userIds[i]).val();
        }
        var ids = idsArr.substring(1, idsArr.length);
        var idsInteger = idsArr.substring(1, idsArr.length);
        $("#ids").val(ids);
        var loading=layer.load();
        token_post("<%=request.getContextPath()%>/user/checkCountAndMoney",
            $("#fm").serialize(),
            function (data){
                if(data.code == 200){
                    /* layer.msg(data.msg, {icon: 6},function () {*/
                    var html = "已选择 "+data.data.number+" 件商品， 总商品金额：￥"+data.data.totalMoney+" ,  运费：￥"+data.data.totalFreight+"  ";
                    html += "<br>";
                    html += "应付金额：￥"+data.data.finalMoney+" ";
                    $("#moneyDiv").html(html);
                    window.location.href = "<%=request.getContextPath()%>/user/toMyOrder?ids="+ids+"&token="+cookie.get("TOKEN")+"&idsInteger="+idsInteger;
                } else {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 5})
                }

            }
        )


    }



</script>
</html>
