package org.trax.tags;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.RankConfig;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.PinConfig;

public class DisplayBadgesTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private int col = 9;
	

	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();

		Collection<AwardConfig> badges = null;
		try
		{
			badges = getAwards();
		} 
		catch (IOException e1)
		{
			throw new JspException(e1);
		}
		
		try
		{
			AwardConfig firstBadgeConfig = badges.iterator().next();
			String activePageId=firstBadgeConfig.getTypeName();
			if (firstBadgeConfig.getName().contains("Palm"))
			{
				//palms are a special case
				activePageId = "Palms";
			}
			writer.write("\n<span id='"+activePageId+"'/>\n");// Mark it for the tabs
//			if (isBadge)
//			{
//				writer.write("<input type='checkbox' id='showRequired' value='Show Required'><span>Show Required</span>");
//			}
			if(activePageId.startsWith("Faith"))
			{
				writer.write("<div style='color:#99CC99' class='inprogresslabel'>Religious emblems of any faith may be added upon request. " +
					"Just click on Feedback in the upper right hand corner.</div>");
			}
			else if(activePageId.equals("DutyToGod"))
			{
				writer.write("<div style='color:#99CC99' class='inprogresslabel'>Duty To God for any faith may be added upon request. " +
					"Just click on Feedback in the upper right hand corner.</div>");
			}
			writer.write("<table id='badgeList' cellspacing='0' class='dataTable' summary='Badge List'>");
			writer.write("<thead>");
			writer.write("<tr>");
			for (int i = 1; i <= col; i++)
			{
				writer.write("<th>&nbsp;</th>");
			}
			writer.write("</tr>");
			writer.write("</thead>");
			writer.write("<tbody class='highlightable'>");
			int i=1;
			for (AwardConfig awardConfig : badges)
			{
				if (i==1)
				{
					writer.write("<tr>");
				}
				String classes = awardConfig.isRequired()?"class='awardImage required'":"class='awardimage'";
				String awardTitle = "";
			
				writer.write("<td "
								+"><a href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"' "+awardTitle+">" +
						"<img "+classes+" src='"+awardConfig.getImageSource()+"' alt='"+awardConfig.getDescription()+"' />");
				writer.write("<div class='meritbadgename'>"+awardConfig.getName() +"</div>"
								+"</a>"
								+"</td>");
				if ((i%col) == 0)
				{
					writer.write("</tr><tr>");
				}
				i++;
			}
			int columnPad = col - (badges.size() % col);
			for (int j = 0; j < columnPad; j++)
			{
				writer.write("<td>&nbsp;</td>");
			}
			writer.write("</tr>");
			writer.write("</tbody>");
			writer.write("<tfoot>");
			writer.write("</tfoot>");
			writer.write("</table>");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	private Collection<AwardConfig> getAwards() throws IOException
	{
		Object bean = pageContext.getRequest().getAttribute(name);
		if (bean == null)
		{
			String message = "No " + name + " found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof Collection<?>))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		return (Collection<AwardConfig>)bean;
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

}
