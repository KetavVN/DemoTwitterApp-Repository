<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="resources/js/jquery-ui/jquery-ui.js"></script>
	</head>
	<body>
		<div id="details">
			<%-- enctype creates binding issues - http://stackoverflow.com/questions/26759095/form-data-not-binding-with-spring-controller-annotation-getting-new-object-after --%>
			<%-- <sf:form action="register" method="POST" enctype="multipart/form-data" modelAttribute="user"> --%>
			<sf:form action="register" method="POST" modelAttribute="user">
				<table>
					<tr>
						<td><label>User Name:</label></td>
						<td><sf:input path="userName" id="userName" size="20"/></td>
						<td><sf:errors path="userName" cssClass="error" /></td>
					</tr>
					<tr>
						<td><label>Password:</label></td>
						<td><sf:password path="passphrase" id="password" size="20"/></td>
						<td><sf:errors path="passphrase" cssClass="error" /></td>
					</tr>
					<tr>
						<td><label>Email:</label></td>
						<td><sf:input path="email" id="email" size="20"/></td>
						<td><sf:errors path="email" cssClass="error" /></td>
					</tr>
					<tr>
						<td><label>Image:</label></td>
						<td><input type="file" id="avatar" size="20"/></td>
						<td><sf:errors path="avatar" cssClass="error" /></td>
					</tr>
					<tr>
						<td colspan="3"><input type="submit" id="submit" value="Register" size="20"/></td>
					</tr>
				</table>
			</sf:form>
		</div>
		<div id="value">
		</div>
	</body>
</html>
