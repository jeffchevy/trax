package org.trax.tags;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.trax.dto.NameComplete;
import org.trax.model.AwardConfig;
import org.trax.model.Scout;
import org.trax.model.User;

@SuppressWarnings("serial")
public class GroupReportTag extends TagSupport
{
	public static final String AWARD_MAP = "awardMap";
	public static final String ACTIVITY_BADGE_MAP = "activityBadgeMap";
	public static final String BELT_LOOP_MAP = "beltLoopMap";
	public static final String PIN_MAP = "pinMap";
	public static final String ELECTIVE_MAP = "electiveMap";
	public static final String RANK_SCOUT_MAP = "rankScoutMap";
    protected static final Format formatter = new SimpleDateFormat("MM/dd/yyyy");
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private String styleId;
	
	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();
		
		try
		{
			List<Scout> scouts = (List<Scout>)pageContext.getSession().getAttribute("scouts");
			Map<String, List<NameComplete>> awardScoutMap = (Map<String, List<NameComplete>>)pageContext.getRequest().getAttribute(name);
			
			String colHeaderName = "";
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			boolean isCub = user.getUnit().isCub();
			if (isCub)
			{
				if(name.equals(RANK_SCOUT_MAP)) {colHeaderName=AwardConfig.RANK+"s";}
				else if(name.equals(ELECTIVE_MAP)) {colHeaderName=AwardConfig.RANK+" Electives";}
				else if(name.equals(PIN_MAP)) {colHeaderName=AwardConfig.PIN;}
				else if(name.equals(BELT_LOOP_MAP)) {colHeaderName="Belt Loops";}
				else if(name.equals(ACTIVITY_BADGE_MAP)) {colHeaderName="Activity Badges";}
				else if(name.equals(AWARD_MAP)) {colHeaderName="Other Awards";}
			}
			else
			{
				colHeaderName = name.contains("required")? "Required Merit Badges": (name.contains("rank")? "Scout Rank - Trail to Eagle" : "Merit Badges/Awards");
			}
			
			writer.write("<table id='"+styleId+"' border='1' cellspacing='0' class='dataTable' summary='"+colHeaderName+"' class='cell'><caption>"+colHeaderName+"</caption>");
			writer.write("<thead><tr>");
			writer.write("<th class='hidden'></th>"); //sort order column
			writer.write("<th></th>"); //award name column
			int checkedScoutsCount = 0;
			for (Scout scout : scouts)
			{
				if (scout.isSelected()||scout.isChecked())
				{
					checkedScoutsCount++;
					writer.write("<th>"+scout.getFullName()+"</th>");
				}
			}
			writer.write("<th>Total</th></tr></thead>");
			
			int totalAwardCount=0;
			String tbody = "<tbody>";
			int redBeadCount=0;
			int yellowBeadCount=0;
			int silverArrowCount=0;
			int goldArrowCount=0;
			for (String awardKey : awardScoutMap.keySet())
			{
				StringTokenizer st = new StringTokenizer(awardKey, ":");
				String sortOrder = st.nextToken();
				String awardName = st.nextToken();
				tbody+="<tr><td class='hidden'>"+sortOrder+"</td><td class='header'>"+awardName+"</td>";
				
				int awardCount = 0;
				for (Scout scout : scouts)
				{
					if (scout.isSelected()||scout.isChecked())
					{
						List<NameComplete> nameCompleteList = (List<NameComplete>)awardScoutMap.get(awardKey);
						String complete = "";
						String classes = "";
						for (NameComplete nameComplete : nameCompleteList)
						{
							if (nameComplete.getFullName().equals(scout.getFullName()))
							{
								complete = (nameComplete.getComplete());
								if (complete!=null && !complete.isEmpty())
								{
									if(complete.charAt(2)=='/' && complete.charAt(5)=='/')
									{
										//it contains a date - count it!
										awardCount++;
										//if it is complete (i.e. has a date) then let the user know if its awarded or purchased
										classes = (nameComplete.isPurchased() ? "purchased ":"")+ (nameComplete.isAwarded()?"awarded": "");
									} 
									if (isCub)
									{
										redBeadCount += getCount(complete, "(\\d Red)");
										yellowBeadCount += getCount(complete, "(\\d Yellow)");
										silverArrowCount += getCount(complete, "(\\d Silver)");
										goldArrowCount += getCount(complete, "(\\d Gold)");
									}
								}
								break;
							}
						}
						tbody+="<td class='"+classes+"'>"+complete+"</td>";
					}
				}
				totalAwardCount += awardCount;
				tbody+="<td>"+awardCount+"</td></tr>";
			}
			
			if (isCub)
			{
				totalAwardCount +=redBeadCount+yellowBeadCount+silverArrowCount+goldArrowCount;
				
				tbody+=createExtraRow(checkedScoutsCount, "Red Bead", redBeadCount, awardScoutMap.size()+1, "redBeadHeader");
				tbody+=createExtraRow(checkedScoutsCount, "Yellow Bead", yellowBeadCount, awardScoutMap.size()+2, "yellowBeadHeader");
				tbody+=createExtraRow(checkedScoutsCount, "Silver Arrow Point", silverArrowCount, awardScoutMap.size()+1, "silverBeadHeader");
				tbody+=createExtraRow(checkedScoutsCount, "Gold Arrow Point", goldArrowCount, awardScoutMap.size()+2, "yellowBeadHeader");
			}

			tbody+="</tbody>";
			String tfoot = "<tfoot>";
			tfoot+=createExtraRow(checkedScoutsCount, "Award Count", totalAwardCount, 1, "header");
			tfoot+="<tfoot>";
			writer.write(tfoot+tbody);
			writer.write("</table>");
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	private int getCount(String complete, String pattern) {
		int count=0;
		Pattern ptn = Pattern.compile(pattern);
		 Matcher matcher = ptn.matcher(complete);
		 while(matcher.find())
		 {
			 String group = matcher.group();
			 count+=Character.getNumericValue(group.charAt(0));
		 }
		return count;
	}
	
	//sort order is the length of rows + some number, this is really a kludge
	private String createExtraRow(int checkedScoutsCount, String rowTitle, int count, int sortOrder, String className) {
		String rowString = "";
		if(count>0) 
		{
			rowString = "<tr><td class='hidden'>"+sortOrder+"</td><td class='"+className+"'>"+rowTitle+"</td>";
			for (int i = 0; i < checkedScoutsCount; i++)
			{
				String blankCellClass =  className.equals("header")?"":" class='specialty'";
				rowString+="<td "+blankCellClass+"></td>";
			}
			rowString+="<td>"+count+"</td></tr>";
		}
		return rowString;
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

	public String getStyleId()
	{
		return styleId;
	}

	public void setStyleId(String styleId)
	{
		this.styleId = styleId;
	}
}
