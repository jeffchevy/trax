<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<h1>Email Verified</h1>
<p>Thank you for confirming your email address.<br />
</p>
<form:form method="post" action="saveuserCredentials.html" commandName="userCredentialsForm">
	<form:hidden path="userId" />
	<table>
		<tr>
			<td colspan="3" >
				<h1>Create your own username and password then you will be able to login</h1>
			</td>
		</tr>		
		<tr>
			<td><form:label path="username">Username</form:label></td>
			<td><form:input path="username" /></td>
			<td><form:errors cssClass="errors" path="username" /></td> 
			<td rowspan=3>
			<p style="color:red">For your security please follow these Password recommedations</p>
			   <ol> 
				   <li>At least 8 characters in length</li>
				   <li>Include at least 1 numeric character (0-9)</li>
				   <li>Contain both upper and lower case characters</li>
				   <li>Should not be the same as your username</li>
			   </ol>
			</td>
		</tr>
		<tr>
			<td><form:label path="password">Password</form:label></td>
			<td><form:password path="password" /></td>
			<td><form:errors cssClass="errors" path="password" /></td> 
		</tr>
		<tr>
			<td><form:label path="confirmPassword">Confirm Password</form:label></td>
			<td><form:password path="confirmPassword" /></td>
			<td><form:errors cssClass="errors" path="confirmPassword" /></td> 
		</tr>
		<tr>
			<td colspan="2" class='buttoncell' >
				<input class="button" type="submit" value="Submit"/>
			</td>
		</tr>
	</table>
</form:form>