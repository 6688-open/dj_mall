<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>滑块图片验证码</title>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/res/imgver/style.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/res/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/layer/layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/res/imgver/img_ver.js"></script>
</head>
<body bgcolor="#778899">
<br><br><br><br>
<center>
	<div class="verBox">
		<div id="imgVer" style="display:inline-block;"></div>
	</div>
</center>
</body>
<script>
		imgVer({
			el:'$("#imgVer")',//绑定的节点
			width:'260',//设置长
			height:'116',//设置宽
			img:[//图片库
				'<%=request.getContextPath() %>/res/imgver/images/ver.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-1.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-2.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-3.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-4.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-5.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-7.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-8.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-9.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-10.png',
				'<%=request.getContextPath() %>/res/imgver/images/ver-11.png',
			],
			success:function () {
				//alert('执行登录函数');
				/*layer.alert("验证成功");*/
				window.parent.location.reload();
				parent.window.location.href = "<%=request.getContextPath()%>/toIndex";
			},
			error:function () {
				//alert('错误什么都不执行')
				layer.msg("验证不通过", {icon: 5,time:1000});
			}
		});
</script>
</html>