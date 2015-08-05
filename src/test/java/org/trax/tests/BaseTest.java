package org.trax.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    public WebDriver driver;
    public String baseUrl = "http://localhost:8080/trax";

    @BeforeClass
    public void before() {
        driver = new FirefoxDriver();
    }

    @AfterClass
    public void after() {
        driver.quit();
    }

}
