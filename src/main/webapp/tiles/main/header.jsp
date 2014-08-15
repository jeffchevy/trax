<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<html>
	<body>
		<div id="header">
			<div class="art-Header-jpeg"></div>
			<div class="art-Logo">
				<h1 id="name-text" class="art-Logo-name">
					<a href="home.html"></a>
				</h1>
				<div id="slogan-text" class="art-Logo-text">
					ScoutTrax
				</div>
			</div>
			<security:authorize ifAnyGranted="ROLE_USER">
			<!-- Welcome <security:authentication property="principal.fullName" />!!
			<span style="float: right"><a href="logout.html">Logout</a>
				</span> -->
			</security:authorize>
		</div>
	</body>
</html>
