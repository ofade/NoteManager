<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Register</title>
	</head>
	<body>
	<c:if test="${not empty invalidUsernameMessage}">
		<p>User with such name already exists. Please pick another username.</p>
	</c:if>
		<form:form id="registerForm" method="post" action="register" modelAttribute="user">

			<form:label path="username">Enter your user-name</form:label>
			<form:input id="username" name="username" path="username" /><br>
			<form:label path="username">Please enter your password</form:label>
			<form:password id="password" name="password" path="password" /><br>
			<input type="submit" value="Submit" />
		</form:form>
	</body>
</html>