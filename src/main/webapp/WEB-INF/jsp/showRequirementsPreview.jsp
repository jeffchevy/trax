
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
	<title>Users</title>
		<style type="text/css">
			span.hilite { font-weight: bold; }
			#page1 {
				background-color: #454;
				height: 550px auto;
				background-image: none;
			}
		</style>
		
		<script type="text/javascript">
		$(document).ready(function()
		{
			$('#requirementList').dataTable({
				"bPaginate": false,
				"bLengthChange": false 
			});
		});
		</script>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		<div class='cell' id='page1'>
			<div>
	 			<form:form action="saveRequirementData.html" method="post"> 
				<table id="requirementList" border=1 width="100%" cellspacing="0" class="dataTable" summary="List of Requirements for Awards, Merit Badges and Ranks">
					<thead>
						<tr>
							<th>Award Config Id</th>
							<th>Award Name</th>
							<th>text</th>
							<th>isLeaderAuthorized</th>
							<th>Sort Order</th>
						</tr>
					</thead>
					<tbody class="highlightable">
					<c:forEach var="requirement" items="${importRequirements}">
						<tr>
					    <td>${requirement.awardConfig.id}</td>
					    <td>${requirement.awardConfig.name}</td>
					    <td>${requirement.text}</td>
					    <td>${requirement.leaderAuthorized}</td>
					    <td>${requirement.sortOrder}</td>
					    </tr>
					</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
					<input type="submit" value="Update Requirements" class="button"/>
				</form:form>
			</div>
		</div>
		</div>
	</div>
	</body>
</html>