<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"

"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title>ScoutTrax.org Free online scout advancement tracking.</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="google-site-verification" content="r8_DZ-vawNf3PjfAGDvN0YERMTlf-nnpI5TB5dvLDFQ" />
	<meta name="keywords" content="Free, online, tracking, software, Boy scout, scout, cub scout, Varsity, Venture, Venturing, Troop, Patrol, Team, Crew, merit badge, 
	requirements, camping, leadership, scout master, scout leader, scout book, rank, Eagle, Eagle Scout, user friendly, easy, 
	simple, parents, Awards, camping, service, Merit Badge, email, den, safe, secure, advancement, BSA, LDS, Duty to God, email, report "/>

	<script type="text/javascript" src="js/jquery.js"></script>
	<script src="js/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		 <style type="text/css">
		 #main {
			margin: auto;
	  		width: 500px;
	  		background-position: center center;
		}
		#main-nav {
			width:auto;
			padding: 8px 0;
		}
		#main-nav li, #main-nav a:link, #main-nav a:visited {
			display:inline;
		} 
		ul#main-nav {
		    list-style: none outside none;
		    height: auto;
		    margin: 0;
		    padding: 0;
		    width: auto;
		}
		
		ul#main-nav li a {
		  display: block;
		  width: 7em;
		  height: 2em;
		  border: .1em solid blue;
		  text-decoration: none;
		  color: #999;
		  font-weight: bold;
		  text-align: center;
		  background: #144;
		} 
		ul#main-nav li.active {
			color: #080807;
		}
		
		ul#main-nav li a:hover {
		  background: black;
		  color: #fff;
		} 
		body {
		    background: url("images/header_cbg.jpg") repeat-y scroll center top transparent;
		    color: black;
		    font: 12px/1.8em "Liberation sans",Arial,Helvetica,sans-serif;
		    font-weight: bold;
		    height: auto;
		    margin-top: 150px;
		    border: none;
		}
		img {
			border: none;
	    }
	</style>
	</head>
	<body>
		<div id='main'>
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="navigation" />
			<tiles:insertAttribute name="body" />
			<tiles:insertAttribute name="footer" />
		</div>
		<script type="text/javascript">
		$(document).ready(function()
		{
			if($('#home').length != 0) {$('a', '#nav-home').addClass('active');}
			else if($('#issues').length != 0){$('a', '#nav-issues').addClass('active');}
			
			if($.support.opacity === false)//check to see if browser supports CSS3
				alert('This browser is not supported by ScoutTrax.\n  You may continue to explore using this browser, but many features will not work or display properly. \nWe recommend that you download Firefox, Chrome or Safari for viewing ScoutTrax.org.');
			}
		});
		</script>
	</body>
</html>