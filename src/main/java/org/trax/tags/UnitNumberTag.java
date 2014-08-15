package org.trax.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.trax.model.UnitType;
import org.trax.model.User;
import org.trax.model.cub.CubUnitType;

public class UnitNumberTag extends TagSupport
{
	private PageContext pageContext = null;
	private Tag parent = null;
	private Integer number = null;

	@Override
	public int doStartTag() throws JspException
	{
		JspWriter writer = pageContext.getOut();
		
		try
		{
			int size = 27;
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (obj == null || !(obj instanceof User))
			{
				return SKIP_BODY;
			}
	        User user = (User)obj; 
			Integer unitNumber = user.getUnit().getNumber();
			String numString = String.valueOf(unitNumber);
			String unitTypeName = user.getUnit().getTypeOfUnit().getName().toLowerCase();
			if ( user.getUnit().isCub())
			{
				unitTypeName = "pack";
			}
			writer.write("<a id='nav-troop' title='Click here to manage the "+unitTypeName+"' href='troopManage.html'>");
			
			//put the unitType on the page, can use this to determine if its troop or cub scouts
			writer.write("<img id='is"+unitTypeName+"' src='images/book/"+unitTypeName+".png' alt='troop' class='image'/>");
			int i=0;
			String prefix = "scout_";
			if (!(user.getUnit().getTypeOfUnit().getName().equals("Troop")
					|| user.getUnit().getTypeOfUnit().getName().equals("11 Year Olds") ))
			{
				prefix = "bsa_";
			}
			
			if (unitNumber >= 1000)
			{
				writer.write("<img src='images/numbers/"+prefix+numString.charAt(i++)+".png' width="+size+" alt='troop' />");		
			}
			if (unitNumber >= 100)
			{
				writer.write("<img src='images/numbers/"+prefix+numString.charAt(i++)+".png' width="+size+" alt='troop' />");
			}
			if (unitNumber >= 10)
			{
				writer.write("<img src='images/numbers/"+prefix+numString.charAt(i++)+".png' width="+size+" alt='troop' />");
			}
			writer.write("<img src='images/numbers/"+prefix+numString.charAt(i)+".png' width="+size+" alt='troop' />");		
			writer.write("</a>");		
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

	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}

	public static void main(String args[])
	{
		int number = 1789;
		String numString = String.valueOf(number);
		System.out.println("Digit 1:"+numString.charAt(0));
		System.out.println("Digit 2:"+numString.charAt(1));
		System.out.println("Digit 3:"+numString.charAt(2));
		System.out.println("Digit 4:"+numString.charAt(3));
	}
}
