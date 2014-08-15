<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
<html>
	<head>
		<title>Registration</title>
	</head>
	<body>
		<div>
			<ul id='main-nav'>
				<li id="nav-register"><a title='Register' href='organization.html'>Register</a></li>
				<li id="nav-login"><a title='Login' href='login.html'>Log in</a></li>
				<security:authorize ifAnyGranted="ROLE_USER">
				</security:authorize>	
				<security:authorize ifAnyGranted="ROLE_LEADER">
				</security:authorize>	
			</ul>	
		</div>
	</body>
</html>