package org.trax.helpers;

import org.openqa.selenium.WebDriver;

public class BobcatHelper {

    private WebDriver driver;

    public BobcatHelper(WebDriver driver) {
        this.driver = driver;
    }

    String awardName = "Bobcat";
    String subtitle = "Requirements";

    public void validatePageElements() {
        NavigationHelper navigationHelper = new NavigationHelper(driver);
        navigationHelper.validateAwardTitle(awardName, subtitle);
        //TODO - add more page validations
    }


}
