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
import org.trax.model.BaseUnitType;
import org.trax.model.cub.CubUnitType;
import org.trax.model.Scout;
import org.trax.model.User;

public class SelectAgesTag extends TagSupport
{
	public static final String AGE_11 = "Age 11";
	public static final String AGE_12_13 = "Age 12-13";
	public static final String AGE_16_18 = "Age 16-18";
	public static final String AGE_14_15 = "Age 14-15";

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
			String selectedUnitType = getSelectedUnitType();
			String selected = " selected='selected'";
			writer.write("<form id='showscoutsbyunit' action='showscoutsbyage.html' method='get'>");
			writer.write("Show <select id='unitTypeName' name='unitTypeName'>");
			
					
			List<? extends BaseUnitType> unitTypes = getUnitTypes();
			if (!(unitTypes.get(0) instanceof CubUnitType))
			{
				//don't add for cubs, they have the pack option
				writer.write("<option value='"+Scout.ALL+"'"+(selectedUnitType.equalsIgnoreCase(Scout.ALL)?selected:"")+">"+Scout.ALL+"</option>");
			}
			for(BaseUnitType unitType: unitTypes)
			{
				writer.write("<option value='"+unitType.getName()+"'"+(selectedUnitType.equalsIgnoreCase(unitType.getName())?selected:"")+">"+unitType.getName()/*+" Age "+unitType.getStartAge()+"-"+unitType.getEndAge()*/+"</option>\n");
			}
			writer.write("</select></form>");	
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SKIP_BODY;
	}
	
	private String getSelectedUnitType() throws IOException
	{
		String unitTypeName = (String)pageContext.getSession().getAttribute("unitTypeName");
		if (unitTypeName == null)
		{
			unitTypeName = Scout.ALL;
		}
		return unitTypeName;
	}
	
	private List<? extends BaseUnitType> getUnitTypes() throws IOException
	{
		List<? extends BaseUnitType> unitTypeName = (List<? extends BaseUnitType>)pageContext.getRequest().getAttribute("unitTypes");
		if (unitTypeName == null)
		{
			unitTypeName = unitTypeName = (List<? extends BaseUnitType>)pageContext.getSession().getAttribute("unitTypes");
		}
		return unitTypeName;
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
