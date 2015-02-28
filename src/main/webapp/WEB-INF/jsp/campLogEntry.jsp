<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
	<head>
		<title></title>
	    <link rel="stylesheet" href="css/jquery-ui.all.css">
		<style>
			#logEntry {
				background-color: #445544;
			    color: white;
			    font-size: 1.5em;
			}
			#logEntry.cell {
				text-align: left;
			}
		</style>
	</head>
	<body>
		<form:form action="updateCampEntry.html" method="POST" commandName="logEntry"> 
		 <form:hidden path='id'></form:hidden>
 			<table>
 			<caption>Camp log for ${scout.fullName}</caption>
				<tr>
					<td><form:label path="location">Location</form:label></td>
					<td colspan='2'><form:input size="50" path="location"/></td>
					<td><form:errors cssClass="errors" path="location"/></td> 
				</tr>
				<tr>
					<td><form:label path="departDate">Depart Date</form:label></td>
					<td>
						<c:if test="${not empty logEntry.departDate}">
							<fmt:formatDate pattern="MM/dd/yyyy" var="departDateValue" value="${logEntry.departDate}" />
						</c:if>
						<input name="departDate" id="departdatepicker" type="text" value="${departDateValue}"/>
					</td>
					<td><form:errors cssClass="errors" path="departDate" /></td> 
				</tr>
				<tr>	
					<td><form:label path="returnDate">Return Date</form:label></td>
					<td>
						<c:if test="${not empty logEntry.returnDate}">
							<fmt:formatDate pattern="MM/dd/yyyy" var="returnDateValue" value="${logEntry.returnDate}" />
						</c:if>
						<input name="returnDate" id="returndatepicker" type="text" value="${returnDateValue}"/>
					</td>
					<td><form:errors cssClass="errors" path="returnDate" /></td> 
				</tr>
				<tr>
					<td><form:label path="description">Description</form:label></td>
					<td colspan='2'><form:textarea cols="70" rows="3" path="description" /></td>
					<td><form:errors cssClass="errors" path="description" /></td> 
				</tr>
				<tr>
					<td colspan='3' style='text-align: right'>
					<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" /> 
					<input id='updateButton' type="submit" value="Update" class="button"/>
					</td>
				</tr>
			</table>
		</form:form>
	    
		<script type="text/javascript">
			$(function() {
				$( "#departdatepicker" ).datepicker();
			});
			$(function() {
				$( "#returndatepicker" ).datepicker();
			});
		</script>
	</body>
</html>
