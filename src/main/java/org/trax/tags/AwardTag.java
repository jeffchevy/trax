package org.trax.tags;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.jfree.util.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Leader;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.RequirementConfig;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;

public class AwardTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private Format formatter = new SimpleDateFormat("MM/dd/yyyy");
	private static Map<String, Integer> splitMap = new HashMap<String, Integer>();

	
	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();
		
		try
		{
			Award award = getAward();
			if (award == null )//no scout or many are selected
			{
				return SKIP_BODY;
			}
			Scout scout = getScout();
			if (scout == null )//no scout or many are selected
			{
				return SKIP_BODY;
			}
			Map<Long, Long> requirementConfigIdAndCount = getRequirementConfigIdAndCount();
			String pinLink="";
			String electiveLink="";
			String worksheetLink="";
			String requirementsLabel = "Requirements";
			String activePageId=AwardConfig.AWARDS;
			
			boolean isRank = false;
			AwardConfig awardConfig = award.getAwardConfig();
			activePageId = awardConfig.getTypeName().replace(' ', '_');

			if(awardConfig.getLink()!=null && scout.getUnit().isCub()==false)
			{
				worksheetLink = "<a id='worksheetLink' title='Click here to view "+awardConfig.getName()+" worksheet' target='_blank' href='"+awardConfig.getLink()+"'>" +
						"<img id='awardimage' src='images/book/worksheet.png' alt='View Worksheet'></a>";
			}

			if (awardConfig instanceof RankConfig)
			{
				if(awardConfig instanceof CubRankConfig && scout.getUnit().isCub()==true)
				{
					if (awardConfig.getName().equals("Wolf") || awardConfig.getName().equals("Bear"))
					{
						electiveLink = "<div><a class='electiveLink' href='selectElective.html?awardName="+awardConfig.getName()+" Electives'>" +
							"Click here for Arrow Point Trail (Electives)</a></div>" +
						"<a href='selectElective.html?awardName="+awardConfig.getName()+" Electives'>" +
								"<img height='40' alt='"+awardConfig.getName()+" Electives' title='"+awardConfig.getName()+" Electives' src='images/cub/electives/Electives1.png'></a>";
					}
					else if (awardConfig.getName().equals("Tiger Cub"))
					{
						electiveLink = "<div><a class='electiveLink' href='selectElective.html?awardName="+awardConfig.getName()+" Electives'>" +
						"Click here for Tiger Cub Track Bead (Electives)</a></div>";
					}
					else if (awardConfig.getName().equals("Webelos Award"))
					{
						electiveLink = "<div><a class='electiveLink' href='cubBadges.html?type=Activity Badges'>" +
							"Click here for Activity Badges</a></div>"+
						"<a href='cubBadges.html?type=Activity Badges'>" +
							"<img height='50' alt='Activity Badges' title='Activity Badges' src='images/cub/activitybadges/Aquanaut-link.png'></a>";
					}
				}
				else if(scout.getUnit().isCub()==false)
				{
					isRank = true; //boy scout ranks only
					if (awardConfig.getName().contains("Palm"))
					{
						activePageId = "Palms";
					}
				}
			} 
			else if(awardConfig instanceof BeltLoopConfig)
			{
				requirementsLabel = "Belt Loop Requirements";
				electiveLink = "<div class='row'><div id='pinLinkDiv'>" +
						"<a id='pinLink' href='selectPin.html?awardName="+awardConfig.getName()+"'>Click here to view "+awardConfig.getName() + " Pin requirements.</a>" +
								"</div></div>";
			}
			else if(awardConfig instanceof PinConfig)
			{
				requirementsLabel = "Pin Requirements";
			}
			
			writer.write("<span id='"+activePageId+"'></span>\n");// Column 1
			writer.write("<div id='page1' class='cell'>\n");// Column 1
			
			writer.write("	<div class='table'>\n"); //for all of column 1
			String genericImage = scout.getUnit().isCub() ? "cubscout" :"scout";
			String genericImageTag = "<img class='dropshadow' alt='"+scout.getFullName()+"' src='images/"+genericImage+".png'>";
			String scoutImageTag = "<img class='dropshadow' alt='"+scout.getFullName()+"' src='scoutimage.html?id="+scout.getId()+"'>";
			String imageTag = (scout.getProfileImage()==null)?genericImageTag:scoutImageTag;
			String scoutImageLink = scout.isSelected() || scout.isChecked()?
				"<a id='scoutimage' href=#>"+imageTag + "<span style='position:relative;left:-5px;color:white; top:-1.3em;font-weight:bold;font-size:10px;text-shadow: 4px -2px 6px #000000;'>Click for Report</span></a>":imageTag;

			
			writer.write(
					"<div class='row' id='scout_info'>" +
					"	<div class='table'>" + 
					"		<div class='row'>" +
					"			<div class='cell'>" +
					scoutImageLink +
					"			</div>" +
					"			<div id='awardTitles' class='cell'>" +
					"				<p id='awardname'>"+awardConfig.getName()+"</p>" +
					"				<p id='subtitle'>"+requirementsLabel+"</p>" +
					worksheetLink +
					electiveLink +
					"			</div>" +
					"			<div id='awardimagediv' class='cell'>" +
					"				<img id='awardimage' alt='"+awardConfig.getName()+"' src='"+awardConfig.getImageSource()+"'>" +
					"				<div id='previousnextaward'>" +
					"					<a href='prevAward.html?awardConfigId="+awardConfig.getId()+"'><img alt='Show Previous Badge' title='Show Previous Badge' src='images/resultset_previous.png'></a>" + //TODO images not showing up???
					"					<a href='nextAward.html?awardConfigId="+awardConfig.getId()+"'><img alt='Show Next Badge' title='Show Next Badge' src='images/resultset_next.png'></a>" +
					" 				</div>" +
					"			</div>" +
					" 		</div>" + //row
					" 	</div>" + //table
					"</div>"); //end scout info row
			
			writer.write("	<div class='row'>\n" +
					"			<div class='cell'>\n" +
					"				<div class='table'>\n"); //requirements column 1
			boolean canSelect = (scout.isSelected() || scout.isChecked()) || requirementConfigIdAndCount!=null;//?"":" disabled='disabled' ";
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//show inprogress or earned checkboxes if leader,
			// cubs can do it because there is no scout Master conference or board of review
			// or if scout and its not a Rank 
			if (user instanceof Leader || user.getUnit().isCub() || !(award.getAwardConfig() instanceof RankConfig))
			{
				writeRequirementHeaderRow(writer, award, scout, requirementConfigIdAndCount, canSelect);
			}
			List<Scout> scouts = (List<Scout>)pageContext.getSession().getAttribute("scouts");
//			int totalCount = scouts==null?1:scouts.size();
			
			//crazy code to make pages even for Ranks
			Integer requirementSplit = splitMap.get(awardConfig.getName());
			List<RequirementConfig> rcs = awardConfig.getRequirementConfigs();
			requirementSplit = requirementSplit == null? (rcs.size()<=10? rcs.size():-1 ):requirementSplit;
			
			int requirementIndex=0;
			boolean onlyOnePage = true;
			for (RequirementConfig requirementConfig : rcs)
			{
				//if not specified above just split it half way through
				int spaceForEarnedInprogress = 2;
				requirementSplit = requirementSplit==-1?(awardConfig.getRequirementConfigs().size()/2)-spaceForEarnedInprogress:requirementSplit;
				if ((requirementIndex++) == requirementSplit )
				{
					endP1StartP2(writer, scout, requirementConfigIdAndCount, scouts, false, pinLink);
					if (awardConfig.getSponsors()!=null && ! awardConfig.getSponsors().isEmpty()) {
					    writer.write(" <div class='row'>\n" 
					            + "  <div class='cell'>\n" 
			                    + "    <a id='supporter' href='"+awardConfig.getSponsors().iterator().next().getLink()+"'>"
			                    + "      <img class='dropshadow' alt='supporter' src='"+awardConfig.getImageSource()+"'></a>"
			                    + "</div>"
			                    + "</div>");
                    } 

					onlyOnePage = false;
				}
				
				//write row
				writer.write("<div class='row'>");

				String checked = "";
				String dateCompletedString = "";
				String signature = "";
				long count = 0; 
				String multipleBoysText = "";
				
				String indeterminateClass = "";
				if (requirementConfigIdAndCount == null)
				{
					//only one scout is selected
					if (scout.isSelected() || scout.isChecked())
					{
						if(award.getRequirements()!=null)
						{
							for (Requirement requirement : award.getRequirements())
							{
								if (requirement.getRequirementConfig().getId() == requirementConfig.getId())
								{
									checked = "checked='checked' ";
									if (requirement.getDateCompleted()!=null)
									{
										dateCompletedString = formatter.format(requirement.getDateCompleted());
										try {
											signature = requirement.getUser().getFullName();
										} catch (Exception e) {
											Log.warn("Failed to load user from requirement, this only seems to be a problem when deselecting the check all box!");
										}
										break;
									}
								}
							}
						}
						else
						{
							//System.out.println("This award has no requirements "+ awardConfig.getName());
						}
					}
				}
				else
				{
					int totalSelectedCount = getSelectedScoutsCount();
					
					Long achievedCount = requirementConfigIdAndCount.get(requirementConfig.getId());
					count = achievedCount==null?0:achievedCount;
					if (count == totalSelectedCount)
					{
						checked = "checked='checked' "; //only check it if everyone has it already
					}
					else if (count > 0 && totalSelectedCount > 1)
					{
						indeterminateClass = " indeterminate";
					}
					multipleBoysText = "<span class='requirementCount' title='"+count+" of the "+totalSelectedCount+" selected boys have passed off this requirement'>"+count+"</span>";
				}
				
				//Requirements checkbox
				writer.write("<div class='cell'>");
				writer.write(multipleBoysText);
				boolean isAuthorized = user instanceof Leader || !requirementConfig.getLeaderAuthorized();
				if(canSelect && isAuthorized)// && !requirementConfig.getText().endsWith("following:")) // if it ends like this don't include a checkbox
				{
					if (requirementConfig.getText().length()>5 && requirementConfig.getCanSelect()) //so things like "1." don't get their own checkbox
					{
						writer.write("<input id='r"+requirementConfig.getId()+"' class='requirementscheckbox"+indeterminateClass+"' "+checked + " value="+requirementConfig.getId()+" type='checkbox' title='Check to pass off requirement'/>");
						writer.write("<label for='r"+requirementConfig.getId()+"'>&nbsp;</label>");
					}
				}
				writer.write("</div>");// end the cell
				
				writeRequirementText(writer, isRank, requirementConfig);
				if (requirementConfig.getText().length()>5 && requirementConfig.getCanSelect()) //so things like "1." don't get their own checkbox
				{
					if(canSelect) //only show if scout is selected
					{
						String title="";
						String classes=" class='cell datebox' ";
						if (signature != null && !signature.equals(""))
						{
							title=" title='Passed off by "+signature+"' ";
							if(!signature.equals(user.getFullName())) 
							{
								// @scoutmaster conference doublecheck this since it was signed by someone else
								classes=" class='cell datebox doublecheck' ";
								title=" title='Passed off by "+signature+"' ";
							}
						}
						writer.write("<input "+classes+title+" type='text' value='"+dateCompletedString+"'>");
					}
				}
				writer.write("</div>");// end the row
			}
			if(onlyOnePage)
			{
				endP1StartP2(writer, scout, requirementConfigIdAndCount, scouts, true, pinLink);
			}
			writer.write("		</div>"); //end requirements column 2 table
			writer.write("</div>"); //table, cell, row, row2 table, cell, row, end both pages table
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}


	
	
	
	private String getQuote()
	{
		String quote = null;
		String author="Robert Baden-Powell";

		int randomNumber = (int)(Math.random()*9);
		switch (randomNumber)
		{
			case 0:	quote="An individual step in character training is to put responsibility on the individual.";
				break;
			case 1:	quote="Be Prepared... the meaning of the motto is that a scout must prepare himself by previous thinking out and practicing how to act on any accident or emergency so that he is never taken by surprise.";
				break;
			case 2:	quote="We never fail when we try to do our duty, we always fail when we neglect to do it.";
				break;
			case 3:	quote="If ever there were a time when the principles of Scouting were vitally needed, that time is now. " +
					"If ever there were a generation who would benefit by keeping physically strong, mentally awake, and morally straight, " +
					" that generation is the present generation.";
			author="Thomas Monson";
			break;
			case 4:	quote="Leave this world a little better than you found it.";
			break;
			case 5:	quote="Courage, not compromise, brings the smile of God's approval.";
			author="Thomas Monson";
			break;
			case 6:	quote="The 'Aims of Scouting' are character development, citizenship training, and personal fitness.";
			break;
			case 7:	quote="The eight <b>Methods<b> for achieving the 'Aims of Scouting' are:  Patrols, Ideals, Outdoor Programs, Advancement, " +
					"Associations With Adults, Personal Growth, Leadership Development, Uniform";
			case 8: quote="One of our methods in the Scout movement for taming a hooligan is to appoint him head of a Patrol. He has all the " +
					"necessary initiative, the spirit and the magnetism for leadership, and when responsibility is thus put upon him it gives " +
					"him the outlet he needs for his exuberance of activity, but gives it in a right direction.";
			break;
		}
		String text = "";
		if (quote != null)
		{
			text = "<div id='pagequote'>"+quote+"<br><br><strong>"+author+"</strong></div>";
		}
		return text;
	}
	private boolean writeRequirementHeaderRow(JspWriter writer, Award award, Scout scout,
			Map<Long, Long> requirementConfigIdAndCount, boolean canSelect) throws IOException
	{
		if(canSelect) //only show if scout is selected and has priviledges
		{
			//select Award for completion
			boolean hasEarned = award.getDateCompleted() != null;
			boolean hasPurchased = award.getDatePurchased() != null;
			boolean hasAwarded = award.getDateAwarded() != null;
			
			if(!hasEarned && !(award.getAwardConfig() instanceof RankConfig))
			{	
				writer.write("<div class='row'>");
				writer.write("<div class='cell'>");
				String inProgress = (award.getInProgress() == true) ? "checked='checked' ":"";
				writer.write("		<input id='awardinprogress' "+inProgress+"class='requirementcheckbox' type='checkbox' title='Check to pass mark as in progress'/>");
				writer.write("<label for='awardinprogress'>&nbsp;</label>");
				writer.write("</div>");//cell
				writer.write("<div title='Click this checkbox to start working on this award' class='cell awardheader'>In Progress</div><div class='cell'>");
				writer.write("</div>");//cell
				writer.write("</div>");//row
			}
			writer.write("<div class='row'>");
			writer.write("<div class='cell'>");
			
			String earned = hasEarned ? "checked='checked' ":"";
			String purchased = hasPurchased ? "checked='checked' ":"";
			String awarded = hasAwarded ? "checked='checked' ":"";
			
			writer.write("		<input id='awardearned' "+earned+"class='requirementcheckbox' type='checkbox' title='Check to mark award as earned'/>");
			writer.write("<label for='awardearned'>&nbsp;</label>");
			writer.write("</div>");
			writer.write("<div class='cell awardheader'>Earned"
					+"<div><input type='checkbox' "+purchased+" title='Check to mark award as purchased' id='awardpurchasedcheckbox'>"
					+"<label for='awardpurchasedcheckbox'> </label>Purchased</div>" 
					+"<div style=''><input type='checkbox' "+awarded+" title='Check to mark award as awarded' id='awardawardedcheckbox'>"
					+"<label for='awardawardedcheckbox'> </label>Awarded</div></div>");
			
			String dateCompletedString = "";
			String title="";
			String classes=" class='cell' ";//default Classes
			if(award.getDateCompleted()!=null)
			{
				dateCompletedString =  formatter.format(award.getDateCompleted());
				User signOffUser = award.getUser();
				if (signOffUser != null && !signOffUser.equals(""))
				{
					User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
					if(!signOffUser.getFullName().equals(user.getFullName())) 
					{
						// @scoutmaster conference doublecheck this since it was signed by someone else
						classes=" class='cell doublecheck' ";
						title=" title='Passed off by "+signOffUser.getFullName()+"' ";
					}
				}
			}
			writer.write("<input id='awarddatebox' "+title+classes+" type='text' value='"+dateCompletedString+"'>");
			writer.write("</div>");//row
		}
		
		
		return canSelect;
	}

	private void writeRequirementText(JspWriter writer, boolean isRank, RequirementConfig requirementConfig)
			throws IOException
	{
		String text = requirementConfig.getText();
		
		String classes = "cell requirementtext";
		
		//do not do special coloring for Scout Ranks
		if(!isRank)
		{
			if(text.trim().matches("[\\s\\S]*[Dd]o .* of the following[\\s\\S]*")
				|| text.trim().matches("[\\s\\S]*Complete .*the following[\\s\\S]*")
				|| text.trim().matches("[\\s\\S]*[Dd]o.* the following[\\s\\S]*")
				|| text.startsWith("Chapter"))//match  the \s\S match anything including \n and \r add class
			{
				classes+=" doFollowingRequirement";
			}
			else
			if(text.matches("^\\d*\\.[\\s\\S]*")||text.startsWith("Section "))//match any that start with a number followed by a dot(.) the \s\S match anything including \n and \r add class
			{
				classes+=" numberedRequirement";
			}
		}
		writer.write("<div class='"+classes+"'>");
			//replace \n and then replace all areas with more than one space with a non breaking space, this should work for all leading spaces.
		writer.write(""+text.replace("\n", "<br>").replace("  ", "&nbsp;&nbsp;").replace("&nbsp; ", "&nbsp;&nbsp;"));
		
		writer.write("</div>");// end the cell
	}

	private void endP1StartP2(JspWriter writer, Scout scout, Map<Long, Long> requirementConfigIdAndCount,
			List<Scout> scouts, boolean needsQuote, String pinLink) throws IOException
	{
		writer.write("				</div>" + //requirements 1 table 
				pinLink+
				"				</div>" + //cell
				"			</div>" + // row
				"		</div>" + //table
				"	</div>");//+ // end page 1 cell
		writer.write("<div id='page2' class='cell'>");
		writer.write("<div class='table' id='pagetwo'>"); //requirements colum2 table
		if(needsQuote)
		{
			writer.write(getQuote());
		}
	}

	@SuppressWarnings("unchecked")
	private Map<Long, Long> getRequirementConfigIdAndCount() throws IOException
	{
		Object bean = pageContext.getRequest().getAttribute("requirementConfigIdAndCount");
		if (bean == null)
		{
			return null; // this is not a problem, it is only their if choosing multiple boys
		}
		else if (!(bean instanceof Map))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		return (Map<Long, Long>)bean;
	}

	private Award getAward() throws IOException
	{
		Object bean = pageContext.getSession().getAttribute(name);
		if (bean == null)
		{
			String message = "No " + name + " found in the request.";
			pageContext.setAttribute("errorMessage", message);
			//throw new IOException(message);
		}
		else if (!(bean instanceof Award))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			//throw new IOException(message);
		}
		return (Award)bean;
	}
	
	private Scout getScout() throws IOException
	{
		Object bean = pageContext.getSession().getAttribute("scout");
		if (bean == null)
		{
			String message = "No 'scout' found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof Scout))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		return (Scout)bean;
	}
	
	private int getSelectedScoutsCount() throws IOException
	{
		List<Scout> selectedScouts = getSelectedScouts();
		return selectedScouts==null?0:selectedScouts.size();
	}
	
	private List<Scout> getSelectedScouts() throws IOException
	{
		Object bean = pageContext.getSession().getAttribute("scouts");
		if (bean == null)
		{
			String message = "No " + name + " found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof List<?>))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		int selectedCount = 0;
		List<Scout> scouts = (List<Scout>)bean;
		List<Scout> selectedScouts = new ArrayList<Scout>();
		for (Scout scout : scouts)
		{
			if (scout.isChecked())
			{
				selectedScouts.add(scout);
			}
		}
		return selectedScouts;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#getParent()
	 */
	public Tag getParent()
	{
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
	 */
	public void setPageContext(PageContext pageContext)
	{
		this.pageContext = pageContext;
	}

	/**
	 * @return the pageContext
	 */
	public PageContext getPageContext()
	{
		return this.pageContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#setParent(javax.servlet.jsp.tagext.Tag)
	 */
	public void setParent(Tag parent)
	{
		this.parent = parent;
	}

	public void setName(String s)
	{
		name = s;
	}

	public String getName()
	{
		return name;
	}
	//this is used to split the requirements on a page. Since I have not come up with an algorithm to do this for now just look it up in this list
	// if not found here, just split it in half
	static 
	{
		splitMap.put("Scout", 10);
		splitMap.put("Tenderfoot", 10);
		splitMap.put("Second Class", 13);
		splitMap.put("First Class", 10);
		splitMap.put("Star", 7);
		splitMap.put("Life", 8);
		splitMap.put("Eagle", 3);
		splitMap.put("Silver Palm (1st)", 7);
		splitMap.put("Silver Palm (2nd)", 7);
		splitMap.put("Silver Palm (3rd)", 7);
		splitMap.put("Bronze Palm (1st)", 7);
		splitMap.put("Bronze Palm (2nd)", 7);
		splitMap.put("Bronze Palm (3rd)", 7);
		splitMap.put("Gold Palm (1st)", 7);
		splitMap.put("Gold Palm (2nd)", 7);
		splitMap.put("Gold Palm (3rd)", 7);
		splitMap.put("American Cultures", 5);
		splitMap.put("American Heritage", 10);
		splitMap.put("American Labor", 7);
		splitMap.put("Architecture", 10);
		splitMap.put("Athletics", 16);
		splitMap.put("Athletics", 12);
		splitMap.put("Basketry", 8);
		splitMap.put("Bugling", 8);
		splitMap.put("Citizenship in the Community", 11);
		splitMap.put("Citizenship in the Nation", 7);
		splitMap.put("Communications", 12);
		splitMap.put("Computers", 23);
		splitMap.put("Cooking", 26);
		splitMap.put("Cooking (2014)", 33);
		splitMap.put("Electronics", 8);
		splitMap.put("Emergency Preparedness", 26);
		splitMap.put("Environmental Science", 17);
		splitMap.put("First Aid", 14);
		splitMap.put("Fingerprinting", 10);
		splitMap.put("Gardening", 15);
		splitMap.put("Geocaching", 13);
		splitMap.put("Hiking", 7);
		splitMap.put("Insect Study", 15);
		splitMap.put("Lifesaving", 15);
		splitMap.put("Personal Fitness", 29);
		splitMap.put("Pets", 7);
		splitMap.put("Public Speaking", 7);
		splitMap.put("Scuba Diving", 8);
		splitMap.put("Sculpter", 7);
		splitMap.put("Surveying", 8);
		splitMap.put("Swimmnig", 10);
		splitMap.put("Medal of Heroism Award", 1);
		splitMap.put("Historic Trails Award", 5);
		splitMap.put("Honor Medal Award", 1);
		splitMap.put("Medal of Merit Award", 1);
		splitMap.put("Paul Bunyan Woodsman", 7);
		splitMap.put("Firem'n Chit", 8);
		splitMap.put("James M. Stewart Good Citizenship Award", 1);
		splitMap.put("Mile Swim", 5);
		splitMap.put("Spirit of the Eagle", 4);
		splitMap.put("Recruiter Strip", 1);
		splitMap.put("Donor Awareness Patch", 1);
		splitMap.put("OA Distinguished Service Award", 1);
		splitMap.put("Young American", 6);
		splitMap.put("Order of Arrow Tripple Crown", 1);
		splitMap.put("Totin' Chip", 5);
		splitMap.put("Good Turn For America Award", 5);
		splitMap.put("Religious Emblem Award", 4);
		splitMap.put("Tracking", 12);
		splitMap.put("Trust Award", 1);
		splitMap.put("Shooting Sports (Ranger Award elective)", 12);
		splitMap.put("Carpentry", 7);
		splitMap.put("Pathfinding", 8);
		splitMap.put("Signaling", 7);
		splitMap.put("Denali", 8);
		splitMap.put("Arts and Hobbies Bronze Award", 16);
		splitMap.put("Sea Scout Bronze Award", 4);
		splitMap.put("Sports Bronze Award", 7);
		splitMap.put("Silver Award", 5);
		splitMap.put("Gold Award", 14);
		splitMap.put("Religious Life Bronze Award", 11);
		splitMap.put("Bobcat", 11);
		splitMap.put("Tiger Cub", 13);
		splitMap.put("Webelos Award", 12);
		splitMap.put("Arrow Of Light", 14);
		splitMap.put("Family Member", 7);
		splitMap.put("Communicator", 9);
		splitMap.put("Traveler", 6);
		splitMap.put("Fitness", 5);
		splitMap.put("Aquanaut", 4);
		splitMap.put("God and Me", 21);
		splitMap.put("Tiger Elective Adventures 2015", 21);
		splitMap.put("Wolf Elective Adventures 2015", 21);
		splitMap.put("Bear Elective Adventures 2015", 21);
		splitMap.put("AOL Elective Adventures 2015", 21);
	}

}
