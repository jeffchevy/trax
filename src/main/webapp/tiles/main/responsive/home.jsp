
<!doctype html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap-theme.css" />
<link type="text/css" href="css/jquery-ui.all.css" rel="stylesheet" />
<style type="text/css">
	body {
		background: white;
		color: black;
		margin: 0;
	}
	h1 {
		color: black;
	}
	img {
		border: none;
	}
	.startNow {
	    margin-left: 30%;
  			margin-top: 15px;
	}
	.row {
		padding: 20px;
	}
	#loginFormat {
		right: 30%;
		position: relative;
	}
	#theList {
		text-align:center; 
		padding:0;
		margin:0;
		font-family:Verdana,Arial;
	}
	.vcenter {
	    display: inline-block;
	    vertical-align: middle;
	    float: none;
	}
</style>
</head>



</head>


<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-1 
						col-md-6 col-md-offset-1 
						col-sm-6 col-sm-offset-1 
						col-xs-12
						vcenter">
						<img alt="ScoutTrax Logo" src="images/scouttrax_logo2.png" class="img-responsive">
			</div>
			<div class="col-lg-2 col-md-2 col-sm-2 vcenter">
				<a id="nav-home" title='Login' href='login.html'><img alt="Scout Trax Login" src="images/StartNowText2.png" class="startNow" />
			</div>
		</div>
		<div id="theList" class="row">Tiger Cub - Wolf - Bear - Webelos - 11 Year Old - Scouts - Varsity - Venture - Duty to God</div>
		<div class="col-lg-3 col-lg-offset-1 
					col-md-3 col-md-offset-1 
					col-sm-4
					col-xs-11 col-md-offset-1">
				<a class="youtube" href="#" rel="uLbM6YGSswA" title="Getting Started">
				<img 
					alt="Getting Started" class="img-responsive" 
					src="images/gettingstarted.png" /></a>
		</div>
		<div class="col-lg-3
					col-md-3 
					col-sm-4
					col-xs-11">
				<a class="youtube" href="#"
				rel="hN9LmH3xmHY" title="Features of Scout trax"> 
				<img
					alt="About Scouting" class="img-responsive"
					src="images/features.png"></a>
		</div>
		<div class="col-lg-3
					col-md-3 
					col-sm-4
					col-xs-11">
				<a class="youtube" class="img-responsive" href="#" rel="oE7emvBDwys" title="Advanced features for leaders">
				<img
					alt="Features for Leaders" class="img-responsive"
					src="images/extrafeatures.jpg"></a>
		</div>
	</div>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.11.2.custom.min.js"></script>
	<script	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery.youtubepopup.min.js"></script>
	 <script type="text/javascript">
		$(document).ready(function()
		{ 
            $('a.youtube').YouTubePopup({ 
	            	autoplay: 1, 
	            	hideTitleBar: true,
	            	clickOutsideClose: true,
	            	showBorder: false
	            	});
		});
	</script>
</body>