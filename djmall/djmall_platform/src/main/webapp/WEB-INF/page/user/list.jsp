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

		<div align="left" style="width:800px ; height:160px;"  >
			<form id = "fmt">
				<input type="hidden" name="pageNo" id = "pageNo" value="1" />
				<input type="text" name="username" placeholder="用户名/手机号/邮箱" size="14px"  />模糊匹配<br><br>
				级别	 <input type="radio" name="lever" value="1"/>商户
				<input type="radio" name="lever" value="3"/>买家
				<input type="radio" name="lever" value="2" />管理员<br><br>

				性别     男： <input type="radio" name="sex" value="1"/>
				女：  <input type="radio" name="sex" value="2" /><br><br>
				状态 <select name="status">
				<option value="">请选择</option>
				<option value="1">正常</option>
				<option value="2">未激活</option>
			</select><br><br>
				<input type="button" value="search" onclick="search(page(1))" />----
				<input type="reset" value="reset" onclick="search(page(1))" />
			</form><br>
			<%--<form id = "fm">
                <input type="hidden" name="_method" value="DELETE"/>--%>
            </form>
		</div>




	<h2 align="center">用户管理展示</h2> <br>
	<div  id="div" align="center"></div>
	<br>
	<table border="1px" align="center" cellspacing="0px">
		<tr >
			<th>用户ID</th>
			<th>用户名</th>
			<th>手机号</th>
			<th>邮箱</th>
			<th>性别</th>
			<th>级别</th>
			<th>状态</th>
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
				"<%=request.getContextPath()%>/user/list",
				$("#fmt").serialize(),
				function (data) {
					var html = "";
					for (var i = 0; i < data.data.list.length; i++) {
						var user = data.data.list[i];
						
							html += "<tr>";
							html += "<td>";
							html += "<input type='checkbox' name='idcheck' value=' "+user.id+ "'/>--";
							/*判断是否被激活*/
							/*html += "<input type = 'hidden' id='"+user.id+"' value = '"+user.status+"'/>";*/
							html += user.id ;
							html +=  "</td>";
							html += "<td>"+user.username+"</td>";
							html += "<td>"+user.phone+"</td>";
							html += "<td>"+user.email+"</td>";
							/*性别*/
							if(user.sex == 1){
								html += "<td>男</td>";
							}
							if(user.sex == 2){
								html += "<td>女</td>";
							}
							/*级别*/
							if(user.lever == 1){
								html += "<td>商户</td>";
							}
							if(user.lever == 2){
								html += "<td>管理员</td>";
							}
							if(user.lever == 3){
								html += "<td>买家</td>";
							}
							/*状态*/
							if(user.status == 1){
								html += "<td>正常</td>";
							}
							if(user.status == 2){
								html += "<td>未激活</td>";
							}

							html += "</tr>";
						
					}
					$("#tbody").html(html);
					
					var divhtml = "";
					divhtml += "<input type='button' value='修改' onclick='up()' size='15px'/>------";
					divhtml += "<input type='button' value='激活' onclick='status()'/>------";
					divhtml += "<input type='button' value='重置密码' onclick='resetPassword()'/>------";
					divhtml += "<input type='button' value='删除' onclick='del()'/>";
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
	/*重置*/
		function reset() {
			search();
		}


		/* 批量删除*/
		function del() {
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


		}


		/* 批量激活*/
		function status() {
			var userIds = $('input[name="idcheck"]:checked');
			if (userIds.length <= 0) {
				layer.msg('至少选择一项', {time: 500, icon: 5});
				return false;
			}
			layer.confirm('确认激活？', {
				btn: ['YES', 'NO'],
				btn1: function (index, layero) {
					var idsArr = "";
					for (var i = 0; i < userIds.length; i++) {
						idsArr += "," + $(userIds[i]).val();
					}
					var ids = idsArr.substring(1, idsArr.length);
					var loading=layer.load();
					$.post("<%=request.getContextPath()%>/user/status",
							{"ids": ids},
							function (data){
								if(data.code == 200){
									layer.msg(data.msg, {icon: 6},function () {
										window.location.href = "<%=request.getContextPath()%>/user/toList"

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
		function up() {
			var obj = $("input[name='idcheck']:checked");
			if (obj.length <= 0) {
				layer.msg('至少选择一项', {time: 500, icon: 5});
				return;
			}
			if (obj.length > 1) {
				layer.msg('只能选择一项', {time: 500, icon: 5});
				return;
			}

			layer.open({
				type: 2,
				title: '添加',
				shadeClose: true,
				shade: 0.8,
				area: ['380px', '90%'],
				content:'<%=request.getContextPath()%>/user/toUpdate/'+$(obj[0]).val() ,
				end: function(){              //修改完刷新页面
					location.reload();
				}
			});
		}





		/*重置密码*/
		function  resetPassword() {
			var obj = $("input[name='idcheck']:checked");
			if (obj.length <= 0) {
				layer.msg('至少选择一项', {time: 500, icon: 5});
				return;
			}
			if (obj.length > 1) {
				layer.msg('只能选择一项', {time: 500, icon: 5});
				return;
			}
            layer.confirm('确认重置？', {
                btn: ['YES', 'NO'],
                btn1: function (index, layero) {
                    var loading=layer.load();
                    $.post(
                        "<%=request.getContextPath()%>/user/restPassword/"+$(obj[0]).val(),
                        {},
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


		}














	
	
	 /*		全选反选											*/
	/* function selectQuanOrFan(){
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
	} */
	 
	/*function reset() {
		search();
	}
	
	

	function add(){
		layer.open({
			  type: 2,
			  title: '添加',
			  shadeClose: true,
			  shade: 0.8,
			  area: ['380px', '90%'],
			  content:'<%=request.getContextPath()%>/user/toAdd' ,
			  end: function(){ 
			        location.reload();
			    }
			}); 
	}


	 /!*  *!/
	function update(id){
		layer.open({
		    type: 2,
		    area: ['400px', '400px'],
		    maxmin:true,
		    content: ['<%=request.getContextPath()%>/user/toUpdate/'+id, 'no'], 
		     end: function(){ 
		        location.reload();
		    } 
		});
	}

	
	function deleteUser(id){
		layer.confirm('确认删除？', {
			btn: ['确认', '取消'],
			btn1: function(index, layero){
				var index1 = layer.load();
				$.post(
					"<%=request.getContextPath()%>/user/user/"+id,
					$("#fm").serialize()
					,function(data){
						if(data.code == 200){
							layer.msg('删除成功', {
								icon: 1,shade: 0.01,time: 500
							}, function(){
								/!*  var index = parent.layer.getFrameIndex(window.name);
								parent.layer.close(index);   *!/   //关闭展示框
								window.location.href = "<%=request.getContextPath()%>/user/toList";
							}); 
						} else {
							layer.close(index1);
							layer.msg(data.msg,{icon: 5,time:500});
						}
					}
				)
			}, 
			btn2:function(index, layero){
				return;
			}
		})
	}
	
	
	
	/!* 管理角色 *!/
	function userRole(userId){
		layer.open({
		    type: 2,
		    area: ['400px', '400px'],
		    maxmin:true,
		    content: ['<%=request.getContextPath()%>/user/userRoleList/'+userId, 'no'], 
		     end: function(){ 
		        location.reload();
		    } 
		});
	}
	
	
	

   /!* 管理角色  树*!/
		function userRoleTree(userId){
			layer.open({
			    type: 2,
			    area: ['400px', '400px'],
			    maxmin:true,
			    content: ['<%=request.getContextPath()%>/user/userRoleListTree/'+userId, 'no'], 
			    end: function(){ 
			        location.reload();
			    }  
			});
		}*/
 
	
</script>
</html>