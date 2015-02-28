<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
<%@ page import="org.trax.model.Scout"%>
<%@ page import="org.trax.model.Leader"%>
<html>
	<head>
	<title>Scout Report</title>
	<style>
	h1{
		page-break-before: always;
	}
	body, h1, h5 {
		color: black;
		background: white;
	}
	table {
		color: black;
		background-color: white;
		border: solid thin black;
		width: 100%;
		text-align: left;
		padding: 0 5px 25px 5px;
	}
	.hidden {
		display: none;
	}
	caption {
		font-size: 1.5em;
		color: #fff;
		background: #343;
	}
	td {
		border-left: solid thin #888;
		border-right: solid thin #888;
		border-bottom: solid thin #888;
	}
	thead {
		background-color: #CCCCAA;
		color: black;
		border-bottom: solid thin #000;
		text-align: center;
	}
	thead
	</style>
	<script src="js/jquery.js"></script>
	
	<script type="text/javascript" src="js/jquery.dataTables.js"></script>
	<script type="text/javascript">
	$(document).ready(function()
	{
		$('.award').dataTable({
			"aaSorting": [[ 0, "asc" ]],
			"bPaginate": false,
			"bLengthChange": false,
			"bInfo": false,
			"bFilter": false
		});
		$('.log').dataTable({
			"aaSorting": [[ 1, "asc" ]],
			"bPaginate": false,
			"bLengthChange": false,
			"bInfo": false,
			"bFilter": false
		});
	});
	</script>
	</head>
	
	<body>
	<div class="table">	
		<div class="row">
		<div class='cell'>
			<div>
				<trax:scoutreport></trax:scoutreport>
			</div>
		</div>
		</div>
	</div>
	</body>
</html>