<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<style>
.error {
	color: #ff0000;
}
 
.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
	
	<form:form method="post" action="saveuser.html" commandName="leader"><span class='errors'>${errorMessage}</span>
	<table>
		<tr>
			<td colspan="2" >
				<h1>Enter information about you</h1>
			</td>
		</tr>		
		<tr>
			<td><form:label path="firstName">First Name*</form:label></td>
			<td><form:input path="firstName" /></td>
			<td><form:errors cssClass="errors" path="firstName" /></td> 
		</tr>
		<tr>
			<td><form:label path="lastName">Last Name*</form:label></td>
			<td><form:input path="lastName" /></td>
			<td><form:errors cssClass="errors" path="lastName" /></td> 
		</tr>
		<tr>
			<td><form:label path="email">Email*</form:label></td>
			<td><form:input path="email" /></td>
			<td><form:errors cssClass="errors" path="email" /></td> 
		</tr>
		<tr>
			<td><form:label path="position">Position*</form:label></td>
			<td><form:select path="position">
				<form:options items="${positions}" itemValue="positionName"/>
			</form:select></td>
		</tr>
		<tr>
			<td><form:label path="phone">Telephone</form:label></td>
			<td><form:input path="phone" /></td>
			<td><form:errors cssClass="errors" path="phone" /></td> 
		</tr>
		<tr>
			<td><form:label path="cellPhone">Cell Phone</form:label></td>
			<td><form:input path="cellPhone"/></td>
			<td><form:errors cssClass="errors" path="cellPhone" /></td> 
		</tr>
		<tr>
			<td><form:label path="workPhone">Work Phone</form:label></td>
			<td><form:input path="workPhone"/></td>
			<td><form:errors cssClass="errors" path="workPhone" /></td> 
		</tr>
		<tr>
			<td><form:label path="address">Address</form:label></td>
			<td><form:input path="address" /></td>
			<td><form:errors cssClass="errors" path="address" /></td> 
		</tr>
		<tr>
			<td><form:label path="state">State*</form:label></td>
			<td><form:select path="state"><form:options items="${states}" /></form:select></td>
		</tr>		
		<tr>
			<td><form:label path="city">City*</form:label></td>
			<td><form:input path="city" /></td>
			<td><form:errors cssClass="errors" path="city" /></td> 
		</tr>
		<tr>
			<td><form:label path="zip">Zip*</form:label></td>
			<td><form:input path="zip" /></td>
			<td><form:errors cssClass="errors" path="zip" /></td>
		</tr>
	<!-- 	
		<tr>
			<td colspan="2">
				<script type="text/javascript"  src="http://api.recaptcha.net/challenge?k=6LdaP78SAAAAAJMvevZpMTYhnZXJE9bGdZUDoX86">
				</script>
				<noscript>
				    <iframe src="http://api.recaptcha.net/noscript?k=6LdaP78SAAAAAJMvevZpMTYhnZXJE9bGdZUDoX86"
				        height="300" width="500" frameborder="0"></iframe><br>
				    <textarea name="recaptcha_challenge_field" rows="3" cols="40"></textarea>
				    <input type="hidden" name="recaptcha_response_field" value="manual_challenge">
				</noscript>
			</td>
		</tr>
	 -->	
		<tr>
			<td>
				<h5 style="color:gray;">* indicates a required field</h5>
			</td>
			<td class='buttoncell' >
				<input class="button" type="submit" value="Save"/>
			</td>

		</tr>
	</table>
		
	</form:form>
	<script type="text/javascript" src="js/new/jquery.mask.js"></script>
	<!-- <script type="text/javascript" src="js/jquery.maskedinput-custom-1.3.js"></script> -->
	<script type="text/javascript">
			$(document).ready(function()
			{
				$("#phone").mask("(000) 000-0000", {placeholder: "(___) ___-____"});
				$("#workPhone").mask("(000) 000-0000", {placeholder: "(___) ___-____"});
				$("#cellPhone").mask("(000) 000-0000", {placeholder: "(___) ___-____"});
				if($('#position').find('option[value="Cubmaster"]').length > 0)
				{
					//change to record the cub position instead of the scout position
					$('#position').attr("name","cubPosition");
				}
				
			});
	</script>