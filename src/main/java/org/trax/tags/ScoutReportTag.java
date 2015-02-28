package org.trax.tags;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.trax.dto.ScoutReportDto;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.CampLogEntry;
import org.trax.model.DutyToGodConfig;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.ServiceLogEntry;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;

@SuppressWarnings("serial")
public class ScoutReportTag extends TagSupport
{
	protected static final Format formatter = new SimpleDateFormat("MM/dd/yyyy");
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
			List<ScoutReportDto> scouts = getScoutReports();
			for (ScoutReportDto scout : scouts)
			{
				writeOneScoutReport(scout, writer);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	private void writeOneScoutReport(ScoutReportDto scoutReport, JspWriter writer) throws IOException
	{
		String namePosition = scoutReport.getFullname();
		if (scoutReport.getPositionName()!=null && scoutReport.getPositionName().length()!=0)
		{
			namePosition += " ("+scoutReport.getPositionName()+")";
		}
		writer.write("<h1>"+namePosition+"</h1>");
		
		Date birthday = scoutReport.getBirthday();
		if(birthday!=null)
		{
			int futureAge = 18;
			if(scoutReport.isCub())
			{
				futureAge=11;
			}

			Integer monthsTo = getMonthsDifference(birthday, futureAge);
			Date futureBirthday = DateUtils.addYears(birthday, futureAge);
			writer.write("<p>( "+futureAge+" years old in "+monthsTo+" months. "+DateFormatUtils.format(futureBirthday, "MM/dd/yyyy")+")</p>");
			
		}
		
		writer.write((scoutReport.getBsaMemberId() !=null?"BSA Member Id: "+scoutReport.getBsaMemberId():""));
		
		Collection<CampLogEntry> campLogs = scoutReport.getCampEntries();
		Collection<ServiceLogEntry> serviceLogs = scoutReport.getServiceEntries();
		Collection<LeadershipLogEntry> leadershipLogs = scoutReport.getLeadershipEntries();
		
		Map<String, Award> rankMap = new HashMap<String, Award>();
		Map<String, Award> badgeMap = new HashMap<String, Award>();
		Map<String, Award> requiredBadgeMap = new HashMap<String, Award>();
		Map<String, Award> awardMap = new HashMap<String, Award>();
		Map<String, Award> dtgMap = new HashMap<String, Award>();
		Map<String, Award> electiveMap = new HashMap<String, Award>();
		Map<String, Award> pinMap = new HashMap<String, Award>();
		Map<String, Award> beltLoopMap = new HashMap<String, Award>();
		Map<String, Award> activityBadgeMap = new HashMap<String, Award>();
		
		
		boolean isCub=false;
		for (Award award : scoutReport.getAwards())
		{
			AwardConfig awardConfig = award.getAwardConfig();
			isCub = scoutReport.isCub();
			String beadsText="";
			if(isCub)
			{
				
				if (awardConfig instanceof CubRankConfig)
				{
					beadsText = generateBeadsText(award);
				}
				else if (awardConfig instanceof CubRankElectiveConfig)
				{
					beadsText = generateArrowPointTexts(award);
				}
			}
			
			if(award.getDateCompleted() == null)
			{
				double percent = Math.ceil(((((double) award.getRequirements().size())
						/ awardConfig.getRequirementConfigs().size()) * 100));
				award.setPercentComplete(""+percent+"%" +(beadsText==""?"":beadsText));
			}
			
			// only include awards in progress or completed
			if (award.getDateCompleted() != null || 
				 award.getInProgress() ||
				 award.getAwardConfig().isRequired() || 
				(award.getAwardConfig() instanceof RankConfig && 
				 award.getRequirements() != null && 
				 award.getRequirements().size()>0))
			{
				//attach sort order so can be sorted in display, this is a hack but works for now
				String key = awardConfig.getSortOrder()+":"+awardConfig.getName();
				if(isCub)
				{
					if (awardConfig instanceof CubRankConfig)
					{
						rankMap.put(key, award);
					}
					else if (awardConfig instanceof CubRankElectiveConfig)
					{
						electiveMap.put(key, award);
					}
					else if (awardConfig instanceof PinConfig)
					{
						pinMap.put(key, award);
					}
					else if (awardConfig instanceof BeltLoopConfig)
					{
						beltLoopMap.put(key, award);
					}
					else if (awardConfig instanceof CubDutyToGodConfig)
					{
						dtgMap.put(key, award);
					}
					else if (awardConfig instanceof ActivityBadgeConfig)
					{
						activityBadgeMap.put(key, award);
					}
					else // its an awardif (awardConfig instanceof AwardConfig)
					{
						awardMap.put(key, award);
					}
				}
				else
				{
					if (awardConfig instanceof CubRankConfig || 
									awardConfig instanceof CubRankElectiveConfig ||
									awardConfig instanceof PinConfig ||
									awardConfig instanceof BeltLoopConfig ||
									awardConfig instanceof CubDutyToGodConfig ||
									awardConfig instanceof ActivityBadgeConfig)
					{
						//skip these
					}
					else if (awardConfig instanceof RankConfig)
					{
						rankMap.put(key, award);
					}
					else if (awardConfig instanceof BadgeConfig)
					{
						if (awardConfig.isRequired())
						{
							requiredBadgeMap.put(key, award);
						}
						else
						{
							badgeMap.put(key, award);
						}
					}
					else if (awardConfig instanceof DutyToGodConfig)
					{
						dtgMap.put(key, award);
					}
					else // its an awardif (awardConfig instanceof AwardConfig)
					{
						awardMap.put(key, award);
					}
				}
			}
		}

		if(isCub)
		{
			writeAwardTable(writer, rankMap, "Cub Ranks", "Total Complete");
			writeAwardTable(writer, electiveMap, "Electives", "Cub Rank Electives");
			writeAwardTable(writer, pinMap, "Pins", "Total Complete");
			writeAwardTable(writer, beltLoopMap, "Belt Loops", "Total Complete");
			writeAwardTable(writer, activityBadgeMap, "Activity Badges", "Total Complete");
			writeAwardTable(writer, awardMap, "Awards", "Total Complete");
			writeAwardTable(writer, dtgMap, "Duty To God", "Total Complete");
		}
		else
		{
			writeAwardTable(writer, rankMap, "Ranks", "Total Complete");
			writeAwardTable(writer, requiredBadgeMap, "Required Badges", "Total Complete(13 for Eagle)");
			writeAwardTable(writer, badgeMap, "Merit Badges", "Total Complete(9 for Eagle)");
			writeAwardTable(writer, awardMap, "Awards", "Total Complete");
			writeAwardTable(writer, dtgMap, "Duty To God", "Total Complete");
		}

		
		if (!(campLogs == null || campLogs.isEmpty()))
		{
			writeTableStart(writer, "Camp", "Location");
			writer.write("<th>Depart</th><th>Return</th><th>Description</th></tr></thead>");
			int totalNights = 0;
			for (CampLogEntry logEntry : campLogs)
			{
				writer.write("<tr>" + "<td>" + logEntry.getLocation() + "</td>" + "<td>" + formatter.format(logEntry.getDepartDate())
						+ "</td>" + "<td>" + formatter.format(logEntry.getReturnDate()) + "</td>" + "<td>" + logEntry.getDescription()
						+ "</td>" + "</tr>\n");
				if(logEntry.getDepartDate().getDate()<logEntry.getReturnDate().getDate())
				{
					//normal case
					totalNights+=(logEntry.getReturnDate().getDate()-logEntry.getDepartDate().getDate());
				}
				else
				{
					totalNights+=1; //TODO this should be a more complicated algorithm
				}
			}
			writer.write("<tfoot><td><b>Total Nights</b></td><td></td><td><b>"+totalNights+"</b></td><td></td></tfoot>\n</table>\n");
			//writeTableEnd(writer);
		}
		
		int totalHours = 0;
		if (!(serviceLogs == null || serviceLogs.isEmpty()))
		{
			writeTableStart(writer, "Service", "Location");
			writer.write("<th>Depart</th><th>Hours</th><th>Description</th></tr></thead>");
			for (ServiceLogEntry logEntry : serviceLogs)
			{
				writer.write("<tr>" + "<td>" + logEntry.getTypeOfProject() + "</td>" + "<td>" + formatter.format(logEntry.getServiceDate())
						+ "</td>" + "<td>" + logEntry.getTimeInHours() + "</td>" + "<td>" + logEntry.getDescription()
						+ "</td>" + "</tr>\n");
				totalHours+=logEntry.getTimeInHours();
			}
			writer.write("<tfoot><td><b>Total Hours</b></td><td></td><td><b>"+totalHours+"</b></td><td></td></tfoot>\n</table>\n");
		}
		
		if (!(leadershipLogs == null || leadershipLogs.isEmpty()))
		{
			writeTableStart(writer, "Leadership", "Position");
			writer.write("<th>Depart</th><th>Return</th><th>Description</th></tr></thead>");
			for (LeadershipLogEntry logEntry : leadershipLogs)
			{
				if(logEntry.getEndDate()!=null)
				{
					writer.write("<tr>" + "<td>" + logEntry.getPosition()+ "</td>" + "<td>" + formatter.format(logEntry.getStartDate())
						+ "</td>" + "<td>" + formatter.format(logEntry.getEndDate()) + "</td>" + "<td>" + logEntry.getDescription()
						+ "</td>" + "</tr>\n");
				}
			}
			writeTableEnd(writer);
		}			

	}

	private static final Integer getMonthsDifference(Date birthdate, int age)
	{
		if (birthdate == null)
		{
			return null;
		}
		int monthsInAYear = 12;

		Calendar startCalendar = Calendar.getInstance();
		Date now = startCalendar.getTime();
		int m1 = birthdate.getYear() * monthsInAYear + birthdate.getMonth();
		int m2 = now.getYear() * monthsInAYear + now.getMonth();
		return (age * monthsInAYear) - (m2 - m1 + 1);
	}

	private void writeTableStart(JspWriter writer, String logTypeName, String columnName) throws IOException
	{
		writer.write("<table id='scout" + logTypeName + "' cellspacing='0' class='log dataTable scoutTable' summary='List of " + logTypeName + "s'>" + "<caption>" + logTypeName
						+ "</caption>" + "<thead><tr>" + "<th>" + columnName + "</th>");
	}

	private void writeTableEnd(JspWriter writer) throws IOException
	{
		writer.write("<tfoot></tfoot>\n</table>\n");
	}

	private void writeAwardTable(JspWriter writer, Map<String, Award> awardMap, String awardTypeName, String totalString) throws IOException
	{
		if (!(awardMap == null || awardMap.isEmpty()))
		{
			String footerString = "";
			writer.write("<table id='scout" + awardTypeName.replace(' ', '_') + "' class='award' cellspacing='0' class='dataTable scoutTable' summary='List of " + awardTypeName
							+ "s'>");
			writer.write("<caption>" + awardTypeName + "</caption>");
			writer.write("<thead><tr><th class='hidden'>SortOrder</th>" + "<th>Name</th><th>Earned</th><th>Purchased</th><th>Awarded</th><th>Recorded by</th></tr></thead>");
			writer.write("<tbody>");
			int completeAwardCount = 0;
			if ("Required Badges".equals(awardTypeName))
			{
				footerString = "<p>*Only one of (Swimming, Hiking, Cycling) and (Emergency Prepardness or Lifesaving) counts for Eagle</p>";
				List<BadgeConfig> requiredBadges = this.getRequiredMeritBadges();
				for (BadgeConfig badgeConfig : requiredBadges)
				{
					String sortOrder = "" + badgeConfig.getSortOrder();
					String awardName = badgeConfig.getName();
					String key = sortOrder + ":" + awardName;
					Award award = (Award) awardMap.get(key);
					String complete = "0%";// default
					String purchased = "";// default
					String awarded = "";// default
					String userName = "&nbsp;";
					if (award != null)
					{
						if (award.getDateCompleted() != null)
						{
							complete = formatter.format(award.getDateCompleted());
						}
						else
						{
							complete = award.getPercentComplete();
						}
						complete = (String) (award.getDateCompleted() == null ? award.getPercentComplete() : formatter.format(award.getDateCompleted()));
						completeAwardCount += award.getDateCompleted() == null ? 0 : 1;
						purchased = (award.getDatePurchased() == null ? "" : formatter.format(award.getDatePurchased()));
						awarded = (award.getDateAwarded() == null ? "" : formatter.format(award.getDateAwarded()));
						userName = award.getDateCompleted() == null ? "&nbsp;" : award.getUser().getFullName();
					}
					String row = "<tr><td class='hidden'>" + sortOrder + "</td>" + "<td>" + awardName + "</td>" + "<td>" + complete + "</td>" + "<td>" + purchased + "</td>"
									+ "<td>" + awarded + "</td>" + "<td>" + userName + "</td></tr>\n";
					writer.write(row);
				}
			}
			else
			{
				for (String key : awardMap.keySet())
				{
					StringTokenizer st = new StringTokenizer(key, ":");
					String sortOrder = st.nextToken();
					String awardName = st.nextToken();
					Award award = (Award) awardMap.get(key);
					try
					{
						String complete;
						if (award.getDateCompleted() == null || (award.getPercentComplete() != null && !award.getPercentComplete().contains("%")))
						{
							complete = award.getPercentComplete();
						}
						else
						{
							complete = formatter.format(award.getDateCompleted());
						}
						completeAwardCount += award.getDateCompleted() == null ? 0 : 1;
						String purchased = (award.getDatePurchased() == null ? "" : formatter.format(award.getDatePurchased()));
						String awarded = (award.getDateAwarded() == null ? "" : formatter.format(award.getDateAwarded()));

						String row = "<tr><td class='hidden'>" + sortOrder + "</td>" + "<td>" + awardName + "</td>" + "<td>" + complete + "</td>" + "<td>" + purchased + "</td>"
										+ "<td>" + awarded + "</td>" + "<td>" + (award.getUser() == null ? "&nbsp;" : award.getUser().getFullName()) + "</td></tr>\n";
						writer.write(row);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			writer.write("</tbody>\n");
			writer.write("<tfoot><td><b>" + totalString + "</b></td><td><b>" + completeAwardCount + "</b></td><td></td><td></td><td></td></tfoot>\n</table>\n");
			writer.write(footerString);
		}
	}

	private List<ScoutReportDto> getScoutReports() throws IOException
	{
		Object bean = pageContext.getRequest().getAttribute("scoutReports");
		if (bean == null)
		{
			String message = "No 'scoutReports' found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof List<?>))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}

		return (List<ScoutReportDto>) bean;
	}

	private List<BadgeConfig> getRequiredMeritBadges() throws IOException
	{
		Object bean = pageContext.getRequest().getAttribute("requiredMeritBadges");
		if (bean == null)
		{
			String message = "No 'requiredMeritBadges' found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof List<?>))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}

		return (List<BadgeConfig>) bean;
	}

	public static String generateBeadsText(Award award)
	{
		String beadsText = "";
		boolean isWolf = award.getAwardConfig().getName().equals("Wolf");
		boolean isBear = award.getAwardConfig().getName().equals("Bear");
		if (isWolf || isBear)
		{
			// count all requirements with a number, for each 3 they earn a bead
			int numberedRequirements = 0;
			for (Requirement requirement : award.getRequirements())
			{
				String text = requirement.getRequirementConfig().getText();
				if (text != null && !text.isEmpty() && Character.isDigit(text.charAt(0)))
				{
					numberedRequirements++;
				}
			}
			numberedRequirements = numberedRequirements > 12 ? 12 : numberedRequirements; // don't
																							// count
																							// more
																							// than
																							// 12
																							// for
																							// bear
			beadsText = "(" + numberedRequirements / 3 + (isWolf ? " yellow" : " red") + " beads)";
			if (award.getDateCompleted() != null)
			{
				award.setPercentComplete(formatter.format(award.getDateCompleted()) + " (4 " + (isWolf ? "yellow" : "red") + " beads)");
			}
		}
		return beadsText;
	}

	public static String generateArrowPointTexts(Award award)
	{
		String beadsText = "";
		boolean isWolfElective = award.getAwardConfig().getName().equals("Wolf Electives");
		boolean isBearElective = award.getAwardConfig().getName().equals("Bear Electives");
		if (isWolfElective || isBearElective)
		{
			// count all requirements with a number, for each 3 they earn a bead
			int arrowpointCount = (award.getRequirements().size() / 10);
			if (arrowpointCount == 1)
			{
				beadsText = " (1 Gold)";
			}
			if (arrowpointCount > 1)
			{
				beadsText = " (1 Gold/" + (arrowpointCount - 1) + " Silver)";
			}
			if (award.getDateCompleted() != null)
			{
				award.setPercentComplete(formatter.format(award.getDateCompleted()) + " (1 Gold/6 Silver)");
			}
		}
		return beadsText;
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
	 * @see
	 * javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext
	 * )
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
