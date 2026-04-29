package com.jitu.landingpage;

import com.jitu.common.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TC-LP-04 | Metrics / trust strip")
public class TC_LP_04_MetricsTrustStrip extends BaseTest {

        @BeforeEach
        void openSite() {
                driver.get("https://jitu.one");
        }

        @Test
        @DisplayName("LP-021 | Key metrics visible")
        void LP_021_keyMetricsVisible() {
                WebElement metricsStrip = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.cssSelector("div[class*='_statsBar']")));

                assertTrue(metricsStrip.isDisplayed(), "Metrics strip should be visible on the page");

                String metricsText = metricsStrip.getText();

                assertTrue(metricsText.contains("5+"),
                                "Metric '5+' (test types) should be visible in the strip");
                assertTrue(metricsText.contains("<10s"),
                                "Metric '<10s' (generation speed) should be visible in the strip");
                assertTrue(metricsText.contains("CSV"),
                                "Metric 'CSV' (export format) should be visible in the strip");
                assertTrue(metricsText.contains("HALLUCINATIONS"),
                                "Metric '0 Hallucinations / Rule-based only' should be visible in the strip");
        }

        @Test
        @DisplayName("LP-022 | Metric copy accuracy")
        void LP_022_metricCopyAccuracy() {
                WebElement metricsStrip = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.cssSelector("div[class*='_statsBar']")));

                assertTrue(metricsStrip.isDisplayed(), "Metrics strip should be present for copy review");

                List<WebElement> metricItems = metricsStrip.findElements(
                                By.cssSelector("[class*='_statItem']"));

                System.out.println("=== LP-022 Metric Copy Audit ===");
                System.out.println("Total metric items found: " + metricItems.size());
                for (int i = 0; i < metricItems.size(); i++) {
                        System.out.println("Metric [" + (i + 1) + "]: " + metricItems.get(i).getText());
                }
                System.out.println("Full strip text:\n" + metricsStrip.getText());
                System.out.println("=== End of audit — manual review required ===");

                assertFalse(metricsStrip.getText().isBlank(),
                                "Metrics strip should contain visible copy — was blank/empty");
        }
}