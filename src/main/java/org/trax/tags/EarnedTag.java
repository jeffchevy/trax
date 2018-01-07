package org.trax.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Requirement;
import org.trax.model.Scout;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.util.Helper;

@SuppressWarnings("serial")
public class EarnedTag extends BaseTag
{
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
				Map<String, Integer> awardCounts = new HashMap<String, Integer>();
				String htmlLists = "";
				
				Set<Award> awards = getAwards();
				if(awards==null)
				{
					return SKIP_BODY;
				}
				boolean isNewCubsMode = isNewCubs();
				
				for (Award award : awards)
				{
					AwardConfig awardConfig = award.getAwardConfig();

					if (award.getDateCompleted() != null)
					{
						String awardName = awardConfig.getName().replace("'", "");
						

						if (scout.getUnit().isCub())
						{
							
							if(isNewCubsMode)
							{
								if( Helper.isNewCubsAward(award))
								{
									//only processing 2015 Awards
									htmlLists += createImageLink(awardConfig, awardConfig.getImageSource(), awardName);
									incrementAwardCount(awardCounts, awardConfig);
								}
							}
							else
							{
								if (Helper.isClassicCubAward(award))
								{
									//only processing legacy Awards
									if (awardConfig instanceof CubRankConfig)
									{
										boolean isWolf = awardConfig.getName().equals("Wolf");
										boolean isBear = awardConfig.getName().equals("Bear");
										if(isWolf || isBear)
										{
											//if they earn the award, they automatically get the beads
											htmlLists += createImageLink(awardConfig, "images/cub/ranks/PTR"+awardConfig.getName()+"4.png", awardConfig.getName());
											awardCounts.put((isWolf ? "Wolf":"Bear")+" Bead", 4);
										}
									}
									htmlLists += createImageLink(awardConfig, awardConfig.getImageSource(), awardName);
									incrementAwardCount(awardCounts, awardConfig);
								}
							}
						}
						else 
						{
							//this award is for older scouts
							if (Helper.isAnyCubAward(awardConfig))
							{
								continue;
							}
							incrementAwardCount(awardCounts, awardConfig);
							htmlLists += createImageLink(awardConfig, awardConfig.getImageSource(), awardName);
						}
					}
					else //not complete yet
					{
						if(scout.getUnit().isCub())
						{
						    if(! isNewCubsMode)
                            {
    							if(awardConfig instanceof CubRankConfig)
    							{
    								htmlLists = addBeadImage(awardCounts, htmlLists, award, awardConfig);
    							}
    							else if(awardConfig instanceof CubRankElectiveConfig)
    							{
    								htmlLists = addElectivesImage(awardCounts, htmlLists, award, awardConfig);
    							}
                            }
						    
							if(awardConfig instanceof DutyToGodConfig)
							{
								htmlLists = addReligiousKnot(htmlLists, award, awardConfig);
							}
						}
					}
				}
				//hide the carousel if no awards earned
				if( ! awardCounts.isEmpty() )
				{
					writer.write("<span id='earnedlabel'>Earned (" );
					for (String awardType : awardCounts.keySet())
					{
						writer.write(""+awardCounts.get(awardType)+ " " + awardType+", ");
					}
					writer.write(")</span>");
					writer.write("<div id='earnedmerritbadges' class='jcarousel jcarousel-skin-tango'><ul>");
					writer.write(htmlLists);
					writer.write("</ul></div>");
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


	private void incrementAwardCount(Map<String, Integer> awardCounts, AwardConfig awardConfig)
	{
		if (!awardCounts.containsKey(awardConfig.getTypeName()))
		{
			awardCounts.put(awardConfig.getTypeName(), 1);
		}
		else
		{
			awardCounts.put(awardConfig.getTypeName(), ((Integer)awardCounts.get(awardConfig.getTypeName())).intValue()+1);
		}
	}


	private String addBeadImage(Map<String, Integer> awardCounts, String htmlLists, Award award, AwardConfig awardConfig)
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
			int beadCount = requirementCount/3;
			if (beadCount!=0)
			{
				awardCounts.put((isWolf ? "Wolf":"Bear")+" Bead", beadCount);
				htmlLists += createImageLink(awardConfig, "images/cub/ranks/PTR"+awardConfig.getName()+beadCount+".png", awardConfig.getName());
			}
		}
		return htmlLists;
	}

	private String addElectivesImage(Map<String, Integer> awardCounts, String htmlLists, Award award, AwardConfig awardConfig)
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
			int arrowpointCount = isWolfElective ? (award.getRequirements().size()/10):(award.getRequirements().size()/10);
			int tenCount = arrowpointCount*10;//truncate to the nearest factor of ten, 10,20,30 etc
			if (tenCount!=0)
			{
				awardCounts.put((isWolfElective ? "Wolf":"Bear")+" Electives", tenCount);
				htmlLists += createImageLink(awardConfig, "images/cub/electives/"+awardConfig.getName()+tenCount+".png", awardConfig.getName());
			}
		}
		return htmlLists;
	}

	/* The religious knot is earned when all 6 of the specific requirements are passed off, check that here */ 
	private String addReligiousKnot(String htmlLists, Award award, AwardConfig awardConfig)
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
				htmlLists += createImageLink(awardConfig, "images/awards/dtg/Religious Knot.png", awardConfig.getName());
			}
		}
		return htmlLists;
	}

	private String getEarnedMessage(int count, String name)
	{
		return count==0?"":count+" "+name+(count>1?"s":"")+", ";
	}

}
