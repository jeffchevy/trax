<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/TraxTags.tld" prefix="trax"%>

<div id='mainmenu2'>
	<ul id='nav'>
		<li><a id="nav-home" title='Home' href='home.html'><img src="images/book/ScoutTraxLogoSmall.png" alt="Scout Trax"></a></li>
		<li><trax:unitnumber/></li>
		<li><a id="nav-advancement" title='Manage advancement records' href='advancement.html'></a></li>
		<security:authorize  access="hasRole('ROLE_LEADER')">
			<li><a id="nav-report" title='Report' href='report.html'></a></li>
		</security:authorize>
		<security:authorize ifNotGranted="ROLE_LEADER">
			<li><a id="nav-scoutreport" title='Report'></a></li>
		</security:authorize>
		<!--
		<li><a id="nav-shirt" title='Shirt' href='shirt.html'></a></li>
		 
		<li><a onclick="return false" id="nav-calendar" title='Coming soon - My yearly plan' href='calendar.html'><img class='construction' alt='My yearly plan' src='images/book/under-construction.png'></a></li>
		 -->
		<li><a id="nav-email" title='Create and send email' href='createEmail.html'></a></li>
		<li><a id="nav-training" title='Add/View leader training' href='training.html'></a></li>
		<security:authorize  access="hasRole('ROLE_LEADER')">
		</security:authorize>
		
			</ul>	
</div>