package org.trax.tags;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.trax.dto.NameComplete;
import org.trax.model.Scout;

public class TrainingTag extends TagSupport
{
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
			Collection<Scout> scouts = (Collection<Scout>)pageContext.getSession().getAttribute("scouts");
			Map<String, List<NameComplete>> trainingMap = (Map<String, List<NameComplete>>)pageContext.getRequest().getAttribute(name);
			
			String colHeaderName = "Adult Training";
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
			for (String awardKey : trainingMap.keySet())
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
						List<NameComplete> nameCompleteList = (List<NameComplete>)trainingMap.get(awardKey);
						String td = ("<td class=''>");
						String complete = "";
						for (NameComplete nameComplete : nameCompleteList)
						{
							if (nameComplete.getFullName().equals(scout.getFullName()))
							{
								complete = (nameComplete.getComplete());
								if (!complete.contains("%"))
								{
									awardCount++;
								} 
								break;
							}
						}
						tbody+=td+complete+"</td>";
					}
				}
				totalAwardCount += awardCount;
				tbody+="<td>"+awardCount+"</td></tr>";
			}
			tbody+="</tbody>";
			
			String tfoot = "<tfoot><tr><td class='hidden'></td><td>Award Count</td>";
			for (int i = 0; i < checkedScoutsCount; i++)
			{
				tfoot+="<td></td>";
			}
			tfoot+="<td>"+totalAwardCount+"</td></tr><tfoot>";
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
