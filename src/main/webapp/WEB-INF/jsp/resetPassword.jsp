<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script>
	$(document).ready(function()
	{ 
		$('#password').focus();		
	});
</script>	
<h1>Reset Password</h1>
<p>Please enter the new password you would like to use.</p>
<form:form method="post" action="resetPassword.html" commandName="resetPasswordForm">
<form:errors path="*" cssClass="errorblock" element="div" />
	<table>
		<form:hidden path="userId" />
		<form:hidden path="username" />
		<tr>
			<td><form:label cssClass='biginput' path="password">Password</form:label></td>
			<td><form:password path="password" /></td>
			<td><form:errors cssClass="errors" path="password" /></td> 
		</tr>
		<tr>
			<td><form:label cssClass='biginput' path="confirmPassword">Confirm Password</form:label></td>
			<td><form:password path="confirmPassword" /></td>
			<td><form:errors cssClass="errors" path="confirmPassword" /></td> 
			<td rowspan=3>
				<p style="color:red">For your security please follow these password recommendations</p>
			    <ol> 
				   <li>At least 8 characters in length</li>
				   <li>Include at least 1 numeric character (0-9)</li>
				   <li>Include both upper and lower case characters</li>
				   <li>Should not be the same as your username</li>
			    </ol>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Submit"/>
			</td>
		</tr>
	</table>
</form:form>