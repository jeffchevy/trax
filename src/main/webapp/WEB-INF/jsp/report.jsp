<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<style type="text/css">
		#page1 {
				background-image: none;
				background-color: #454;
				font: 22px;
		}
	</style>
	<script type="text/javascript">
		$(function() {
			$( "#startdatepicker" ).datepicker();
		});
		$(function() {
			$( "#enddatepicker" ).datepicker();
		});
		$(document).ready(function()
		{
		 	$('#completeReport').click(
			 	function()
			 	{
			 		window.open('scoutreport.html', 'SCOUTREPORT', 'WIDTH=700,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=no,toolbar=no,location=no'); w.focus(); return false;
			 	});
		 	$('#toFirstClass').click(
			 	function()
			 	{
			 		window.open('toFirstClass.html', 'SCOUTREPORT', 'WIDTH=700,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=no,toolbar=no,location=no'); w.focus(); return false;
			 	});
		});
	</script>
	<div class="table">	
		<div class="row">
		<div class='cell' id='page1'>
			<div>
				<span id='successMessage'>${successMessage}</span>		
	 			<form:form modelAttribute="report" action="createreport.html" method="get" commandName="reportForm"> 
	 			<table>
	 					<caption id='reportTitle'>
	 					<security:authorize access='!principal.unit.isCub'>
								Court of Honor Report
							</security:authorize>
							<security:authorize access='principal.unit.isCub'>
								Pack Meeting Report
							</security:authorize>
	 					</caption>
	 				<tr>
						<td colspan="2" style="text-align: center">Use this to create a report since the last awards ceremony. <br>Enter the dates to cover, then select the boys to include from the list on the right.</td>
					</tr>
					<tr style="text-align: left">
						<td><form:label path="startDate">Start Date</form:label></td>
						<td><form:label path="endDate">End Date</form:label></td>
					</tr>
					<tr>
						<td>
							<c:if test="${not empty reportForm.startDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="startDateValue" value="${reportForm.startDate}" />
							</c:if>
							<input name="startDate" id="startdatepicker" type="text" value="${startDateValue}" />
						</td>
						<td>
							<c:if test="${not empty reportForm.endDate}">
								<fmt:formatDate pattern="MM/dd/yyyy" var="endDateValue" value="${reportForm.endDate}" />
							</c:if>
							<input name="endDate" id="enddatepicker" type="text" value="${endDateValue}" />
						</td>
					</tr>
					<!-- handle this with the errorMessage 
					<tr>
						<td><form:errors cssClass="errors" path="startDate" /></td> 
						<td><form:errors cssClass="errors" path="endDate" /></td> 
					</tr>
					 -->
				</table>
				<input type="submit" value="Create Report" class="button"/>
				</form:form>
				<br>
				<hr><br>
				<table width="100%">
	 				<caption>Complete Record Report</caption>
	 				<tr>
						<td colspan="2" style="text-align: center">Use this to create a complete printable report.<br>
						Select the boys to include from the list on the right.</td>
					</tr>
				</table>
				<input id="completeReport" type="button" value="Create Report" class="button"/>
				<hr><br>
				<table width="100%">
					<security:authorize access='!principal.unit.isCub'>
						<caption>Tenderfoot to First Class Report</caption>
					</security:authorize>
					<security:authorize access='principal.unit.isCub'>
						<caption>Rank Report</caption>
					</security:authorize>
	 				<tr>
						<td colspan="2" style="text-align: center">Use this to create a complete printable report.<br>
						Select the boys to include from the list on the right.</td>
					</tr>
				</table>
				<input id="toFirstClass" type="button" value="Create Report" class="button"/>
			</div>
		</div>
		</div>
	</div>