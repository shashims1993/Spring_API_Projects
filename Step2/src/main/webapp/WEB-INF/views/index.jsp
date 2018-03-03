<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" isELIgnored="false"%>
<html>
<head>
<title>Activity Stream</title>


</head>
<body>
	<h1>SEND YOUR MESSAGE HERE</h1>
	<!-- create a form which will have textboxes for Sender Name and Message content along with a Send 
Submit button. Handle errors like empty fields -->

	<form action="sendMessage">
		<table style="width: 100" >
			<c:if test="${!empty errorMsg}">
				<b>${errorMsg}</b>
			</c:if>
			<tr>
				<font color="green"><td>Sender Name</td></font>
				<td><input type="text" name="sender"></td>
			</tr>
			<tr>
				<td>Message</td>
				<td><input type="text" name="message"></td>
			</tr>
			<tr>

				<center>
					<td><input type="submit" name="Send" value="send"></td>
				</center>
			</tr>
		</table>
	</form>

	<!-- display all existing messages in a tabular structure with Sender Name, Posted Date and Message -->

	<c:if test="${!empty list}">
		<center>
			<table name="users" border="1.0" width="75" cellpadding="1.5" align="left">

				<tr>
					<th>Sender</th>
					<th>Message</th>
					<th>Posted Date</th>
				</tr>
				<c:forEach items="${list}" var="data">
					<tr>
						<td>${data.senderName}</td>
						<td>${data.message}</td>
						<td>${data.postedDate}</td>
					</tr>
				</c:forEach>


			</table>
		</center>
	
	</c:if>
</body>
</html>