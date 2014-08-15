package org.trax.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.trax.form.UserCredentialsForm;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.service.TraxService;

@Component
public class UserValidator implements Validator
{
	@Autowired
	private TraxService traxService;

	public boolean supports(Class clazz)
	{
		if (Scout.class.isInstance(clazz))
		{
			return false; // don't validate
		}
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors)
	{
		User user = (User) target;
		if (user.getUnit()!= null && user.getUnit().getNumber()==null)
		{
			errors.rejectValue("unit.number", "user.unit.number.required");
		}
		
		if (traxService != null)
		{
			User duplicate = traxService.getUserByUsername(user.getUsername());

			if (duplicate != null)
			{
				errors.rejectValue("username", "user.unique.username");
			}

		}
	}

	public void validateCredentials(Object target, Errors errors)
	{
		UserCredentialsForm form = (UserCredentialsForm) target;
		if (traxService != null)
		{
			User duplicate = traxService.getUserByUsername(form.getUsername());

			if (duplicate != null)
			{
				errors.rejectValue("username", "user.unique.username");
			}
			if (!form.getPassword().equals(form.getConfirmPassword()))
			{
				errors.rejectValue("password", "user.password.mismatch");
			}
		}
	}
	public void validatePasswordResetCredentials(Object target, Errors errors)
	{
		UserCredentialsForm form = (UserCredentialsForm) target;
		if (traxService != null)
		{
			if (!form.getPassword().equals(form.getConfirmPassword()))
			{
				errors.rejectValue("password", "user.password.mismatch");
			}
		}
	}
}
