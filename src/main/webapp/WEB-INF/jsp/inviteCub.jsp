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
			});
		</script>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		 	<div id="page1" class="cell">
				    <div id="pagequote">
						<form:form id='transferform' method="post" action="cubInvite.html"  commandName="leader">
							<form:hidden path='city' ></form:hidden>
							<form:hidden path='state' ></form:hidden>
							<form:hidden path='zip' ></form:hidden>
						    <c:if test="${not isCub}">
								<h5>Invite the Pack leader from your organization</h5>
							</c:if>
							<c:if test="${isCub}">
								<h5>Invite the Troop leader from your organization</h5>
							</c:if>
							<table>
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
									<td><form:label path="email">Email*</form:label></td>
									<td><form:input path="email" /></td>
									<td><form:errors cssClass="errors" path="email" /></td> 
								</tr>
								<tr>
									<td><form:label path="cubPosition">Position*</form:label></td>
									<td><form:select path="cubPosition">
										<form:options items="${positions}" itemValue="positionName"/>
									</form:select></td>
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
									<td colspan="2" class='buttoncell' >
										<input class="button" type="submit" value="Invite Leader"/>
										<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" /> 
									</td>
								</tr>		
							</table>
						</form:form>
				</div>
		</div> 
		<!-- <div id='page2' class='cell'>
 		</div> -->	
	 </div>	
	</div>
	</body>
</html>
