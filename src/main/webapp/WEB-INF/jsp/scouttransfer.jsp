<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
	<head>
		<title>Add Scout</title>
	    <link rel="stylesheet" href="css/jquery-ui.all.css">
		
		<script src="js/jquery.js"></script>
		
	    <script type="text/javascript" src="js/jquery-ui-1.11.2.custom.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function()
			{
				$.get('loadstatecouncils.html', {state: $('#state').val()}, function(councilNames)
				{
					$('#council').html(councilNames);
				});
				$('#state').change(function()
				{
					$.get('loadstatecouncils.html', {state: $(this).val()}, function(councilNames)
					{
						$('#council').html(councilNames);
					})
					.error(function(){
						alert("No councils exist for this state");
					})
					.complete(function() { 
						//alert("complete"); 
					});
				});
			});
		</script>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		 	<div id="page1" class="cell">
				    <div id="pagequote">
					    <c:if test="${not empty searchForm}" >
					    	<form:form action="orgsearch.html" method="post" commandName="searchForm">
							    <h5>Move <c:out value="${fullName}"/> to a new unit?</h5>
							    <p>Use this search to determine if the new unit is already using ScoutTrax</p>
							    <div>
								    <form:hidden path="scoutId" />
								    <table>
										<tr>
											<td><form:label path="stateName">State</form:label></td>
											<td>
												<form:select id='state' path="stateName">
													<form:options items="${states}" /></form:select>
											</td>
										</tr>
									    <tr>
										    <td>
										    	<form:label path="councilName">Council</form:label>
										    </td>
										    <td>
												<form:select id='council' path="councilName">
													<form:options items="${councils}" /></form:select>
										    </td>
									    </tr>
									    <tr>
											<td><form:label path="typeOfUnit">Unit type*</form:label></td>
											<td><form:select path="typeOfUnit">
												<form:options items="${unitTypes}" itemValue="id" itemLabel="name"/>
											</form:select></td>
										</tr>
									    <tr>
										    <td>
										    	<form:label path="unitNumber">Unit #:</form:label>
										    </td>
										    <td>
										    	<form:input path="unitNumber" cssStyle="width:5em"/> 
										    </td>
									    </tr>
									    <tr>
										    <td></td>
										    <td>
									 			<input class="button" type="submit" value="Search"/>
										    </td>
									    </tr>
									</table>
							    </div>
						    </form:form>
					    </c:if>
					    <hr>
					    <c:if test="${not empty scoutTransfer}" >
						<form:form id='transferform' method="get" action="transferScout.html" commandName="scoutTransfer">
						    <form:hidden path="scoutId" />
						    <form:hidden path="leader.id" />
							<table>
								<th colspan=2></th>
										<tr>
									<td><form:label path="leader.firstName">First Name</form:label></td>
									<td><form:input path="leader.firstName" /></td>
									<td><form:errors cssClass="errors" path="leader.firstName" /></td> 
								</tr>
								<tr>
									<td><form:label path="leader.lastName">Last Name</form:label></td>
									<td><form:input path="leader.lastName" /></td>
									<td><form:errors cssClass="errors" path="leader.lastName" /></td> 
								</tr>
								<tr>
									<td><form:label path="leader.email">Email</form:label></td>
									<td><form:input path="leader.email" /></td>
									<td><form:errors cssClass="errors" path="leader.email" /></td> 
								</tr>
								<tr>
									<td><form:label path="leader.position">Position</form:label></td>
									<td><form:select path="leader.position">
										<form:options items="${positions}" itemValue="positionName"/>
									</form:select></td>
								</tr>
								<tr>
									<td><form:label path="leader.city">City</form:label></td>
									<td><form:input path="leader.city" /></td>
									<td><form:errors cssClass="errors" path="leader.city" /></td> 
								</tr>
								<tr>
									<td><form:label path="leader.zip">Zip</form:label></td>
									<td><form:input path="leader.zip" /></td>
									<td><form:errors cssClass="errors" path="leader.zip" /></td>
								</tr>
								<tr>
									<td colspan="2" class='buttoncell' >
										<input class="button" type="submit" value="Transfer"/>
										<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" /> 
									</td>
								</tr>		
							</table>
						</form:form>
					</c:if>
				</div>
		</div> 
		<!-- <div id='page2' class='cell'>
 		</div> -->	
	 </div>	
	</div>
	</body>
</html>