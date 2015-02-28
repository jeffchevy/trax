<!doctype html>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
	<head>
	<title>ScoutTrax.org Free online scout advancement tracking.</title>
	<meta http-equiv="ScoutTrax.org - Free online scout advancement tracking." content="text/html; charset=UTF-8" />
	<meta name="google-site-verification" content="r8_DZ-vawNf3PjfAGDvN0YERMTlf-nnpI5TB5dvLDFQ" />
	<meta name="keywords" content="Free, online, tracking, software, Boy scout, Boy Scout Software, Cub Scout Software, Pack Software, Den Software, 
	scout, cub scout, Scouting Software, Scout Software, Varsity, Venture, Venturing, Troop, Patrol, Team, Crew, merit badge, Scout Advancement Tracking, Cub Scout Tracking,
	Boy Scout Tracking, Scout Tracking, Cub Scout Advancement Tracking, Boy Scout Advancement Tracking, Venture Scout Advancement Tracking, Varsity Scout Advancement Tracking,
	requirements, camping, leadership, scout master, scout leader, scout book, rank, Eagle, Eagle Scout, user friendly, easy, 
	simple, parents, Awards, camping, service, Merit Badge, email, den, safe, secure, advancement, BSA, LDS, Duty to God, email, report "/>
	<meta name="description" content="ScoutTrax.org is a FREE online scout management software for your Den, Pack, Troop, Team and Crew. This software is available for cub scout, boy scouts ages 7-21."/> 
	<script type="text/javascript"js/jquery.js type="text/javascript"></script>
	<script type="text/javascript">
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-37757159-1']);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	
	</script>
	
		<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">	
		<style type="text/css">
			#successMessage {
			color: green;
		}
		</style>
		<script type="text/javascript">
		$(document).ready(function()
		{
			if($('.errors').text().length != 0)
			{
				// reformat the text by stripping out html elements.
				alert($('.errors').text().replace('\n', '').replace('<br>','\n').replace('.','.\n'));
				//set focus to the first forms first visible input
				$('input[type!=hidden]:first', 'form:first').focus();
			}
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
		    });
			    
			if($('#successMessage').text().length != 0)
			{
				//$('#successMessage').text().replace('\n', '').replace('<br>','\n').replace('.','.\n');
				//alert($('#successMessage').text().replace('\n', '').replace('<br>','\n').replace('.','.\n'));
				
			    $( "#successMessage" ).dialog("open");
			}
			*/
			
		
			if(navigator.appName == 'Microsoft Internet Explorer') {//check to see if browser supports CSS3
				alert('This browser is not supported by ScoutTrax.\n  You may continue to explore using this browser, but many features will not work or display properly. \nWe recommend that you download Firefox, Chrome or Safari for viewing ScoutTrax.org.');
			}
			else
			if($.support.opacity === false) {//check to see if browser supports CSS3
				alert('This browser is not supported by ScoutTrax.\n  You may continue to explore using this browser, but many features will not work or display properly. \nWe recommend that you download Firefox, Chrome or Safari for viewing ScoutTrax.org.');
			}
		});
		</script>
	</head>
	<body>
	<c:if test="${not empty errorMessage}">
		<span class='errors'>${errorMessage}</span>
	</c:if>
	<c:if test="${not empty successMessage}">
		<span id='successMessage'>${successMessage}</span>
	</c:if>	
	<tiles:insertAttribute name="body" />
	</body>
</html>