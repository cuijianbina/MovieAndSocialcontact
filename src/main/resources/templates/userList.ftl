<html>
	<head>
		<title>freemarker</title>
		<meta charset="utf-8"></meta>
	</head>
	
	<body>
		<table border="1" align="center" width="50% ">
			<tr>
			 	<td>编号</td>
			 	<td>用户名</td>
			 	<td>手机号</td>
			 	<td>性别</td>
		 	</tr>
		 	<#list dataFreemarker as user>
		 		<tr>
		 			<td>${user.id}</td>
		 			<td>${user.username}</td>
		 			<td>${user.mobile}</td>
		 			<td>${user.gender}</td>
		 		</tr>
		 	</#list>
		</table>
	</body>
</html>