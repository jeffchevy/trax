<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
	<title>Service Log</title>
		<style type="text/css">
			@import "css/data_table.css";
			@import "css/TableTool.css";
			#page1 {
				background-color: #454;
				height: 550px;
				background-image: none;
			}
		</style>
		<link rel="stylesheet" href="css/jquery.ui.datepicker.css">
		<link rel="stylesheet" href="css/jquery.ui.theme.css">
		<link rel="stylesheet" href="css/jquery.ui.all.css">
		<link rel="stylesheet" href="css/jquery.ui.base.css">
		<link rel="stylesheet" href="css/jquery.ui.core.css"> 
		
		<script type="text/javascript">
			$(document).ready(function()
			{
				$('#serviceEntryList').dataTable({
					"bPaginate": false,
					"bLengthChange": false,
					"bInfo": false,
					"sDom": '<"top">rt<"bottom"if<"clear">>' 
				});
				$("#saveButton").click(function(){
					if ($('.scoutName').length!=0 //will be zero if this is a scout logged in
						&& ($('.scoutName:checked').length==0&&$('.selectedscout').length==0))
					{
						alert("Select a scout or scouts before saving");
					}
					else
					{
						$( "#service" ).submit();
					}
				});
			});
			$(function() {
				$( "#serviceDatepicker" ).datepicker();
			});
		</script>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		<div class='cell' id='page1'>
			<div>
			<span id="ServiceLog"></span>
			
	 			<form:form action="saveservicelog.html" method="post" commandName="service"> 
		 			<table>
					<tr>
						<td><form:label path="typeOfProject">Type Of Project*</form:label></td>
						<td><form:errors cssClass="errors" path="typeOfProject" /></td> 
						<td><form:label path="serviceDate">Service Date*</form:label></td>
						<td><form:label path="timeInHours">Hours*</form:label></td>
						<td><form:errors cssClass="errors" path="timeInHours" /></td> 
						<td><form:label path="description">Description*</form:label></td>
						<td><form:errors cssClass="errors" path="description" /></td> 
					</tr>
					<tr>
						<td colspan='2'><form:input path="typeOfProject" /></td>
						<td>
							<c:if test="${not empty service.serviceDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="serviceDateValue" value="${service.serviceDate}" />
							</c:if>
							<input name="serviceDate" size="6" id="serviceDatepicker" type="text" value="${serviceDateValue}" />
						</td>
						<td colspan='2'><form:input path="timeInHours" /></td>
						<td colspan='2'><form:input path="description" /></td>
						<td colspan='2'><input id="saveButton" type="button" value="Save" class="button"/></td>
					</tr>
					</table>			
				</form:form>
				<br><br>
				<table id="serviceEntryList" cellspacing="0" class="dataTable scoutTable" summary="List of Service log entries">
					<caption>Service Log</caption>
					<thead>
						<tr>
							<th>Type of Project</th>
							<th>Date</th>
							<th>Hours</th>
							<th>Description</th>
							<th>Recorded by</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody class="highlightable">
					<c:forEach var="logEntry" items="${serviceLog}">
					  <tr>
					    <td>${logEntry.typeOfProject}</td>
					    <td>
							<c:if test="${not empty logEntry.serviceDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" value="${logEntry.serviceDate}" />
							</c:if>
						</td>
					    <td>${logEntry.timeInHours}</td>
					    <td>${logEntry.description}</td>
					    <td>${logEntry.signOffLeader.fullName}</td>
						<td>${logEntry.editLink}</td>					    
						<td>${logEntry.removeLink}</td>
					  </tr>
					</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
			</div>
		</div>
		</div>
	</div>
	</body>
</html>