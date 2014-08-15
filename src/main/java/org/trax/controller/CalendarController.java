package org.trax.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Badge;
import org.trax.model.BadgeConfig;
import org.trax.model.Requirement;
import org.trax.model.Scout;
import org.trax.service.TraxService;

@Controller
public class CalendarController
{
    @Autowired
    private TraxService traxService;

    @RequestMapping(value="/calendar.html", method=RequestMethod.GET)
    public ModelAndView showCalendar(Map<String, Object> model)
    {
//    	Collection<BadgeConfig> badges = traxService.getAllBadges();
        return new ModelAndView("showCalendar", model);
    }
}

