<div id="sponsor">
<a id='printpreview' href="#" title='Print Preview'><img alt="Print Preview" src="images/printpreview.png"></a>
	<div id='printdiv'>
		<div id='awardreportheader' class='row' >
			<h1>${scout.unit.typeOfUnit.name} ${scout.unit.number} ${award.awardConfig.name} report</h1>
		</div>
		<div id='inlineReport'></div>
	</div>
	
</div>
<script type="text/javascript">
$(document).ready(function()
{
	$('#awardreportheader').addClass('hidden');
	$('#printpreview').addClass('hidden');
	
	$.get('showReportTable.html', function(data) {
		if(data.length>0)
		{
			$('#printpreview').removeClass('hidden');
		}
		
	    $('#inlineReport').html(data);
	    /*
	    $('.awardReportTable').dataTable({
			"aaSorting": [[ 0, "asc" ]],
			"bPaginate": false,
			"bLengthChange": false,
			"bInfo": false,
			"sDom": '<"top">it<"bottom"if<"clear">>'
		});
	    */
	});
	$('#printpreview').click(function(){
		$('#awardreportheader').removeClass('hidden');
		var currentdate = new Date(); 
	    var localDate = "" + currentdate.getDate() + "/"
	                + (currentdate.getMonth()+1)  + "/" 
	                + currentdate.getFullYear() + " @ "  
	                + currentdate.getHours() + ":"  
	                + currentdate.getMinutes() + ":" 
	                + currentdate.getSeconds();
	                
        myWindow=window.open('','','WIDTH=1000,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=yes');
        myWindow.document.write('<html><head><p id="todaysdate">'+localDate+'</p>');
        
        myWindow.document.write('<style type="text/css">');
		myWindow.document.write('table {border: border-collapse: collapse; text-align: center; font: 12px;white-space:wrap;}');
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
</script>
<style type="text/css">
.hidden {
	position: absolute;
	left: 0px;
	top: -500px;
	width: 1px;
	height: 1px;
	overflow: hidden;
}
#inlineReport {
    border: thin none #FFFFFF;
    margin: 30px 20px 30px 20px;
    white-space: wrap;
    overflow-x: auto;
    max-width: 1120px;
    width: 1120px;
}
td.rowheader {
	font:bold;
    min-width: 20em;
	width: 20em;
	text-align: left;
	background: #D1DC9A !important
}
td.completed {
	background: #666 !important;
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
th.rotateheader {
	background: black;
	color: white;
	font: bold;
	height: 140px;
  	white-space: nowrap;
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
.awardReportTable {
	background: black;
	color: white;
}
.awardReportTable td {
	border: thin solid black;
	background: #F1DC9A;
	color: #000;
}
#sponsorRow {
	background:black;
}
</style>
