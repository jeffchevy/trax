<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"

"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="css/trax.css" type="text/css" media="screen, print">
		<script type="text/javascript" src="js/jquery.js"></script>

		<style type="text/css">
		#art-main{
			margin-left: auto;
    		margin-right: auto;
		}

		</style>
	</head>
	<body id="body">
		<div id="main">
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="menu" />
			<tiles:insertAttribute name="body" />
			<tiles:insertAttribute name="footer" />
		</div>
	</body>
</html>