<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"

"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="css/skin.css" />
	<link rel="stylesheet" type="text/css" href="css/forms.css" />
	<link type="text/css" href="css/jquery-ui-1.8.10.custom.css" rel="Stylesheet" />
	<link type="text/css" href="css/jquery.ui.datepicker.css" rel="Stylesheet" />
	<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">	

	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery.jcarousel.js"></script>
	<script src="js/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
	
	<script src="js/jquery.dataTables.js" type="text/javascript"></script>
	<script src="js/SearchHighlight.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/jquery.idletimer.js" type="text/javascript"></script>   
	<script src="js/jquery.idletimeout.js" type="text/javascript"></script> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><tiles:insertAttribute name="title" ignore="true" />
	   ScoutTrax.org Free online scout tracking.</title>
	<meta name="keywords" content="Free, online, tracking, software, Boy scout, scout, cub scout, Varsity, Venture, Venturing, Troop, Patrol, Team, Crew, merit badge, 
	requirements, camping, leadership, scout master, scout leader, scout book, rank, Eagle, Eagle Scout, user friendly, easy, 
	simple, parents, awards, tool, email, den, safe, secure, advancement, BSA, "/>
	<style type="text/css">
		input#awardearned[type=checkbox],
		input#awardinprogress[type=checkbox],
		input#allscouts[type=checkbox],
		input.requirementscheckbox[type=checkbox], 
		#scouts input[type=checkbox]
		{
			display:none;
		}
	
		input#awardinprogress[type=checkbox] + label, 
		input#awardinprogress[type=checkbox]:checked + label,
		input#awardearned[type=checkbox] + label, 
		input#awardearned[type=checkbox]:checked + label,
		input.requirementscheckbox[type=checkbox] + label, 
		input.requirementscheckbox[type=checkbox]:checked + label
		{
		    background: url("images/book/CheckboxUnselected.png") no-repeat;
		    height: 28px;
		    width: 30px;
		    display: inline-block;
		    padding: 0 2px 0 0;
		    vertical-align: middle;
		}
		
		input#awardearned[type=checkbox]:checked + label,
	 	input#awardinprogress[type=checkbox]:checked + label,
	 	input.requirementscheckbox[type=checkbox]:checked + label
		{
		    background: url("images/book/CheckboxSelected.png") no-repeat;
		}
		
		input.indeterminate[type=checkbox] + label
		{
		    background: url("images/book/CheckboxPartial.png") no-repeat !important;
		    height: 30px;
		    width: 30px;
		    display: inline-block;
		    padding: 0 2px 0 0;
		    vertical-align: middle;
		}
		input#allscouts[type=checkbox] + label,
		#scouts input[type=checkbox] + label,
		#scoutList input[type=checkbox] + label
	   {
	       background: url("images/book/CheckboxUnselected16.png") no-repeat;
	       height: 16px;
	       width: 16px;
	       display: inline-block;
	       padding: 0 0 0 0px;
	   }
	   
	   input#allscouts[type=checkbox]:checked + label,
	   #scouts input[type=checkbox]:checked + label,
	   #scoutList input[type=checkbox]:checked + label
	    {
	        background: url("images/book/CheckboxSelected16.png") no-repeat;
	        height: 16px;
	        width: 16px;
	        display:inline-block;
	        padding: 0 0 0 0px;
	    }
		.verticalalign {
			vertical-align: middle;
		}
		
		#unitTypeName {
			font-size: 12px;
			padding-left: 0;
		}
		
	.errors { color: red; font-size: 1.2em; white-space: nowrap; }
	body {
		color: white;
	}
	.awardimage {
		max-width: 100px;
		max-height: 100px;
	}
	
	.inprogresslabel, #earnedlabel, #sponsorlabel {
		font-weight: bold;
		font-size: 1.4em;
		color: white;
		text-align: center;
	}
	
	#earnedlabel{
		position:absolute;
		top:	100px;
		left:	18%;
		z-index: 1;
	}
	
	body {
		background: #000;
		padding: 0px;
		margin: 0px;
		font-family: Arial,"Trebuchet MS",Helvetica,Sans-Serif;
	    font-size: 12px;
	    font-style: normal;
	    font-weight: normal;
	}
	p {
		font-family: Arial,"Trebuchet MS",Helvetica,Sans-Serif;
	    font-size: 12px;
	    font-style: normal;
	    background: white;
	    border: 1px black;
	    color: black;
	    text-align: left;
	    margin: 1px;
	    padding: 2px;
	}
	.table, .row, .cell {
		/*border: 1px solid white;*/
	}
	#awardList {
		background: none repeat scroll 0 0 white;
	    border: thin solid;
	    color: black;
	    margin: 0 0 5px 22px;
	    padding: 0 7px;
	    text-align: left;
	}
	
	
	#campEntryList tr, #serviceEntryList tr, #leadershipEntryList tr {
		text-align: left;
	}
	#campEntryList td, #serviceEntryList td, #leadershipEntryList td {
		border: thin solid black;
		background: white;
		color: black;
	}
	#campEntryList, #serviceEntryList, #leadershipEntryList {
		width: 100%;
	}
	
	.table {
		display: table;
		/*width: 100%;*/
	}
	
	.row {
		display: table-row;
	}
	.image {
		border: none;
	    padding-bottom: 5px;
	}
	.cell {
		display: table-cell;
		text-align: right;
		vertical-align: top;
	}
	#footer{width:100%}
	.tablefooter {
		border: 1px solid black;
		text-align: center;
		background-color: white;
		width: 50%;
	}
	
	.smalllabel {
		color: #454;
		font-size: 1em;
	}
	.biglabel {
		color: #454;
		font: 1em bold;
	}
	
	.dropshadow {
	 -moz-box-shadow: 9px 6px 6px #333; /* Firefox */
	 -webkit-box-shadow: 9px 6px 6px #333; /* Safari/Chrome */
	 box-shadow: 9px 6px 6px #333; /* Opera and other CSS3 supporting browsers */
	 -ms-filter: "progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#333')";/* IE 8 */
	 : progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#333');/* IE 5.5 - 7 */ 
	} 
	#mainmenu {
		width: 130px;
		padding-right: 20px;
		min-width: 130px;
background: none repeat scroll 0 0 #000000;	}
	div#mainmenu2 {
		position: absolute;
		top: 0;
	}
	#earned {
		height: 128px;
	 	text-align: center;
	    padding-top: 10px;
	    background: none repeat scroll 0 0 #000000;
	}
	#topmiddle {
	    background: none repeat scroll 0 0 #000000;
	}
	#rightcolumn {
	    background: none repeat scroll 0 0 #000000;
	}
	#scoutnamesColumn {
	    background: none repeat scroll 0 0 #000000;
	}
	.meritbadge {
		margin: 5px;
	}
	
	#rankmenu {
		
	}
	
	#awardTitles {
		color: #ddd;
		text-shadow: 4px 4px 6px #000000;
		background-color: rgba(0,0,0,0.2);
		text-align: center;
	}
	#pagequote, .darkbackground {
		color: #ddd;
		text-shadow: 4px 4px 6px #000000;
		font-size: 2em;
		background-color: rgba(0,0,0,0.5);
		text-align: center;
		padding: 0.5em;
		margin: 1.5em;
		border-radius: 15px;
		-moz-border-radius: 15px;
	}
	.awardheader {
	    background-color: rgba(0, 0, 0, 0.7);
	    border-left: thin solid black;
	    border-top: thin solid black;
	    color: white;
	    font-size: 1.5em;
	    padding-left: 15px;
	    text-align: left;
	    vertical-align: middle;
	}
	
	#pages {
		width: 100%;
	}
	
	/*#content {
		position: absolute;
	    top: 150px;
	    width: 1200px;
	    z-index: 1;
	}*/
			
	#troopList, #leaderList, #scoutList {
		color: black;
		background-color: white;
		border: solid thin black;
		width: 100%;
		text-align: left;
	}
	
	#troopList td, #leaderList td, #scoutList td {
		border-left: solid thin #888;
		border-bottom: solid thin #888;
	}
	
	#troopList thead, .scoutTable thead, #leaderList thead, #scoutList thead {
		background-color: #CCCCAA;
		color: black;
		border-bottom: solid thin #000;
		text-align: center;
	}
	#import {
		float: right;
		margin: 20px;
		padding:20px;
	}
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
	#pagetwo {
	    margin-top: 80px;
	}
	#personalInfo {
	}
	#reminder {
	
	}
	
	#updateprofilepicture {
	    color: white;
	    position: relative;
	    top: 0.5em;
	    background: none repeat scroll 0 0 black;
	    color: #FFFFFF;
	    height: 20px;
	    opacity: 0.7;
	    padding: 0 7px 0 23px;
	    right: 1px;
	    top: 0;
	}
		
	#scoutimage, div#awardimagediv{
		width: 100px; 
	}
	img#scoutimage, #awardimage { /*mirror reflections*/
		margin: 0 0 10px 5px;
		max-height: 140px;
	    max-width: 100px;
	}
	img { border: none; }
	#previousnextaward {
		text-align: center;
	}
	
	div#awardTitles{
		width: 100%;
		height: 100px; 
		padding-top: 25px;
		vertical-align: top;
	}
	p#awardname, p#subtitle, #awardLink {
		color: white;
		background: transparent;
		text-transform: uppercase;
		font-family: simplex, herman, georgia;
	    font-size: 2.2em;
	    font-style: normal;
	    font-weight: bold;
	    text-align: center;
	    margin: 0;
    	padding: 0;
	}
	p#subtitle {
		font-size: 1.5em;
		text-transform: none;
	}
	
	#awardLink {
		text-transform: none;
		font-size: 1.2em;
	}
	#sponsor {
		background:  no-repeat center top transparent;
		height: 95px;
	}
	
	#inprogress {
		height: 9em;
		width: 12em;
		background: #000;
	}
	#rank a:link,#rank a:visited {
	    color: black;
	    text-decoration: none;
	    padding: 0 7px;
	    border-left: thin white solid;
	    border-top: thin white solid;
	    border-right: thin white solid;
	}
	#rank li {
		display: inline;
	}
	#rank {
	    background-color: #cca;
		list-style: none outside none;
		font-size: 1.3em;
		text-align: left;
		margin: 0;
	    padding: 0;
	}
	#show {
		display: inline;
	}
	.active {
	   border-left: medium solid red !important;
	    border-right: medium solid red !important;
	    border-top: medium solid red !important;
	    background: #454 !important;
		padding: 0 3px;
		color: #EEE !important;
	}
	
	#nav {
		list-style: none outside none;
		width: auto;
		margin: 0;
		padding: 0;
	}
	
	#nav li {
		
	}
	
	#scouts {
		width: 10em;
		font-size: 1.3em;
		margin: 0;
		padding: 0;
	}
	li.allscouts {
		background: #454;
		color: #711 important!;
	}
	#scoutnames {
		padding-top: 2px;
		text-align: left !important;
		background: #000000;
	}
	
	#scouts li {
		list-style: none outside none;
		text-align: left;
		background-color: #777;
	}
	
	#scouts a:link, #scouts a:visited {
		color: white;
		text-decoration: none;
		margin: 0;
		padding: 0;
	}
	#scouts .selectedscout {
	    background-color: #444 !important;
	}
	
	.selectedscout {
	    color: white;
	    font-weight: bold;
	    border: thin solid red;
	    background-color: #444 !important;
	} 
	
	.doublecheck {
		border: thin solid red !important;
	}
	#nav-home, #nav-troop, #nav-advancement, #nav-calendar, #nav-email, #nav-shirt, #nav-report, #nav-scoutreport, #nav-training
	{
		display: block;
		font-size: 1.6em;
		height: 120px;
		width: 130px;
	}
	
	#nav-home {
		padding: 0px 0 0 0px;
	}
	#nav-home img{
		padding: 20px 0 0 0px;
		width: 200px;
	}
	
	#nav-troop {
		padding: 30px 0 0 5px;
	}
	
	#nav-advancement {
		background: url("images/book/Book Button.png") no-repeat center top transparent;
		padding: 0 0 0 23px;
	}
	
	#nav-tools {
		background: url("images/book/tool.png") no-repeat center top transparent;
		display: block;
		font-size: 1.6em;
		height: 30px;
		width: 40px;
	}
	
	#nav-calendar {
		background: url("images/book/CalendarButton.png") no-repeat center top transparent;
		padding: 0 0 0 15px
	}
	
	#nav-email {
		background: url("images/book/email.png") no-repeat center top transparent;
	}
	
	#nav-training {
		background: url("images/book/training.png") no-repeat center top transparent;
	}
	
	#nav-shirt {
		background: url("images/book/shirt.png") no-repeat center top transparent;
		padding: 0 0 0 20px;
	}
	
	#nav-report, #nav-scoutreport {
		background: url("images/book/report.png") no-repeat center top transparent;
		padding: 0 0 0 20px;
	}
	
	#nav-sash {
		background: url("images/book/sash.png") no-repeat center top transparent;
		display: block;
		font-size: 1.6em;
		height: 122px;
		width: 90px;
	}
	.cleared
	{
		float: none;
		clear: both;
		margin: 0;
		padding: 0;
		border: none;
		font-size:1px;
	}
	.requirementcheckbox {
	 	width:	1.4em;
	 	margin:	0 0 5px 0;
	}
	.requirementtext {
	    background: none repeat scroll 0 0 white;
	    border: thin solid;
	    color: black;
	    margin: 0 0 5px 22px;
	    padding: 0 7px;
	    text-align: left;
	    width: 85%;
	}
	.center
	{
		margin-left:auto;
		margin-right:auto;
		width:40%;
	}
	#electiveLink{
		margin-left: 10px;
		background-color: rgba(0, 0, 0, 0);
		color: #FFA;
    	white-space: nowrap;
    	font-size: 1.1em;
    	text-decoration: none;
    	padding: 0px;
	}
	#pinLinkDiv {
		margin-top: 10px;
	}
	
	#pinLink {
		margin-left: 20px;
		background-color: rgba(0, 0, 0, 0.5);
		color: #FFA;
    	white-space: nowrap;
    	font-size: 1.3em;
    	text-decoration: none;
    	border-radius: 15px 15px 15px 15px;
    	padding: 0.5em;
	}
	.numberedRequirement {
		background: none repeat scroll 0 0 #FFFFAA;
    	font-weight: bold;
	}
	.doFollowingRequirement{
		background: none repeat scroll 0 0 	#CCEEFF;
    	font-weight: bold;
	}
	
	.datebox, #awarddatebox {
		background: #B0D1B2;
	    border: thin solid black;
	    margin: 0;
	    padding: 0 5px;
	    text-align: left;
	    width: 5.6em;
	    color: black;
	    font-size: 12px;
	}
	
	#page1 .datebox, #awarddatebox {
		background: -moz-linear-gradient(right center , #334533, #B0D1B2) repeat scroll 0 0 transparent;
		background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#334533), to(#B0D1B2));
	}
	.dataTables_filter {
		margin-right: 27px;
	}
	div.spacercolumn {
		background: none repeat scroll 0 0 transparent;
	    color: transparent;
	}
	
	#scout_info {
		height: 9em;
	}
	
	.requirementCount {
		color: wheat;
	    font-size: 1.2em;
	    left: -30px;
	    position: relative;
	    top: 20px;
	    background-color: rgba(0,0,0,0.8);
	    padding: 0 5px;
	}
	
	.meritbadgelink {
		border: none;
		color: black;
		text-align: center;
	}
	
	rr #scoutrightpocket , Boy_Scout_left_sleeve ,#scoutleftsleeve,#scoutrightsleeve,
	#venturerleftpocket,#venturerrightpocket,#venturerleftsleeve,#venturerrightsleeve {
		width: 250px;
		height: 410px;
		border: thick solid black;
	}
	
	#scoutleftpocket {
		background: url("images/shirt/Boy_Scout_left_pocket.png") no-repeat center top transparent;
	}
	#scoutrightpocket {
		background: url("images/shirt/Boy_Scout_left_sleeve_(Boy_Scouts_of_America).png") no-repeat center top transparent;
	}
	#scoutleftsleeve {
		background: url("images/shirt/Boy_Scout_left_sleeve.png") no-repeat center top transparent;
	}
	#scoutrightsleeve {
		background: url("images/shirt/Boy_Scout_right_sleeve.png") no-repeat center top transparent;
	}
	#venturerleftpocket {
		background: url("images/shirt/Venturer_left_pocket.png") no-repeat center top transparent;
	}
	#venturerrightpocket {
		background: url("images/shirt/Venturer_right_pocket.png") no-repeat center top transparent;
	}
	#venturerleftsleeve {
		background: url("images/shirt/Venturer_left_sleeve.png") no-repeat center top transparent;
	}
	#venturerrightsleeve {
		background: url("images/shirt/Venturer_right_sleeve.png") no-repeat center top transparent;
	}
	.ranktab {
		background: #ABA;
	}
	
	.logtab {
		background: #787;
	}
	.hidden, .errors, label.error {
		position: absolute;
		left: 0px;
		top: -500px;
		width: 1px;
		height: 1px;
		overflow: hidden;
	}
	th.sorting_asc {
		background: url("images/sort_asc.png") no-repeat scroll right center #797;
	}
	th.sorting_desc {
		background: url("images/sort_desc.png") no-repeat scroll right center #797;
	}
	.bottom {
		font-size: 1.8em;
	}
	caption {
		font-size: 1.5em;
		color: #fff;
		background: #343;
	}
	.dataTables_filter {
	    float: right;
	    text-align: right;
	    width: 50%;
	    margin-right: 20px;
		margin-bottom: 30px;
	}
	
	<!-- For the timeout - we want to force people to click a button, so hide the close link in the toolbar -->
	a.ui-dialog-titlebar-close { display:none }
	
	.edit_profilepicture {
	    background: none repeat scroll 0 0 #000000;
	    color: #FFFFFF;
	    display: none;
	    height: 20px;
	    opacity: 0.7;
	    padding: 6px 7px 0 23px;
	    position: absolute;
	    right: 1px;
	    top: 0;
	}	
	.profile-picture:hover {
	    text-decoration: none;
	}
	.profile-picture:hover .edit_profilepicture {
	    display: block;
	}
	.profile-picture img {
	    display: block;
	    margin: auto;
	    max-width: 180px;
	}
	.profile-picture span {
	    background-color: #000000;
	    height: 800px;
	    opacity: 0.3;
	    position: absolute;
	    right: 0;
	    top: 0;
	    width: 1px;
	}
	.edit_profilepicture {
	    background: none repeat scroll 0 0 #000000;
	    color: #FFFFFF;
	    display: none;
	    height: 20px;
	    opacity: 0.7;
	    padding: 6px 7px 0 23px;
	    position: absolute;
	    right: 1px;
	    top: 0;
	}
	.edit_profilepicture:hover {
	    opacity: 0.95;
	}
	span.edit_profilepicture_icon {
	    background: url("http://static.ak.fbcdn.net/rsrc.php/v1/yA/r/QbfalTL4SQ2.png") no-repeat scroll left 4px transparent;
	    height: 18px;
	    left: 7px;
	    opacity: 0.95;
	    position: absolute;
	    top: 4px;
	    width: 18px;
	}	
	
	.link {
		color: white;
		text-align: left;
		text-decoration: none;
		background: #252525;
		border: thin solid #111;
		padding: 4px;
	}
	a:visited#feedback, a:active#feedback, 
	a:visited#logout, a:active#logout
	{
		color: #888;
	}
	
	.spinner {
	    position: fixed;
	    top: 50%;
	    left: 50%;
	    margin-left: -50px; /* half width of the spinner gif */
	    margin-top: -50px; /* half height of the spinner gif */
	    text-align:center;
	    z-index:10000;
	    overflow: auto;
	    width: 100px; /* width of the spinner gif */
	    height: 102px; /*hight of the spinner gif +2px to fix IE8 issue */
	}
	
	#rowspacer {
		width: 100%;
		height: 30px;
	}
	</style>
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
	</body>
	<style>
	#traxreporttable {
		background:white !important; 
		height:40px;
		color:red !important;
		column-span:all;
		-webkit-column-span:all;
		}
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
			else if($('#Star').length != 0){$('a', '#nav-starrank').addClass('active');}
			else if($('#Life').length != 0){$('a', '#nav-liferank').addClass('active');}
			else if($('#Eagle').length != 0){$('a', '#nav-eaglerank').addClass('active');}
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
			
		    
		    /*$('.requirementscheckbox').each(function(){
				$(this).prop("indeterminate", true);
			});*/
		});
		</script>
</html>