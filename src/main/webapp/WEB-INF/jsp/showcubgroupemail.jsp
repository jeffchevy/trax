<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="js/dataTableColumnFilter.js" type="text/javascript"></script>
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
		#mailGroup {
		    text-align: left;
		    width: 100%;
		    margin: 0 0 20px;
   			padding: 0 0 20px;
   			background-color: rgba(0, 0, 0, 0.5);
		    border-radius: 15px 15px 15px 15px;
		    color: #DDDDDD;
		}
		.darkbackground {
			margin: 0 0 20px !important;
		}
		.darkbackground th {
			background: #343;
		}
		#mailMessage {
			width: 100%;
		}
		#mailMessage textarea{
			width: 100%;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function()
		{
			var leaderTable = $('#leaderList').dataTable({
				"aoColumns": [
				  	{ "iDataSort": 1 },
				  	null,
				  	null,
				  	null,
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

			var scoutTable = $('#scoutList').dataTable({
		        "aoColumns": [
		    	  	{ "iDataSort": 1 },
				  	null,
				  	null,
				  	null,
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
		    
		    $('#leaders').click(function(event){
	 			if($('#tiger').attr('checked'))
	 			{
	 				$('.tiger', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#wolf').attr('checked'))
	 			{
	 				$('.Wolf', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#bear').attr('checked'))
	 			{
	 				$('.Bear', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#webelos').attr('checked'))
	 			{
	 				$('.Webelos', '#leaderList').find('input').attr("checked", this.checked);
	 			}
			});
			$('#youth').click(function(event){
	 			if($('#tiger').attr('checked'))
	 			{
	 				$('.tiger', '#scoutList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#wolf').attr('checked'))
	 			{
	 				$('.Wolf', '#scoutList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#bear').attr('checked'))
	 			{
	 				$('.Bear', '#scoutList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#webelos').attr('checked'))
	 			{
	 				$('.Webelos', '#scoutList').find('input').attr("checked", this.checked);
	 			}
			});
			$('#tiger').click(function(event){
	 			event.stopPropagation();
	 			if($('#leaders').attr('checked'))
	 			{
	 				$('.tiger', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#youth').attr('checked'))
	 			{
	 				$('.tiger', '#scoutList').find('input').attr("checked", this.checked);
	 			}
			});
			$('#wolf').click(function(event){
	 			event.stopPropagation();
	 			if($('#leaders').attr('checked'))
	 			{
	 				$('.Wolf', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#youth').attr('checked'))
	 			{
	 				$('.Wolf', '#scoutList').find('input').attr("checked", this.checked);
	 			}
			});
			$('#bear').click(function(event){
	 			event.stopPropagation();
	 			if($('#leaders').attr('checked'))
	 			{
	 				$('.Bear', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#youth').attr('checked'))
	 			{
	 				$('.Bear', '#scoutList').find('input').attr("checked", this.checked);
	 			}
			});
			$('#webelos').click(function(event){
	 			event.stopPropagation();
	 			if($('#leaders').attr('checked'))
	 			{
	 				$('.Webelos', '#leaderList').find('input').attr("checked", this.checked);
	 			}
	 			if($('#youth').attr('checked'))
	 			{
	 				$('.Webelos', '#scoutList').find('input').attr("checked", this.checked);
	 			}
			});
		});
		    
	</script>		


	<div class="table">					
		<div class="row">
			<form:form modelAttribute="selection" action="sendgroupemail.html" commandName="emailGroupForm" method="post"> 
				<div id='page1' class='cell'>
				<div class="darkbackground">
					<table id="mailGroup" cellspacing="0" class="dataTable" summary="Mail group">
						<caption></caption>
						<thead>
							<tr>
								<th colspan=2 style="text-align:center">Choose recipients</th>
							</tr>
						</thead>
						<tbody class="highlightable">
							<tr>
								<c:if test="${leader.organization.hasTiger==true}">
									<td><input id="tiger" class="unitTypes" type="checkbox" ><label for="tiger">Tiger</label></td>
								</c:if>
							</tr>
							<tr>
								<td><input id="wolf" class="unitTypes" type="checkbox" ><label for="wolf">Wolf</label></td>
								<td><input id="leaders" class="people" type="checkbox" checked="checked"><label for="Leaders">Leaders</label></td>
							</tr>
							<tr>
								<td><input id="bear" class="unitTypes" type="checkbox" ><label for="bear">Bear</label></td>
								<td><input id="youth" class="people" type="checkbox" checked="checked"><label for="youth">Youth</label></td>
							</tr>
							<tr>
								<td><input id="webelos" class="unitTypes" type="checkbox" ><label for="webelos">Webelos</label></td>
								<td></td>
							</tr>
						</tbody>
						<tfoot>
						</tfoot>
					</table>
					<table id="mailMessage">
						<tr><th>Write your Message</th></tr>
						<tr>
							<td><form:label path="subject">Subject*</form:label>
							<form:input path="subject"/></td>
							<td><form:errors cssClass="errors" path="subject" /></td> 
						</tr>
						<tr>
							<td><form:textarea path="message" rows="8"/></td>
							<td><form:errors cssClass="errors" path="message" /></td> 
						</tr>
						<tr>
							<td class='buttoncell' >
								<input class="button" type="submit" value="Send"/>
								<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" /> 
							</td>
						</tr>		
					</table>
					</div>

				
					<table id="leaderList" cellspacing="0" class="dataTable" summary="Leaders">
						<caption>Adults</caption>
						<thead>
							<tr>
								<th></th>
								<th>Name</th>
								<th>Email</th>
								<th>Unit Type</th>
								<th>Position</th>
							</tr>
						</thead>
						<tbody class="highlightable">
						<c:forEach var="leader" items="${leaders}">
							<tr class="${leader.unit.typeOfUnit.name}">
								<td><form:checkbox path="selections" id="${leader.id}" value="${leader.id}" /><label for='${leader.id}'>&nbsp;</label></td>
								<td>
									${leader.fullName}
									<c:if test="${leader.accountEnabled==false}">
							    		<img src='images/yellowpaw.png' alt="this account has not been activated." title="this account has not been activated.">
							    	</c:if>
							    </td>					
							    <td>${leader.email}</td>
							    <td>${leader.unit.typeOfUnit.name}</td>
							    <c:if test="${leader.position!=null}"> <!-- only scouts and leaders have a position -->
							    	<td>${leader.position.positionName}</td>
							    </c:if>
							    <c:if test="${leader.position==null}"> <!-- only scouts and leaders have a position -->
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
							</tr>
						</tfoot>
					</table>					
		 		</div>	
		 		<div id='page2' class='cell'>
					<table id="scoutList" cellspacing="0" class="dataTable" summary="Scouts">
						<caption>Youth<!-- &nbsp;<trax:selectages/> --> </caption>
						<thead>
							<tr>
								<th></th><th>Name</th><th>Email</th><th>Unit Type</th><th>Position</th>
							</tr>
						</thead>
						<tbody class="highlightable">
						<c:forEach var="scout" items="${scouts}">
							<tr class="${scout.unit.typeOfUnit.name}">
							<td><form:checkbox  path="selections" id="${scout.id}" value="${scout.id}" /><label for='${scout.id}'>&nbsp;</label></td>
								<!-- cssClass="requirementscheckbox" -->
							<td>
								${scout.fullName}
								<c:if test="${scout.accountEnabled==false}">
						    		<img src='images/yellowpaw.png' alt="this account has not been activated." title="this account has not been activated.">
						    	</c:if>
						    </td>					
						    <td>${scout.email}</td>
						    <td>${scout.unit.typeOfUnit.name}</td>
						    <c:if test="${scout.position!=null}"> <!-- only scouts and leaders have a position -->
						    	<td>${scout.position.positionName}</td>
						    </c:if>
						    <c:if test="${scout.position==null}"> <!-- only scouts and leaders have a position -->
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
						</tr>
						</tfoot>
					</table>
				</div>
			</form:form>
		</div>
	</div>