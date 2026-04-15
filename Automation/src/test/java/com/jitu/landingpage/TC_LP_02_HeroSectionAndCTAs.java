package com.jitu.landingpage;

import com.jitu.common.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions; // [ADDED] needed for explicit wait conditions
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TC-LP-02 | Hero section and CTAs")
public class TC_LP_02_HeroSectionAndCTAs extends BaseTest {

    @BeforeEach
    void openSite() {
        driver.get("https://jitu.one");
    }

    @Test
    @DisplayName("LP-006 | Hero headline visible")
    void LP_006_heroHeadlineVisible(){
        WebElement heroHeadline = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[class*='_heroTitle']"))
        );
        assertTrue(heroHeadline.getText().contains("Write"));
    }

    @Test
    @DisplayName("LP-007 | Hero subheading is present and visible")
    void LP_007_heroSubheadingIsPresentAndVisible() {
        WebElement Subhero = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p[class*='_heroTagline']"))
        );
        assertTrue(Subhero.getText().contains("From"));
    }

    @Test
    @DisplayName("LP-008 | Prime CTA is present and visible")
    void LP_008_primeCTAIsPresentAndVisible() {
        WebElement btnPrime = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='/dashboard']"))
        );
        assertTrue(btnPrime.isDisplayed());
        assertEquals("Start generating", btnPrime.getText());
    }

    @Test
    @DisplayName("LP-009 | Secondary CTA is present and visible")
    void LP_009_secondaryCTAIsPresentAndVisible() {
        WebElement btnSecondary = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href='#how']"))
        );
        assertTrue(btnSecondary.isDisplayed());
        assertEquals("See how it works", btnSecondary.getText());
    }

}