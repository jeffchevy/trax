   <%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
   		<style type="text/css">
			@import "css/data_table.css";
			@import "css/TableTool.css";
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
			}
			
			td {
				border-left: solid thin #888;
				border-bottom: solid thin #888;
			}
			
			th, .header {
				background-color: #CCCCAA;
				color: black;
				border: solid thin #000;
				text-align: center;
			}
			.header {
				text-align: left;
				width: 15em;
			}
			.redBeadHeader {
				text-align: right;
				color: red;
				background-color: #fff;
			}
			.yellowBeadHeader {
				text-align: right;
				color: #CC0;
				background-color: #fff;
			}
			.silverBeadHeader {
				text-align: right;
				color: #666;
				background-color: #fff;
			}
			.specialty {
				background-color: #fff;
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
		</style>
		<script type="text/javascript">
			$(document).ready(function()
			{
				$('.dataTable').dataTable({
					"aaSorting": [[ 0, "asc" ]],
					"bPaginate": false,
					"bLengthChange": false,
					"sDom": '<"top">rt<"bottom"if<"clear">>',
					"oLanguage": {
						"sInfo": " _TOTAL_ awards"
					}  
				});
				$('#printpreview').click(function(){
                    myWindow=window.open('','','WIDTH=1000,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=yes,titlebar=yes');
                    myWindow.document.write('<html><head><style type="text/css">');
					myWindow.document.write('table {border: thin solid black; border-collapse: collapse; text-align: center; font: 12px;}');
					myWindow.document.write('td{border: solid thin #888;}');
					myWindow.document.write('.bottom {margin-bottom: 30px;}');
					myWindow.document.write('#requiredreport_filter {display:none;}');
					myWindow.document.write('#rankreport_filter {display:none;}');
					myWindow.document.write('#awardreport_filter {display:none;}');
					myWindow.document.write('</style>');
					myWindow.document.write('</head><body>');
					myWindow.document.write($('#printdiv').html());
					myWindow.document.write('</body></html>');
					myWindow.focus();
					return false;
            	});
            	
            	//search throw all the tables and remove the columns for boys with no awards
			    var $tables = $('.dataTable');
			    $.each($tables, function(index, $table) {
				    $(this).find('th').each(function(i) {
					    var remove = 0;
					
					    var tds = $(this).parents('table').find('tr td:nth-child(' + (i + 1) + ')')
					    tds.each(function(j) { if (this.innerHTML == '') remove++; });
					
					    if (remove == ($(this).parents('table').find('tr').length - 1)) {
					        $(this).hide();
					        tds.hide();
					    }
					});
			    });
			});
		</script>
   
<div class='table'>
	<div id='page1' class='cell'>
		<a id='printpreview' href="#" title='Print Preview'><img alt="Print Preview" src="images/printpreview.png"></a>
		<div id='printdiv'>
			<div class='row'>
				<h1>Pack ${scout.unit.number} Group report</h1>
				<h2>${todaysDate}</h2>
			</div>
			<div class='row'>
				<trax:groupreport name='rankScoutMap' styleId='rankreport'/>
			</div>
			<div class='row'>
				<trax:groupreport name='electiveMap' styleId='awardreport'/>
			</div>
			<div class='row'>
				<trax:groupreport name='beltLoopMap' styleId='requiredreport'/>
			</div>
			<div class='row'>
				<trax:groupreport name='pinMap' styleId='awardreport'/>
			</div>
			<div class='row'>
				<trax:groupreport name='activityBadgeMap' styleId='awardreport'/>
			</div>
			<div class='row'>
				<trax:groupreport name='awardScoutMap' styleId='awardreport'/>
			</div>
		</div>
	</div>
</div>


