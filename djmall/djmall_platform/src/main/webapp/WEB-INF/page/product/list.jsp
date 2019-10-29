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

	<div align="right" style="border: 1px  ; width:900px ; height:700px; margin-left: 50px">

	<h2 align="center">商品管理展示</h2>
		<div align="left" style="width:800px ; height:160px;"  >
			<form id = "fmt">
				<input type="hidden" name="pageNo" id="pageNo"  value="1" />
				名称:<input type="text" name="productName"  placeholder="名称关键字" size="8px"  /><br><br>
				分类:
				<c:forEach items="${dicList}" var="dicPro">
					<input type="checkbox" name="dictionaryCodeArr" value="${dicPro.code}" > ${dicPro.dictionaryName}
				</c:forEach> <br><br>

				<input type="button" value="search" onclick="search(page(1))" />----
				<input type="reset" value="reset" onclick="search(page(1))" />
			</form>
		</div>
	<div  id="div" align="center"></div>
	<br>
	<table border="1px" align="center" cellspacing="0px">
		<tr >
            <th><input type="checkbox" onclick="selectQuanOrFan()" id="checkAll"/></th>
			<th>商品名称</th>
			<th>状态</th>
			<th>邮费</th>
			<th>商品图片</th>
			<th>描述</th>
			<th>点赞量</th>
			<th>订单量</th>
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
		$.post(
				"<%=request.getContextPath()%>/product/list",
				$("#fmt").serialize(),
				function (data) {
					var html = "";
					for (var i = 0; i < data.data.list.length; i++) {
						var pro = data.data.list[i];
						
							html += "<tr>";
							html += "<td>";
							html += "<input type='checkbox' name='idcheck' value=' "+pro.id+ "'/>";
							html +=  "</td>";
							html += "<td>"+pro.productName+"</td>";
							/*状态*/
							if(pro.productStatus == 1){
								html += "<td>上架</td>";
							}
							if(pro.productStatus == 2){
								html += "<td>下架</td>";
							}
							if(pro.freight == 0){
								html += "<td>包邮</td>";
							} else {
								html += "<td> 平邮"+pro.freight+" 元</td>";
							}
                            html += "<td>"+' <img src="http://'+pro.picture+'" style="width: 50px" height="50px"/>'+"</td>";
							html += "<td>"+pro.productDescribe+"</td>";
							html += "<td>"+pro.thumbNumber+"</td>";
							html += "<td>"+pro.orderNumber+"</td>";


							html += "</tr>";
						
					}
					$("#tbody").html(html);
					
					var divhtml = "";
                    divhtml += "<input type='button' value='增加' onclick='add()' size='15px'/>------";
					divhtml += "<input type='button' value='修改' onclick='up()' size='15px'/>------";
					divhtml += "<input type='button' value='上架/下架' onclick='status()'/>------";
					divhtml += "<input type='button' value='查看评论' onclick='resetPassword()'/>------";
					divhtml += "<input type='button' value='导入' onclick='del()'/>";
					$("#div").html(divhtml);
					
                     var pagehtml = "";
					 pagehtml += "当前"+(data.data.pageNo)+"页";
		    		 pagehtml += "<input type='button' value='上一页' onclick='page("+(eval(data.data.pageNo)-1)+" , "+data.data.totalPage+"  )' />";
		    		 pagehtml += "<input type='button' value='下一页' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
		    		 pagehtml += "总"+(data.data.totalPage)+"页";  
		    		 $("#pagediv").html(pagehtml);
				}
			);}
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




		function add(){
		layer.open({
			  type: 2,
			  title: '添加',
			  shadeClose: true,
			  shade: 0.8,
			  area: ['380px', '90%'],
			  content:'<%=request.getContextPath()%>/product/toAdd' ,
			  end: function(){
			        location.reload();
			    }
			});
	}


		/* 修改状态  上架下架*/
		function status() {
			var userIds = $('input[name="idcheck"]:checked');
			if (userIds.length <= 0) {
				layer.msg('至少选择一项', {time: 500, icon: 5});
				return false;
			}
            if (userIds.length > 1) {
                layer.msg('只能选择一项', {time: 500, icon: 5});
                return;
            }
			layer.confirm('确认修改状态？', {
				btn: ['YES', 'NO'],
				btn1: function (index, layero) {
					var loading=layer.load();
					$.post("<%=request.getContextPath()%>/product/status/"+$(userIds[0]).val(),
							{},
							function (data){
								if(data.code == 200){
									layer.msg(data.msg, {icon: 6},function () {
										window.location.href = "<%=request.getContextPath()%>/product/toList"

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

		/* 	去修改 */
		function  up() {
			var obj = $("input[name='idcheck']:checked");
			if (obj.length <= 0) {
				layer.msg('至少选择一项', {time: 500, icon: 5});
				return;
			}
			if (obj.length > 1) {
				layer.msg('只能选择一项', {time: 500, icon: 5});
				return;
			}
            layer.confirm('确认修改？', {
                btn: ['YES', 'NO'],
                btn1: function (index, layero) {
					layer.open({
						type: 2,
						title: '修改',
						shadeClose: true,
						shade: 0.8,
						area: ['380px', '90%'],
						content:'<%=request.getContextPath()%>/product/toUpdate/'+$(obj[0]).val() ,
						end: function(){              //修改完刷新页面
							location.reload();
						}
					});
                },
                btn2: function (index, layero) {
                    return;
                }
            })


		}







		/*批量删除*/
		/*function del() {
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
					$.post("<%=request.getContextPath()%>/user/delUser",
							{"ids": ids},
							function (data){
								if(data.code == 200){
									layer.msg(data.msg, {icon: 6},function () {
									window.location.href = "<%=request.getContextPath()%>/user/toList"

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


		}*/






	
	


	




	

</script>
</html>