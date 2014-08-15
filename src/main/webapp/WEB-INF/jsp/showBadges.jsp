
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="/WEB-INF/TraxTags.tld" prefix="trax"%>


<html>
	<head>
	<title>Users</title>
		<style type="text/css">
			span.hilite { font-weight: bold; }
			
			.meritbadgename {
				min-height: 3.0em;
				text-align: center;
				color: white;
				font-weight: bold
			}
			/*.eagleRequired:before {
                 opacity: 0.9;
                 content: url('images/EagleRequired2.png');
                 position:relative;
                 left:-35px;
                 top:-145px;
            }*/
			a {
				border: none;
				color: #445544;
			}
			#pages {
				padding-right: 20px;
			}
			.cell {
    			text-align: center;
			}
			#page1 {
				width: 100%;
				color: white;
				background: #454;
				padding: 0 0 0 20px;
				border-right: solid thin grey;
			}
		</style>
		
		<script type="text/javascript">
			$(document).ready(function()
			{
				oTable = $('#badgeList').dataTable({
					"bPaginate" : false,
					 "bFilter" : false,
					 "bSort" : false,
					 "bInfo": false
				});
			});
		</script>
	</head>
	<body>
		<div id="page1">
	    	<trax:displaybadges name="badgeConfigs"/>
		</div>
	</body>
</html>