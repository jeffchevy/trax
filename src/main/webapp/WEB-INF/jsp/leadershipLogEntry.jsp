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
			    text-align: left;
			}
		</style>
	</head>
	<body>
		<form:form action="updateLeadershipEntry.html" method="POST" commandName="logEntry"> 
		 <form:hidden path='id'></form:hidden>
 			<table>
 			<caption>Leadership for ${scout.fullName}</caption>
				<tr>
					<td><form:label path="position">Position</form:label></td>
					<td>
						<form:select path="position">
							<form:options items="${positions}" itemValue="positionName"/>
						</form:select> 
					</td>
				</tr>
				<tr>
					<td><form:label path="startDate">Start Date</form:label></td>
					<td>
						<c:if test="${not empty logEntry.startDate}">
							<fmt:formatDate pattern="MM/dd/yyyy" var="startDateValue" value="${logEntry.startDate}" />
						</c:if>
						<input name="startDate" id="startdatepicker" type="text" value="${startDateValue}"/>
					</td>
				</tr>
				<tr>	
					<td><form:label path="endDate">End Date</form:label></td>
					<td>
						<c:if test="${not empty logEntry.endDate}">
							<fmt:formatDate pattern="MM/dd/yyyy" var="endDateValue" value="${logEntry.endDate}" />
						</c:if>
						<input name="endDate" id="enddatepicker" type="text" value="${endDateValue}"/>
					</td>
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
				$( "#startdatepicker" ).datepicker();
			});
			$(function() {
				$( "#enddatepicker" ).datepicker();
			});
		</script>
	</body>
</html>
