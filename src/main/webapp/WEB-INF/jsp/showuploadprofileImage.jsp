<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
<html>
	<head>
	<title>Users</title>
	<style type="text/css">
	label {
		padding-right: 20px; 
	}
	
	#page1 {
		background-image: none;
	}
	p {
		color: white;
		background: black;
	}
	</style>	
		
	</head>
	
	<body>
	<div class="table">	
		<div class="row">
			<div id='page1' class='cell'>
				<div class='row'>
					<form:form modelAttribute="uploadItem" method="post" action="saveprofileimage.html" enctype="multipart/form-data">
	               		<form:label for="fileData" path="fileData">Upload a Picture of ${scout.fullName }</form:label>
	               		<form:input path="fileData" type="file" accept="image/*"/>
	               		<input class="button" type="submit" value="Import"/>
			     	</form:form>
				</div>
				<div class='row'>
				<p>The image should be 100 pixels wide and 120 pixels tall. </p>
				<p>A larger picture can be uploaded but it will take longer for pages to load.</p>
				
				</div>
	 		</div>	
	 		
		</div>
	</div>
	</body>
</html>