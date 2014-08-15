package org.trax.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class TraxAjaxViewResolver implements ModelAndViewResolver
{
    public ModelAndView resolveModelAndView(Method handlerMethod, Class handlerType, Object returnValue, ExtendedModelMap implicitModel,
            NativeWebRequest webRequest)
    {
        Annotation[] methodAnnotations = handlerMethod.getDeclaredAnnotations();
       
        if (methodAnnotations.length > 0 && methodAnnotations[methodAnnotations.length -1] instanceof ResponseBody)
        {
            ModelAndView mav = new ModelAndView();
            MappingJacksonJsonView view = new MappingJacksonJsonView();
            mav.setView(view);
            mav.addObject(returnValue);
            return mav;
        }
        return UNRESOLVED;
    }
}
