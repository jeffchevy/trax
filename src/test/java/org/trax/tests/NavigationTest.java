package org.trax.tests;

import org.testng.annotations.Test;
import org.trax.helpers.BobcatHelper;
import org.trax.helpers.LoginPageHelper;
import org.trax.helpers.NavigationHelper;
import org.trax.helpers.TigerHelper;

public class NavigationTest extends BaseTest {

    @Test
    public void Navigation() throws Exception {

        //Login
        driver.get(baseUrl);
        LoginPageHelper loginPageHelper = new LoginPageHelper(driver);
        loginPageHelper.login("pack", "pack");

        //Validate Navigation items
        NavigationHelper navigationHelper = new NavigationHelper(driver);

        navigationHelper.clickTiger();
        TigerHelper tigerHelper = new TigerHelper(driver);
        tigerHelper.validatePageElements();

        navigationHelper.clickBobcat();
        BobcatHelper bobcatHelper = new BobcatHelper(driver);
        bobcatHelper.validatePageElements();


        //TODO finish other page helpers
        navigationHelper.clickWolf();
        navigationHelper.clickBear();
        navigationHelper.clickWebelos();
        navigationHelper.clickArrowOfLight();
        navigationHelper.clickBeltLoops();
        navigationHelper.clickPins();
        navigationHelper.clickFaith();
        navigationHelper.clickAwards();

    }
}
