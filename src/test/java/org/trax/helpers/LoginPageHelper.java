package org.trax.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPageHelper {

    private WebDriver driver;

    public LoginPageHelper(WebDriver driver) {
        this.driver = driver;
    }


    public void clickStartHere() {
        driver.findElement(By.className("startNow")).click();
    }

    public void setUsername(String username) {
        driver.findElement(By.id("username")).sendKeys(username);
    }

    public void setPassword(String password) {
        driver.findElement(By.id("password")).sendKeys(password);
    }

    public void clickSignIn() {
        driver.findElement(By.className("button")).click();
        //TODO change this so it isn't a sleep
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void login(String username, String password) {
        clickStartHere();
        setUsername(username);
        setPassword(password);
        clickSignIn();
    }
}
