<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<html>
	<head>
		<title>Add Scout</title>
	    <link rel="stylesheet" href="css/jquery-ui.all.css">

		<script type="text/javascript" src="js/new/jquery.mask.js"></script>
		<script type="text/javascript">
			$(document).ready(function()
			{
				$(function() {
					$("#datepicker").datepicker();
					$("#phone").mask("(000) 000-0000", {placeholder: "(___) ___-____"});
					$("#workPhone").mask("(000) 000-0000", {placeholder: "(___) ___-____"});
					$("#cellPhone").mask("(000) 000-0000", {placeholder: "(___) ___-____"});
					$("#datepicker").mask("00/00/0000", {placeholder: "__/__/____"});
				});
								
				$("#remove").click(function(){
					$( "#removeDialog" ).dialog("open");
				});
				
				$( "#removeDialog" ).dialog({
					resizable: false,
					autoOpen: false,
					width: 340,
					height:160,
					modal: true,
					buttons: {
						"Remove user": function() {
							window.location.href='retireuser.html?userId='+${scout.id};
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
				
				$("#importImage").click(function(){
					var iSize = ($("#fileData")[0].files[0].size / 1024);
					if ((Math.round(iSize * 100) / 100)>200)
					{
						alert("File size is TOO BIG, it should be less than 100kb");
					}
					else
					{
						$( "#uploadItem" ).submit();
					}
				});
				$("#fileData").change(function ()
				{
					var iSize = ($("#fileData")[0].files[0].size / 1024);
					if (iSize / 1024 > 1)
					{
					   if (((iSize / 1024) / 1024) > 1)
					   {
					       iSize = (Math.round(((iSize / 1024) / 1024) * 100) / 100);
					       alert("File size " + iSize + "Mb is too big, it should be less than 100kb");
					   }
					   else
					   {
					       iSize = (Math.round((iSize / 1024) * 100) / 100)
					       alert("File size " + iSize + "Mb is too big, it should be less than 100kb");
					   }
					}
					else
					{
					   iSize = (Math.round(iSize * 100) / 100)
					   if(iSize>100)
					   {
					   		alert("File size " + iSize + "kb is too big, it should be less than 100kb")
					   	}
					}   
				}); 
			});
			
		</script>
		<style type="text/css">
		caption {
		    font-size: 1em;
		}
		</style>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		 	<div id="page1" class="cell">
				<form:form id='savescoutform' method="post" action="savescout.html" commandName="scout">
					<form:hidden path="id"/>
					<table class="darkbackground">
						<caption>
							<c:if test="${empty scout.firstName}">Enter the scout's information</c:if>
							<c:if test="${not empty scout.firstName}">Update the scout's information</c:if>
						</caption>
						<tr>
							<td><form:label path="firstName">First Name*</form:label></td>
							<td><form:input path="firstName"/></td>
						</tr>
						<tr>
							<td colspan="2"><form:errors cssClass="errors" path="firstName" /></td> 
						</tr>
						<tr>
							<td><form:label path="middleName">Middle Name</form:label></td>
							<td><form:input path="middleName"/></td>
						</tr>
						<tr>
							<td colspan="2"><form:errors cssClass="errors" path="middleName" /></td> 
						</tr>
						<tr>
							<td><form:label path="lastName">Last Name*</form:label></td>
							<td><form:input path="lastName"/></td>
							<td><form:errors cssClass="errors" path="lastName" /></td> 
						</tr>
						<tr>
							<td><form:label path="bsaMemberId">BSA Member Id</form:label></td>
							<td><form:input path="bsaMemberId" /></td>
							<td><form:errors cssClass="errors" path="bsaMemberId" /></td> 
						</tr>
						<tr>
							<td><form:label path="birthDate">Birth Date</form:label></td>
							<td>
								<c:if test="${not empty scout.birthDate}">
									<fmt:formatDate pattern="MM/dd/yyyy" var="birthDateValue" value="${scout.birthDate}" />
								</c:if>
								<input name="birthDate" id="datepicker" type="text" value="${birthDateValue}" />
								Age: <span>${scout.age}</span>
							</td>
							<td><form:errors cssClass="errors" path="birthDate" /></td> 
						</tr>	
						<tr>
							<td><form:label path="email">Email</form:label></td>
							<td><form:input path="email" /></td>
							<td><form:errors cssClass="errors" path="email" /></td> 
						</tr>
						<tr>
							<td><form:label path="phone">Telephone</form:label></td>
							<td><form:input path="phone" /></td>
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
							<td><form:label path="city">City</form:label></td>
							<td><form:input path="city" /></td>
							<td><form:errors cssClass="errors" path="city" /></td> 
						</tr>
						<tr>
							<td><form:label path="zip">Zip</form:label></td>
							<td><form:input path="zip" /></td>
							<td><form:errors cssClass="errors" path="zip" /></td>
						</tr>
						<tr>
							<td><form:label path="state">State*</form:label></td>
							<td>
								<form:select path="state">
									<form:options items="${states}" /></form:select>
							</td>
						</tr>	
						<tr>
							<td><form:label path="unit.typeOfUnit">Unit type*</form:label></td>
							<td><form:select path="unit.typeOfUnit">
								<form:options items="${unitTypes}" itemValue="id" itemLabel="name"/>
							</form:select></td>
						</tr>
						<tr>
							<td><form:label path="unit.number">Unit Number*</form:label></td>
							<td style="white-space: nowrap;">
								<form:input cssStyle="width:5em" path="unit.number" />
							</td>
							<td><form:errors cssClass="errors" path="unit.number" /></td>
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
							<td>
								<h5 style="color:gray;">* indicates a required field</h5>
							</td>
							<td class='buttoncell' >
								<security:authorize access="hasRole('ROLE_LEADER')">
						  			<c:if test="${not empty scout.firstName}">
							  			<input id="remove" class="button" title="Click to remove a boy." type="button" value="Remove">
						  				<input class="button" title="Click to transfer a boy to another troop/team or crew." type="button" value="Transfer" 
							  				onclick="location.href='showtransfer.html?scoutId=${scout.id}&fullName=${scout.fullName}&state=${scout.organization.state}&council=${scout.organization.council}'"/>
									</c:if>
								</security:authorize>
								<input class="button" type="submit" value="Save" title="Click to save the changes made to this record."/>
								<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" title="Click to cancel this update."/> 
							</td>
						</tr>
					</table>
				</form:form>
			</div> 
			<div id='page2' class='cell'>
				<div class='row'>
					<img id='scoutimage' class='dropshadow' alt='${scout.fullName}' src='scoutimage.html?id=${scout.id}'>
				</div>
				<div class="darkbackground" class='row'>
					<form:form modelAttribute="uploadItem" method="post" action="saveprofileimage.html" enctype="multipart/form-data">
						<form:hidden path='scoutId' ></form:hidden>
	               		<form:label for="fileData" path="fileData">Upload Picture</form:label>
	               		<form:input path="fileData" type="file" accept="image/*"/>
	               		<input id="importImage" class="button" type="button" value="Import"/>
			     	</form:form>
				<br><label>The image should be 100 x 120 pixels. </label>
				<br>
				</div>
	 		</div>
		</div>	
		<div id="removeDialog" title="Warning, action cannot be undone!">
			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
			   This will permanently remove this User. Are you sure?</p>
		</div>
	</div>
	</body>
</html>
