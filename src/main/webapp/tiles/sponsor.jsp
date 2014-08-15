<div id="sponsor">
<a id='printpreview' href="#" title='Print Preview'><img alt="Print Preview" src="images/printpreview.png"></a>
	<div id='printdiv'>
		<div id='awardreportheader' class='row' >
			<h1>${scout.unit.typeOfUnit.name} ${scout.unit.number} ${award.awardConfig.name} report</h1>
			<h2 id='reportdate'>${todaysDate}</h2>
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
	});
	$('#summaryTable').dataTable({
		"sDom": '<"top">rt<"bottom"if<"clear">>',
		"oLanguage": {
			"sInfo": " _TOTAL_ Boys"
		}  
	});
	$('#printpreview').click(function(){
		$('#awardreportheader').removeClass('hidden');
		
        myWindow=window.open('','','WIDTH=1000,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=yes');
        myWindow.document.write('<html><head><style type="text/css">');
		myWindow.document.write('table {border: border-collapse: collapse; text-align: center; font: 12px;}');
		myWindow.document.write('td{border: solid thin #888;}');
		myWindow.document.write('.bottom {margin-bottom: 30px;}');
		myWindow.document.write('th.rotateheader {font: bold; height: 140px;	white-space: nowrap;}');
		myWindow.document.write('th.rotateheader > div { transform: translate(15px, 50px) rotate(-45deg); -webkit-transform: translate(17px, 51px) rotate(-45deg); width: 30px;}');
		myWindow.document.write('th.rotateheader > div > span { border-bottom: 1px solid #888; padding: 5px 10px;}');
		myWindow.document.write('td.completed {background: #888;}');
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
@import "css/data_table.css";
@import "css/TableTool.css";
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
    white-space: nowrap;
    overflow-x: auto;
    max-width: 1120px;
    width: 1120px;
}
#summaryTable td {
	border: thin solid black;
	background: #F1DC9A;
	color: #000;
}
#summaryTable {
}
.rowheader {
	font:bold;
}
/*
.rowheader {
    position:absolute;
    width: 10em;
    top:auto;
    left:0;
    border-right: 0px none black;    
}*/
td.completed {
	background: #666 !important;
}
th.rotateheader {
	font: bold;
	height: 140px;
  	white-space: nowrap;
}
td.rowheader {
	font-style: bold;
	text-align: left;
	background: #D1DC9A !important
}
th.rotateheader > div {
  transform: translate(17px, 51px) rotate(-45deg);
  -webkit-transform: translate(17px, 51px) rotate(-45deg);
  width: 30px;
}

th.rotateheader > div > span {
  border-bottom: 1px solid #fff;
  padding: 5px 10px;
}
#summaryTable {
	background: black;
	color: white;
}
#sponsorRow {
	background:black;
}
</style>
