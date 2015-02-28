<script type="text/javascript">
	if ( $.browser 
			&& (($.browser.mozilla && $.browser.version <=13) 
					|| $.browser.msie )) 
	{
		//this is probably IE, IE shows up as Mozilla, but FF is at version 34, IE is at version 12
		alert("Warning!\r\nInternet Explorer may cause some problems with ScoutTrax.org.\r\n The most common is requirements not being saved.\n\r We recommend using Safari, Chrome or Firefox.")
	}


	$(document).ready(function()
	{ 
		$('#username').focus();	
		$('#about').click(function(){
                   window.open('scoutTraxUserGuide.html', 'About ScoutTrax', 'WIDTH=800,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=no,titlebar=no'); w.focus(); return false;
           });	
        $('#aboutCub').click(function(){
                   window.open('userGuideForCubs.html', 'About ScoutTrax', 'WIDTH=800,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=no,titlebar=no'); w.focus(); return false;
           });
	});
</script>
<style>
input.text {
	border: thin solid black;
}
.darkbackground {
    background-color: rgba(0, 0, 0, 0.0);
    border-radius: 15px 15px 15px 15px;
    color: #DDDDDD;
    font-size: 2em;
    margin: 1.5em;
    padding: 0.5em;
    text-align: center;
}
.warninput {
 font-size: 11px;
 color: #C00;
}
</style>	 
<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		<span style="color: red">${SPRING_SECURITY_LAST_EXCEPTION.message}</span>
		<!-- Login failed due to: this was always appearing so comment out until we find the problem--> 
</c:if>
<div class="table">
	<div id="centertop" class="row">
	<table>
		<tr>
		<td>
			<div class='action darkbackground'>
				<label class='biginput'><br><a href='organization.html' title='Create a new ScoutTrax account for your pack, troop, team, crew or ship'>Organization not registered?</a></label>
			</div>	
		</td>
		<td>
		<div class="cell">
			<form action="j_spring_security_check" method="post">
				<table class="">
					<tr>
						<td></td>
						<td></td>
						<td>
							<h1>Sign in with your ScoutTrax ID</h1>
						</td>
					</tr>		
					<tr>
						<td></td>
						<td><label class='biginput'>Username</label></td>
						<td><input id='username' type="text" name="j_username"></td>
					</tr>
					
					<tr>
						<td></td>
						<td><label class='biginput'>Password</label></td> 
						<td><input id='password' type="password" name="j_password"></td>
					</tr>	
					<tr>
						<td></td>
						<td></td>
						<td class='buttoncell' >
							<input class='button' type="submit" value="Sign In" />
							<a class="warninput" title="I forgot my username or password" href="forgot.html">Forgot login or password?</a></label>
						</td>
					</tr>
				</table>
			</form>
		</div>
		</td>
		<td>
			<div id="fb-root"></div>
				<script>(function(d, s, id) {
				  var js, fjs = d.getElementsByTagName(s)[0];
				  if (d.getElementById(id)) return;
				  js = d.createElement(s); js.id = id;
				  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1";
				  fjs.parentNode.insertBefore(js, fjs);
				}(document, 'script', 'facebook-jssdk'));</script>
				<div class="fb-like-box" data-href="http://www.facebook.com/ScoutTrax" 
					data-width="292" data-height="60" data-show-faces="false" 
					data-border-color="black" data-stream="false" data-header="false">
				</div>
		</td>
		</tr>
		</table>
	</div>
</div>





