<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<link rel="stylesheet" href="css/jquery.ui.datepicker.css">
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.js"></script>
		<script type="text/javascript" src="js/jquery.jeditable.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.0.custom.js"></script>
		<script type="text/javascript" src="js/jquery.validate.js"></script>
		<script type="text/javascript" src="js/jquery.jeditable.datepicker.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.editable.js"></script>
		<script type="text/javascript" src="js/dataTableColumnFilter.js"></script>
		<title>Users</title>
		<style type="text/css">
			@import "css/data_table.css";
			@import "css/TableTool.css";
			span.hilite { font-weight: bold; }
			.dataTables_filter {
			    float: right;
			    text-align: right;
			    width: 100%;
			    margin-right: 0px;
				margin-bottom: 30px;
			}
			.requiredreport_info {
				font-size: 1.2em;
			}
			.bottom {
				font-size: 1.8em;
			}
			caption {
				font-size: 1.5em;
				color: #fff;
				background: #343;
			}
			.odd {
				background-color: #CDC;
			}
			th.sorting_asc {
				background: url("images/sort_asc.png") no-repeat scroll right center #797;
			}
			th.sorting_desc {
				background: url("images/sort_desc.png") no-repeat scroll right center #797;
			}
			table {
				white-space: nowrap;
			}
			#ui-dialog-title-dialog {
				color: #A11; 
			}
			#page1, #page2 {
				background: #454;
				border-left: solid thin black;
			}
			#pagequote {
				margin: 0;
			}
			#subject {
				width: 80%;
			}
			select { font-size: 12px !important; }
			.darkbackground {
				margin: 0 0 20px !important;
			}
			.darkbackground th {
				background: #343;
			}
			input, select { 
				font-size: 12px; padding-left: 12px; padding-right: 0px; 
			}
			th.rotateheader {
				font: bold;
				height: 130px;
			  	white-space: nowrap;
			}
			td.rowheader {
				font-style: bold;
				text-align: left;
				background: #D1DC9A !important
			}
			th.rotateheader > div {
			  transform: translate(17px, 51px) rotate(-45deg);
			  -webkit-transform: translate(17px, 51px) rotate(-45deg);
			  width: 30px;
			  color: white;
			}
			th.rotateheader > div > span {
			  border-bottom: 1px solid #fff;
			  padding: 5px 10px;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function()
			{
				var leaderTable = $('#leaderList').dataTable({
			        "bFilter": false,
			        "bStateSave": false,
					"bPaginate": false,
					"bLengthChange": false,
					"bInfo": false,
					"aaSorting": [[ 0, "asc" ]],
					"aoColumns": [
							 {"sType": "string"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
							 {"sType": "date"},
 							 {"sType": "date"}
						 ],
					"sDom": '<"top">rt<"bottom"if<"clear">>'
				})
				<security:authorize  access="hasRole('ROLE_LEADER')">
				.makeEditable(
                	{
                     "aoColumns":
                         [
                        		null, //name
                        		<c:forEach var="course" items="${courses}">
							 {
							 	sUpdateURL: function(value, settings){
							 		var columnPosition = this.cellIndex;
							 	    var columnName = $(this).closest('table').find('th:eq('+columnPosition+')').html();
							 	    var rowId = this.parentNode.id
							 		$.ajax({
							 			url : "addcourse.html",
							 			type: "POST",
							 			dataType: 'json',
							 			data: {value: value, id: rowId, columnName:columnName},
							 			success: function(data){
							 				if(data.errorMessage!==undefined)
							 				{
							 					alert("An error occurred:"+data.errorMessage);
							 					value="";
							 				}
							 				else if( data.successMessage!==undefined)
							 				{
							 					alert(data.successMessage);
							 				}
							 				else alert("Ahh!");
							 			},
									      error: function (xhr, ajaxOptions, thrownError) {
									        alert(xhr.status);
									        alert(thrownError);
									      }
							 			}); 
									 
                               		return value;
              						},
							 	
							 	loadtext: 'loading...', 
							 	indicator: 'Saving course Date...', 
							 	tooltip: 'Click to edit course', 
							 	type: 'datepicker', 
							 	onblur: 'cancel',
							 	submit:'OK', 
							 	width:'100px',
							 	placeholder: '        ',
							 	event: 'click'
							 },
							</c:forEach>
                         ]          
                	});
				    </security:authorize>
				//$("#hasdatepicker").mask("99/99/9999");
			} );    
		</script>		
	</head>
	
	<body>
	<div class="table">					
		<div class="row">
			<div id='page1' class='cell'>
					<table id="leaderList" cellspacing="0" class="dataTable" summary="Leaders">
						<caption>Adult Training</caption>
						<thead>
							<tr>
								<th>Leader Name</th>
								<c:forEach var="course" items="${courses}">
								 <th class="rotateheader"><div>${course.name}</div></th>
								</c:forEach>
							</tr>
						</thead>
						<tfoot>
						<tr>
								<td>Online Training Links</td>
								<c:forEach var="course" items="${courses}">
								 <td>${course.name}<br>
								 <c:if test="not empty course.link"></c:if>
									 <a class='trainingLink' title='Click here to view training' target='_blank' href='${course.link}'>
									 <img id='awardimage' src='images/onlinetraining.png' alt='View link'></a>
								 </td>
								</c:forEach>
							</tr>
						</tfoot>
						<tbody class="highlightable">	
						<c:forEach items="${trainingTable}" var="trainingRows">
								<c:forEach items="${trainingRows}" var="data" varStatus="status">
									<c:if test="${status.count==1}">
									  <tr id='${data}'> 
									</c:if>
									<c:if test="${status.count==2}">
									  	<td style='font-weight:bold;'>
											${data}
									    </td>
									</c:if>
									<c:if test="${status.count>2}">
										<td>
											${data}
									    </td>
									</c:if>
								</c:forEach>
						    </tr>
						</c:forEach>
						</tbody>
						<tfoot>
							<security:authorize  access="hasRole('ROLE_LEADER')">
								<tr><td colspan="100%">To add a course date, click on a table cell and select the appropriate date.</td></tr>
							</security:authorize>
						</tfoot>
					</table>
	 		</div>	
		</div>
	</div>
	</body>
</html>