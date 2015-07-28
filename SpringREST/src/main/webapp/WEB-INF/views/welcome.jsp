<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link href="resources/css/master.css" type="text/css" rel="stylesheet"></link>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<title>Insert title here</title>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="resources/js/jquery-ui/jquery-ui.js"></script>
		<script src="resources/js/tweet.js"></script>
	</head>
	<body>
		<h1>Welcome ${user.userName}!</h1>
		<div>
			Details:
			<table>
				<tr>
					<td>email:</td>
					<td>${user.email}</td>
				</tr>
				<tr>
					<td>age:</td>
					<td>${user.age}</td>
				</tr>
			</table>
			<sf:form action="delete" method="POST" modelAttribute="user">
				<input type="submit" id="submit" value="DeleteMe" size="20" />
			</sf:form>
		</div>
		<br/>
		<div id="tweetDiv">
			<label>Post New Tweet:</label><br/>
			<textarea cols="30" rows="5" id="tweetArea"></textarea><br/>
			<input type="button" id="tweet" value="Post Tweet" />
		</div>
		<br/>
		<div id="timeline_updates">
			<h2>Timeline Updates:</h2>
			<input type="button" id="refresh" value="Refresh" />
			<table class="timeline_update_tab" id="timeline_updates_tab">
				<thead>
					<tr>
						<td width="200px">From:</td>
						<td width="500px">Message:</td>
					</tr>
				</thead>
				<tbody class="timeline_rows">
					
				</tbody>
			</table>
		</div>
		<div id="template" class="template" style="display: none;">
			<table id="template_table" class="template_table">
				<tbody id="template_body">
					<tr id="tempalte_row" class="template_row">
						<td class="template_cellFrom"></td>
						<td class="template_cellMessage"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</body>
</html>