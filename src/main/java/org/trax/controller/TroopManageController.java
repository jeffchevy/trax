package org.trax.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.trax.form.SelectionForm;
import org.trax.form.UploadItem;
import org.trax.model.BaseUnitType;
import org.trax.model.Leader;
import org.trax.model.Organization;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.service.ImportService;
import org.trax.service.TraxService;

@Controller
public class TroopManageController
{
	static final String ERROR_MESSAGE = "errorMessage";
    @Autowired
    private TraxService traxService;
    @Autowired
	private ImportService importService;
    
	/** need this for the scout and leader dialogs
 	 * make this accessible to the model
	 * 
	 * @return all the state in the union
	 */
	@ModelAttribute("states")
	public static String[] getStates()
	{
		return UserController.getStates();
	}
	
	@ModelAttribute("unitTypes")
    public List<? extends BaseUnitType> getUnitTypes(Map<String, Object> model)
    {
    	//@TODO should not have to put it in the model?
	   model.put("unitTypes", traxService.getUnitTypes());
	   return traxService.getUnitTypes();
    }
	 
    @RequestMapping(value="/troopManage.html", method=RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request, Map<String, Object> model, @RequestParam(value = "message", required = false) String message)
    {
    	request.getSession().setAttribute("navigationItem", "troopManage.html");
    	//request.getSession().removeAttribute("scout");no longer need to remove because we don't display in tiles anyway
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Organization organization = user.getOrganization();
        Collection<Leader> leaders = traxService.getLeaders(organization.getId(),user.getUnit().isCub());
        Collection<Scout> scouts = traxService.getScouts(organization.getId(), "All");
        model.put("leaders", leaders);
        model.put("scouts", scouts);
       	model.put("selection", new SelectionForm());
        model.put("successMessage", message);
        
        return new ModelAndView("showTroop", model);
    }
    
	@RequestMapping(value = "/upload.html", method = RequestMethod.POST)
	public ModelAndView uploadScoutNet(Map<String, Object> model, HttpServletRequest request, 
								 UploadItem uploadItem,
								 BindingResult result)
	{
		CommonsMultipartFile fileData = uploadItem.getFileData();
		
		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				model.put(ERROR_MESSAGE, error.getDefaultMessage());
			}
			return new ModelAndView("showTroop", model);
		}
		
		try
		{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			importService.updateScoutNetData(user.getOrganization(), fileData, user);
		} 
		catch (Exception e)
		{
			String message = "Error in File format, check that you are importing the correct File. If file appears correct, contact ScoutTrax.org Administrator";
			model.put(ERROR_MESSAGE,message);
			return new ModelAndView("redirect:troopManage.html?message="+message, model);
		}
		
		return new ModelAndView("redirect:troopManage.html?message=Successfully updated records.", model);
	}
	
	/**
	 * make this accessible to the model
	 * @return
	 */
	@ModelAttribute("uploadItem")
	public UploadItem getUploadItem()
	{
		return new UploadItem();
	} 
}

