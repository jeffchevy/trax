<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div  id="centerdialog">
	<h1>Forgot your login or password?</h1>
	<p>To reset your password, type the email address you used to create your ScoutTrax account.<br />
		An email will be sent with your username and a link to create a new password.
	</p>
	<form:form method="post" action="forgotResponse.html" commandName="user">
		<table>
			<tr>
				<td><form:label path="email">Email</form:label></td>
				<td><form:input size="60" path="email" /></td>
				<td><form:errors cssClass="errors" path="email" /></td> 
			</tr>
			<tr>
				<td>or<br>
				 <form:label path="username">Username</form:label></td>
				<td><form:input size="30" path="username" /></td>
				<td><form:errors cssClass="errors" path="username" /></td> 
			</tr>
			<tr>
				<td colspan="2" class='buttoncell' >
					<input class="button" type="submit" value="Submit"/>
				</td>
			</tr>
		</table>
	</form:form>
</div>
