package com.jitu.landingpage;

import com.jitu.common.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TC-LP-02 | Hero section and CTAs")
public class TC_LP_02_HeroSectionAndCTAs extends BaseTest {

    @BeforeEach
    void openSite() {
        driver.get("https://jitu.one");
    }

    @Test
    @DisplayName("LP-004 | Hero headline visible")
    void LP_004_heroHeadlineVisible() {

    }

    @Test
    @DisplayName("LP-005 | Hero subheading is present and visible")
    void LP_005_heroSubheadingIsPresentAndVisible() {

    }

    @Test
    @DisplayName("LP-006 | Prime CTA is present and visible")
    void LP_006_primeCTAIsPresentAndVisible() {

    }

    @Test
    @DisplayName("LP-007 | Secondary CTA is present and visible")
    void LP_007_secondaryCTAIsPresentAndVisible() {

    }

    @Test
    @DisplayName("LP-008 | Content is present and visible")
    void LP_008_contentIsPresentAndVisible() {

    }
}