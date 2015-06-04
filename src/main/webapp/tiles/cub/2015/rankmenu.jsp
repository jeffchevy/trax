<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<ul id='rank'>
	<li id='nav-bobcatrank' class='ranktab'><a title='Bobcat Badge' href='changerank.html?rankoption=Bobcat'>Bobcat</a></li>
	
	
	<security:authorize access='principal.organization.hasTigers'>
		<li id='nav-tigerrank' class='ranktab'><a title='Tiger Cub Badge' href='changerank.html?rankoption=Tiger Cub'>Tiger</a></li>
	</security:authorize>
	<li id='nav-wolfrank' class='ranktab'><a title='Wolf' href='changerank.html?rankoption=Wolf'>Wolf</a></li>
	<li id='nav-bearrank' class='ranktab'><a title='Bear' href='changerank.html?rankoption=Bear'>Bear</a></li>
	<li id='nav-webelosrank' class='ranktab'><a title='Webelos Award' href='changerank.html?rankoption=Webelos Award'>Webelos</a></li>
	<li id='nav-arrowoflightrank' class='ranktab'><a title='Arrow Of Light' href='changerank.html?rankoption=Arrow Of Light'>Arrow Of Light</a></li>
	<li id='nav-faith'><a title='Faith' href='cubBadges.html?type=Faith'>Faith</a></li>
	<li id='nav-award'><a title='Other Awards' href='cubBadges.html?type=CubAwards'>Awards</a></li>
</ul>	
