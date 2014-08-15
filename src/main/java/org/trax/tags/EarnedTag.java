package org.trax.tags;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.LazyInitializationException;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.Scout;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;

@SuppressWarnings("serial")
public class EarnedTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private int size = 47;

	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();
		
		try
		{
			boolean showAwards = shouldShowAwards();
			if (!showAwards)//no scout or many are selected
			{
				return SKIP_BODY;
			}
			Scout scout = (Scout)pageContext.getSession().getAttribute("scout");
			if (scout == null )//no scout or many are selected
			{
				return SKIP_BODY;
			}
			if (scout.isSelected() || scout.isChecked())
			{
				String htmlLists = "";
				int mbCount = 0;
				int rankCount = 0;
				int awardCount = 0;
				int beltloopCount = 0;
				int pinCount = 0;
				int activityBadgeCount = 0;
				int tigerElectiveRequirementCount = 0;
				int wolfArrowpointCount = 0;
				int bearArrowpointCount = 0;
				int wolfBeadCount = 0;
				int bearBeadCount = 0;
				
				Set<Award> awards = getAwards();
				if(awards==null)
				{
					return SKIP_BODY;
				}
				for (Award award : awards)
				{
					AwardConfig awardConfig = award.getAwardConfig();
					if (award.getDateCompleted() != null)
					{
						String awardName = awardConfig.getName().replace("'", "");
						if (scout.getUnit().isCub())
						{
							if (awardConfig instanceof BeltLoopConfig)
							{
								beltloopCount++;
								htmlLists += createImageLink(awardConfig, "cub/beltloops/"+awardName, awardName);
							}
							else if (awardConfig instanceof PinConfig)
							{
								pinCount++;
								htmlLists += createImageLink(awardConfig, "cub/pins/"+awardName, awardName);
							}
							else if(awardConfig instanceof CubRankElectiveConfig)
							{
								htmlLists += createImageLink(awardConfig, "cub/electives/"+awardName, awardName);
							}
							else if(awardConfig instanceof ActivityBadgeConfig)
							{
								activityBadgeCount++;
								htmlLists += createImageLink(awardConfig, "cub/activitybadges/"+awardName, awardName);
							}
							else if (awardConfig instanceof CubRankConfig)
							{
								rankCount++;
								boolean isWolf = awardConfig.getName().equals("Wolf");
								boolean isBear = awardConfig.getName().equals("Bear");
								if(isWolf || isBear)
								{
									//if they earn the award, they automatically get the beads
									htmlLists += createImageLink(awardConfig, "cub/ranks/PTR"+awardConfig.getName()+"4", awardConfig.getName());
									if(isWolf){wolfBeadCount=4;}else{bearBeadCount=4;}
								}
								htmlLists += createImageLink(awardConfig, "cub/ranks/"+awardName, awardName);
							}
							else if (awardConfig instanceof DutyToGodConfig)//cub and scout
							{
								awardCount++;
								htmlLists += createImageLink(awardConfig, "awards/dtg/"+awardName, awardName);
							}
							else if (awardConfig instanceof CubAwardConfig)//cub and scout
							{
								awardCount++;
								htmlLists += createImageLink(awardConfig, "cub/awards/"+awardName, awardName);
							}
							else 
							{
								//must be a scout award, we don't want to show it --should never happen
								System.out.println("***Not showing "+awardName+" is not valid for a cub scout");
								continue;
							}
						}
						else //ScoutUnit, only show scout awards
						{
							if (awardConfig instanceof BeltLoopConfig
									|| awardConfig instanceof PinConfig
									|| awardConfig instanceof CubRankElectiveConfig
									|| awardConfig instanceof ActivityBadgeConfig
									|| awardConfig instanceof CubRankConfig
									|| awardConfig instanceof DutyToGodConfig
									|| awardConfig instanceof CubAwardConfig)
							{
								continue;
							}
							
							if (awardConfig instanceof RankConfig)
							{
								htmlLists += createImageLink(awardConfig, "Ranks/"+awardName, awardName);
								rankCount++;
								continue;
							}
							else if (awardConfig instanceof BadgeConfig)
							{
								htmlLists += createImageLink(awardConfig, "meritbadges/"+awardName, awardName);
								mbCount++;
							}
							else if (awardConfig instanceof DutyToGodConfig)
							{
								awardCount++;
								htmlLists += createImageLink(awardConfig, "awards/dtg/"+awardName, awardName);
							}
							else if (awardConfig instanceof AwardConfig)
							{
								awardCount++;
								htmlLists += createImageLink(awardConfig, "awards/"+awardName, awardName);
							}
							else 
							{
								//must be a cub award, we don't want to show it 
								System.out.println("***Not showing "+awardName+" we do not show cub awards for scouts");
								continue;
							}
						}
					}
					else //not complete yet
					{
						if(scout.getUnit().isCub())
						{
							if(awardConfig instanceof CubRankConfig)
							{
								boolean isWolf = awardConfig.getName().equals("Wolf");
								boolean isBear = awardConfig.getName().equals("Bear");
								if(isWolf || isBear)
								{
									int requirementCount=0;
									
									for(Requirement r: award.getRequirements())
									{
										//count the numbered requirements
										if(r.getDateCompleted()!=null && Character.isDigit(r.getRequirementConfig().getText().charAt(0)))
										{
											requirementCount++;
										}
									}
									int beadCount = isWolf ? (wolfBeadCount=requirementCount/3):(bearBeadCount=requirementCount/3);
									if (beadCount!=0)
									{
										htmlLists += createImageLink(awardConfig, "cub/ranks/PTR"+awardConfig.getName()+beadCount, awardConfig.getName());
									}
								}
							}
							else if(awardConfig instanceof CubRankElectiveConfig)
							{
								//for these they get an award for every ten (bead, arrowpoint, etc...) 
								/*if(awardConfig.getName().equals("Tiger Cub Electives"))
								{
									htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+
									"'><img src='images/"+subFolder+"/"+awardConfig.getName()+".png' " +
									"height="+size+" title='Click to view "+awardConfig.getName()+" details' alt='"+awardConfig.getDescription()+"' border='none'/></a></li>";
								}
								else*/ 
								boolean isWolfElective = awardConfig.getName().equals("Wolf Electives");
								boolean isBearElective = awardConfig.getName().equals("Bear Electives");
								if(isWolfElective || isBearElective)
								{
									int arrowpointCount = isWolfElective ? (wolfArrowpointCount=award.getRequirements().size()/10):(bearArrowpointCount=award.getRequirements().size()/10);
									int tenCount = arrowpointCount*10;//truncate to the nearest factor of ten, 10,20,30 etc
									if (tenCount!=0)
									{
										htmlLists += createImageLink(awardConfig, "cub/electives/"+awardConfig.getName()+tenCount, awardConfig.getName());
									}
								}
							}
							else if(awardConfig instanceof DutyToGodConfig)
							{
								if(awardConfig.getName().equals("Faith in God"))
								{
									int religiousKnotCount=0;
									for (Requirement requirement : award.getRequirements())
									{
										if (requirement.getDateCompleted()!=null)
										{
											if(requirement.getRequirementConfig().getText().contains("%%%"))
											{
												religiousKnotCount++;
											}
										}
									}
									int totalRkCount=6;//TODO There are 6 requirements for the ReligiousKnot -- This is bad, just not sure how to do it good
									if (religiousKnotCount==totalRkCount)
									{
										htmlLists += createImageLink(awardConfig, "awards/dtg/Religious Knot", awardConfig.getName());
										awardCount++;//just count as an award, does not need its own category
									}
								}
							}
						}
					}
				}
				//hide the carousel if no awards earned
				if(rankCount+mbCount+awardCount+beltloopCount+pinCount+activityBadgeCount+wolfArrowpointCount+bearArrowpointCount > 0)
				{
					writer.write("<span id='earnedlabel'>Earned (" 
							+ getEarnedMessage(rankCount, "Rank")
							+ getEarnedMessage(mbCount, "Merit Badge")
							+ getEarnedMessage(awardCount, "Award")
							+ getEarnedMessage(beltloopCount, "Belt Loop")
							+ getEarnedMessage(pinCount, "Pin")
							+ getEarnedMessage(activityBadgeCount, "Activity Badge")
							+ getEarnedMessage(wolfBeadCount, "Wolf Bead")
							+ getEarnedMessage(wolfArrowpointCount, "Wolf Arrow Point")
							+ getEarnedMessage(bearBeadCount, "Bear Bead")
							+ getEarnedMessage(bearArrowpointCount, "Bear Arrow Point")
							+")</span>");
					writer.write("<ul id='earnedmerritbadges' class='jcarousel-skin-tango'>");
					writer.write(htmlLists);
					writer.write("</ul>");
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	private String createImageLink(AwardConfig awardConfig, String imageName, String awardName)
	{
		String imageLink="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+
		"'><img src='images/"+imageName+".png' " +
		"height="+size+" title='Click to view "+awardName+" details' alt='"+awardConfig.getDescription()+"' border='none'/></a></li>";
		return imageLink;
	}

	private String getEarnedMessage(int count, String name)
	{
		return count==0?"":count+" "+name+(count>1?"s":"")+", ";
	}

	private boolean shouldShowAwards()
	{
		boolean showAwards = true;
		List<Scout> scouts = (List<Scout>)pageContext.getSession().getAttribute("scouts");
		if(scouts != null)
		{
			int checkedCount=0;
			for (Scout scout : scouts)
			{
				if (scout.isChecked() || scout.isSelected())
				{
					checkedCount++;
				}
				if (checkedCount>1)
				{
					break;
				}
			}
			if(checkedCount >1 || checkedCount == 0)
			{
				showAwards = false;
			}
		}
		return showAwards;
	}
	
	private Set<Award> getAwards() throws IOException
	{
		Object bean = pageContext.getSession().getAttribute("awards");
		if (bean == null)
		{
			String message = "No 'awards' found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof Set<?>))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		Set<Award> awards = (Set<Award>)bean;
		try
		{
			//TODO this will throw an exception if the awards cannot be found, 
			// so just swallow it and return null, the tag does not need to draw anything
			if (awards.size()==0)
			{
				return null;
			}
		}
		catch (LazyInitializationException e)
		{
			// TODO Auto-generated catch block
			return null;
		}
		return awards;
	}
	
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
		return (Map)bean;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

}
