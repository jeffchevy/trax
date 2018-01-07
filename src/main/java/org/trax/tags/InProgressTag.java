package org.trax.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.RankConfig;
import org.trax.model.Scout;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.pu2015.ChildAwardConfig;
import org.trax.util.Helper;

@SuppressWarnings("serial")
public class InProgressTag extends BaseTag
{
	private static final Logger logger = Logger.getLogger(InProgressTag.class);
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
				Map<String, Integer> awardCounts = new HashMap<String, Integer>();
		        boolean isRankShowing = false;
		        Set<Award> awards = getAwards();
				if(awards==null)
				{
					return SKIP_BODY;
				}
				boolean isNewCubsMode = isNewCubs(); 
				
		        String htmlLists = "";
				int mbCount = 0;
		        for (Award award : awards)
				{
					AwardConfig awardConfig = award.getAwardConfig();
		        	

		        	if(award.getDateCompleted() == null)
		        	{
		        		if (awardConfig instanceof ChildAwardConfig && award.getRequirements()==null || (award.getRequirements().size()==0 && ! award.getInProgress() ))
						{
							continue;
						}
		        		
						String awardName = awardConfig.getName().replace("'", "");
						if (scout.getUnit().isCub())
						{
							if( ! Helper.isAnyCubAward(awardConfig))
							{
								//this must be a scout award, we don't want to show it --should never happen
								logger.error("***Not showing "+awardName+" is not valid for a cub scout");
								continue;
							}

							if(isNewCubsMode)
							{
								if( Helper.isNewCubsAward(award))
								{
									//only processing 2015 Awards
									htmlLists += createImageLink(awardConfig, awardConfig.getImageSource(), awardName);
									mbCount++;
								}
							}
							else 
							{
								if(Helper.isClassicCubAward(award)) //classic cubs
								{
									if (awardConfig instanceof CubRankConfig)
									{
										String unitTypeName = scout.getUnit().getTypeOfUnit().getName();
										if (isRankShowing == false)
										{
											//cannot start on a wolf unless wolf, bear, unless a Bear, webelos unless webelos, aofLight unless earned webelos
											if(awardName.equals("Bobcat")||awardName.equals("Arrow Of Light")||(awardName.equals(unitTypeName)))
											{
												mbCount++;
												//htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'><img src='"+awardConfig.getImageSource()+"' height="+(size+10)+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
												htmlLists+=createImageLink(awardConfig, awardConfig.getImageSource(),awardName);
											}
											isRankShowing = true;
										}
										continue;
									}
									
									if(awardConfig instanceof CubRankElectiveConfig)
									{
										String unitTypeName = scout.getUnit().getTypeOfUnit().getName();
										if (!awardName.startsWith(unitTypeName))
										{
											continue;//don't leave a space for it, it should not show up at all
										}
									}
									htmlLists+=createImageLink(awardConfig, awardConfig.getImageSource(), awardName);
									mbCount++;
								}
							}
						}
						else if (! Helper.isAnyCubAward(awardConfig))
						{
							//This is an older scout
							if (awardConfig instanceof RankConfig)
							{
								if (isRankShowing == false)
								{
									mbCount++;
									//htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'><img src='"+awardConfig.getImageSource()+"' height="+(size+10)+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
									htmlLists+=createImageLink(awardConfig, awardConfig.getImageSource(),awardName);
									isRankShowing = true;
								}
								continue;
							}
							
							if (!(awardConfig instanceof BadgeConfig 
								|| awardConfig instanceof DutyToGodConfig 
								|| !(awardConfig instanceof RankConfig)))
							{
								//must be a cub award, we don't want to show it 
								logger.error("***Not showing "+awardName+" we do not show cub awards for scouts");
								continue;
							}
							mbCount++;
							htmlLists+=createImageLink(awardConfig, awardConfig.getImageSource(),awardName);
							//htmlLists+="<li><a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' title='Click to view "+awardName+" details'>" +
							//"<img src='"+awardConfig.getImageSource()+"' height="+size+" title='"+awardName+"' alt='"+awardConfig.getDescription()+"' /></a></li>";
						}
		        	}
				}
		        
		        //hide the carousel if no awards in progress
				if(mbCount > 0)
				{
					writer.write("<div class='inprogresslabel'>In Progress ("+mbCount+")</div>");
	        		writer.write("<div id='inprogressAwards' class='jcarousel-skin-tango'><ul>");
			        writer.write(htmlLists);
	        		writer.write("</ul></div><div class='inprogresslabel'>In Progress</div>");
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
}
