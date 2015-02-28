<%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
<link rel="stylesheet" href="css/jquery-ui.all.css">

<div class='table' id="content">
	<div class='row'>
		<trax:award name='award'/>
	</div>
</div>
<div id="spinner" class="spinner" style="display:none">
    <img id="img-spinner" src="images/spinner.gif" alt="Loading" />
</div>
<div id="noScoutSelectedDialog" title="Warning!" >
	<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
	   You must first select a scout before updating requirements.</p>
</div>
	<script type="text/javascript" src="js/new/jquery.mask.js"></script>
	<!-- <script type="text/javascript" src="js/jquery.maskedinput-custom-1.3.js"></script> -->
	<script type="text/javascript">
		$(document).ready(function()
		{
			$('.datebox').mask("00/00/0000", {placeholder: "__/__/____"});
			$('.datebox').datepicker({
				buttonImage: '/images/datepicker.gif',
				onSelect: function(dateText, inst){ 
					//make sure its checked
					var selectedbox = $(this).prevAll().last().find('.requirementscheckbox');
					if(selectedbox !== undefined)
					{
						selectedbox.attr('checked',true);
						 $.get('requirementpassedoff.html',{requirementConfigId: selectedbox.val(), ischecked: true, newDate: dateText}, function(data) {
		                    theResult = eval('('+data+')'); //get the JSON data
		                    var scoutsEarned = theResult.count;
		                    var awardComplete = theResult.awardComplete;
		                    
		                    if(scoutsEarned==0)
		                    {
                    			if(selectedbox.previousSibling !== undefined && selectedbox.previousSibling !== null)
		                    	{
		                    		selectedbox.previousSibling.innerHTML="";
		                    	}
		                    }
		                    else
		                    {
	                    		if(selectedbox.previousSibling !== undefined && selectedbox.previousSibling !== null)
		                    	{
		                    		selectedbox.previousSibling.innerHTML=scoutsEarned;
		                    	}
		                    }
		                    if(awardComplete)
		                    {
		                    	//alert("Congrats on completing the award!");
		                    	//TODO could refresh here, to move the in progress award to earned!
		                    }
	                    });
					} 								
				}
			});
			//Added on blur in case they don't use the calendar onSelect, this.val.length==0 we can prevent double firing?
			$('.datebox').blur(function(){ 
					//make sure its checked
					var selectedbox = $(this).prevAll().last().find('.requirementscheckbox');
					if(selectedbox !== undefined)
					{
						if($(this).val().length !=0)
						{
							selectedbox.attr('checked',true);
							 $.get('requirementpassedoff.html',{requirementConfigId: selectedbox.val(), ischecked: true, newDate: $(this).val()}, function(data) {
			                    theResult = eval('('+data+')'); //get the JSON data
			                    var scoutsEarned = theResult.count;
			                    var awardComplete = theResult.awardComplete;
			                    
			                    if(scoutsEarned==0)
			                    {
	                    			if(selectedbox.previousSibling !== undefined && selectedbox.previousSibling !== null)
			                    	{
			                    		selectedbox.previousSibling.innerHTML="";
			                    	}
			                    }
			                    else
			                    {
		                    		if(selectedbox.previousSibling !== undefined && selectedbox.previousSibling !== null)
			                    	{
			                    		selectedbox.previousSibling.innerHTML=scoutsEarned;
			                    	}
			                    }
			                    if(awardComplete)
			                    {
			                    	//alert("Congrats on completing the award!");
			                    	//TODO could refresh here, to move the in progress award to earned!
			                    }
		                    });
		                }
					} 								
				});
			$('#awarddatebox').mask("00/00/0000", {placeholder: "__/__/____"});
			$('#awarddatebox').datepicker({
				onSelect: function(dateText, inst){ 
					var selectedbox = $('#awardearned');
					if($(this).val().length !=0)
					{
						document.location.href="updateaward.html?ischecked=true&newdate="+dateText;
					}
				}
			});
			//Added on blur in case they don't use the calendar onSelect, this will cause a double fire
			$('#awarddatebox').change(function(){ 
				var selectedbox = $('#awardearned');
				if($(this).val().length !=0)
				{
					document.location.href="updateaward.html?ischecked=true&newdate="+this.value;
				}
			});
				
			$('#awardearned').click( function()
			{
				//make sure at least one boy is selected and we have scouts (not when a boy is logged in)
                if ($('.scoutName:checked').length==0 && $('.selectedscout').length==0 && $('.scoutName').length>0)
				{
					$( "#noScoutSelectedDialog" ).dialog("open");
					return false;
				}
				else if(this.checked===false)
                {
                	if(!confirm("If you continue this award will be removed from this\n"+ 
							"scouts earned list."))
                	{
                		return false;
                	}
                }
				document.location.href="updateawardearned.html?ischecked="+this.checked;
				
				return false;
			});
			$('#awardinprogress').click( function()
			{
				if(this.checked===false)
                {
                	if(!confirm("If you continue this award will be removed from this\n"+
							"scouts progress list and all requirement information will be lost."))
                	{
                		return false;
                	}
                }
				document.location.href="updateawardinprogress.html?ischecked="+this.checked;
				
				return false;
			});
			/*if($('.requirementscheckbox:checked').length==$('.requirementscheckbox').length)
			{
				//on page load if all are checked check parent
				$('#awardearned').attr('checked',true); 
			}*/
			$( "#noScoutSelectedDialog" ).dialog({
					resizable: false,
					autoOpen: false,
					width: 340,
					height:160,
					modal: true,
					buttons: {
						"OK": function() {
							$( this ).dialog( "close" );
							return false;
						}
					}
			});
			$('#awardawardedcheckbox').click(function(mystuff) 
            {
            	//make sure at least one boy is selected and we have scouts (not when a boy is logged in)
                if ($('.scoutName:checked').length==0 && $('.selectedscout').length==0 && $('.scoutName').length>0)
				{
					$( "#noScoutSelectedDialog" ).dialog("open");
					return false;
				}
				else
				{
	                var selectedbox = this;
					$.get('awardawarded.html',{ischecked: selectedbox.checked}, function(data) {});
				}
            });
			$('#awardpurchasedcheckbox').click(function(mystuff) 
            {
            	//make sure at least one boy is selected and we have scouts (not when a boy is logged in)
                if ($('.scoutName:checked').length==0 && $('.selectedscout').length==0 && $('.scoutName').length>0)
				{
					$( "#noScoutSelectedDialog" ).dialog("open");
					return false;
				}
				else
				{
	                var selectedbox = this;
					$.get('awardpurchased.html',{ischecked: selectedbox.checked}, function(data) {});
				}
            });
			
            $('.requirementscheckbox').click(function(mystuff) 
            {
            	//make sure at least one boy is selected and we have scouts (not when a boy is logged in)
                if ($('.scoutName:checked').length==0 && $('.selectedscout').length==0 && $('.scoutName').length>0)
				{
					$( "#noScoutSelectedDialog" ).dialog("open");
					return false;
				}
				else
				{
	                var selectedbox = this;
	                if(selectedbox.checked===false)
	                {
	                	if(!confirm("If you continue you will lose information about this requirement"))
	                	{
	                		return false;
	                	}
	                }
	                
	                $.get('requirementpassedoff.html',{requirementConfigId: $(this).val(), ischecked: selectedbox.checked}, function(data) {
	                    theResult = eval('('+data+')'); //get the JSON data
	                    var date = theResult.dateCompleted;
	                    var scoutsEarned = theResult.count;
	                    var awardComplete = theResult.awardComplete;
	                    if(date!==undefined)
	                    {
	                    	//$(this).parent().nextAll().last().html(date);
	                    	selectedbox.parentNode.nextSibling.nextSibling.value=date;
	                    }
	                    else
	                    {
	                    	//clear the date
	                    	//$(this).parent().nextAll().last().html("");
	                    	selectedbox.parentNode.nextSibling.nextSibling.value="";
	                    }
	                    if(scoutsEarned==0)
	                    {
	                    	//$(this).prev().html(""); 
	                    	if(selectedbox.previousSibling !== undefined && selectedbox.previousSibling !== null)
	                    	{
	                    		selectedbox.previousSibling.innerHTML="";
	                    	}
	                    }
	                    else
	                    {
	                    	//$(this).prev().html(scoutsEarned);
	                    	if(selectedbox.previousSibling !== undefined && selectedbox.previousSibling !== null)
	                    	{
	                    		selectedbox.previousSibling.innerHTML=scoutsEarned;
	                    	}
	                    }
	                    if(awardComplete)
	                    {
	                    	//alert("Congrats on completing the award!");
	                    	//TODO could refresh here, to move the in progress award to earned!
	                    }
	                    var currentAwardName = $('#awardname').html();
	                    if(selectedbox.checked===true)
	                    {
		                    if (currentAwardName=="Wolf" || currentAwardName=="Bear")
			               	{
			               		if(!isNaN(selectedbox.parentNode.nextSibling.innerHTML.charAt(0)))
			               		{
			               			// its starts with a number
			               			var numberedRequirements = 0
			               			$('.requirementscheckbox:checked').parent().next().each(function(){
			               				if(!isNaN($(this).html().charAt(0)))
			               				{
			               					numberedRequirements+=1;
			               				}
			               			});
			       					if(numberedRequirements%3==0)
			               			{
			               				var beadMsg = "Congratulations you just earned your first "+currentAwardName+" bead!";
			               				if(numberedRequirements/3==2) {
			               					beadMsg = "Congratulations you just earned your second "+currentAwardName+" bead!";
			               				}
			               				else if(numberedRequirements/3==3) {
			               					beadMsg = "Congratulations you just earned your third "+currentAwardName+" bead!";
			               				}
			               				else if(numberedRequirements/3==4) {
			               				    beadMsg = "Congratulations you just earned your fourth bead and completed your "+currentAwardName+" Rank requirements";
			               				}
			               				alert(beadMsg);
			               				location.reload(); //refresh the page, to show the bead
			               			}
			               			else
			               			{ //just let the user know how many so they don't have to count
			               				var countMsg = '<span class="requirementCount" title="You have passed off '+numberedRequirements+' numbered requirements">'+numberedRequirements+'</span>';
										$(mystuff.currentTarget.parentNode).prepend(countMsg);
			               			}
			               		}
			               	}
			               	else if(currentAwardName=="Wolf Electives" || currentAwardName=="Bear Electives")
			               	{
		               			var requirementCount = 0
		               			$('.requirementscheckbox:checked').parent().next().each(function(){
		               				requirementCount+=1;
		               			});
		       					if(requirementCount%10==0)
		               			{
		               				var awardName = ($('#Bear').length>0?"Bear":"Wolf")
		               				var arrowpointMsg = "Congratulations you just earned your first Arrow Point!";
		               				if(requirementCount/10==2) {
		               					arrowpointMsg = "Congratulations you just earned your second Arrow Point!";
		               				}
		               				else if(requirementCount/10==3) {
		               					arrowpointMsg = "Congratulations you just earned Arrow Point number "+requirementCount/10+"!";
		               				}
		               				else if(requirementCount/10==4) {
		               					arrowpointMsg = "Wow you are amazing, "+requirementCount+" requirements and Arrow Point number "+requirementCount/10+"!";
		               				}
		               				else if(requirementCount/10==5) {
		               					arrowpointMsg = "You've completed "+requirementCount+" requirements and Arrow Point number "+requirementCount/10+"!";
		               				}
		               				else if(requirementCount/10==6) {
		               					arrowpointMsg = "Wow you are amazing, "+requirementCount+" requirements and Arrow Point number "+requirementCount/10+"!";
		               				}
		               				else if(requirementCount/10==7) {
		               				    arrowpointMsg = "Congratulations you just earned all your "+currentAwardName + " Arrow Points!";
		               				}
		               				alert(arrowpointMsg);
		               				//TODO when this line is uncommented, LazyInstantiation session closed????
		               				//location.reload(); //refresh the page, to show the arrowpoint in the earned section
		               				location = 'selectElective.html?awardName='+awardName+' Electives';
		               			}
		               			else if ($('.scoutName').length!=0 && ($('.scoutName:checked').length==1 || $('.selectedscout').length==1))
		               			{ 
		               				//if there is one scout - just let the user know how many so they don't have to count
		               				var countMsg = '<span class="requirementCount" title="You have passed off '+requirementCount+' requirements">'+requirementCount+'</span>';
									$(mystuff.currentTarget.parentNode).prepend(countMsg);
		               			}
			               	} 
			               	else if(currentAwardName=="Faith in God")
			               	{
			               		if(selectedbox.parentNode.nextSibling.innerHTML.indexOf('Religious')>=0 
			               			&& $('.religiousKnot').length==$('.religiousKnot:checked').length)//selectedbox.parentNode.nextSibling
			               		{
				               		alert("Congratulations, you just earned your religious knot!");
				               		location.reload(); //refresh the page, to show the religious knot in the earned section
			               		}
			               	}
			            }                                   
	                }); //end $.get('requirementpassedoff.html'

					var checked_status= this.checked;
					if($('#awardearned').attr('checked')==true && this.checked == false){
						$('#awardearned').attr('checked', false);
					}
					else if($('#awardearned').attr('checked')==false && this.checked == true){
						//check all siblings if all are checked check parent
						if($('.requirementscheckbox:checked').length==$('.requirementscheckbox').length)
						{
							$('#awardearned').attr('checked',true); 
						}
					}
				}
              });//end $('.requirementscheckbox')
 				/*$('#awardearned').click(function(){
				var checked_status = this.checked;
				$('.requirementscheckbox').each(function()
				{
					this.checked = checked_status;
				});
			});
			*/
			if($('#awardname').html()=="Faith in God")
			{
				$('.requirementtext').each(function(i, val) {
				  //var newRequirement = val.innerHTML.replace('%%%', '<span class="religiousKnot">&nbsp;&nbsp;&nbsp;&nbsp;</span>');
				   if(val.innerHTML.indexOf('%%%')>=0)
				   {
				   		var newRequirement = val.innerHTML.replace('%%%', '<img src="images/awards/dtg/Religious Knot2.png" alt="Religious Knot Image" title="This is required for the religious knot">');
				  		val.innerHTML = newRequirement;
						var selectedbox = $(this).prevAll().last().find('.requirementscheckbox').addClass('religiousKnot');				  
				   }
				});
			}
		});
			
	suppressAjaxSpinner = false
	$(function() {
        $("input:submit, a.buttonAnchor, a.submitAnchor, button").button();
        $(document).ajaxStart(function(){ if (!suppressAjaxSpinner){ $("#spinner").show()}});
        $(document).ajaxStop(function(){ $("#spinner").hide()});
        $(document).ajaxError(function(){ $("#spinner").hide()});
    });
			
</script>
<style>
.religiousKnot
{
    background: url("images/awards/dtg/Religious Knot.png") no-repeat;
    background-size:80px 60px;
}
#noScoutSelectedDialog { display: none; }
</style>
