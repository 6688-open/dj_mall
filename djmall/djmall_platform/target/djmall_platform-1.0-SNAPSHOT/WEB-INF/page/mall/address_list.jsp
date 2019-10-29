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
<script src="<%=request.getContextPath()%>/res/cookie.js"></script>
<script src="<%=request.getContextPath()%>/res/token.js"></script>
</head>

<body>
	<br>

	<div align="right" style="border: 1px  ; width:900px ; height:700px; margin-left: 50px">



	<h2 align="center">地址列表</h2>
			<form id = "fmt">
				<input type="hidden" value="search" onclick="search(page(1))" />
				<input type="hidden" name="pageNo" id="pageNo"  value="1" />
				<input type="hidden" name="token" id="token"  />
			</form>
	<div  id="div" align="center">
        <br><br>
        <input type='button' onclick='add()'  value='添加收货地址'/>
    </div>

	<br>
	<table border="1px" align="center" cellspacing="0px">
		<tr >
			<th>编号</th>
			<th>收货人</th>
			<th>详细地址</th>
			<th>手机号码</th>
			<th>操作</th>
		</tr>
		<tbody id = "tbody"></tbody>
	</table>
	${msg} <br/>
	<div align="center" id = "pagediv"></div>
	</div>
</body>
<script type="text/javascript">
		/*$(function () {
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
		});*/


	$(function () {
		search();
	});
	
	function search() {
		$("#token").val(cookie.get("TOKEN"));
		token_post(
				"<%=request.getContextPath()%>/address/list",
				$("#fmt").serialize(),
				function (data) {
					var html = "";
					for (var i = 0; i < data.data.list.length; i++) {
						var user = data.data.list[i];
							html += "<tr>";
							html += "<td>"+user.id+"</td>";
							html += "<td>"+user.addressName+"</td>";
							html += "<td>"+user.address+"</td>";
							html += "<td>"+user.phone+"</td>";
							html += "<td>";
							html += "<input type='button' onclick='up("+user.id+")'  value='修改'/>";
							html += "<input type='button' onclick='del("+user.id+")'  value='删除'/>";
							html += "</td>";
							html += "</tr>";
						
					}
					$("#tbody").html(html);
					

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





		/*去修改收货地址*/
		function up(id){
			layer.open({
				type: 2,
				title: '修改收货地址',
				shadeClose: true,
				shade: 0.8,
				area: ['450px', '70%'],
				content:'<%=request.getContextPath()%>/address/toUpdate?token='+cookie.get('TOKEN')+"&id="+id ,
				end: function(){              //修改完刷新页面
					location.reload();
				}
			});
		}

        /*新增收货地址*/
        function add(){
            layer.open({
                type: 2,
                title: '新增收货地址',
                shadeClose: true,
                shade: 0.8,
                area: ['450px', '70%'],
                content:'<%=request.getContextPath()%>/address/addAddress?token='+cookie.get('TOKEN') ,
                end: function(){              //修改完刷新页面
                    location.reload();
                }
            });
        }



		/*function del(id){
			window.location.href = "<%=request.getContextPath()%>/address/delAddress?token="+cookie.get('TOKEN')+"&id="+id
		}*/
		/*删除 收获地址*/
		function del(id) {
			layer.confirm('确认删除？', {
				btn: ['YES', 'NO'],
				btn1: function (index, layero) {
					var loading=layer.load();
					token_post("<%=request.getContextPath()%>/address/delAddress?id="+id,
							{},
							function (data){
								if(data.code == 200){
									layer.msg(data.msg, {icon: 6},function () {
									window.location.href = "<%=request.getContextPath()%>/address/toList?token="+cookie.get('TOKEN');

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
		function  update() {
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