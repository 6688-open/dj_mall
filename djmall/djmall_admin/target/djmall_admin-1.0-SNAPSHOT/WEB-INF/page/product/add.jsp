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

    <h3>商品添加</h3>
    <form id="fm">
        <p>
            <label for="productName">商品名称：</label>
            <input id="productName" name="productName" type="text" placeholder="商品名称" maxlength="20"/>
        </p>
        <p>
            <label for="freightId">邮费：</label>
            <select name="freightId" id="freightId">
                <c:forEach items="${freightAll}" var="fre">
                    <option value="${fre.id}">${fre.logName}--${fre.freight}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="productDescribe"></label>
            <textarea  name="productDescribe" id="productDescribe" rows="5" cols="30">描述</textarea>
        </p>
        <p>
            <label for="file">图片：</label>
            <input id="file" name="file" type="file" />
        </p>
        <p>
            <label for="dictionaryCode">分类：</label>
            <select name="dictionaryCode" id="dictionaryCode" onchange="changeAttr(this.value)">
                <option value="1">请选择</option>
                <c:forEach items="${dictionaryList}" var="dic">
                    <option value="${dic.code}">${dic.dictionaryName}</option>
                </c:forEach>
            </select>
        </p>
        <p>
            <label for="dictionaryCode">SKU：</label>
            <input type="button" value="+" onclick="addSku()"/>
            <input type="button" value="生成SKU" onclick="generateSkU()"/>
        </p>

        <table border="1px" align="left" cellspacing="0px">

            <tr>
                <td>属性名</td>
                <td>属性值</td>
            </tr>
            <tbody id="tbodyAttr"></tbody>
        </table>


        <p>
            <label for="dictionaryCode">：</label>
        </p>
        <table border="1px" align="left" cellspacing="0px">
            <tr>
                <td colspan="5"><h3>生成后SKU</h3></td>

            </tr>
            <tr>
                <td>编号</td>
                <td>SKU属性</td>
                <td>库存</td>
                <td>价格(元)</td>
                <td>折扣(%)</td>
                <td></td>
            </tr>
            <tbody id="SKUTb"></tbody>
        </table><br><br><br><br>





        <div id="add" hidden>
            <h3>添加自定义sku</h3>
            <p>
                <label for="propertyName">属性名：</label>
                <input type="text" name="propertyName" id="propertyName" maxlength="20"/><br/>
            </p>
            <p>
                <label for="propertyValue">属性值：</label>
                <input type="text" name="propertyValue" id="propertyValue" maxlength="15"/>
            </p>

            <input type="button" onclick="saveProperty()" value="提交">
        </div>



        <div style="color: #00FF00;width:100px ; height:200px;  margin-left:150px; "  >
            <p>
                <input type="submit" value="确认添加">
            </p>
        </div>

    </form>

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
                    remote:{
                        url:"<%=request.getContextPath()%>/product/uniq",
                        type:"post",
                        data:{userName:function(){return $("#productName").val()}}
                    },
                },
                productDescribe: {
                    required: true,
                    minlength: 15,
                },
                skuPrice: {
                    required: true,
                },
                skuRate: {
                    required: true,
                },
                skuStatus: {
                    required: true,
                },
                skuCount: {
                    required: true,
                },
                file:"required",
            },
            messages:{
                productName: {
                    required: "请输入商品名",
                    minlength: "商品名至少由2个字母/数字/汉字组成",
                    remote:"商品名称已存在  请更换"
                },
                productDescribe: {
                    required: "请输入产品描述",
                    minlength: "至少输入15个字"
                    // rangelength: "密码长度为 6-14 个字符",
                },
                skuPrice: {
                    required: "请输入价格"
                    // rangelength: "密码长度为 6-14 个字符",
                },
                skuStatus: {
                    required: "请输入上架/下架"
                    // rangelength: "密码长度为 6-14 个字符",
                },
                skuRate: {
                    required: "请输入折扣"
                    // rangelength: "密码长度为 6-14 个字符",
                },
                skuCount: {
                    required: "请输入库存"
                    // rangelength: "密码长度为 6-14 个字符",
                },
                file: {
                    required: "请添加图片",
                },
            }
        })
    })


    $.validator.setDefaults({
        submitHandler: function(form) {

            var dictionaryCode = $("#dictionaryCode").val();
            if(dictionaryCode == 1){
                layer.msg("请选择分类", {icon: 5,time:1000});
                return;
            }

            var skuCount = $("#skuCount").val();
            if(skuCount == null){
                layer.msg("请生成SKU", {icon: 5,time:1000});
                return;
            }

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
                    url: "<%=request.getContextPath()%>/product/add",
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




    /*展示属性名  属性值*/
  function changeAttr(productCode) {
      $.post(
          "<%=request.getContextPath()%>/product/attrList/"+productCode,
          {},
          function (data) {
              var html = "";
              var attrShow = "";
              var attrId = "";
              for (var i = 0; i < data.data.proAttValueList.length; i++) {
                  var att = data.data.proAttValueList[i];

                  attrShow += " "+att.attrShow+" ";
                  attrShow += ":";
                  attrId += " "+att.attrId+" ";
                  attrId += ":";

                  html += "<tr>";
                  html += "<td>";
                  html +="<input type='text' size='2px' value='"+att.attrId+':'+att.attrShow+"'/>";
                  html += " "+att.attrShow+" "
                  html +="</td>";
                 /* html += "<td>"+att.attrValueShow+"</td>";*/
                  html += "<td>";
                  var attArr = att.attrValueShowArr;
                  var attArrId = att.attrValueIdArr;
                  for (var j = 0; j < attArr.length; j++) {
                      html += "<input type='checkbox' id='idcheck' name='idcheck' value=' "+attArrId[j]+":"+attArr[j]+" '/>  ";
                      html += " "+attArr[j]+" ";

                  }
                  html +=  "</td>";
                  html += "</tr>";

              }
              $("#tbodyAttr").html(html);
             /* $("#attrShow").val(attrShow.substring(0,attrShow.length-1));
              $("#attrId").val(attrId.substring(0,attrId.length-1));*/

              $("#SKUTb").html("");


              if($("#dictionaryCode").val() == 1){
                  $("#tbodyAttr").html("");
                  $("#SKUTb").html("");
              }



          }
      );

  }

    function addSku() {
        layer.open({
            type: 1,
            title: false,
            area:['400px','400px'],
            closeBtn: 0,
            shadeClose: true,
            skin: 'yourclass',
            content:$("#add")
        });
    }
    /*保存自定义属性值  属性id*/
    function saveProperty(){

        var propertyName = $("#propertyName").val();
        if(propertyName == null || "" == propertyName){
            layer.msg("请输入属性名", {icon: 5,time:1000});
            return;
        }
        var propertyValue = $("#propertyValue").val()
        if(propertyValue == null || "" == propertyValue){
            layer.msg("请输入属性值", {icon: 5,time:1000});
            return;
        }


        var html = "";
           html += "<tr>";
           html += "<td>";
           html += $("#propertyName").val();
           html +="<input type='text' maxlength='6' value='-1:"+$("#propertyName").val()+"'/>";
            html += "</td>";
            html += "<td>";
                var strs= new Array();
                var propertyValue = $("#propertyValue").val();
                strs = propertyValue.split(",");
                for (var i = 0; i <strs.length ; i++) {
                    html +="<input type='checkbox' value='-1:"+strs[i]+"' id='skuId'/>";
                    html +=strs[i];
                }
            html += "</td>";
        html += "</tr>";
        $("#tbodyAttr").append(html);
        $("#add").hide();

        /*拼接自定义id  和自定义属性值*/
       /* $("#attrShow").val( $("#attrShow").val()+":"+$("#propertyName").val()  );
        $("#attrId").val($("#attrId").val()+":"+$("#skuId").val().substring(0,2));*/

        $("#propertyValue").val("");
        $("#propertyName").val("");

        layer.closeAll();
    }

    // 笛卡尔积算法
    // 参数d格式[["蓝色", "红色"],["S", "M"], ["1", "2"]];
    function dkej(d) {
        var total = 1;
        for (var i = 0; i < d.length; i++) {
            total *= d[i].length;
        }
        var e = [];
        var itemLoopNum = 1;
        var loopPerItem = 1;
        var now = 1;
        for (var i = 0; i < d.length; i++) {
            now *= d[i].length;
            var index = 0;
            var currentSize = d[i].length;
            itemLoopNum = total / now;
            loopPerItem = total / (itemLoopNum * currentSize);
            var myIndex = 0;
            for (var j = 0; j < d[i].length; j++) {
                for (var z = 0 ; z < loopPerItem; z++) {
                    if (myIndex == d[i].length) {
                        myIndex = 0;
                    }
                    for (var k = 0; k < itemLoopNum; k++) {
                        e[index] = (e[index] == null ? "" : e[index] + ":") + d[i][myIndex];
                        index++;
                    }
                    myIndex++
                }
            }
        }
        return e;
    }






    function generateSkU() {
        var SkuNames = [];
        var SKuIDS = [];
        var SKuNameValues = [];
        var SkuIdsValus = [];
        var tr = $("#tbodyAttr tr");//获取tbody中所有tr
        for (var i = 0; i <tr.length ; i++) {
            var $tr = $("#tbodyAttr tr")[i];
            var $td = $($tr).find("td")[1];
            var aa = [];
            var id = [];
            var cc = [];
            var dd = [];
            var cbx = $(tr[i]).find(":checked");
            if(cbx.length > 0){
                var $tds = $($tr).find("td")[0];
                var skuAttr = $($tds).find("input");
                cc.push(skuAttr.val().split(":")[0]);
                dd.push(skuAttr.val().split(":")[1]);

                for (var k = 0; k <cbx.length ; k++) {
                    var val = $(cbx[k]).val();
                    // 获取 选中的  name值 放进aa数组中
                    aa.push(val.split(":")[1]);
                    // 获取选中的id值 放进 id数组中
                    id.push(val.split(":")[0]);
                }
            }
            // 放进一个大的数组中 格式就是 【【】，【】，【】】  长度不为0 才会拼接到数组中
            if(aa.length > 0){
                SkuNames.push(aa);
                SKuIDS.push(id);
                SKuNameValues.push(cc);
                //属性名ID的数组
                SkuIdsValus.push(dd);
            }

        }
        var dkej1 = dkej(SkuNames);
        var dkej2 = dkej(SKuIDS);
        var SKuNameValues = dkej(SKuNameValues);
        var SkuIdsValus = dkej(SkuIdsValus);
        var html = "";
        for (var i = 0; i < dkej1.length; i++) {
            html += "<tr id = '"+i+"'>";
            // 编号 根据循环自动生成
            html += "<td >";
            html += "<h5>"+(i+1)+"</h5>";
            html += "</td>";
            html += "<td>" ;
            html += "<h5>"+dkej1[i]+"</h5>";
            // 拼去 属性值
            html += '<input type="hidden" size="2px"   name = "skuAttrValueNames" value="'+dkej1[i]+'">';
            html += '<input type="hidden" size="2px"   name = "skuAttrValueIds" value="'+dkej2[i]+'">';
            html += '<input type="hidden" size="2px"   name = "skuAttrIds" value="'+SKuNameValues+'">';
            html += '<input type="hidden" size="2px"   name = "skuAttrNames" value="'+SkuIdsValus+'">';
            html += "</td>";
            html += "<td>" ;
            html += '<input type="text" id="skuCount" maxlength="6" name = "skuCount" value="200" >';
            html += "</td>";
            html += "<td>" ;
            html += '<input type="text" name = "skuPrice" maxlength="6" value="100">';
            html += "</td>";
            html += "<td>" ;
            html += '<input type="text" name = "skuRate" maxlength="3" value="0">';
            html += "</td>";
            html += '<input type="hidden" name = "skuStatus" value="1">';
            html += ' <input id="isDefault" name="isDefault" type="hidden" value="0"/>';
            html += "<td>" ;
            html += '<input type="button" value="移除" onclick="remove('+i+')">';
            html += "</td>";
            html += "</tr>";
        }
        $("#SKUTb").html(html);
    }


    /*移除*/
    function remove(i) {
        $("#"+i).remove();
    }



</script>
</html>
