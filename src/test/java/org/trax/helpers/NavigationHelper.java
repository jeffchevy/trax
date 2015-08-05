package org.trax.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationHelper {

    private WebDriver driver;

    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
    }

    public void clickBobcat() {
        clickMenuWithValidation("nav-bobcatrank", "Bobcat Badge", "Bobcat");
    }

    public void clickTiger() {
        clickMenuWithValidation("nav-tigerrank", "Tiger Cub Badge", "Tiger");
    }

    public void clickWolf() {
        clickMenuWithValidation("nav-wolfrank", "Wolf", "Wolf");
    }

    public void clickBear() {
        clickMenuWithValidation("nav-bearrank", "Bear", "Bear");
    }

    public void clickWebelos() {
        clickMenuWithValidation("nav-webelosrank", "Webelos Award", "Webelos");
    }

    public void clickArrowOfLight() {
        clickMenuWithValidation("nav-arrowoflightrank", "Arrow Of Light", "Arrow Of Light");
    }

    public void clickBeltLoops() {
        clickMenuWithValidation("nav-beltloops", "Belt Loops", "Belt Loops");
    }

    public void clickPins() {
        clickMenuWithValidation("nav-pins", "Pins", "Pins");
    }

    public void clickFaith() {
        clickMenuWithValidation("nav-faith", "Faith", "Faith");
    }

    public void clickAwards() {
        clickMenuWithValidation("nav-award", "Other Awards", "Awards");
    }


    private void clickMenuWithValidation(String linkId, String expectedLinkTitle, String expectedLinkText) {
        driver.findElement(By.id(linkId)).findElement(By.cssSelector("a")).click();

        String linkText = driver.findElement(By.id(linkId)).getText();
        String linkTitle = driver.findElement(By.id(linkId)).findElement(By.cssSelector("a")).getAttribute("title");

        assert linkText.equals(expectedLinkText) : "The Link text " + linkText + " did not equal " + expectedLinkText;
        assert linkTitle.equals(expectedLinkTitle) : "The Link title " + linkTitle + " did not equal " + expectedLinkTitle;

    }

    public void validateAwardTitle(String awardName, String subtitle) {
        assert driver.findElement(By.id("awardname")).getText().equals(awardName.toUpperCase()) : "Page award title " +
                driver.findElement(By.id("awardname")).getText() + " did not equal " + awardName;
        assert driver.findElement(By.id("subtitle")).getText().equals(subtitle) : "Page subtitle " +
                driver.findElement(By.id("subtitle")).getText() + " did not equal " + subtitle;
    }
}
