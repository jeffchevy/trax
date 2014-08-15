<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<div >
	<div ></div>

	<div >
		
	</div>

	<security:authorize ifAnyGranted="ROLE_USER">
	Welcome <security:authentication property="principal.fullName" />!!
	<span style="float: right"><a href="logout.html">Logout</a>
		</span>
	</security:authorize>
</div>