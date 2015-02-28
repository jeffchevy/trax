<%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
	<title>Users</title>
		<style type="text/css">
			@import "css/TableTool.css";
			#page1 {
				background-color: #454;
				height: 550px;
				background-image: none;
			}
		</style>
		<link rel="stylesheet" href="css/jquery-ui.all.css">

		<script type="text/javascript">
		$(document).ready(function()
		{
			/* 
			
		    l - length changing input control
		    f - filtering input
		    t - The table!
		    i - Table information summary
		    p - pagination control
		    r - processing display element

			"sDom": '<"top"i>rt<"bottom"flp><"clear">'
			means
			'<' gives '<div>'
			'<"class"' gives '<div class="class">'
			'>' gives '</div>'
			<div class="top">i</div>rt<div class="bottom">flp</div><div class="clear"></div>
			*/
			$('#campEntryList').dataTable({
					"aaSorting": [[ 1, "asc" ]],
					"bPaginate": false,
					"bLengthChange": false,
					"bInfo": false,
					"sDom" : '<"top">rtf<"bottom"if<"clear">>',
					"tableTools": {
			            "aButtons": [
			                "copy",
			                "csv",
			                "xls",
			                {
			                    "sExtends": "pdf",
			                    "sPdfOrientation": "landscape",
			                    "sPdfMessage": "Your custom message would go here."
			                },
			                "print"
			            ]
			        }
			});
			$("#saveButton").click(function(){
				if ($('.scoutName').length!=0 //will be zero if this is a scout logged in
						&& ($('.scoutName:checked').length==0&&$('.selectedscout').length==0))
				{
					alert("Select a scout or scouts before saving");
				}
				else
				{
					$( "#campEntry" ).submit();
				}
			});
		});
		/*$( "#departdatepicker" ).click(function(event){
 			$( "#returndatepicker" ).value($( "#departdatepicker" ).value());
		});*/
		$(function() {
			$( "#departdatepicker" ).datepicker();
		});
		$(function() {
			$( "#returndatepicker" ).datepicker();
		});
		</script>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		<div class='cell' id='page1'>
			<div>
				<span id="CampLog"></span>
	 			<form:form action="savecamplog.html" method="post" commandName="campEntry">
	 			<table>
					<tr>
						<td><form:label path="location">Location*</form:label></td>
						<td><form:errors cssClass="errors" path="location"/></td> 
						<td><form:label path="departDate">Depart Date*</form:label></td>
						<td><form:label path="returnDate">Return Date*</form:label></td>
						<td><form:label path="description">Description*</form:label></td>
						<td><form:errors cssClass="errors" path="description" /></td> 
					</tr>
					<tr>
						<td colspan='2'><form:input path="location" /></td>
						<td>
							<c:if test="${not empty campEntry.departDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="departDateValue" value="${campEntry.departDate}" />
							</c:if>
							<input name="departDate" size="6" id="departdatepicker" type="text" value="${departDateValue}" />
						</td>
						<td>
							<c:if test="${not empty campEntry.returnDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="returnDateValue" value="${campEntry.returnDate}" />
							</c:if>
							<input name="returnDate" size="6" id="returndatepicker" type="text" value="${returnDateValue}" />
						</td>
						<td colspan='2'><form:input path="description" /></td>
						<td colspan='2'><input id="saveButton" type="button" value="Save" class="button"/></td>
					</tr>
					</table>
				</form:form>
				<br><br>
				<table id="campEntryList" cellspacing="0" class="dataTable scoutTable" summary="List of Camping entries">
				<caption>Camp Log</caption>
					<thead>
						<tr>
							<th>Location</th>
							<th>Depart Date</th>
							<th>Return Date</th>
							<th>Description</th>
							<th>Recorded by</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody class="highlightable">
					<c:forEach var="logEntry" items="${campLog}">
						<c:if test="${not empty logEntry.location}">
							<tr>
						    <td>${logEntry.location}</td>
						    <td>
								<c:if test="${not empty logEntry.departDate}">
									<fmt:formatDate pattern="MM/dd/yyyy" value="${logEntry.departDate}" />
								</c:if>
							</td>
							<td>
								<c:if test="${not empty logEntry.returnDate}">
									<fmt:formatDate pattern="MM/dd/yyyy" value="${logEntry.returnDate}" />
								</c:if>
							</td>
						    <td>${logEntry.description}</td>
						    <td>${logEntry.signOffLeader.fullName}</td>
						    <td>${logEntry.editLink}</td>
						    <td>${logEntry.removeLink}</td>
					    </tr>
					    </c:if>
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