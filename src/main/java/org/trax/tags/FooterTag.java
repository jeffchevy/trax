package org.trax.tags;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.trax.model.Scout;

public class FooterTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private Format formatter = new SimpleDateFormat("MM/dd/yyyy");


	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();
		
		try
		{
			Scout scout = getScout();
			if (scout == null ) //no scout or many are selected
			{
				return SKIP_BODY;
			}
			Map<Long, Long> requirementConfigIdAndCount = getRequirementConfigIdAndCount();
			if(scout != null || requirementConfigIdAndCount!=null)
			{
				writer.write("<div class='cell'>\n");
				writer.write("	<div id='footer' class='table'>\n");
				writer.write("		<div class='row'>\n");
				writer.write("			<div class='cell tablefooter'>\n");
				if (requirementConfigIdAndCount == null) 
				{
					if(scout.isSelected()||scout.isChecked())
					{
						//only show personal info if the scout is selected<a title='Click to edit this scouts record' href='userUpdate.html?userId="+scout.getId()+"'>
						String fullName = scout.getFirstName()+" "+scout.getLastName();
						Date birthDate = scout.getBirthDate();
						String birthDateString = birthDate==null?"????":formatter.format(birthDate);
						String homePhone = scout.getPhone()==null?"":"<span class='smalllabel'>home </span><span class='biglabel'>"+scout.getPhone()+" </span>";
						String cellPhone = scout.getCellPhone()==null?"":"<span class='smalllabel'>cell </span><span class='biglabel'>"+scout.getCellPhone()+" </span>";
						String workPhone = scout.getWorkPhone()==null?"":"<span class='smalllabel'>work </span><span class='biglabel'>"+scout.getWorkPhone()+" </span>";
						String textMessageNumber=scout.getTextMessageNumber();
						textMessageNumber = textMessageNumber==null?"????":textMessageNumber;
						String email=scout.getEmail();
						email = email==null?"????":email;
						writer.write(
								"<a title='Click to edit this scouts record' href='userUpdate.html?userId="+scout.getId()+"'>" +
									"<div>" +
										"<div><span class='biglabel'>"+fullName+"</span></div>" +
										"<div><span class='smalllabel'> birthday </span><span class='biglabel'>"+birthDateString+"</span></div> " +
										"<div>"+homePhone+cellPhone+workPhone+"</div>" +
										"<div><span class='smalllabel'>email </span><span class='biglabel'>"+email+"</span></div>" +
									"</div>" +
								"</a>");
						writer.write("</div>");// cell
								
						writer.write("<div id='reminder' class='cell tablefooter'>" +	"<span class='smalllabel'>reminders</span>"+/*<span class='biglabel'>"+award.getReminders()+"</span>*/"</div>\n");
					}
					else {
						//scout not selected
						writer.write("</div>");// cell
					}
				}
				else
				{
					writer.write("</div>");// cell
				}

				//column 2
				//writer.write("			<div id='reminder' class='cell tablefooter'>" +	"<span class='smalllabel'>reminders</span>"+/*<span class='biglabel'>"+award.getReminders()+"</span>*/"</div>\n");
				writer.write("		</div>\n");// row
				writer.write("	</div>\n");// table
				writer.write("</div>\n");// cell
			}
		}
		catch (IOException e)
		{
			//Not a problem, just now scout loaded yet, skip this tag
		}

		return SKIP_BODY;
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
