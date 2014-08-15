<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<h1>Forgot your password?</h1>
<p>To reset your password, type the full email address you used to create your ScoutTrax account.<br />
</p>
<form:form method="post" action="resetpassword.html" commandName="emailAddressForm">
	<table>
		<tr>
			<td><form:label path="email">Email Address</form:label></td>
			<td><form:input path="email" /></td>
			<td><form:errors cssClass="errors" path="email" /></td> 
		</tr>
		<tr>
			<td colspan="2" class='buttoncell' >
				<input class="button" type="submit" value="Submit"/>
			</td>
		</tr>
	</table>
</form:form>