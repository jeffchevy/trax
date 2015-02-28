<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
<html>
	<head>
	<script src="js/dataTableColumnFilter.js" type="text/javascript"></script>
	
	<title>Users</title>
		<style type="text/css">
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
			#upload {
				background: #343;
				border-left: solid thin black;
			}
			#uploadItem {
				margin: 0  20px 0 0;
				float: left;
				text-align: left;
			}
			#inviteCubs {
				float: right;
				margin: 0 20px 0 20px;
			}
		select { font-size: 12px !important; }
		</style>
		<script type="text/javascript">
			$(document).ready(function()
			{
				var leaderTable = $('#leaderList').dataTable({
					"aoColumns": [
			        	{ "bVisible": false },
			        	<security:authorize  access="hasRole('ROLE_LEADER')">
					  		{ "bSortable": false },
					  	</security:authorize>
					  	{ "iDataSort": 0 },
					  	null,
					  	null,
					  	{"sType": "date"},
					  	null
			        ],
			        "bFilter": false,
			        "bStateSave": true,
					"bPaginate": false,
					"bLengthChange": false,
					"bInfo": false,
					<security:authorize  access="hasRole('ROLE_LEADER')">
					  	"aaSortingFixed": [[ 3, "desc" ]],
					 </security:authorize>
					"sDom": '<"top">rt<"bottom"if<"clear">>'
				});
				
				$("#leaderunitfilter").each(function()
				{ 
					this.innerHTML = fnCreateSelect( leaderTable.fnGetColumnData(3) );
			        $('select', this).change( function () {
			            leaderTable.fnFilter( $(this).val(), 3 );
			        } );
			    } );
			    //TODO for now comment this, later remember preferences as a cookie
			     //$('select', "#leaderunitfilter").val("<security:authentication property="principal.unit.typeOfUnit.name" />").trigger('change');
				var scoutTable = $('#scoutList').dataTable({
			        "aoColumns": [
			        	{ "bVisible": false },
			        	<security:authorize  access="hasRole('ROLE_LEADER')">
					  		{ "bSortable": false },
					  	</security:authorize>
					  	{ "iDataSort": 0 },
					  	null,
					  	null,
					  	{"sType": "date"},
					  	null
			        ],
			        "bFilter": false,
			        "bStateSave": true,
			        "bPaginate": false,
					"bLengthChange": false,
					"bInfo": false,
					"sDom": '<"top">rt<"bottom"if<"clear">>',
					<security:authorize  access="hasRole('ROLE_LEADER')">
					  	"aaSortingFixed": [[ 3, "desc" ]]
					</security:authorize>
					
				});
				$("#unitfilter").each(function()
				{ 
					this.innerHTML = fnCreateSelect( scoutTable.fnGetColumnData(3) );
			        $('select', this).change( function () {
			            scoutTable.fnFilter( $(this).val(), 3 );
			        } );
			    } );
			    $('#sendInvitation').click(function(){
			    	if($('input[name="selections"]:checked').length == 0)
			    	{
			    		alert("First select youth or adults, then invite!");
			    		return false;
			    	}
			    	$('#selection').submit();
			    });
			    //TODO for now comment this, later remember preferences as a cookie
			    //$('select', "#unitfilter").val("<security:authentication property="principal.unit.typeOfUnit.name" />").trigger('change'); 
			        
			});
		</script>
	</head>
	
	<body>
	<div class="table">	
		<div class="row">
		<form:form modelAttribute="selection" action="invite.html" method="post"> 
			<div id='page1' class='cell'>
		 		<security:authorize  access="hasRole('ROLE_LEADER')">
			 		<div class='row'>
							<div class='cell'>
								<input value="Add Scout" type="button" class="button" onclick="location.href='addscout.html'"/>
							</div>
					</div>
				</security:authorize>
				<div class='row'>
					<table id="scoutList" cellspacing="0" class="dataTable" summary="Scouts">
					<caption>Youth<!-- &nbsp;<trax:selectages/> --> </caption>
						<thead>
							<tr>
								<th>fullName</th>
								<security:authorize  access="hasRole('ROLE_LEADER')">
									<th></th>
								</security:authorize>
								<th>Name</th><th>Unit Type</th><th>Phone</th><th>Last Login</th><th>Position</th>
							</tr>
						</thead>
						<tbody class="highlightable">
						<c:forEach var="scout" items="${scouts}">
							<tr>
							<td>${scout.fullName}</td>
							<security:authorize  access="hasRole('ROLE_LEADER')">
								<td>
									<form:checkbox path="selections" id="${scout.id}" value="${scout.id}" />
									<!-- eventually add a image for the checkbox<label for="${scout.id}">&nbsp;</label> -->
								</td>
							</security:authorize>
							<td>
								<security:authorize  access="hasRole('ROLE_LEADER')">
									<a href='userUpdate.html?userId=${scout.id}' title='${scout.fullName}'>
								</security:authorize>
									${scout.fullName}
								<security:authorize  access="hasRole('ROLE_LEADER')">
									</a>
								</security:authorize>
								<security:authorize  access="hasRole('ROLE_LEADER')">
									<c:if test="${scout.accountEnabled==false}">
							    		<img src='images/yellowpaw.png' alt="this account has not been activated." title="this account has not been activated.">
							    	</c:if>
								</security:authorize>
						    </td>			
						    <td>${scout.unit.typeOfUnit.name}</td>
						    <td>${scout.phone}
							    <c:if test="${not empty scout.cellPhone}"> 
							    	<br>Cell: ${scout.cellPhone}
							    </c:if>
							    <c:if test="${not empty scout.workPhone}"> 
							    	<br>Work: ${scout.workPhone}
							    </c:if>
						    </td>
						    <td>
						    	<c:if test="${not empty scout.lastLoginDate}">
									<fmt:formatDate pattern="MM/dd/yyyy" var="dateValue" value="${scout.lastLoginDate}" />
									${dateValue}
								</c:if>
							</td>
						    <c:if test="${scout.position!=null}"> <!-- only scouts and leaders have a position -->
						    	<td>${scout.position.positionName}</td>
						    </c:if>
						    <c:if test="${scout.cubPosition!=null}"> <!-- only scouts and leaders have a position -->
						    	<td>${scout.cubPosition.positionName}</td>
						    </c:if>
						    <c:if test="${scout.position==null && scout.cubPosition==null}"> <!-- only scouts and leaders have a position -->
						    	<td></td>
						    </c:if>
						  </tr>
						</c:forEach>
						</tbody>
						<tfoot>
							<tr>
								<th></th>
								<th>Filter:</th>
								<th></th>
								<th id='unitfilter' title="Filter Unit type by selecting one of these options"></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>
	 		</div>	
	 		<div id='page2' class='cell'>
				<security:authorize  access="hasRole('ROLE_LEADER')">
				<div class='row'>
					<div class='cell'>
						<input value="Add Leader" type="button" class="button" onclick="location.href='addleader.html'"/>
					</div>
				</div>
				</security:authorize>
				<div class='row'>
		 			<table id="leaderList" cellspacing="0" class="dataTable" summary="Leaders">
					<caption>Adults</caption>
						<thead>
							<tr>
							<th>fullName</th>
							<security:authorize  access="hasRole('ROLE_LEADER')">
								<th></th>
							</security:authorize>
								<th>Name</th>
								<th>Unit Type</th>
								<th>Phone</th>
								<th>Last Login</th>
								<th>Position</th>
							</tr>
						</thead>
						<tbody class="highlightable">
						<c:forEach var="leader" items="${leaders}">
							<tr>
							<td>${leader.fullName}</td>
							<security:authorize  access="hasRole('ROLE_LEADER')">
								<td><form:checkbox path="selections" id="${leader.id}" value="${leader.id}" /></td>
							</security:authorize>
							<td>
							<security:authorize  access="hasRole('ROLE_LEADER')">
								<a href='userUpdate.html?userId=${leader.id}' title='Update record'>
							</security:authorize>
								${leader.fullName}
							<security:authorize  access="hasRole('ROLE_LEADER')">
								</a>
							</security:authorize>
							<security:authorize  access="hasRole('ROLE_LEADER')">
									<c:if test="${leader.accountEnabled==false}">
							    		<img src='images/yellowpaw.png' alt="this account has not been activated." title="this account has not been activated.">
							    	</c:if>
							</security:authorize>
							
						    </td>					
						    <td>${leader.unit.typeOfUnit.name}</td>
						    <td>${leader.phone} ${leader.workPhone} ${leader.cellPhone}</td>
						    <td>
						    	<c:if test="${not empty leader.lastLoginDate}">
									<fmt:formatDate pattern="MM/dd/yyyy" var="dateValue" value="${leader.lastLoginDate}" />
									${dateValue}
								</c:if>
								
							</td>
						    <c:if test="${leader.position!=null}"> <!-- only scouts and leaders have a position -->
						    	<td>${leader.position.positionName}</td>
						    </c:if>
						    <c:if test="${leader.cubPosition!=null}"> <!-- only scouts and leaders have a position -->
						    	<td>${leader.cubPosition.positionName}</td>
						    </c:if>
						    <c:if test="${leader.position==null && leader.cubPosition==null}"> <!-- only scouts and leaders have a position -->
						    	<td></td>
						    </c:if>
						    </tr>
						</c:forEach>
						</tbody>
						<tfoot>
							<tr>
								<th></th>
								<th>Filter:</th>
								<th></th>
								<th id='leaderunitfilter' title="Filter Unit type by selecting one of these options"></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
					<security:authorize  access="hasRole('ROLE_LEADER')">
						<div class='row'>
							<input id="sendInvitation" type="button" value="Send Invitation" class="button" title="Click this to invite selected scouts and leaders to join ScoutTrax.org"/>
						</div>
			 			
					</security:authorize>
				</div>
			</div>
		</form:form>
			
		</div>
		<security:authorize access="hasRole('ROLE_LEADER')">
			<div id='upload' class='row'>
			<!-- <div id='inviteCubs' class='row'>
			 			<p>Cub scouts can use ScoutTrax too. Click here to invite them to join. 
			 			These boys will not be viewable by you until they turn 11 years old. The cub leaders will not be able to see the older scouts.</p>
			  -->
				<form:form modelAttribute="uploadItem" method="post" action="upload.html" enctype="multipart/form-data">
	                		<form:label for="fileData" path="fileData">Select Unit Advancement PDF file</form:label>
	                		<form:input path="fileData" type="file"/>
	                		<input class="button" type="submit" value="Import"/>
		     	</form:form>
  				<security:authorize access='!principal.unit.isCub'>
					<input id='inviteCubs' title="Cub scouts can use ScoutTrax too. Click the 'Invite Pack' button to invite them to join. These boys will not be viewable by you until they turn 11 years old." value="Invite Pack" type="button" class="button" onclick="location.href='cubInvite.html?isCub=true'"/>
				</security:authorize>
				<security:authorize access='principal.unit.isCub'>
					<input id='inviteCubs' title="Troop, Teams, Crews and Ships can use ScoutTrax too. Click the 'Invite Troop' button to invite them to join. These boys will not be viewable by you. Only by their new leader." value="Invite Troop" type="button" class="button" onclick="location.href='cubInvite.html?isCub=false'"/>
				</security:authorize>
			</div>
		</security:authorize>
	</div>
	</body>
</html>