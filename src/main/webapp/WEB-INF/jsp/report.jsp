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
		function isAtLeastOneSelected() {
				//make sure at least one boy is selected and we have scouts (not when a boy is logged in)
			if ($('.scoutName:checked').length==0 && $('.selectedscout').length==0 && $('.scoutName').length>0)
			{
				$( "#noScoutSelectedDialog" ).dialog("open");
				return false;
			}
			return true;
		}
		
		$(document).ready(function()
		{
		 	$('#completeReport').click(
			 	function()
			 	{
 	 				if (isAtLeastOneSelected())
 	 				{
 	 					window.open('scoutreport.html', 'SCOUTREPORT', 'WIDTH=700,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=no,toolbar=no,location=no'); w.focus(); return false;			
 	 				}
			 	});
		 	$('#toFirstClass').click( function()
 			{
		 		if (isAtLeastOneSelected())
				{
		 			document.location.href="toFirstClass.html";			
	 			}
 				
 			});
		 	$('#honorReport').click( function()
 			{
		 		if (isAtLeastOneSelected())
				{
		 			$( "#reportForm" ).submit();		
	 			}
 				
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
					<input id="honorReport" type="button" value="Create Report" class="button"/>
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
						<caption>Scout to First Class Report</caption>
					</security:authorize>
					<security:authorize access='principal.unit.isCub'>
						<caption>Bobcat to Bear Report</caption>
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