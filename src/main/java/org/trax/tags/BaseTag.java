package org.trax.tags;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.LazyInitializationException;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Scout;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;
import org.trax.model.cub.pu2015.ChildAwardConfig;
import org.trax.model.cub.pu2015.Cub2015RankConfig;
import org.trax.model.cub.pu2015.Cub2015RankElectiveConfig;

public class BaseTag extends TagSupport
{

	protected PageContext pageContext = null;
	private Tag parent = null;
	private String name = null;
	private int size = 47;

	public BaseTag()
	{
		super();
	}

	protected String createImageLink(AwardConfig awardConfig, String imageName, String awardName)
	{
		String imageLink="<li>" +
				"<a class='meritbadgelink' href='selectBadge.html?badgeConfigId="+awardConfig.getId()+"'>" +
				"<img src=\""+imageName+"\" height="+size+" title=\"Click to view "+awardConfig.getDescription()+" details\" alt=\""+awardConfig.getDescription()+"\" border='none'/>" +
			    "</a></li>";
		return imageLink;
	}

	protected boolean shouldShowAwards()
	{
		boolean showAwards = true;
		List<Scout> scouts = (List<Scout>)pageContext.getSession().getAttribute("scouts");
		if(scouts != null)
		{
			int checkedCount=0;
			for (Scout scout : scouts)
			{
				if (scout.isChecked() || scout.isSelected())
				{
					checkedCount++;
				}
				if (checkedCount>1)
				{
					break;
				}
			}
			if(checkedCount >1 || checkedCount == 0)
			{
				showAwards = false;
			}
		}
		return showAwards;
	}

	protected Set<Award> getAwards() throws IOException
	{
		Object bean = pageContext.getSession().getAttribute(AwardConfig.AWARDS);
		if (bean == null)
		{
			String message = "No 'awards' found in the request.";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		if (!(bean instanceof Set<?>))
		{
			String message = "<h2>Wrong type:" + bean.getClass() + "</h2>";
			pageContext.setAttribute("errorMessage", message);
			throw new IOException(message);
		}
		Set<Award> awards = (Set<Award>)bean;
		try
		{
			//TODO this will throw an exception if the awards cannot be found, 
			// so just swallow it and return null, the tag does not need to draw anything
			if (awards==null || awards.size()==0)
			{
				return null;
			}
		}
		catch (LazyInitializationException e)
		{
			// TODO Auto-generated catch block
			return null;
		}
		return awards;
	}

	public Tag getParent()
	{
		return parent;
	}

	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}

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
	
	public boolean isNewCubs()
	{
		Object newCubs = pageContext.getSession().getAttribute("Cub2015");
		Boolean isNewCubs = (Boolean) (newCubs!=null ? newCubs : false);
		return isNewCubs;
	}

}