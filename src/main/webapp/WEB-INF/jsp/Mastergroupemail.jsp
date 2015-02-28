<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
	<script src="js/dataTableColumnFilter.js" type="text/javascript"></script>
	<title>Users</title>
		<style type="text/css">
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
		</style>
		<script type="text/javascript">
			$(document).ready(function()
			{
			    $('#leaders').click(function(event){
		 			if($('#elevenYearOlds').attr('checked'))
		 			{
		 				$('.ELEVENYEAROLDS', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#troop').attr('checked'))
		 			{
		 				$('.TROOP', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#team').attr('checked'))
		 			{
		 				$('.TEAM', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#crew').attr('checked'))
		 			{
		 				$('.CREW', '#leaderList').find('input').attr("checked", this.checked);
		 			}
				});
				$('#youth').click(function(event){
		 			if($('#elevenYearOlds').attr('checked'))
		 			{
		 				$('.ELEVENYEAROLDS', '#scoutList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#troop').attr('checked'))
		 			{
		 				$('.TROOP', '#scoutList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#team').attr('checked'))
		 			{
		 				$('.TEAM', '#scoutList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#crew').attr('checked'))
		 			{
		 				$('.CREW', '#scoutList').find('input').attr("checked", this.checked);
		 			}
				});
				$('#elevenYearOlds').click(function(event){
		 			event.stopPropagation();
		 			//get all cells with value ELEVENYEAROLDS
		 			if($('#leaders').attr('checked'))
		 			{
		 				$('.ELEVENYEAROLDS', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#youth').attr('checked'))
		 			{
		 				$('.ELEVENYEAROLDS', '#scoutList').find('input').attr("checked", this.checked);
		 			}
				});
				$('#troop').click(function(event){
		 			event.stopPropagation();
		 			if($('#leaders').attr('checked'))
		 			{
		 				$('.TROOP', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#youth').attr('checked'))
		 			{
		 				$('.TROOP', '#scoutList').find('input').attr("checked", this.checked);
		 			}
				});
				$('#team').click(function(event){
		 			event.stopPropagation();
		 			if($('#leaders').attr('checked'))
		 			{
		 				$('.TEAM', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#youth').attr('checked'))
		 			{
		 				$('.TEAM', '#scoutList').find('input').attr("checked", this.checked);
		 			}
				});
				$('#crew').click(function(event){
		 			event.stopPropagation();
		 			//get all cells with value ELEVENYEAROLDS
		 			if($('#leaders').attr('checked'))
		 			{
		 				$('.CREW', '#leaderList').find('input').attr("checked", this.checked);
		 			}
		 			if($('#youth').attr('checked'))
		 			{
		 				$('.CREW', '#scoutList').find('input').attr("checked", this.checked);
		 			}
				});
			});
			    
		</script>		
	</head>
	
	<body>
	<div class="table">					
		<div class="row">
			<form:form modelAttribute="selection" action="sendMastergroupemail.html" commandName="emailGroupForm" method="post"> 
				<div id='page1' class='cell'>
				<table id="mailGroup" cellspacing="0" class="dataTable darkbackground" summary="Mail group">
						<caption></caption>
						<thead>
							<tr>
								<th colspan=2 style="text-align:center">MAIL MASTER LIST</th>
							</tr>
						</thead>
						<tbody class="highlightable">
							<tr>
								<td><input id="elevenYearOlds" class="unitTypes" type="checkbox" ><label for="elevenYearOlds">11 Year old</label></td>
								<td><input id="leaders" class="people" type="checkbox" checked="checked"><label for="Leaders">Leaders</label></td>
							</tr>
							<tr>
								<td><input id="troop" class="unitTypes" type="checkbox" ><label for="troop">Troop</label></td>
								<td><input id="youth" class="people" type="checkbox" checked="checked"><label for="youth">Youth</label></td>
							</tr>
							<tr>
								<td><input id="team" class="unitTypes" type="checkbox" ><label for="team">Team</label></td>
								<td></td>
							</tr>
							<tr>
								<td><input id="crew" class="unitTypes" type="checkbox" ><label for="crew">Crew</label></td>
								<td></td>
							</tr>
						</tbody>
						<tfoot>
						</tfoot>
					</table>
					<table class="darkbackground">
						<tr><th>Write your Message</th></tr>
						<tr>
							<td><form:label path="subject">Subject*</form:label>
							<form:input path="subject"/></td>
							<td><form:errors cssClass="errors" path="subject" /></td> 
						</tr>
						<tr>
							<td><form:textarea path="message" rows="8" cols="60"/></td>
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
		 		<div id='page2' class='cell'>
					
				</div>
			</form:form>
		</div>
	</div>
	</body>
</html>