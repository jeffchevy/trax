<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
	<head>
		<title></title>
	    <link rel="stylesheet" href="css/jquery.ui.datepicker.css">
		<style>
			#logEntry {
				background-color: #445544;
			    color: white;
			    font-size: 1.5em;
			    text-align: left;
			}
		</style>
	</head>
	<body>
		<form:form action="updateServiceEntry.html" method="POST" commandName="logEntry"> 
		 <form:hidden path='id'></form:hidden>
 			<table>
 			<caption>Service for ${scout.fullName}</caption>
				<tr>
					<td><form:label path="typeOfProject">Type of Project</form:label></td>
					<td colspan='2'><form:input size="50" path="typeOfProject"/></td>
					<td><form:errors cssClass="errors" path="typeOfProject"/></td> 
				</tr>
				<tr>
					<td><form:label path="serviceDate">Depart Date</form:label></td>
					<td>
						<c:if test="${not empty logEntry.serviceDate}">
							<fmt:formatDate pattern="MM/dd/yyyy" var="serviceDateValue" value="${logEntry.serviceDate}" />
						</c:if>
						<input name="serviceDate" id="servicedatepicker" type="text" value="${serviceDateValue}"/>
					</td>
				</tr>
				<tr>
					<td><form:label path="timeInHours">Hours</form:label></td>
					<td colspan='2'><form:input size="50" path="timeInHours"/></td>
					<td><form:errors cssClass="errors" path="timeInHours"/></td> 
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
				$( "#servicedatepicker" ).datepicker();
			});
		</script>
	</body>
</html>
