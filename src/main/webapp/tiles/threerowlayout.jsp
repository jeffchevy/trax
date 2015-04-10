<!doctype html>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
	<meta name="google-site-verification" content="r8_DZ-vawNf3PjfAGDvN0YERMTlf-nnpI5TB5dvLDFQ" />
	<meta name="keywords" content="Free, online, tracking, software, Boy scout, scout, cub scout, Varsity, Venture, Venturing, Troop, Patrol, Team, Crew, merit badge, 
	requirements, camping, leadership, scout master, scout leader, scout book, rank, Eagle, Eagle Scout, user friendly, easy, 
	simple, parents, Awards, camping, service, Merit Badge, email, den, safe, secure, advancement, BSA, LDS, Duty to God, email, report "/>
	<meta name="viewport" content="width=device-width, initial-scale=1">
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
	<script src="js/jquery.js"></script>
	
	<script src="js/jquery-ui-1.11.2.custom.min.js" type="text/javascript"></script>
	<script src="bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">	

	<title><tiles:insertAttribute name="title" ignore="true" />www.ScoutTrax.org Free online scout advancement tracking.</title>
	<link rel="stylesheet" href="css/forms.css" type="text/css" media="screen, print">
	<style type="text/css">
		body {
			background: white;
			color: black;
			margin: 0;
		}
		h1 {
			color: black;
		}
		.table {width: 100%}
		#centerlogo {
			padding: 2% 25%;
			width: 50%;
			background: black;
		}
		#centertop, #centerbottom {
			padding: 2% 17% 0% 17%;
			width: 40%;
			white-space: nowrap;
			text-align: center;
			background:white;
		}
		
		#centerbottom {
			padding: 2% 30% 0% 30%;
		}
		.biginput {
			color: black;
		}
		#logo {
			position: fixed;
			left: 15%;
			top: 24%;
			z-index: -1;
			border: none;
		}
		img {border: none;}
		.errorblock {
			color: red;
			font-size: 2em;
		}
		#loginFormat {
			right: 30%;
			position: relative;
		}
		table {
			white-space: nowrap;
			text-align: left;
		}
		#successMessage {
			color: green;
		}
		.errors { color: red; font-size: 1.2em; white-space: nowrap; }
		.hidden, .errors, label.error {
			position: absolute;
			left: 0px;
			top: -500px;
			width: 1px;
			height: 1px;
			overflow: hidden;
		}
	</style>
	<link rel="stylesheet" href="bootstrap/css/bootstrap.css" type="text/css" media="screen, print">
	<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.css" type="text/css" media="screen, print">
	
	<script type="text/javascript">
		$(document).ready(function()
		{
			if($('.errors').text().length != 0)
			{
				var errorText = $('.errors').map(function() {
				    return "\n "+$(this).text();
				}).get();
				alert(errorText);
				// reformat the text by stripping out html elements.
				//alert($('.errors').text().replace('\n', '').replace('<br>','\n').replace('.','.\n'));
				//set focus to the first forms first visible input
				$('input[type!=hidden]:first', 'form:first').focus();
			}
			    
			if($('#successMessage').text().length != 0)
			{
				//alert($('#successMessage').text().replace('\n', '').replace('<br>','\n').replace('.','.\n'));
				
				$('#successMessage').dialog({
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
				$('#successMessage').text().replace('\n', '').replace('<br>','\n').replace('.','.\n');
				
			    $( "#successMessage" ).dialog("open");
			    
			}
			if(navigator.appName == 'Microsoft Internet Explorer') {//check to see if browser supports CSS3
				alert('This browser is not supported by ScoutTrax.\n  You may continue to explore using this browser, but many features will not work or display properly. \nWe recommend that you download Firefox, Chrome or Safari for viewing ScoutTrax.org.');
			}
			else
				if($.support.opacity === false)//check to see if browser supports CSS3
			{
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
	<div class="container-fluid">
		<div class="row">
			<tiles:insertAttribute name="top" />
		</div>
		<div class="row" >
			<tiles:insertAttribute name="bottom" />
		</div>
	</div>
	</body>
</html>