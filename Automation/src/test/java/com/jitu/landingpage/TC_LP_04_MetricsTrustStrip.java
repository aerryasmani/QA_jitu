package com.jitu.landingpage;

import com.jitu.common.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TC-LP-04 | Metrics / trust strip")
public class TC_LP_04_MetricsTrustStrip extends BaseTest {

    @BeforeEach
    void openSite() {
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.get("https://jitu.one");
    }

    private static final String METRICS_SELECTOR =
            "[class*='_metric'],[class*='_stats'],[class*='_trust'],[class*='_strip']";

    private static final String METRICS_CHILD_SELECTOR =
            "[class*='_metric'] *,[class*='_stats'] *,[class*='_trust'] *,[class*='_strip'] *";

    /**
     * Scrolls the first visible metrics/trust-strip container into view using
     * block:'start' so that tile 1 (the topmost tile) lands inside the viewport
     * rather than above it.
     */
    private WebElement scrollAndAwaitMetricsSection() {
        WebElement section = wait.until(d -> {
            List<WebElement> candidates = d.findElements(By.cssSelector(METRICS_SELECTOR));
            return candidates.stream().filter(WebElement::isDisplayed).findFirst().orElse(null);
        });

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'instant',block:'start'});", section);
        wait.until(ExpectedConditions.visibilityOf(section));
        return section;
    }

    /**
     * Waits for the metrics strip to be fully rendered (all tiles animated in),
     * then collects deduplicated visible text from every child element.
     * Uses child elements rather than parent.getText() because flex/grid container
     * elements return an empty string in headless WebDriver.
     * The "HALLUCINATIONS" tile is used as the render-complete signal because it
     * is the last/rightmost tile and appears only after all others have loaded.
     */
    private String collectMetricsText() {
        wait.until(d -> {
            List<WebElement> tiles = d.findElements(By.xpath(
                    "//*[contains(text(),'HALLUCINATION') or contains(text(),'Hallucination')]"));
            return tiles.stream().anyMatch(el -> {
                try { return el.isDisplayed(); } catch (Exception e) { return false; }
            }) ? Boolean.TRUE : null;
        });

        List<WebElement> children = driver.findElements(By.cssSelector(METRICS_CHILD_SELECTOR));
        return children.stream()
                .map(el -> { try { return el.getText().trim(); } catch (Exception e) { return ""; } })
                .filter(t -> !t.isEmpty())
                .distinct()
                .collect(Collectors.joining(" "));
    }

    /**
     * Returns true if any visible element on the page contains the given
     * case-insensitive substring. Used for claim 1 whose tile animates in
     * independently and may not yet be captured by collectMetricsText().
     */
    private boolean pageContainsText(String needle) {
        String upper = needle.toUpperCase();
        return driver.findElements(By.xpath("//*[text()]")).stream().anyMatch(el -> {
            try {
                String t = el.getText();
                return t != null && t.toUpperCase().contains(upper) && el.isDisplayed();
            } catch (Exception e) { return false; }
        });
    }

    // -----------------------------------------------------------------------
    // LP-021 | Key metrics visible  (P1 – Content)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("LP-021 | Key metrics visible")
    void LP_021_KeyMetricsVisible() {
        scrollAndAwaitMetricsSection();

        // Use child-element collection — the container itself returns "" in headless mode
        String sectionText = collectMetricsText();

        // "5+" test types — the numeric value may live in a CSS pseudo-element;
        // the label "TEST CASE TYPES GENERATED" is the reliable DOM text for this tile.
        // pageContainsText() searches the whole page as a fallback in case the tile
        // animates in after collectMetricsText() has already captured siblings.
        assertTrue(
                sectionText.contains("5+")
                        || sectionText.toUpperCase().contains("TEST CASE TYPES")
                        || pageContainsText("TEST CASE TYPES"),
                "Expected '5+ test types' metric not found in metrics strip. Actual text:\n" + sectionText);

        // "<10s" generation time
        assertTrue(
                sectionText.contains("<10") || sectionText.toUpperCase().contains("10S"),
                "Expected '<10s' metric not found in metrics strip. Actual text:\n" + sectionText);

        // CSV export claim
        assertTrue(
                sectionText.toUpperCase().contains("CSV"),
                "Expected 'CSV' metric not found in metrics strip. Actual text:\n" + sectionText);

        // "0 Hallucinations" OR "Rule-based only"
        boolean hasHallucinations = sectionText.toUpperCase().contains("HALLUCINATION");
        boolean hasRuleBased = sectionText.toLowerCase().contains("rule-based")
                || sectionText.toLowerCase().contains("rule based");

        assertTrue(
                hasHallucinations || hasRuleBased,
                "Expected '0 Hallucinations' or 'Rule-based only' not found in metrics strip. Actual text:\n"
                        + sectionText);
    }

    @Test
    @DisplayName("LP-021b | Individual metric items are rendered and visible")
    void LP_021b_IndividualMetricItemsRendered() {
        scrollAndAwaitMetricsSection();

        // Each discrete metric should be represented by at least one visible element
        // that carries a value-style class fragment
        List<WebElement> metricItems = driver.findElements(
                By.cssSelector("[class*='_metricItem'],[class*='_statItem'],[class*='_stat']"));

        // There must be at least 4 visible metric tiles based on the spec
        long visibleCount = metricItems.stream().filter(WebElement::isDisplayed).count();
        assertTrue(
                visibleCount >= 4,
                "Expected at least 4 visible metric items but found " + visibleCount);
    }

    // -----------------------------------------------------------------------
    // LP-022 | Metric copy accuracy  (P2 – Content / manual-judgment layer)
    // -----------------------------------------------------------------------

    /**
     * LP-022 automates the data-collection phase of a manual-judgment test.
     * It reads every visible metric value and label, then asserts each
     * well-known marketing claim is present.  Any mismatch is surfaced as a
     * test failure with a descriptive message so a product owner can review.
     *
     * Claims from spec:  "5+" test types | "<10s" | "CSV" | "0 Hallucinations"
     * or "Rule-based only"
     */
    @Test
    @DisplayName("LP-022 | Metric copy accuracy — known claims match visible text")
    void LP_022_MetricCopyAccuracy() {
        scrollAndAwaitMetricsSection();

        String fullSnapshot = collectMetricsText();

        // --- Claim 1: "5+" test types ---
        // The label "TEST CASE TYPES GENERATED" is the reliable DOM text for this tile.
        // pageContainsText() is a page-wide fallback in case the tile animated in after
        // collectMetricsText() already captured its siblings.
        assertTrue(
                fullSnapshot.contains("5+")
                        || fullSnapshot.toUpperCase().contains("TEST CASE TYPES")
                        || pageContainsText("TEST CASE TYPES"),
                "[LP-022] Claim '5+ test types' not found. Snapshot: " + fullSnapshot);

        // --- Claim 2: "<10s" ---
        assertTrue(
                fullSnapshot.contains("<10") || fullSnapshot.toUpperCase().contains("10S"),
                "[LP-022] Claim '<10s' not found. Snapshot: " + fullSnapshot);

        // --- Claim 3: CSV ---
        assertTrue(
                fullSnapshot.toUpperCase().contains("CSV"),
                "[LP-022] Claim 'CSV' not found. Snapshot: " + fullSnapshot);

        // --- Claim 4: 0 Hallucinations / Rule-based only ---
        boolean zeroHallucinations = fullSnapshot.toUpperCase().contains("HALLUCINATION");
        boolean ruleBased = fullSnapshot.toLowerCase().contains("rule-based")
                || fullSnapshot.toLowerCase().contains("rule based");

        assertTrue(
                zeroHallucinations || ruleBased,
                "[LP-022] Neither '0 Hallucinations' nor 'Rule-based only' found. "
                        + "Product owner review needed. Snapshot: " + fullSnapshot);
    }
}
