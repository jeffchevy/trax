package org.trax.controller;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Scout;
import org.trax.model.Sponsor;
import org.trax.service.TraxService;

@Controller
public class ImageController {

    @Autowired
    private TraxService traxService;

    @RequestMapping("/scoutimage.html")
    public void getImage(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/png"); // "image/jpeg" ... you may need
                                              // to present the right content
                                              // type here
        if (id == 0)
            return; // TODO kludge don't do anything, should not be getting here
        Scout scout = (Scout) traxService.getUserById(id);

        byte[] profileImage = scout.getProfileImage();
        if (profileImage != null) {
            response.getOutputStream().write(profileImage, 0, profileImage.length);
            response.getOutputStream().flush();
        }
    }

    /**
     * Ajax call to Get the supporter logo if it exists
     * 
     * @param id
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/supporterimage.html")
    public void getSupporterImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/png"); // "image/jpeg" ... you may need
                                              // to present the right content
                                              // type here
        Award award = (Award) request.getSession().getAttribute(ScoutsController.AWARD);
        AwardConfig awardConfig = traxService.getAwardConfig(award.getAwardConfig().getId());

        Set<Sponsor> sponsors = awardConfig.getSponsors();
        for (Sponsor sponsor : sponsors) {
            byte[] logo = sponsor.getLogo();
            if (logo != null) {
                response.getOutputStream().write(logo, 0, logo.length);
                response.getOutputStream().flush();
            }
            break;
        }
    }
}
