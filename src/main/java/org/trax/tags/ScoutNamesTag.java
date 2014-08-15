package org.trax.tags;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.trax.model.Scout;
import org.trax.model.User;

public class ScoutNamesTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;


	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();

		try
		{
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (obj == null || !(obj instanceof User))
			{
				return SKIP_BODY;
			}
			if (obj instanceof Scout)
			{
				//this is a single scout, don't include this tag at all
				return SKIP_BODY;
			}
			
			List<Scout> scouts = getScouts();
			String allChecked = "checked=checked";

			String list = "";
			int checkedScouts = 0;
			for (Scout scout : scouts)
			{
				boolean shouldCheck = scout.isChecked();// && !isOneScout;
				if (!scout.isChecked())
				{
					allChecked = "";
				}
				if (scout.isChecked() || scout.isSelected())
				{
					checkedScouts++;
				}
				String checked = shouldCheck?"checked=checked":"";
				String checkbox = "<input id='sn"+scout.getId()+"' type='checkbox' title='Select a scout' "+checked+" name='scoutIds' value='"+scout.getId()+"' class='scoutName'>" +
						"<label class='verticalalign' for='sn"+scout.getId()+"'>&nbsp</label>";

				String link = "<a title='Click to change to this scout' href='changescout.html?id="+scout.getId()+"'>" +
						scout.getFirstName()+" "+scout.getLastName()+"</a> ";
				String innerHtml = scout.isSelected() || scout.isChecked()?" class='selectedscout'" :"";
				list += "<li"+innerHtml+">"+checkbox+link+"</li>\n";
			}
			String showCount = checkedScouts>1?checkedScouts +" scouts selected":"";
			writer.write("<form method='get' id='scoutsform' action='manyscouts.html'>");
			writer.write("<ul id='scouts'>");
			writer.write("<li class='allscouts'>" +
					"<input id='allscouts' type='checkbox' title='Select all scouts' "+allChecked+" value='allScouts'> " +
					"<label for='allscouts' style='font-size:12px;color:yellow;padding-left:20px;white-space:nowrap'>All </label>"+showCount+"</li>");
			writer.write(list);
			writer.write("</ul></form>");	
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	private List<Scout> getScouts() throws IOException
	{
		Object bean = pageContext.getSession().getAttribute(name);
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
		
		return (List<Scout>)bean;
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

	public void setName(String s)
	{
		name = s;
	}

	public String getName()
	{
		return name;
	}

}
