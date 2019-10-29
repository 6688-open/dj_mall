<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	<br>

	
	<div align="right" style="border: 1px  ; width:900px ; height:700px; margin-left: 50px">
	<%--<form id = "fm">
                <input type="hidden" name="_method" value="DELETE"/>--%>
          <%--  </form>--%>
		<div align="center">
			<form id="fm">
				<input id="logName" name="logName" type="hidden"  value="${lo.dictionaryName}" />
		物流公司	<select name="logId" id="log">
						<c:forEach items="${LOGList}" var="lo">
							<option value="${lo.id}">${lo.dictionaryName}</option>
						</c:forEach>
					</select>

				<p>
					<label for="freight">运费：</label>
					<input id="freight" name="freight"  type="text" placeholder="运费" onblur="freightClick()" maxlength="6"/>

				</p>

				<input type='submit' value='新增' onclick='add()'/>
			</form>
		</div>

		<form id = "fmt">
			<input type="hidden" name="pageNo" id = "pageNo" value="1" />
			<input type="hidden" value="search" onclick="search(page(1))" />
		</form>

	<h2 align="center">物流运费展示</h2>
	<table border="1px" align="center" cellspacing="0px">
		<tr >
			<th>物流公司</th>
			<th>运费</th>
			<th>操作</th>
		</tr>
		<tbody id = "tbody"></tbody>
	</table>
	${msg} <br/>
	<div align="center" id = "pagediv"></div>
	</div>
</body>
<script type="text/javascript">

	/*function freight(){
		var log = $("#log").val();
		alert(log)
		if (log == null || log == '') {
			layer.msg('快递公司不能为空', {icon:5});
			return;
		}
	}*/

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
		$.get(
				"<%=request.getContextPath()%>/log/list",
				$("#fmt").serialize(),
				function (data) {
					var html = "";
					for (var i = 0; i < data.data.logList.length; i++) {
						var log = data.data.logList[i];

							html += "<tr>";
							html += "<td>"+log.dicCode+"</td>";
							if(log.freightShow == "0"){
								html += "<td>包邮</td>";
							} else{
								html += "<td>"+"￥"+log.freightShow+"</td>";
							}

							html += "<td><input type='button' value='修改' onclick='up("+log.id+")'/></td>";
							html += "</tr>";

					}
					$("#tbody").html(html);
					/*var divhtml = "";
					divhtml += "<input type='button' value='新增' onclick='add()'/>";
					$("#div").html(divhtml);*/

                   /*  var pagehtml = "";
					 pagehtml += "当前"+(data.data.pageNo)+"页";
		    		 pagehtml += "<input type='button' value='上一页' onclick='page("+(eval(data.data.pageNo)-1)+" , "+data.data.totalPage+"  )' />";
		    		 pagehtml += "<input type='button' value='下一页' onclick='page("+(eval(data.data.pageNo)+1)+" , "+data.data.totalPage+" )' />";
		    		 pagehtml += "总"+(data.data.totalPage)+"页";
		    		 $("#pagediv").html(pagehtml);*/
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
			layer.msg('已经是第一页了', {time: 500, icon:5});
			return;
		}
		search();
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
					freight: {
						required: true,
						minlength: 1

					},

				},
				messages:{
					freight: {
						required: "请输入运费",
						minlength: "分类名称至少由2个字母/数字/汉字组成"
					},
				}
			})
		})






		/*新增*/
		$.validator.setDefaults({
			submitHandler: function(form) {
				if($("#freight").val() < 0){
					layer.msg("运费只能等于0 或者大于0不能出现负数", {icon: 5,time:1000});
					return true;
				}
				if(isNaN($("#freight").val())){
					layer.msg("请输入纯数字", {icon: 5,time:1000});
					return false;
				}


					var loading=layer.load();
					$.post(
							"<%=request.getContextPath()%>/log/insert",
							$("#fm").serialize(),
							function (data) {
								if(data.code == 200){
									layer.msg(data.msg, {icon: 6},function () {
										window.location.href = "<%=request.getContextPath()%>/log/toList"

									})
									return
								}
								layer.close(loading);
								layer.msg(data.msg, {icon: 5})
							}

					);


			}
		})


		function up(id) {
			layer.open({
				type: 2,
				title: '修改',
				shadeClose: true,
				shade: 0.8,
				area: ['280px', '50%'],
				content:'<%=request.getContextPath()%>/log/toUpdate/'+id ,
				end: function(){              //修改完刷新页面
					location.reload();
				}
			});
		}

		/*运费限制*/
		function freightClick() {
			var log = $("#log").val();
			if (log == null || log == '') {
				layer.msg('快递公司不能为空', {icon:5});
				return;
			}
			if(  parseInt($("#freight").val()) == 0){
				$("#freight").val(0);
			} else {
				$("#freight").val(  parseInt($("#freight").val()));
			}

		}

	/*/!*重置*!/
		function reset() {
			search();
		}


		/!* 批量删除*!/
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


		/!* 批量激活*!/
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

		/!* 	去修改 *!/
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





		/!*重置密码*!/
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


		}*/














	
	
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