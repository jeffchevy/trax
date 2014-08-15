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
import org.trax.model.Scout;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;

@SuppressWarnings("serial")
public class InProgressTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private int size = 37;

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
			if (scout == null)
			{
				return SKIP_BODY;
			}
			if (scout.isSelected() || scout.isChecked())
			{
		        boolean noMoreRanks = false;
		        Set<Award> awards = getAwards();
				if(awards==null)
				{
					return SKIP_BODY;
				}
				
		        String htmlLists = "";
				int mbCount = 0;
		        for (Award award : awards)
				{
		        	if(award.getDateCompleted() == null)
		        	{
						AwardConfig awardConfig = award.getAwardConfig();
						String awardName = awardConfig.getName().replace("'", "");
						String imageSource = "";
						if (scout.getUnit().isCub())
						{
							if (awardConfig instanceof CubRankConfig)
							{
								String unitTypeName = scout.getUnit().getTypeOfUnit().getName();
								if (noMoreRanks == false)
								{
									//cannot start on a wolf unless wolf, bear, unless a Bear, webelos unless webelos, aofLight unless earned webelos
									if(awardName.equals("Bobcat")||awardName.equals("Arrow Of Light")||(awardName.equals(unitTypeName)))
									{
										
										mbCount++;
										imageSource = "images/cub/ranks/"+awardName+".png";
										htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'><img src='"+imageSource+"' height="+(size+10)+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
									}
									noMoreRanks = true;
								}
								continue;
							}
							
							if(awardConfig instanceof BeltLoopConfig)
							{
								imageSource = "cub/beltloops/"+awardName;
							}
							else if(awardConfig instanceof PinConfig)
							{
								imageSource = "cub/pins/"+awardName;
							}
							else if(awardConfig instanceof CubRankElectiveConfig)
							{
								String unitTypeName = scout.getUnit().getTypeOfUnit().getName();
								if (awardName.startsWith(unitTypeName))
								{
									imageSource = "cub/electives/"+awardName;
								}
								else
								{
									continue;//don't leave a space for it, it should not show up at all
								}
							}
							else if(awardConfig instanceof ActivityBadgeConfig)
							{
								imageSource = "cub/activitybadges/"+awardName;
							}
							else if (awardConfig instanceof DutyToGodConfig)
							{
								imageSource = "awards/dtg/"+awardName;
							}
							else if(awardConfig instanceof CubAwardConfig)
							{
								imageSource = "cub/awards/"+awardConfig.getName();
							}
							else
							{
								//must be a scout award, we don't want to show it --should never happen
								System.out.println("***Not showing "+awardName+" is not valid for a cub scout");
								continue;
							}
							mbCount++;
							htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'>" +
							"<img src='images/"+imageSource.replace("'", "")+".png' height="+size+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
						}
						else //ScoutUnit, only show scout awards
						{
							if (awardConfig instanceof RankConfig)
							{
								if (noMoreRanks == false)
								{
									mbCount++;
									imageSource ="images/Ranks/"+awardName+".png";
									htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'><img src='"+imageSource+"' height="+(size+10)+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
									noMoreRanks = true;
								}
								continue;
							}
							if (awardConfig instanceof BadgeConfig)
							{
								imageSource = "meritbadges/"+awardName;
							}
							else if (awardConfig instanceof DutyToGodConfig)
							{
								imageSource = "awards/dtg/"+awardName;
							}
							else if (!(awardConfig instanceof RankConfig))
							{
								imageSource = "awards/"+awardName;
							}
							else
							{
								//must be a cub award, we don't want to show it 
								System.out.println("***Not showing "+awardName+" we do not show cub awards for scouts");
								continue;
							}
							mbCount++;
							htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'>" +
							"<img src='images/"+imageSource+".png' height="+size+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
						}
		        	}
				}
		        
		        //hide the carousel if no awards in progress
				if(mbCount > 0)
				{
					writer.write("<div class='inprogresslabel'>In Progress ("+mbCount+")</div>");
			        writer.write("<ul id='inprogressAwards' class='jcarousel-skin-tango'>");
			        writer.write(htmlLists);
	        		writer.write("</ul><div class='inprogresslabel'>In Progress</div>");
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
	
	private boolean shouldShowAwards()
	{
		boolean showAwards = true;
		List<Scout> scouts = (List<Scout>)pageContext.getSession().getAttribute("scouts");
		if(scouts != null)
		{
			int checkedCount=0;
			for (Scout scout : scouts)
			{
				if (scout.isChecked() ||  scout.isSelected())
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
		
		Set<Award> awards = (Set<Award>)bean;
		try
		{
			//TODO this will throw an exception, but we really want it to not do anything
			if (awards.size()==0)
			{
				return null;
			}
		}
		catch (LazyInitializationException e)
		{
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
