package org.trax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.trax.form.AwardConfigForm;
import org.trax.form.CourseForm;
import org.trax.form.SelectionForm;
import org.trax.form.UploadItem;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.CourseConfig;
import org.trax.model.Leader;
import org.trax.model.RequirementConfig;
import org.trax.model.User;
import org.trax.service.ImportService;
import org.trax.service.TraxService;

@Controller
public class ToolsController extends AbstractScoutController
{
	private static final Logger logger = Logger.getLogger(ToolsController.class);
	private static final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	private static final String ERROR_MESSAGE = "errorMessage";
	@Autowired
	private ImportService importService;
	@Autowired
    private TraxService traxService;

	@RequestMapping(value="/training.html", method = RequestMethod.GET)
	public ModelAndView showTraining(Map<String, Object> model,
			HttpServletRequest request,	@Valid SelectionForm selectionForm, BindingResult result) throws IOException
	{
		if (result.hasErrors())
		{
			model.put("errorMessage", "Both dates are required");
			model.put("selectionForm", new SelectionForm());
			
			return new ModelAndView("training", model);
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<Leader> leaders = traxService.getLeaders(user.getOrganization().getId(), user.getUnit().isCub());
		Collection<CourseConfig> allCourses = traxService.getAllCourses();
		
		//add extra row for names and extra columns for id and name
		Object courseTable[][] = new Object[leaders.size()][allCourses.size()+2];
		// all training plus name column
		int column = 0;
		int row = 0;
		
		for (Leader leader : leaders)
		{
			courseTable[row][0]=leader.getId();
			courseTable[row][1]=leader.getFullName();
			Map<String, Date> leaderCourseNames = new TreeMap<String, Date>();
//			List<Course> leaderCourses = traxService.getCourses(leader);
			
			//for (Course course : leaderCourses)
			Set<Award> leaderAwards = leader.getAwards();
			for (Award course : leaderAwards)
			{
				leaderCourseNames.put(course.getAwardConfig().getName(), course.getDateCompleted());
			}

			column = 2;//skip the id and name column
			for (CourseConfig course : allCourses)
			{
				Date date = null;
				for (Award leaderCourse : leaderAwards)
				{
					if (course.getName().equals(leaderCourse.getAwardConfig().getName()))
					{
						date = leaderCourseNames.get(course.getName()); //found
					}
				}
				courseTable[row][column++]= date==null?null:formatter.format(date);
			}
			row++;
		}
		request.setAttribute("courses", allCourses);
		request.setAttribute("trainingTable", courseTable);
		request.setAttribute("trainingEntry", new CourseForm());
		return new ModelAndView("training", model);
	}
	
	@RequestMapping(value = "/addcourse.html", method = RequestMethod.POST)
	public void saveTraining(HttpServletRequest request, HttpServletResponse response, int id, String value, String columnName, Map<String, Object> model)
	{
		//Map<String, String> returnMap = new HashMap<String, String>();
		JSONObject returnMap =new JSONObject();
		PrintWriter writer = null;

		try
		{
			writer = response.getWriter();

			String courseName = columnName.replace("<div>","").replace("</div>","");
			int userId = id;
			Date completionDate = new Date(value);
			traxService.addCourse(courseName, userId, completionDate);
			returnMap.put("successMessage", "Training records have been updated");
		} 
		catch (Exception e)
		{
			logger.warn("Failed to update course "+e.getMessage());
			returnMap.put("errorMessage", "Failed to update course "+e.getMessage());
		}
		writer.print(returnMap);
		//return "training";//returnMap;
	}
	
	@RequestMapping(value = "/tools.html", method = RequestMethod.GET)
	public ModelAndView showForm(Map<String, Object> model,
			@RequestParam(value = "message", required = false) String message)
	{
		model.put("awardConfigs", new AwardConfigForm());
		model.put("successMessage", message);
		return new ModelAndView("tools", model);
	}

	@RequestMapping(value = "/parseAwards.html", method = RequestMethod.POST)
	public ModelAndView parseAwards(Map<String, Object> model, HttpServletRequest request, UploadItem uploadAwards,
			BindingResult result)
	{
		CommonsMultipartFile fileData = uploadAwards.getFileData();

		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				model.put(ERROR_MESSAGE, error.getDefaultMessage());
			}
			return new ModelAndView("showAwardsPreview", model);
		}

		try
		{
			Collection<AwardConfig> acs = importService.parseAwards(fileData);
			model.put("importAwards", acs);
			request.getSession().setAttribute("importAwards", acs);
		} 
		catch (Exception e)
		{
			
			String errorMessage = "***********Failed to import awards " + e.getMessage();
			model.put(ERROR_MESSAGE, errorMessage);
			logger.warn(errorMessage);
			return new ModelAndView("showAwardsPreview", model);
		}

		return new ModelAndView("showAwardsPreview", model);
	}
	

	@RequestMapping(value = "/saveAwardData.html", method = RequestMethod.POST)
	public ModelAndView saveAwardData(HttpServletRequest request, 
			 AwardConfigForm awardConfigsForm,
			 BindingResult result, Map<String, Object> model)
	{
		try
		{
			Collection<AwardConfig> importAwards = (Collection<AwardConfig>)request.getSession().getAttribute("importAwards");
			importService.saveAwardData(importAwards);
			request.getSession().removeAttribute("importAwards");
		} 
		catch (Exception e)
		{
			String errorMessage = "Failed to save award Configuration data "+e.getMessage();
			model.put(ERROR_MESSAGE,errorMessage);
			logger.warn(errorMessage);
			return new ModelAndView("showAwardsPreview", model);
		}
		return new ModelAndView("tools", model);
	}
	

	@RequestMapping(value = "/parseRequirements.html", method = RequestMethod.POST)
	public ModelAndView parseRequirements(Map<String, Object> model, HttpServletRequest request, UploadItem uploadRequirements,
			BindingResult result)
	{
		CommonsMultipartFile fileData = uploadRequirements.getFileData();

		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				model.put(ERROR_MESSAGE, error.getDefaultMessage());
			}
			return new ModelAndView("showRequirementsPreview", model);
		}

		try
		{
			Collection<RequirementConfig> rcs = importService.parseRequirements(fileData);
			model.put("importRequirements", rcs);
			request.getSession().setAttribute("importRequirements", rcs);
		} 
		catch (Exception e)
		{
			String errorMessage = "***********Failed to import requirements " + e.getMessage();
			model.put(ERROR_MESSAGE, errorMessage);
			logger.warn(errorMessage);
			return new ModelAndView("showRequirementsPreview", model);
		}

		return new ModelAndView("showRequirementsPreview", model);
	}
	

	@RequestMapping(value = "/saveRequirementData.html", method = RequestMethod.POST)
	public ModelAndView saveRequirementData(HttpServletRequest request, 
			 AwardConfigForm awardConfigsForm,
			 BindingResult result, Map<String, Object> model)
	{
		try
		{
			Collection<RequirementConfig> importRequirements = (Collection<RequirementConfig>)request.getSession().getAttribute("importRequirements");
			importService.saveRequirementConfigs(importRequirements);
			request.getSession().removeAttribute("importRequirements");
		} 
		catch (Exception e)
		{
			String errorMessage = "Failed to save award Configuration data "+e.getMessage();
			model.put(ERROR_MESSAGE,errorMessage);
			logger.warn(errorMessage);
			return new ModelAndView("showRequirementsPreview", model);
		}
		return new ModelAndView("tools", model);
	}
	/**
	 * make this accessible to the model
	 * 
	 * @return
	 */
	@ModelAttribute("uploadAwards")
	public UploadItem getUploadAwards()
	{
		return new UploadItem();
	}

	/**
	 * make this accessible to the model
	 * 
	 * @return
	 */
	@ModelAttribute("uploadRequirements")
	public UploadItem getUploadRequirements()
	{
		return new UploadItem();
	}
}
