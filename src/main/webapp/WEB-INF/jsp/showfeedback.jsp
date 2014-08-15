<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
	<head>
		<title>Send Feedback</title>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		 	<div id="page1" class="cell">
				<form:form id='emailform' action="sendfeedback.html" commandName="emailForm">
				<table id="pagequote">
					<tr><th colspan=2>Send feedback to ScoutTrax support</th></tr>
					<tr>
						<td><form:label path="message">Message</form:label></td>
						<td><form:textarea path="message" rows="10" cols="40"/></td>
						<td><form:errors cssClass="errors" path="message" /></td> 
					</tr>
					<tr>
						<td colspan="2" class='buttoncell' >
							<input class="button" type="submit" value="Send"/>
							<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" /> 
						</td>
					</tr>		
				</table>
			</form:form>
		</div> 
	 </div>	
	</div>
	</body>
</html>
