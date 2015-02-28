<!doctype html>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<html>
	<head>
	<link rel="stylesheet" type="text/css" href="css/skin.css" />
	<link rel="stylesheet" type="text/css" href="css/forms.css" />
	<link type="text/css" href="css/jquery-ui.all.css" rel="Stylesheet" />
	<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">	

	<script src="js/jquery.js"></script>
	
	<script type="text/javascript" src="js/jquery.jcarousel.js"></script>
	<script src="js/jquery-ui-1.11.2.custom.min.js" type="text/javascript"></script>
	
	<script type="text/javascript" src="js/jquery.dataTables.js"></script>

	
	<script src="js/SearchHighlight.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/jquery.idletimer.js" type="text/javascript"></script>   
	<script src="js/jquery.idletimeout.js" type="text/javascript"></script> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><tiles:insertAttribute name="title" ignore="true" />
	   ScoutTrax.org Free online scout tracking.</title>
	<meta name="keywords" content="Free, online, tracking, software, Boy scout, scout, cub scout, Varsity, Venture, Venturing, Troop, Patrol, Team, Crew, merit badge, 
	requirements, camping, leadership, scout master, scout leader, scout book, rank, Eagle, Eagle Scout, user friendly, easy, 
	simple, parents, awards, tool, email, den, safe, secure, advancement, BSA, "/>
	<link rel="stylesheet" type="text/css" href="css/tileslayout.css" />
	<style type="text/css">
	<security:authorize access='!principal.unit.isCub'>
		#page1, #page2 {
			width: 50%;
			color: white;
			min-height: 100px;
			padding: 10px 35px 10px 20px;
			z-index: 1;
		}
		
		#page1 {
		  /* background: -moz-linear-gradient(right, transparent, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.1) 25%),url("images/scoutbackground.jpg") repeat !important;
		  /*  background: -webkit-linear-gradient(right, color-stop(0%, rgba(0, 0, 0, 0.7)), color-stop(25%, rgba(0, 0, 0, 0.1)), url("images/cub/background.jpg") repeat !important; /* Chrome10+,Safari5.1+ */
		  /*  background: url("images/scoutbackground.jpg") repeat !important; /* Chrome10+,Safari5.1+ */
		  /*  background:     -ms-linear-gradient(right, transparent, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.7) 25%), url("images/scoutbackground.jpg") repeat !important; /* IE10+ */
		  /*  background:      -o-linear-gradient(right, transparent, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.7) 25%), url("images/scoutbackground.jpg") repeat !important; /* Opera 11.10+ */
		    background: linear-gradient(to right, transparent, transparent, transparent, rgba(0, 0, 0, 0.9) 100%), url("images/scoutbackground.jpg") repeat !important; /* W3C */
		}
		#page2 {
			/*background: -moz-linear-gradient(left, transparent, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.1) 25%),url("images/scoutbackground.jpg") repeat !important;*/
		    background: linear-gradient(to left, transparent, transparent, transparent, rgba(0, 0, 0, 0.9) 100%), url("images/scoutbackground.jpg") repeat !important; /* W3C */
		}
	</security:authorize>
	<security:authorize access='principal.unit.isCub'>
		#page1, #page2 {
			width: 50%;
			color: white;
			min-height: 100px;
			padding: 10px 35px 10px 20px;
			z-index: 1;
		}
		
		#page1 {
		    background: linear-gradient(to right, transparent, transparent, transparent, rgba(0, 0, 0, 0.9) 100%), url("images/cub/background.jpg") repeat !important; /* W3C */
		}
		#page2 {
		    background: linear-gradient(to left, transparent, transparent, transparent, rgba(0, 0, 0, 0.9) 100%), url("images/cub/background.jpg") repeat !important; /* W3C */
		}
		th {
			color: white !important;
		}
		caption {
			background: #042856 !important;
			background: #E0DE7D !important;
			color: black !important;
		}
		.datebox, #awarddatebox {
		    background: -moz-linear-gradient(right center , #111133, #9999B2) repeat scroll 0 0 transparent;
		    background: -moz-linear-gradient(right center , #AEB239, #FFFFAA) repeat scroll 0 0 transparent !important;
		}
	</security:authorize>
	#noScoutSelectedDialog { display: none; }
	</style>
	
	<script type="text/javascript">
	function earned_initCallback(carousel)
	{
		// Disable autoscrolling if the user clicks the prev or next button.
		carousel.buttonNext.bind('click', function() {
			carousel.startAuto(0);
		});
		carousel.buttonPrev.bind('click', function() {
			carousel.startAuto(0);
		});
		
		 // Pause autoscrolling if the user moves with the cursor over the clip.
		carousel.clip.hover(function() {
			carousel.stopAuto();}, 
			function() {	carousel.startAuto(); });
	};
	var form_submitted = false;
	var id=0;
	function submitScoutNameForm()
	{
		/*console.log("submitting form - size("+$('.scoutName:checked').length+")");*/
		form_submitted = true;
		$('#scoutsform').submit();
		$( "#updatingRecordDialog" ).dialog("open");
		return true;
	}
	$(document).ready(function()
	{
		if($('#ispack').length > 0)
		{
			
		}
		
		if($('.errors').text().length != 0)
		{
			var errorText = $('.errors').map(function() {
			    return "\n "+$(this).text();
			}).get();
		
			// reformat the text by stripping out html elements.
			//alert($('.errors').text().replace('\n', '').replace('<br>','\n').replace('.','.\n'));
			alert(errorText);
			//set focus to the first forms first visible input
			$('input[type!=hidden]:first', 'form:first').focus();
		}
	
	    // for a single scout
		$('.scoutName').click(function(event){
 			event.stopPropagation();
 			if(id!==0)
 			{
 				//prevent rapid submissions from clobbering each other. 
 				// wait 2 seconds if nothing happens submit, otherwise restart the timeout
 				clearTimeout(id); 
 			}
 			id = setTimeout('submitScoutNameForm()', 2000)
		});

		$('#scoutimage').click(function(){
                   window.open('scoutreport.html', 'SCOUTREPORT', 'WIDTH=700,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=no,toolbar=no,location=no'); w.focus(); return false;
           });
	
		$('#nav-scoutreport').click(function(){
                   window.open('scoutreport.html', 'SCOUTREPORT', 'WIDTH=700,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=n,toolbar=no,location=no'); w.focus(); return false;
           });
		//add the active class when a tab is active
		if($('#Scout').length != 0) {$('a', '#nav-scoutrank').addClass('active');}
		else if($('#Tenderfoot').length != 0){$('a', '#nav-tenderfootrank').addClass('active');}
		else if($('#Second_Class').length != 0){$('a', '#nav-2ndclassrank').addClass('active');}
		else if($('#First_Class').length != 0){$('a', '#nav-1stclassrank').addClass('active');}
		else if($('#Star').length != 0){
			$('a', '#nav-starrank').addClass('active');
			//go get all the required meritbadges for this scout, and fill in the blanks for this requirement
			$.get('getRankMeritBadges.html', function(data) {
				if(data.length>0)
				{
					$(".requirementtext:contains(3. Earn 6 merit badges)").html(data);
				}
			});
		}
		else if($('#Life').length != 0){
			$('a', '#nav-liferank').addClass('active');
			//go get all the required meritbadges for this scout, and fill in the blanks for this requirement
			$.get('getRankMeritBadges.html', function(data) {
				if(data.length>0)
				{
					$(".requirementtext:contains(3. Earn five more merit badges)").html(data);
				}
			});
		}
		else if($('#Eagle').length != 0){
			$('a', '#nav-eaglerank').addClass('active');
			//go get all the required meritbadges for this scout, and fill in the blanks for this requirement
			$.get('getRankMeritBadges.html', function(data) {
				if(data.length>0)
				{
					$(".requirementtext:contains(3. Earn a total of 21 )").html(data);
				}
			});
		}
		else if($('#Palms').length != 0){$('a', '#nav-palms').addClass('active');}
		else if($('#LeadershipLog').length != 0){$('a', '#nav-leadership').addClass('active');}
		else if($('#ServiceLog').length != 0){$('a', '#nav-service').addClass('active');}
		else if($('#CampLog').length != 0){$('a', '#nav-camping').addClass('active');}
		else if($('#Badges').length != 0){$('a', '#nav-badges').addClass('active');}
		else if($('#DTG').length != 0){$('a', '#nav-dtg').addClass('active');}
		else if($('#Faith').length != 0){$('a', '#nav-faith').addClass('active');}
		else if($('#Awards').length != 0){$('a', '#nav-awards').addClass('active');}
		else if($('#bronze').length != 0){$('a', '#nav-bronze').addClass('active');}
		else if($('#gold').length != 0){$('a', '#nav-gold').addClass('active');}
		else if($('#silver').length != 0){$('a', '#nav-silver').addClass('active');}
		else if($('#ranger').length != 0){$('a', '#nav-ranger').addClass('active');}
		else if($('#Bobcat').length != 0){$('a', '#nav-bobcatrank').addClass('active');}
		else if($('#Tiger_Cub').length != 0){$('a', '#nav-tigerrank').addClass('active');}
		else if($('#Bear').length != 0){$('a', '#nav-bearrank').addClass('active');}
		else if($('#Wolf').length != 0){$('a', '#nav-wolfrank').addClass('active');}
		else if($('#Webelos_Award').length != 0){$('a', '#nav-webelosrank').addClass('active');}
		else if($('#Arrow_Of_Light').length != 0){$('a', '#nav-arrowoflightrank').addClass('active');}
		else if($('#Belt_Loops').length != 0){$('a', '#nav-beltloops').addClass('active');}
		else if($('#Activity_Badges').length != 0){$('a', '#nav-activitybadges').addClass('active');}
		else if($('#Pins').length != 0){$('a', '#nav-pins').addClass('active');}
		else if($('#CubAwards').length != 0){$('a', '#nav-award').addClass('active');}
		
		$('#showscoutsbyunit').change(function() {
		    $(this).closest("form").submit();
		   // "showscoutsby.html?showscoutsby="+this.val();
		});
			    	
		$('#allscouts').click(function() {
			var ischecked = this.checked;
			var $scouts = $('input.scoutName');
			$scouts.attr('checked', ischecked);
			
			$('#scoutsform').submit();
		});
		/*$('#successMessage').dialog({
	        autoOpen: false,
	        modal: true,
	        closeOnEscape: false,
	        draggable: false,
	        resizable: false,
	        buttons: {
	            'Ok': function(){
	                $(this).dialog('close');
	                //set focus to the first forms first visible input
					$('input[type!=hidden]:first', 'form:first').focus();
	            }
	        }
	    });*/
	    
		if($('#successMessage').text().length != 0)
		{
			// reformat the text by stripping out html elements.
			$('#successMessage').text().replace('\n', '').replace('<br>','\n').replace('.','.\n');
			alert($('#successMessage').text());
		    /*todo this does not seem to work with jquery1.4.4 ???????????$( "#successMessage" ).dialog("open");*/
		}
		
	    // session timeout - setup the dialog
	    $("#sessionTimeoutDialog").dialog({
	        autoOpen: false,
	        modal: true,
	        width: 300,
	        height: 180,
	        closeOnEscape: false,
	        draggable: false,
	        resizable: false,
	        buttons: {
	            'Yes, Keep Working': function(){
	                $(this).dialog('close');
	            },
	            'No, Logoff': function(){
	                // fire whatever the configured onTimeout callback is.
	                // using .call(this) keeps the default behavior of "this" being the warning
	                // element (the dialog in this case) inside the callback.
	                $.idleTimeout.options.onTimeout.call(this);
	            }
	        }
	    });
		$( "#updatingRecordDialog" ).dialog({
			resizable: false,
			autoOpen: false,
			width: 340,
			height:140,
			modal: true
		});
	    // cache a reference to the countdown element so we don't have to query the DOM for it on each ping.
	    var $countdown = $("#dialog-countdown");
	   
	    // start the idle timer plugin
	    $.idleTimeout('#sessionTimeoutDialog', 'div.ui-dialog-buttonpane button:first', {
	        idleAfter: 1500, // user is considered idle after 25 minutes
	        warningLength: 90, // show the count down for 90 seconds, give them plenty of time
	        pollingInterval: 60, // a request to keepalive (below) will be sent to the server every 1 minutes
	        keepAliveURL: "$.get('keepAlive.html')",
	        serverResponseEquals: 'OK',
	        onTimeout: function(){
	            window.location = "logout.html";
	        },
	        onIdle: function(){
	            $(this).dialog("open");
	        },
	        onCountdown: function(counter){
	            $countdown.html(counter); // update the counter
	        }
	    });

	    $('#inprogressAwards').jcarousel({
        	visible: 7,
        	scroll: 1,
        	vertical: true
    	});

		$('#earnedmerritbadges').jcarousel({
	        visible: 13,
			auto: 10,
			scroll: 1,
			wrap: 'last',
			initCallback: earned_initCallback
		});
		$( "#noScoutSelectedDialog" ).dialog({
			resizable: false,
			autoOpen: false,
			width: 340,
			height:160,
			modal: true,
			buttons: {
				"OK": function() {
					$( this ).dialog( "close" );
					return false;
				}
			}
		});
	    
	    /*$('.requirementscheckbox').each(function(){
			$(this).prop("indeterminate", true);
		});*/
	});
	</script>
	</head>
	<body id="body">
	<div id="fb-root"></div>
	<div class='table'>
			<div class='row'>
				<div class='table'>
					<div class='row'>
						<div id='mainmenu' class='cell'>
							<tiles:insertAttribute name="mainmenu" />
						</div>
						<div class='cell'>
							<div class='table'>
								<div id='topmiddle' class='row'>
									<div class='cell'>
										<div id="earned">
											<tiles:insertAttribute name="earned" />
										</div>
									</div>
								</div>
								<div class='row'>
									<div class='cell'>
										<c:if test="${not empty errorMessage}">
											<span class='errors'>${errorMessage}</span>
										</c:if>
										<c:if test="${not empty successMessage}">
											<div title="ScoutTrax says..." id='successMessage'><p>${successMessage}</p></div>
										</c:if>	
										<div class='table'>
											<div class='row'>
												<div class='cell'>
													<tiles:insertAttribute name="rankmenu" />
												</div>
											</div>
											<div class='row'>
												<div id='pages' class='cell'>
			 										<tiles:insertAttribute name="pages" /> 
												</div>
											</div>
											<div class='row'>
												<tiles:insertAttribute name="footer" /> 
											</div>
										</div>
									</div>
									<div id='scoutnamesColumn' class='cell'>
										<tiles:insertAttribute name="scoutnames" />
									</div>
								</div>
							</div>
						</div>
						<div id='rightcolumn' class='cell'>
							<div class="link">
								<div>Hello, <security:authentication property="principal.firstName" /></div>
								<input id="logout" value="Logout" type="button" class="button" onclick="location.href='logout.html'"/>
								<input value="Feedback" type="button" class="button" onclick="location.href='showfeedback.html'"/>
							</div>
							<!-- 
							<div id="fb-root"></div>
								<script>(function(d, s, id) {
								  var js, fjs = d.getElementsByTagName(s)[0];
								  if (d.getElementById(id)) return;
								  js = d.createElement(s); js.id = id;
								  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1";
								  fjs.parentNode.insertBefore(js, fjs);
								}(document, 'script', 'facebook-jssdk'));</script>
							<div class="fb-like-box" data-href="http://www.facebook.com/ScoutTrax" data-width="50" data-height="50" data-colorscheme="dark" data-show-faces="false" data-border-color="white" data-stream="false" data-header="false"></div>
		 -->
							<tiles:insertAttribute name="inprogress" />
						</div> <!-- end cell -->
					</div><!-- end row -->
				</div><!-- end table -->
			</div>
			<div class='row' id='rowspacer'></div>
			<div class='row' id='sponsorRow'>
				<div class='table'><!-- create another table so this will span all the whole width -->	
					<div class='row'>
						<tiles:insertAttribute name="sponsor" />
					</div>
				</div>
			</div> <!-- row -->
		</div><!-- table -->

		<!-- dialog window markup -->
		<div id="sessionTimeoutDialog" title="Your session is about to expire!" class="hidden">
		    <p>
		        <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
		        You will be logged off in <span id="dialog-countdown" style="font-weight:bold"></span> seconds.
		    </p>
		   
		    <p>Do you want to continue your session?</p>
		</div>
		<div id="updatingRecordDialog" title="Please Wait!" class="hidden">
			<p>
				<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
				Retrieving records, please wait...
			</p>
		</div>
		<div id="noScoutSelectedDialog" title="Warning!">
			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
			   You must first select at least one scout before performing this action.</p>
		</div>
	<style>
	#traxreporttable {
		background:white !important; 
		height:40px;
		color:red !important;
		column-span:all;
		-webkit-column-span:all;
		}
	</style>

	</body>
</html>