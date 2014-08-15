
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
	<title>Manage Site Data</title>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		<div class='cell' id='page1'>
			<div>
			</div>
			<fieldset class='table'>
				<legend>Add or update Merit Badges, Ranks or Awards and their requirements</legend>
				<div class='row'>
					<div id='import' class='cell'>
						 <form:form modelAttribute="uploadAwards" method="post" action="parseAwards.html" enctype="multipart/form-data">
				               <p>
				                   <form:label for="fileData" path="fileData">Award Config File</form:label><br/>
				                   <form:input path="fileData" type="file"/>
				               </p>
				
				               <p>
				                   <input type="submit" value="Import Preview"/>
				               </p>
					       </form:form>
						 <form:form modelAttribute="uploadRequirements" method="post" action="parseRequirements.html" enctype="multipart/form-data">
				               <p>
				                   <form:label for="fileData" path="fileData">Requirement Config File</form:label><br/>
				                   <form:input path="fileData" type="file"/>
				               </p>
				
				               <p>
				                   <input type="submit" value="Import Preview"/>
				               </p>
					       </form:form>
					</div>
				</div>
			</fieldset>
		</div>
		<div class='cell' id='page2'>
		</div>
		</div>
	</div>
	</body>
</html>