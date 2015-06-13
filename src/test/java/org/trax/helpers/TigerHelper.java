package org.trax.helpers;

import org.openqa.selenium.WebDriver;

public class TigerHelper {

    private WebDriver driver;

    public TigerHelper(WebDriver driver) {
        this.driver = driver;
    }

    String awardName = "Tiger Cub";
    String subtitle = "Requirements";

    public void validatePageElements() {
        NavigationHelper navigationHelper = new NavigationHelper(driver);
        navigationHelper.validateAwardTitle(awardName, subtitle);
        //TODO - add more page validations
    }


}
