/**
	from the layout.jsp in the tiles directory
*/

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
		else if($('#Star').length != 0){
			$('a', '#nav-starrank').addClass('active');
			//go get all the required meritbadges for this scout, and fill in the blanks for this requirement
			$.get('getRankMeritBadges.html', function(data) {
				if(data.length>0)
				{
					$(".requirementtext:contains(3. Earn 6 merit badges)").html(data);
				}
			});
		}
		else if($('#Life').length != 0){
			$('a', '#nav-liferank').addClass('active');
			//go get all the required meritbadges for this scout, and fill in the blanks for this requirement
			$.get('getRankMeritBadges.html', function(data) {
				if(data.length>0)
				{
					$(".requirementtext:contains(3. Earn five more merit badges)").html(data);
				}
			});
		}
		else if($('#Eagle').length != 0){
			$('a', '#nav-eaglerank').addClass('active');
			//go get all the required meritbadges for this scout, and fill in the blanks for this requirement
			$.get('getRankMeritBadges.html', function(data) {
				if(data.length>0)
				{
					$(".requirementtext:contains(3. Earn a total of 21 )").html(data);
				}
			});
		}
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
	    
	    /*$('.requirementscheckbox').each(function(){
			$(this).prop("indeterminate", true);
		});*/
	});