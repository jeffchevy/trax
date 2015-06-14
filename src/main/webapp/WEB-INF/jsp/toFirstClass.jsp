
<style type="text/css">
	
	@import "css/dataTables.tableTools.css";
	span.hilite { font-weight: bold; }
	.required {
		background-color: #876;
	}
	.hidden {
		display: none;
	}
	.rank {
		background-color: #676;
	}
	#page1 {
		background-image: none;
		background-color: #454;
		font: 22px;
	}
	table {
	    background-color: white;
	    border: thin solid black;
	    color: black;
	    text-align: center;
	    font: 12px;
	    margin: 20px 0 0;
	}
	
	td {
		border-left: solid thin #888;
		border-bottom: solid thin #888;
		font-size: 10px;
		padding: 0;
	}
	
	th, .header {
		background-color: black;
		color: white;
		border: solid thin #000;
		text-align: center;
		padding: 0;
		margin:0;
		max-width: 2em;
	}
	.header {
		text-align: left;
		white-space: nowrap;
	}
	h1, h2 {
		text-align: center;
		font-weight: bold;
		font-size: 2em;
		margin: 0px;
	}
	h2 {
		font-size: 1.2em;
		margin: 0 0 20px 0;
	}
	.dataTables_info {
	    padding-bottom: 10px;
	    float: left;
	    text-align: left;
	    width: 30%;
	    margin-right: 0px;
	    font-size: 0.7em;
	}
	.dataTables_filter {
	    float: right;
	    text-align: left;
		margin-bottom: 30px;
	}
	.requiredreport_info {
		font-size: 1.2em;
	}
	.bottom {
		font-size: 1.8em;
		margin-bottom: 5em;
	}
	caption {
		font-size: 1.5em;
		color: #fff;
		background: #343;
	}
	.odd, sStripOdd {
		background-color: #CDC;
	}
	th.sorting_asc {
		background: url("images/sort_asc.png") no-repeat scroll right center #797;
	}
	th.sorting_desc {
		background: url("images/sort_desc.png") no-repeat scroll right center #797;
	}
	th.rotateheader {
	background: black;
	color: white;
	font: bold;
	height: 140px;
  	white-space: nowrap;
	}
	td.rowheader {
		font-style: bold;
		text-align: left;
		background: #D1DC9A !important;
		white-space: wrap;
	}
	th.rotateheader > div {
	  transform: translate(17px, 51px) rotate(-45deg);
	  -webkit-transform: translate(17px, 51px) rotate(-45deg);
	  width: 30px;
	}
	
	th.rotateheader > div > span {
	  border-bottom: 1px solid #fff;
	  padding: 2px 10px 0 10px;
	  background: black;
	  cursor: pointer;
	}
	.awardReportTable th.sorting_asc > div > span, .awardReportTable th.sorting_desc > div > span {
	    background: url("images/sort_asc.png") no-repeat scroll -4px 2px #797;
	    transform: translate(17px, 51px) rotate(-45deg);
	    -webkit-transform: translate(17px, 51px) rotate(-45deg);
	    -ms-transform: rotate(0deg);
	}
	.awardReportTable th.sorting_desc > div > span {
	    background: url("images/sort_desc.png") no-repeat scroll -4px -3px #797;
	}
	#page1 {
		repeat scroll 0 0%, url("images/scoutbackground.jpg") repeat scroll 0 0 rgba(0, 0, 0, 0) !important
	}
	td.completed {
    	background: none repeat scroll 0 0 #666 !important;
	}
	#page1 {
	    background: url("images/scoutbackground.jpg") repeat scroll 0 0 rgba(0, 0, 0, 0) !important;
	    padding-right: 140px;
	}
	.awardReportTable tbody td {
	    background: #f1dc9a;
	    border: thin solid black;
	    color: Black;
	    padding-left: 5px;
	}
	tr.awardNameRow td {
		background-color: #343 !important;
		border: #343!important;
    	color: white;
	}
	tr.awardNameRow td:first-child {
		font-size: 1.4em;
    	font-weight: bold;
	}
</style>
<div class='table'>
	<div id='page1' class='cell'>
		<a id='printpreview' href="#" title='Print Preview'><img alt="Print Preview" src="images/printpreview.png"></a>
		<div id='printdiv'>
			<div id="tables" class='row'>
				${ htmlTables}
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function()
	{
		/* 
		
	    l - length changing input control
	    f - filtering input
	    t - The table!
	    i - Table information summary
	    p - pagination control
	    r - processing display element

		"sDom": '<"top"i>rt<"bottom"flp><"clear">'
		means
		'<' gives '<div>'
		'<"class"' gives '<div class="class">'
		'>' gives '</div>'
		<div class="top">i</div>rt<div class="bottom">flp</div><div class="clear"></div>
		*/
		/*
		var awardReportTables = $('.awardReportTable').dataTable({
			"aaSorting": [[ 0, "asc" ]],
			"bPaginate": false,
			"bLengthChange": false,
			"bInfo": false,
			"sDom": '<"top">it<"bottom"if<"clear">>'
		});
		*/
		/*var awardReportTableTools = new $.fn.dataTable.TableTools( awardReportTables, {
	        "buttons": [
                   "copy",
                   "csv",
                   "xls",
                   "pdf",
                   { "type": "print", "buttonText": "Print me!" }
               ]
           });
             
		$( '.ScoutTableDiv' ).append('div.top');
	*/
		$('#printpreview').click(function(){
			$('#awardreportheader').removeClass('hidden');
			var currentdate = new Date(); 
		    var localdate = "" + currentdate.getDate() + "/"
		                + (currentdate.getMonth()+1)  + "/" 
		                + currentdate.getFullYear() + " @ "  
		                + currentdate.getHours() + ":"  
		                + currentdate.getMinutes() + ":" 
		                + currentdate.getSeconds();
		                
	        myWindow=window.open('','','WIDTH=1000,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=yes');
	        myWindow.document.write('<html><head><p id="todaysdate">'+localDate+'</p>');
	        
	        myWindow.document.write('<style type="text/css">');
			myWindow.document.write('table {border: border-collapse: collapse; text-align: center; font: 12px;white-space:wrap; color:black;}');
			myWindow.document.write('td{border: solid thin #888;}');
			myWindow.document.write('td.rowheader {font-style: bold;text-align: left;white-space: wrap;width: 32em;min-width: 32em; max-width: 32em;}');
			myWindow.document.write('tr.awardNameRow td:first-child { font-size: 1.4em; font-weight: bold;}tr.awardNameRow td {background-color: #343 !important; border: medium none #343 !important; color: white;}');
			myWindow.document.write('.bottom {margin-bottom: 30px;}');
			myWindow.document.write('th.rotateheader {font: bold; height: 140px; white-space: nowrap;}');
			myWindow.document.write('th.rotateheader > div { transform: translate(15px, 50px) rotate(-45deg); -webkit-transform: translate(17px, 51px) rotate(-45deg); width: 30px;}');
			myWindow.document.write('th.rotateheader > div > span { border-bottom: 1px solid #888; padding: 5px 10px;white-space: nowrap;}');
			myWindow.document.write('td.completed {background: #888;}');
			myWindow.document.write('#todaysdate {text-align: right;}');
			myWindow.document.write('h1 {margin-bottom: 0px;}');
			myWindow.document.write('caption {font-size: 2em;}');
			myWindow.document.write('.dataTables_filter {display: none;}');
			 
			myWindow.document.write('</style>');
			myWindow.document.write('</head><body>');
			myWindow.document.write($('#printdiv').html());
			myWindow.document.write('</body></html>');
			myWindow.focus();
			return false;
		});
	});
	function ShowLocalDate()
    {
		
	    $('#currentDate').text(localdate)
    }
</script> 
   
