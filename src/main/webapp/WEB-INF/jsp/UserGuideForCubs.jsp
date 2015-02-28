<link type="text/css" href="css/jquery-ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.11.2.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.youtubepopup.min.js"></script>

 <script type="text/javascript">
 	$(function () {
		$.fn.YouTubePopup.defaults.fullscreen = 0;
		$.fn.YouTubePopup.defaults.color1 = 'CCCCCC';
	});
	$(document).ready(function()
	{ 
		$('#username').focus();	
		$('#about').click(function(){
                   window.open('scoutTraxUserGuide.html', 'About ScoutTrax', 'WIDTH=800,HEIGHT=800,resizable=yes,scrollbars=yes,menubar=no,titlebar=no'); w.focus(); return false;
           });	
		$.fn.YouTubePopup.defaults.fullscreen = 1;
        $('a.youtube').YouTubePopup({ 
     	   	autoplay: 1, 
        	hideTitleBar: true,
        	clickOutsideClose: true,
        	showBorder: false
         });
	});
</script>


<style>
#scouttraxlink {
	font-size: 1.3em;
	color: red;
}
body {
	background: white;
	color: black;
	font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
	-webkit-font-smoothing: antialiased;
	font-smoothing: antialiased;
    text-shadow: 1px 1px 1px rgba(0,0,0,0.004);
}
p {
	font-size: 1.0em;
}
ul {
    font-size: 1.0em;
}
h1 {
	text-align:center;
	color: #c22;
	padding-bottom: 0;
	margin-bottom: -20px;
	font-size: 2em;
}
h2 {
	font-size: 1.7em;
}
.subtitle {
	font-size: 1.2em;
}
h3 {
	font-size: 1.4em;
}
h4 {
	font-size: 1.3em;
}
h5 {
	font-size: 1.2em;
	padding-left: 40px;
}
.center {
	text-align: center;
}
#toc {
    background-color: #A9F9F9;
    border: 1px solid #AAAAAA;
    font-size: 95%;
    padding-left: 35px;
    padding-bottom: 10px;
    width: 14em;
}
#watchadd {
	position: absolute;
	left: 50%;
	top: 10.5em;
	z-index: 4;
	}
#play {
	padding: 3px 0 0 5px;
}
.middle {
	margin-left: auto;
    margin-right: auto;
}
</style>

<div class='center'>
	<img border=0 width=600 height=340 src="images/Scout_Trax_Business3.png">
	
	<a id='watchadd' class="youtube" href="#" rel="Os1gLeAFeGY" title="Watch the ad">
		<img id="play" alt="Play" src="images/play.png" /></p></a>
	 
	<table class='middle'>
		<tr>
			<td>
				<a class="youtube" href="#" rel="W2fA1RSoSHk" title="Getting Started">
					<img alt="Getting Started" src="images/gettingstarted.png"></a>
			</td>
			
			<td>
				<a class="youtube" href="#" rel="hN9LmH3xmHY" title="Features of Scout trax">
					<img alt="About Scouting" src="images/features.png"></a>
			</td>
			<td>
				<a class="youtube" href="#" rel="oE7emvBDwys" title="Advanced features for leaders">
					<img alt="Features for Leaders" src="images/extrafeatures.jpg"></a>
			</td>
		</tr>
	</table>
</div>
<h1>ScoutTrax.org User Guide</h1>
<p class='center subtitle'>for Cub Leaders, Parents and Scouts</p>
<div><ol id="toc"></ol></div>
<h2>Welcome</h2>

<p>Welcome to <a href="http://www.scouttrax.org/">ScoutTrax.org</a>
, an innovative Cub Scout tracking tool with features you
won't find together in any other scout tracking software. ScoutTrax was created
by software professionals who do this for free because they are passionate
about scouting.</p>

<h4>ScoutTrax for Cubs is: </h4>
<h5>Simple and free, one-click tracking.</h5>

<ul type=disc>
 <ul type=circle>
  <li >View a Cub Scout's complete record on a single page. </li>
  <li >Don't worry about whether the next pack or sponsoring
      organization will continue to pay for the program. It is free and will
      stay free. </li>
 </ul>
</ul>

<h5>Accessible to Cub Scouts, parents and leaders on the web,
all the time.</h5>

<ul type=disc>
 <ul type=circle>
  <li>ScoutTrax gives the power to the Cub Scout and his parents
      to be more involved with his progress. </li>
  <li>Lost handbook? 
      <p>No worries.  The online
      record is safe and permanent. <i>Note:</i>ScoutTrax is not designed to replace
      all the information in the handbooks. 
      It is, however, a secure and efficient way to track progress
      and save records.)</p>
      <p>Like the BSA, we recommend that a physical copy of all scout records be kept.</p> 
      </li>
  <li>ScoutTrax keeps track of information not included in
      the handbooks, like requirements for belt loops, pins, and religious emblems.</li>
 </ul>
</ul>

<h5>Facilitates communication between scouts, parents and
leaders.</h5>

<ul type=disc>
 <ul type=circle>
  <li>Leaders can email any or all members of their den or
      pack from one simple location to remind them of upcoming events.</li>
  <li>Leaders, parents and cub scouts all have access to
      their records and provide checks and balances for entered data. </li>
  <li>When a leader or parent signs off a requirement, the
      record is viewable by both leaders and parents, updated in real time.</li>
  <li>When a record is updated an email automatically
      notifies the parent.</li>
 </ul>
</ul>

<h5>Safe, Secure and Private </h5>

<ul type=circle>
  <li>Data is kept safe by regular offsite data backups. </li>
  <li>Privacy is protected by encrypted passwords and other
      security measures. </li>
</ul>
<p> 
<h2>Getting Started</h2>
<h5>Packs that belong to the same organization as a Troop that is already registered - Start here!</h5>
<ol>
<li> If the same person is maintaining records for both cubs and older scouts, two separate accounts (with separate user names and passwords) are required - one for cubs, and another for Boy, Varsity and Venture Scouts. (The latter three can be tracked together.)  Both accounts, however, can use the same email. </li>
<li> The Scoutmaster or other leader should login as usual. </li>
<li> On your troop manage screen, near the bottom locate the "Invite Cubs" button. Click this button and enter the leader name, pack and email address. Then invite this leader to join ScoutTrax.org.</li>
<li> The user will receive an email inviting her to join. If an email is not found, check the email spam folder.</li>
<li> Now you are ready to start adding boys, and leaders and inviting them to join you in helping these boys achieve!</li>
</ol>
<h5>Packs Independent of a Troop - Start here!</h5>
<ol>
<li> There are videos that will help you get started with ScoutTrax for Cubs. They are geared specifically to the older boys but the basic functionality is the same. Cub specific videos are in production.</li>
<li> After viewing the videos, or if you choose to skip the videos, click on the "Start Now".</li>
<li> This will take you to the page where you can start registration and access the "Cub User Guide" near the bottom of the page. We recommend that you click on the "Cub User Guide click here" now, so you can use this for reference.</li>
<li> When ready select the "Not registered yet? click here".</li>
<li> Follow the instructions on the screens to register.</li>
</ol>

<p> 
<span style='color:red'>Watch the &quot;Getting
Started&quot; video on the home page.</span></p>
<a href="http://www.scouttrax.org/">
		<img alt="Getting Started" src="images/gettingstarted.png"></a>
<div style='color:red'>It will step you through
the following:</div>
<h5>Create a ScoutTrax account </h5>

<ul type=disc>
 <ul type=circle>
  <li>Any Cub Scout leader may register
from the home page of ScoutTrax.org. Enter information including state, city,
council, charter organization, group, unit type, and unit number. Drop-down
menus will help you along the way. If
you register as a Wolf unit, each Cub Scout you add to the records will, by
default, be listed as a Wolf. This can
easily be changed if you wish to create records for cubs from other dens. Continue
to the next screen until registration is complete.</li>

<li>To complete account activation,
check your email for a link from ScoutTrax. 
By clicking on this link, you will automatically be sent to the Pack
Management page. Now you are ready to add
scouts. </li>

<li>Click on "Add Scout" and enter
information for each Cub Scout in your den/pack.</li>

<li>If you have not added all the cubs
in your pack, (i.e. if you are a den leader who does not have the information
for the other dens) invite other den leaders to add their cubs'
information. You can do this from the same
"Pack Management" page (see below). When
they respond to the invite, ScoutTrax will recognize them as being in the same
pack as you. This way all scouts in your pack can be viewed by any leader as
needed. Also, when a Cub Scout advances
to another den, his records are already viewable by his new leaders.</li>
</ul>
</ul>
<h2>Using the dashboard</h2>

<p><b>Along the left
side is the <i>Dashboard</i> with icons to
help you navigate.</b></p>

<p class='center'>
<img border=0 width=792 height=360 src="images/userguide/cubdashboard2.png">
 </p>

<h5>Click on your Pack icon to manage pack information.</h5>

<p class='center'>
<img border=0 width=135 height=96 src="images/userguide/menupack.jpg">
 </p>

<ul type=circle>
  <li>View all cub scouts and leaders in your pack.</li>
  <li>Import scouts from Council's advancement record</li>
  <li>Look up a cub scout's demographic information such as:
  	<ul type=disc>
      <li>phone number</li>
      <li>address</li> 
      <li>email address</li> 
      <li>date of birth.</li>
      <li>last login date.</li>
      <li>BSA Member Id.</li>
      <li>Leadership position</li>
    </ul>
  </li>
  <li>Leaders can:
	  <ul type=disc>
	      <li>Add, edit or remove scouts or other leaders</li>
	      <li>Transfer Scout records to a new unit.</li> 
	      <li>Send email invitations to parents and leaders to join
      ScoutTrax. </li>
	    </ul>
  <li>Personalize by adding a picture of the scout.</li>
 </ul>

<h5>Click on the book icon to manage
advancement records and all tracking.</h5>

<p class='center'>
<img border=0 width=135 height=96 src="images/userguide/menubook.jpg">
 </p>
<p class='center'>
<img border=0 width=792 height=360 src="images/userguide/cubscoutbook.png">
 </p>
<ul type=circle>
  <li>Select a cub scout from the <b>roster</b> on the upper right side.</li>
  <li>When a single cub scout is selected, images of the <b
     >awards he has earned</b> appear
      across the top of the screen.</li>
  <li>Images of his <b>awards
      in progress</b> are displayed in the far right column.</li>
  <li>Click on <b>tabs </b>along
      the top of the "book" to view ranks, Arrow of Light, belt loops, pins,
      and religious emblems.  </span>Inside the wolf
      and bear tabs, is a <b>link to electives
      </b>(arrow points.)</li>
  <li>The "book" also displays a photo of the Cub Scout and
      his personal information. Click on his photo for a quick report of his
      progress.</li>
</ul>

<h5>Click on the reports icon to view
progress. Customize by changing start and end dates, and selecting the Cub Scout(s).</h5>

<p class='center'>
	<img alt='Report' border=0 width=135 height=96 src="images/userguide/menureport.jpg">
</p>
<ul type=circle>
	<li>Reports can be customized by date and selection of cub scouts. </li>
	
	<li>Prepare for an upcoming Pack Meeting
	by viewing the entire pack and the awards earned since the last pack
	meeting.</li>
	
	<li>To see all completed awards for a group of boys set dates to greater than four years, and it will show you a full report</li>
	
	<li>View an individual Cub Scout's
	record during a specified period of time.</li>
</ul>

<h5>Click on the email icon to communicate with your pack.</h5>

<p class='center'>
<img border=0 width=115 height=96 src="images/userguide/menumail.jpg">
 </p>

 <ul type=circle>
  <li>Specify which dens in your pack you want to email, and
      whether it will be sent to parents, leaders, or both.</li>
  <li>Send email to an individual leader or parent</li>
 </ul>

<h5>Click on the Training icon to keep track of leaders training record.</h5>
<p class='center'>
<img border=0 width=135 height=96 src="images/book/training.png">
 </p>

 <ul type=circle>
  <li>Simply enter the date that training took place.</li>
  <li>Leaders can update training dates and parents and boys can view the training record.</li>
 </ul>

<h2>Additional Features </h2>

 <ul type=circle>
  <li><b>Automatic emails</b>
      are sent to a parent when updates are made to his son's records.</li>
  <li><b>Cub Scout birthday reminders</b> are automatically e-mailed to the den leaders. In
      some dens, the deadline for rank advancement is a birthday. The leaders
      are emailed 30 days before the Cub Scout's birthday and again the day of his
      birthday. This will help the leader remember to review that cub's record
      to make sure he is ready to progress. It also gives the new leaders
      advanced notice so they can prepare for the incoming scout. Note:</i> For obvious reasons, this
      feature only works if a birth date has been entered in the Cub Scout's
      personal information.</li>
  <li><b>Personalize</b>
      a Cub Scout's account by uploading his photo. </li>
 </ul>

<h2>Frequently Asked Questions</h2>

<h4>What if ScoutTrax does not have a
feature I would like to use?</h4>

<p>If there is a popular new feature
people want, we'll add it! Just click on the <b>feedback</b> link in the upper
right hand corner of the software, or comment on <b>facebook.com/ScoutTrax</b>
to report problems, ask questions or give suggestions. You will be happy to
know feedback is reviewed frequently, and any problems reported are usually
handled within 48 hours.</p>

<h4>What if a Cub Scout moves to another pack? </h4>

<p>ScoutTrax
makes record transfer easy.  If the Cub Scout's new pack is not using ScoutTrax, the new pack
is given the opportunity to create an account in ScoutTrax. They can begin to
use ScoutTrax or simply retrieve the Cub Scout's information. </p>

<h4>What if I send an invite to a leader or scout and they don't see it?</h4>
<p>The first email from ScoutTrax.org sometimes get sent to a spam folder. If you send 
and invite and the user does not see it, make sure to have them check their email spam folder.</p>
<h4>How many Cub Scouts can ScoutTrax have in one pack? </h4>
<p>Use it for one Cub Scout or one hundred. </p>

<h4>What if I forget my password?</h4>

<p>Password recovery is available when a scout or leader
has forgotten it. The password is sent to the e-mail that ScoutTrax has on
record for that user. </p>

<h4>What if I cannot find the Religious Emblem of my faith?</h4>

<p>Religious Emblem tracking for any
particular faith will be <b>added upon
request</b> by clicking on the "Feedback" link. Currently the &quot;Faith&quot;
tab includes "Faith in God" progress in the requirements of the Church of Jesus
Christ of Latter-Day Saints. </p>

<h4>How do I pass off a requirement for several Cub Scouts at once?</h4>

<p>From the "book" choose any number of
cub scouts from the "roster" at the right. Pass off the same requirement for
all selected cub scouts by clicking on a requirement's checkbox. This reduces
time when recording the same data for more than one scout, such as a
requirement passed off by several scouts at a den meeting. </p>

<h4>How does a parent update a Cub Scout's achievements?</h4>

<p>Each parent can have a personalized
account, which allows him to see and update his child's progress at any time. A
leader must first send an invitation to a parent from the "Pack" page by
clicking on the check box next to the Cub Scout's name; then click &quot;Send
Invitation.&quot; Once a parent has accepted the invitation and established a
username and password, he can access the record at any time. For privacy reasons, parents cannot view
other scouts' records, like a leader can. </p>

<h4>How is accuracy safeguarded?</h4>

<p>When a cub scout's record has been
updated, an email automatically notifies the parent with a specific list of changes
made. The parent can review this list to
ensure accuracy.</p>

<p>When a leader views the records,
requirement dates will be outlined in red if they were recorded by someone other
than the leader. If there is ever a
question, the leader can simply phone or email the parent for verification. </p>

<script type="text/javascript">
$(document).ready(function()
{ 
	$("#toc").append('<p class="center"><b>Contents</b></p>')
	$("h2, h3").each(function(i) {
	    var current = $(this);
	    current.attr("id", "title" + i);
	    $("#toc").append("<li><a id='link" + i + "' href='#title" +
	        i + "' title='" + current.attr("tagName") + "'>" + 
	        current.html() + "</a></li>");
	});
});
</script>
