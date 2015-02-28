<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
	<script type="text/javascript" src="js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-migrate-1.2.1.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.11.2.custom.min.js"></script>
	
	<script type="text/javascript">
			$(document).ready(function()
			{
				$('#state').change(function()
				{
					$.get('loadstatecouncils.html', {state: $(this).val()}, function(councilNames)
					{
						$('#council').html(councilNames);
					})
				});
				$('#type').change(function()
				{
					$.get('loadUnitTypes.html', {type: $(this).val()}, function(unitTypes)
					{
						$('#types').html(unitTypes);
					});
					if($(this).val()==="Cub")
					{
						$('#hasTigersTr').show();
						//$('#types option:last').attr('selected', true);
					}
				});
				//on initial load, make sure this 
				$.get('loadstatecouncils.html', {state: $('#state').val()}, function(councilNames)
					{
						$('#council').html(councilNames);
					});
			});
		</script>
<form:form method="post" action="saveorganization.html" commandName="orgUnit">
	<table>
		<tr>
			<td colspan="2" >
				<h1>Enter information about your Charter Organization</h1>
			</td>
		</tr>		
		<tr>
			<td><form:label path="state">State*</form:label></td>
			<td><form:select id="state" path="state"><form:options items="${states}" /></form:select></td>
		</tr>		
		<tr>
			<td><form:label path="city">City*</form:label></td>
			<td><form:input path="city" /></td>
			<td><form:errors cssClass="errors" path="city" /></td> 
		</tr>
		<tr>
			<td><form:label path="council">Council*</form:label></td>
			<td><form:select path="council"><form:options items="${councils}" /></form:select></td>
		</tr>
		<tr>
			<td><form:label path="district">District</form:label></td>
			<td><form:input path="district" /></td>
			<td><form:errors cssClass="errors" path="district" /></td>
		</tr>
		<tr>
			<td><form:label path="name" title="Community or Church group">Charter Organization*</form:label></td>
			<td><form:input path="name" /></td>
			<td>(The name of your community or church group)<form:errors cssClass="errors" path="name" /></td>
		</tr>
		<tr>
			<td><form:label path="type">Group*</form:label></td>
			<td><form:select path="type">
				<option value="Scout">Scouts 11-21 year olds</option>
				<option value="Cub">Cubs 7-11 year olds</option>
			</form:select></td>
		</tr>
		<tr id='hasTigersTr' style='display:none;'> 
			<td><form:label path="hasTigers">Include Tiger Cubs?</form:label></td>
			<td><form:checkbox path="hasTigers"/></td>
		</tr>
		<tr>
			<td><form:label path="typeOfUnit">Unit type*</form:label></td>
			<td><form:select id='types' path="typeOfUnit">
				<form:options items="${unitTypes}" itemValue="id" itemLabel="name"/>
			</form:select></td>
		</tr>
		<tr>
			<td><form:label path="number">Unit Number*</form:label></td>
			<td><form:input path="number"/></td>
			<td><form:errors cssClass="errors" path="number" /></td>
		</tr>
		<tr>
			<td>
				<h5 style="color:gray;">* indicates a required field</h5>
			</td>
			<td class='buttoncell' >
				<input class='button' type="submit" value="Next"/>
			</td>
		</tr>
	</table>
</form:form>