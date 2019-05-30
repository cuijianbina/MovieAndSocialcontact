<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">   
<title>Insert title here</title>
 <body>
 <table border=1 align="center">
 	<tr>
	 	<td>编号</td>
	 	<td>用户名</td>
	 	<td>手机号</td>
	 	<td>性别</td>
 	</tr>
 	<c:forEach items="${data}" var="user">
 		<tr>
 			<td>${user.id }</td>
		 	<td>${user.username }</td>
		 	<td>${user.mobile }</td>
		 	<td>${user.gender }</td>
 		</tr>
 	</c:forEach>
 </table>

 </body>
</html>