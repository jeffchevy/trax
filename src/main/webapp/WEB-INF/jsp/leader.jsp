<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<html>
<head>
<script type="text/javascript" src="js/jquery.maskedinput-custom-1.3.js"></script>
<script type="text/javascript">
		$(document).ready(function()
		{
			$(function() {
				$("#datepicker").datepicker();
				$("#phone").mask("?(999) 999-9999");
				$("#workPhone").mask("?(999) 999-9999");
				$("#cellPhone").mask("?(999) 999-9999");
				$("#datepicker").mask("99/99/9999");
			});
			$( "#removeDialog" ).dialog({
				resizable: false,
				autoOpen: false,
				width: 340,
				height:140,
				modal: true,
				buttons: {
					"Remove user": function() {
						window.location.href='retireuser.html?userId='+${leader.id};
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});
				
			$("#remove").click(function(){
				$( "#removeDialog" ).dialog("open");
			});
		});
	</script>
</head>
	<body>
		<form:form method="post" action="saveleader.html" commandName="leader">
		<form:hidden path="id" />
		    <div id="trooppage">
				<table>
					<caption>
							<c:if test="${empty leader.firstName}">Enter the leader's information</c:if>
							<c:if test="${not empty leader.firstName}">Update the leader's information</c:if>
						</caption>
					<tr>
						<td><form:label path="firstName">First Name*</form:label></td>
						<td><form:input path="firstName" /></td>
						<td><form:errors cssClass="errors" path="firstName" /></td> 
					</tr>
					<tr>
						<td><form:label path="lastName">Last Name*</form:label></td>
						<td><form:input path="lastName" /></td>
						<td><form:errors cssClass="errors" path="lastName" /></td> 
					</tr>
					<tr>
						<td><form:label path="email">Email</form:label></td>
						<td><form:input path="email" /></td>
						<td><form:errors cssClass="errors" path="email" /></td> 
					</tr>
					<tr>
						<td><form:label path="position">Position</form:label></td>
						<td>
							<security:authorize access='!principal.unit.isCub'>
								<form:select path="position">
									<form:option value="0" label="Select" />
									<form:options items="${positions}" itemValue="positionName"/>
								</form:select>
							</security:authorize>
							<security:authorize access='principal.unit.isCub'>
								<form:select path="cubPosition">
									<form:option value="0" label="Select" />
									<form:options items="${positions}" itemValue="positionName"/>
								</form:select>
							</security:authorize>
						</td>
					</tr>
					<tr>
						<td><form:label path="bsaMemberId">BSA Member Id:</form:label></td>
						<td><form:input path="bsaMemberId" /></td>
						<td><form:errors cssClass="errors" path="bsaMemberId" /></td> 
					</tr>
					<tr>
						<td><form:label path="phone">Telephone</form:label></td>
						<td><form:input path="phone"/></td>
						<td><form:errors cssClass="errors" path="phone" /></td> 
					</tr>
					<tr>
						<td><form:label path="cellPhone">Cell Phone</form:label></td>
						<td><form:input path="cellPhone"/></td>
						<td><form:errors cssClass="errors" path="cellPhone" /></td> 
					</tr>
					<tr>
						<td><form:label path="workPhone">Work Phone</form:label></td>
						<td><form:input path="workPhone"/></td>
						<td><form:errors cssClass="errors" path="workPhone" /></td> 
					</tr>
					<tr>
						<td><form:label path="address">Address</form:label></td>
						<td><form:input path="address" /></td>
						<td><form:errors cssClass="errors" path="address" /></td> 
					</tr>
					<tr>
						<td><form:label path="state">State*</form:label></td>
						<td><form:select path="state"><form:options items="${states}" /></form:select></td>
					</tr>		
					<tr>
						<td><form:label path="city">City*</form:label></td>
						<td><form:input path="city" /></td>
						<td><form:errors cssClass="errors" path="city" /></td> 
					</tr>
					<tr>
						<td><form:label path="zip">Zip</form:label></td>
						<td><form:input path="zip"/></td>
						<td><form:errors cssClass="errors" path="zip" /></td>
					</tr>
					<tr>
						<td><form:label path="unit.typeOfUnit">Unit type*</form:label></td>
						<td><form:select path="unit.typeOfUnit">
							<form:options items="${unitTypes}" itemValue="id" itemLabel="name"/>
						</form:select></td>
					</tr>
					<tr>
						<td><form:label path="unit.number">Unit Number*</form:label></td>
						<td><form:input path="unit.number" /></td>
					</tr>
					<tr>
						<td>
							<h5 style="color:gray;">* indicates a required field</h5>
						</td>
						<td class='buttoncell' >
							<security:authorize access="hasRole('ROLE_LEADER')">
					  			<input id="remove" class="button" title="Click to remove this leader." type="button" value="Remove" >
							</security:authorize>
							<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()"  title="Click to cancel this update."/>
							<input class="button" type="submit" value="Save" title="Click to save the changes made to this record."/>
						</td>
					</tr>
			</table>
		</div>
		<div id="removeDialog" title="Warning, action cannot be undone!">This will permanently remove this User.</div>
	</form:form>
	</body>
</html>
