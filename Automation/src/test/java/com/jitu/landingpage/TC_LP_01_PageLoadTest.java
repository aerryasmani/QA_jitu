package com.jitu.landingpage; //This is basically your file path. For the context of it's after the java\ is the package com.jitu.[FolderName]

import com.jitu.common.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TC-LP-01 | Page Load")
public class TC_LP_01_PageLoadTest extends BaseTest {

    @BeforeEach
    void openSite() {
        driver.get("https://jitu.one");
    }

    @Test
    @DisplayName("LP-001 | Page title is not blank")
    void LP_001_pageTitleIsNotBlank() {
        String title = driver.getTitle();
        assertFalse(title.isBlank());
    }

    @Test
    @DisplayName("LP-002 | Page title contains Jitu")
    void LP_002_pageTitleContainsJitu() {
        String title = driver.getTitle();
        assertTrue(title.toLowerCase().contains("jitu"));
    }

    @Test
    @DisplayName("LP-003 | Logo is visible")
    void LP_003_logoIsVisible() {
        WebElement logo = driver.findElement(By.cssSelector("a._logo_236fd_14"));
        assertTrue(logo.isDisplayed());
    }
}