
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
	<title>Users</title>
		<style type="text/css">
			@import "css/data_table.css";
			@import "css/TableTool.css";
			
			span.hilite { font-weight: bold; }
		</style>
		
		<script type="text/javascript">
			$(document).ready(function()
			{
				$('#awardList').dataTable({
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
	 			<form:form action="saveAwardData.html" method="post"> 
				<table id="awardList" cellspacing="0" class="dataTable" summary="List of Awards, Merit Badges and Ranks">
					<thead>
						<tr>
							<th>Name</th>
							<th>description</th>
							<th>required</th>
							<th>Sort Order</th>
						</tr>
					</thead>
					<tbody class="highlightable">
					<c:forEach var="award" items="${importAwards}">
						<tr>
					    <td>${award.name}</td>
					    <td>${award.description}</td>
					    <td>${award.required}</td>
					    <td>${award.sortOrder}</td>
					    </tr>
					</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
					<input type="submit" value="Update Awards" class="button"/>
				</form:form>
			</div>
		</div>
		<div class='cell' id='page2'>
		</div>
		</div>
	</div>
	</body>
</html>