package org.trax.tags;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.trax.model.CampLogEntry;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.ServiceLogEntry;

/**
 * only display the type of tags needed
 * @author Jeff
 *
 */
public class LogEntryTag extends TagSupport
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
			writer.write("<table id='entryList' cellspacing='0' class='dataTable scoutTable' summary='List of log entries'><thead><tr>");
			if (name.equals("Camp"))
			{
				writer.write("<th>"+name+"ing</th>" +
						"<th>Depart</th>" +
						"<th>Return</th>" +
						"<th>Description</th>" +
						"<th>Signature</th></tr></thead>");
				Set<CampLogEntry> campLog = (Set<CampLogEntry>)pageContext.getRequest().getAttribute("Camping");
				
				for (CampLogEntry logEntry : campLog)
				{
					if (logEntry instanceof CampLogEntry)
					{
						CampLogEntry cle = (CampLogEntry)logEntry;
						writer.write("<tbody class='highlightable'><tr>"+
						    "<td>"+cle.getLocation()+"</td>"+
						    "<td>"+cle.getDepartDate()+"</td>"+
						    "<td>"+cle.getReturnDate()+"</td>"+
						    "<td>"+cle.getDescription()+"</td>"+
						    "<td>"+cle.getSignOffLeader().getFullName()+"</td>"+
						    "</tr>");
					
					}
				}
			}
			/*else if(name.equals("Service"))
			{
				writer.write("<th>Type of Project</th>" +
						"<th>Date</th>" +
						"<th>Hours</th>" +
						"<th>Description</th>" +
						"<th>Recorded by</th></tr></thead>");
				for (ServiceLogEntry logEntry : sle)
				{
					writer.write("<tbody class='highlightable'><tr>"+
						    "<td>"+logEntry.getTypeOfProject()+"</td>"+
						    "<td>"+logEntry.getStartDate()+"</td>"+
						    "<td>"+logEntry.getTimeInHours()+"</td>"+
						    "<td>"+logEntry.getDescription()+"</td>"+
						    "<td>"+logEntry.getSignOffLeader().getFirstName()+" "+logEntry.getSignOffLeader().getLastName()+"</td>"+
						    "</tr>");
				}
			}
			else if(name.equals("Leadership"))
			{
				
			}
			*/
				
			writer.write("</tbody><tfoot></tfoot></table>");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

//	private Set<LogEntry> getLogEntries() throws IOException
//	{
//		Object bean = pageContext.getRequest().getAttribute(name);
//		if (bean == null)
//		{
//			String message = "No " + name + " found in the request.";
//			pageContext.setAttribute("errorMessage", message);
//			throw new IOException(message);
//		}
//		if (!(bean instanceof Set<?>))
//		{
//			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
//			pageContext.setAttribute("errorMessage", message);
//			throw new IOException(message);
//		}
//		
//		return (Set<LogEntry>)bean;
//	}
	
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
