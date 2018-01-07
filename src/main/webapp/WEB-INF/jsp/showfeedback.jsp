<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
	<head>
		<title>Send Feedback</title>
	</head>
	<body>
	<div class="table">	
		<div class="row">
		 	<div id="page1" class="cell">
				<form:form id='emailform' action="sendfeedback.html" commandName="emailForm">
				<table id="pagequote">
					<tr><th colspan=2>Send feedback to ScoutTrax support</th></tr>
					<tr>
						<td><form:label path="message">Message</form:label></td>
						<td><form:textarea path="message" rows="10" cols="40"/></td>
						<td><form:errors cssClass="errors" path="message" /></td> 
					</tr>
					<tr>
						<td colspan="2" class='buttoncell' >
							<input class="button" type="submit" value="Send"/>
							<input class="button" type="button" name="Cancel" value="Cancel" onclick="history.back()" /> 
						</td>
					</tr>		
				</table>
			</form:form>
		</div> 
	 </div>	
	</div>
<div class="faq">
    <h1 style="text-align:center;">F.A.Q.</h1>
	<h4 style="text-align:left;"><b>
        Q: I need to contact a support representitive
    <br></b>
        A: You're in the right place! Type your question in above and we will email you as soon as possible. You can also contact us via our facebook page.
        <br><br><b>
        Q: I no longer use scouttrax and would like to delete my account.
        <br></b>
        A: We're sorry to here that you are no longer using scouttrax. To have your account removed click on your unit number to enter the troop management screen. From here click on your name and you will be able to modify your information, and if you wish, delete your account. We keep your data for thirty days to avoid data loss, but you will no longer recieve emails, and will not show up in your unit.
        <br><br><b>
        Q: How do you keep this website free?
        <br></b>
        A: This site is created entirley through volenteer work by those who love scoutting. Everything here is donated. If you would like to encourage our developers, or show apreciation, we encourage you to donate to our website.
        <br><br><b>
        Q: I am sad and don't have cookies.
        <br></b>
        A: Sounds like a service project waiting to happen! Talk to your local unit. Alternetivly go to your local grocery store.
        
         
    </h4>
	</div>
	</body>
</html>
