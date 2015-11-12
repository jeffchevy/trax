package org.trax.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

//import net.tanesha.recaptcha.ReCaptcha;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.trax.form.UploadItem;
import org.trax.form.UserCredentialsForm;
import org.trax.model.BaseUnitType;
import org.trax.model.Leader;
import org.trax.model.Organization;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.service.ImportService;
import org.trax.service.TraxService;
import org.trax.util.SimpleEncrypter;
import org.trax.validator.UserValidator;

@Controller
public class UserController
{
	private static final int MAX_PROFILE_IMAGE_SIZE = 200000;
	public static final String ORGANIZATION = "organization";
	private static final String ERROR_MESSAGE = "errorMessage";
	
	@Autowired
	private TraxService traxService;
    @Autowired
	private ImportService importService;
	@Autowired
	private UserValidator userValidator;
	//@Autowired
	//ReCaptcha reCaptcha;

	/**
	 * make this accessible to the model
	 * 
	 * @return all the state in the union
	 */
	@ModelAttribute("states")
	public static String[] getStates()
	{
		return OrganizationController.getStates();
	}

	/**
	 * keep the connection to the server current as long as the site is being used, the timeout will happen on the client side.
	 */
	@RequestMapping(value = "/keepAlive.html", method = RequestMethod.GET)
	public void keepAlive()
	{
		//left blank on purpose
	}
	
	
	@RequestMapping(value = "/addprofileimage.html", method = RequestMethod.GET)
	public String addProfileImage()
	{
		return "showuploadprofileImage";
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
	
	//Image.getScaledInstance()
	@RequestMapping(value = "/saveprofileimage.html", method = RequestMethod.POST)
	public String saveProfileImage(Map<String, Object> model, HttpServletRequest request, 
									 UploadItem uploadItem,
									 BindingResult result)
	{
		CommonsMultipartFile fileData = uploadItem.getFileData();
		
		if (fileData.getSize()>MAX_PROFILE_IMAGE_SIZE)
		{
			model.put(ERROR_MESSAGE,"The image you are trying to import is too large, shrink the image to 100 by 120 pixels."+(fileData.getSize()));
			return "scout";
		}
		if (result.hasErrors())
		{
			return "scout";
		}
		Scout scout = (Scout)traxService.getUserById(uploadItem.getScoutId());
		try
		{
//			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			scout = traxService.refreshScout(scout);
			importService.updateUserImage(fileData, scout);
		} 
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE,"***********Failed to import "+e.getMessage());
			return "scout";
		}
		
		return "redirect:userUpdate.html?userId="+scout.getId();
	}
	
	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public ModelAndView showLeaderForm(HttpServletRequest request, Map<String, Object> model)
	{
		Organization org = (Organization)request.getSession().getAttribute("organization");
		if (org != null)
		{
			model.put("positions", traxService.getLeaderPositions(org.getUnits().iterator().next().isCub()));
		}
		else
		{
			model.put("positions", traxService.getLeaderPositions());
		}
		Leader leader = new Leader();
		Organization organization = (Organization) request.getSession().getAttribute(ORGANIZATION);
		leader.setOrganization(organization);
		leader.setState(organization.getState());
		leader.setCity(organization.getCity());
		
		return new ModelAndView("user", "leader", leader);
	}

	@RequestMapping(value = "/addscout.html", method = RequestMethod.GET)
	public ModelAndView showScoutForm(HttpServletRequest request, Map<String, Object> model)
	{
	    //in case there is a problem create this one completely blank
		Scout scout = new Scout();
		
        try {
            model.put("positions", traxService.getScoutPositions());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            scout.setOrganization(user.getOrganization());
            scout.setState(user.getOrganization().getState());
            scout.setCity(user.getOrganization().getCity());
            scout.setUnitCopy(user.getUnit());
            scout.setZip(user.getZip());
            //needs to be a copy, not the original, that is used by someone else
            scout.setUnitCopy(user.getUnit());
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		return new ModelAndView("scout", "scout", scout);
	}

	@RequestMapping(value = "/addleader.html", method = RequestMethod.GET)
	public ModelAndView showAdditionalLeaderForm(HttpServletRequest request, Map<String, Object> model)
	{
		model.put("positions", traxService.getLeaderPositions());
		Leader leader = new Leader();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		leader.setOrganization(user.getOrganization());
		leader.setState(user.getOrganization().getState());
		leader.setCity(user.getOrganization().getCity());
		leader.setUnitCopy(user.getUnit());
		leader.setZip(user.getZip());
		return new ModelAndView("leader", "leader", leader);
	}

	@RequestMapping(value = "/verify.html", method = RequestMethod.GET)
	public ModelAndView verifyUser(@RequestParam(value = "userId", required = false) String userIdKey,
			@RequestParam(value = "passcode", required = false) String passcode) throws Exception
	{
		UserCredentialsForm form = new UserCredentialsForm();
		form.setUserId(Long.parseLong(SimpleEncrypter.decrypt(userIdKey, passcode)));
		return new ModelAndView("emailVerification", "userCredentialsForm", form);
	}
	
    @RequestMapping(value="/forgot.html", method=RequestMethod.GET)
    public ModelAndView forgot(Map model)
    {
    	// they forgot their username or password
        return new ModelAndView("forgot","user", new User());
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/forgotResponse.html", method=RequestMethod.POST)
    public String forgotResponse(HttpServletRequest request, Map model, User user, BindingResult result ) throws Exception
    {
    	List<User> realUsers = null;
    	boolean hasEmail = user.getEmail()!= null;
		if(hasEmail)
    	{
    		realUsers = traxService.getUserByEmail(user.getEmail());
    	}
    	else if(user.getUsername()!= null)
    	{
    		realUsers = new ArrayList<User>();
    		User realUser = traxService.getUserByUsername(user.getUsername());
    		if (realUser!=null)
			{
    			realUsers.add(realUser);
			}
    	}
		
    	if (realUsers == null || realUsers.size()==0)
    	{
    		model.put("errorMessage","That "+(hasEmail?"email address":"user name")+" does not exist in our system!");
    		return "forgot";
    	}
    	else
    	{
    		//the user may have multiple accounts, set flags here so we can give the best overall message
    		boolean isRetired = true;
    		boolean isEnabled = false;
    		boolean hasUsername = false;
    		boolean hasAnEmail = false;
	    	for (User realUser : realUsers)
			{
	    		if(realUser.getEmail()!=null) 
	    		{
	    			hasAnEmail = true;
	    		}
	    		if(realUser.isEnabled()) 
	    		{
		    		isEnabled = true;
	    		}
	    		if(realUser.getUsername()!=null) 
	    		{
		    		hasUsername = true;
	    		}
	    		if( ! realUser.isRetired()) 
	    		{
		    		isRetired = false;
	    		}
	    		
	    		if(realUser.getEmail()!=null && realUser.getUsername()!=null && realUser.isEnabled() && ! realUser.isRetired()) 
	    		{
		    		traxService.sendPasswordReset(realUser);
	    		}
			}
    		if ( ! hasAnEmail || ! isEnabled || ! hasUsername || isRetired)
			{
        		String message = 
        			! hasAnEmail ? "There is no email address associated with this account. Contact one of your leaders to fix this problem." :
        			! isEnabled || ! hasUsername ? "The account has not been setup. Contact one of your leaders to send you and invitation to join.":
        			isRetired ? "This account has been retired. Contact your leader to fix this problem.": "There was a problem, please contact jeff@scouttrax.org";
        		model.put("errorMessage", message);
        		return "forgot";
			}
	    	
	    	String message = "An email has been sent with login name and password reset instructions";
			model.put("successMessage", message);

    		return "forgotPasswordSuccess";
    	}
    }
    
    @RequestMapping(value="/resetPassword.html", method=RequestMethod.GET)
    public ModelAndView resetPassword(@RequestParam(value="userId", required=false) String userIdKey, 
    		@RequestParam(value="passcode", required=false) String passcode)
    {
        User user = traxService.getUserFromPasscode(userIdKey, passcode);
        UserCredentialsForm resetForm = new UserCredentialsForm(user.getId(), user.getUsername());
        return new ModelAndView("resetPassword","resetPasswordForm", resetForm);
    }

    @RequestMapping(value="/resetPassword.html", method=RequestMethod.POST)
    public String resetPassword(HttpServletRequest request,
                             Map<String, Object> model, 
                             @Valid UserCredentialsForm credentials, 
                             BindingResult result) throws Exception
    {
        userValidator.validatePasswordResetCredentials(credentials, result);

        if (result.hasErrors()) 
        {
//        	for (ObjectError error : result.getAllErrors())
//			{
//				model.put("errorMessage", error.getDefaultMessage());
//			}
            model.put("resetPasswordForm", credentials);
            return "resetPassword";
        }
        User user = traxService.saveCredentials(credentials);
        
        return "resetPasswordSuccess";
    }

    
    
	
	@RequestMapping(value = "/saveuserCredentials.html", method = RequestMethod.POST)
	public String saveCredentials(HttpServletRequest request, Map<String, Object> model,
			@Valid UserCredentialsForm credentials, BindingResult result) throws Exception
	{
		userValidator.validateCredentials(credentials, result);

		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				model.put("errorMessage", error.getDefaultMessage());
			}
			return "emailVerification";
		}
		User user = traxService.saveCredentials(credentials);

		request.getSession().setAttribute(ORGANIZATION, user.getOrganization());
		return "redirect:troopManage.html";
	}

	@RequestMapping(value = "/saveuser.html", method = RequestMethod.POST)
	public String saveMainLeader(HttpServletRequest request, Map<String, Object> model, @Valid Leader leader,
			BindingResult result
	// , @RequestParam("recaptcha_challenge_field") String challenge,
	// @RequestParam("recaptcha_response_field") String response
	) throws Exception
	{
		userValidator.validate(leader, result);

		// ReCaptchaResponse reCaptchaResponse =
		// reCaptcha.checkAnswer(request.getRemoteAddr(), challenge, response);
		if (result.hasErrors()) // || !reCaptchaResponse.isValid())
		{
			// String errMsg = reCaptchaResponse.getErrorMessage();
			// String html = reCaptcha.createRecaptchaHtml(errMsg, null);
			// System.out.println("--->" +html);
			// model.put("reCaptchaHtml", html);
			model.put("positions", traxService.getLeaderPositions());
			return "user";
		}
		try
		{
			Organization organization = (Organization) request.getSession().getAttribute(ORGANIZATION);
			organization = traxService.refreshOrganization(organization.getId());
			leader.setOrganization(organization);
			traxService.saveAndRegisterUser(leader);
		}
		catch (Exception e)
		{
			model.put("errorMessage", e.getMessage());
			model.put("positions", traxService.getLeaderPositions());
			return "user";
		}
		return "accountcreated";
	}
	
	/**
	 * don't remove this use just "retire" him
	 * @param request
	 * @param model
	 * @param user
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/retireuser.html", method = RequestMethod.GET)
	public ModelAndView retireUser(HttpServletRequest request, Map<String, Object> model, @RequestParam(value="userId", required=true)long userId) throws Exception
	{
		traxService.retireUser(userId);
		return new ModelAndView("redirect:troopManage.html");
	}

	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	@InitBinder
	public void allowEmptyDateBinding( WebDataBinder binder )
	{
		// Allow for null values in date fields.
	    binder.registerCustomEditor( Date.class, new CustomDateEditor( dateFormat, true ));
	    // tell spring to set empty values as null instead of empty string.
	    binder.registerCustomEditor( String.class, new StringTrimmerEditor( true ));
	}
	
	@ModelAttribute("unitTypes")
    public List<? extends BaseUnitType> getUnitTypes(Map<String, Object> model)
    {
	   model.put("unitTypes", traxService.getUnitTypes());
	   return traxService.getUnitTypes();
    }
	
	@RequestMapping(value = "/savescout.html", method = RequestMethod.POST)
	public String saveScout(HttpServletRequest request, Map<String, Object> model, @Valid Scout scout,
			BindingResult result) throws Exception
	{
		userValidator.validate(scout, result);

		if (result.hasErrors())
		{
			model.put("errorMessage", result.getAllErrors().iterator().next());
			model.put("positions", traxService.getScoutPositions());
			return "scout";
		}
		try
		{
			// if new scout record
			if (scout.getId() < 1)
			{
				User leader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				scout.setOrganization(leader.getOrganization());
				traxService.saveScout(scout);
			}
			else
			{
				traxService.updateScout(scout);
			}
			//reload the scouts, this prevents caching errors in the case of renaming
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Scout> scouts = traxService.getScouts(user.getOrganization().getId(), "All");
	 		request.getSession().setAttribute("scouts", scouts);
		}
		catch (Exception e)
		{
			model.put("errorMessage", e.getMessage());
			model.put("positions", traxService.getScoutPositions());
			return "scout";
		}
		return "redirect:troopManage.html";
	}

	@RequestMapping(value = "/saveleader.html", method = RequestMethod.POST)
	public String saveLeader(HttpServletRequest request, Map<String, Object> model, @Valid Leader user,
			BindingResult result) throws Exception
	{
		userValidator.validate(user, result);

		if (result.hasErrors())
		{
			model.put(ERROR_MESSAGE, result.getAllErrors());
			model.put("positions", traxService.getLeaderPositions());
			return "leader";
		}
		try
		{
			if (user.getId() < 1)
			{
				Organization organization = (Organization) request.getSession().getAttribute(ORGANIZATION);
				user.setOrganization(organization);
				traxService.saveUser(user);
			}
			else
			{
				traxService.updateLeader(user);
			}
		}
		catch (Exception e)
		{
			model.put("errorMessage", e.getMessage());
			model.put("positions", traxService.getLeaderPositions());
			return "leader";
		}
		return "redirect:troopManage.html";
	}

	@RequestMapping(value = "/userUpdate.html", method = RequestMethod.GET)
	public String userUpdate(HttpServletRequest request, Map<String, Object> model,
			@RequestParam(value = "userId", required = true) long userId)
	{
		String viewName = "";
		UploadItem ui = new UploadItem();

		User user = traxService.getUserById(userId);
		model.put("unitTypes", traxService.getUnitTypes());
		if (user instanceof Scout)
		{
			model.put("positions", traxService.getScoutPositions());
			model.put("scout", user);
			ui.setScoutId(user.getId());
			viewName = "scout";
		}
		else
		{
			model.put("positions", traxService.getLeaderPositions());
			model.put("leader", user);
			viewName = "leader";
		}
		model.put("uploadItem", ui);
		return viewName;
	}
}
