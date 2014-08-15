<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
	<head>
	<title>Users</title>
		<style type="text/css">
			@import "css/data_table.css";
			@import "css/TableTool.css";
			#page1 {
				background-color: #454;
				min-height: 550px;
				height: auto;
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
				$('#leadershipEntryList').dataTable({
					"aaSorting": [[ 1, "asc" ]],
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
						$( "#leadershipEntry" ).submit();
					}
				});
			});
			$(function() {
				$( "#startdatepicker" ).datepicker();
			});
			$(function() {
				$( "#enddatepicker" ).datepicker();
			});
		</script>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		<div class='cell' id='page1'>
			<div>
				<span id="LeadershipLog"></span>
	 			<form:form action="saveleadershiplog.html" method="post" commandName="leadershipEntry"> 
	 			<table>
	 			<!-- headers -->
					<tr>
						<td><form:label path="position">Position*</form:label></td>
						<td><form:label path="startDate">Start Date*</form:label></td>
						<td><form:errors cssClass="errors" path="startDate" /></td> 
						<td><form:label path="endDate">Release Date*</form:label></td>
						<td><form:errors cssClass="errors" path="endDate" /></td> 
						<td><form:label path="description">Description*</form:label></td>
						<td><form:errors cssClass="errors" path="description" /></td> 
					</tr>
					<tr>
						<td>
							<form:select path="position">
								<form:options items="${positions}" itemValue="positionName"/>
							</form:select>
						</td>
						<td colspan='2'>
							<c:if test="${not empty leadershipEntry.startDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="startDateValue" value="${leadershipEntry.startDate}" />
							</c:if>
							<input name="startDate" size="6" id="startdatepicker" type="text" value="${startDateValue}" />
						</td>
						<td>
							<c:if test="${not empty leadershipEntry.endDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="endDateValue" value="${leadershipEntry.endDate}" />
							</c:if>
							<input name="endDate" size="6" id="enddatepicker" type="text" value="${endDateValue}" />
						</td>
						<td colspan='2'><form:input path="description" /></td>
						<td colspan='2'><input id="saveButton" type="button" value="Save" class="button"/></td>
					</tr>
					</table>
				</form:form>
				<br><br>
				<table id="leadershipEntryList" cellspacing="0" class="dataTable scoutTable" summary="List of leadershiping entries">
					<caption>Leadership Log</caption>
					<thead>
						<tr>
							<th>Position</th>
							<th>Start Date</th>
							<th>Release Date</th>
							<th>Description</th>
							<th>Recorded by</th>
							<th></th>
							<th></th>							
						</tr>
					</thead>
					<tbody class="highlightable">
					<c:forEach var="logEntry" items="${leadershipLog}">
						<tr>
					    <td>${logEntry.position.positionName}</td>
						<td>
							<c:if test="${not empty logEntry.startDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" value="${logEntry.startDate}" />
							</c:if>
						</td>
						<td>
							<c:if test="${not empty logEntry.endDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" value="${logEntry.endDate}" />
							</c:if>
						</td>
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