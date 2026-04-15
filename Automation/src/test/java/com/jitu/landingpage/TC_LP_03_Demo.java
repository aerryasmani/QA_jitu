package com.jitu.landingpage;

import com.jitu.common.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TC-LP-03 | Demo / output preview block")
public class TC_LP_03_Demo extends BaseTest {

    @BeforeEach
    void openSite() {
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.get("https://jitu.one");
    }

    @Test
    @DisplayName("LP-020 | Demo panel shows sample output")
    void LP_020_DemoPanelShowsSampleOutput() {

        // give headless Chrome a real viewport so IntersectionObserver fires correctly
        // then scroll the outer terminal panel into the center of the viewport
        WebElement terminal = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div[class*='_terminal']")
            )
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior:'instant',block:'center'});", terminal);

        // wait until _terminalBody is rendered (offsetHeight > 0)
        // using offsetHeight instead of isDisplayed() so CSS animations don't cause false negatives
        WebElement body = wait.until(d -> {
            WebElement el = d.findElement(By.cssSelector("div[class*='_terminalBody']"));
            Number h = (Number) ((JavascriptExecutor) d)
                .executeScript("return arguments[0].offsetHeight;", el);
            return (h != null && h.intValue() > 0) ? el : null;
        });
    
        // --- Terminal title ---
        WebElement terminalTitle = terminal.findElement(
            By.cssSelector("span[class*='_terminalTitle']")
        );
        assertTrue(terminalTitle.getText().contains("jitu / output.json"));
    
        // --- Input comment ---
        WebElement inputComment = body.findElement(
            By.cssSelector("div[class*='_tComment']")
        );
        assertTrue(inputComment.isDisplayed());
        assertTrue(inputComment.getText().contains("login"));
    
        // --- Type tags ---
        WebElement positiveTag = body.findElement(By.cssSelector("span[class*='_tTagPos']"));
        assertTrue(positiveTag.isDisplayed());
        assertTrue(positiveTag.getText().contains("POSITIVE"));
    
        WebElement negativeTag = body.findElement(By.cssSelector("span[class*='_tTagNeg']"));
        assertTrue(negativeTag.isDisplayed());
        assertTrue(negativeTag.getText().contains("NEGATIVE"));
    
        WebElement edgeTag = body.findElement(By.cssSelector("span[class*='_tTagEdge']"));
        assertTrue(edgeTag.isDisplayed());
        assertTrue(edgeTag.getText().contains("EDGE"));
    
        // --- TC IDs present ---
        List<WebElement> vals = body.findElements(By.cssSelector("span[class*='_tVal']"));
        List<String> allText = vals.stream().map(WebElement::getText).toList();
        assertTrue(allText.stream().anyMatch(t -> t.contains("TC-001")));
        assertTrue(allText.stream().anyMatch(t -> t.contains("TC-002")));
        assertTrue(allText.stream().anyMatch(t -> t.contains("TC-003")));
    
        // --- Progress hint ---
        WebElement progressHint = body.findElement(By.cssSelector("div[class*='_typingCursor']"));
        assertTrue(progressHint.isDisplayed());
        assertTrue(progressHint.getText().contains("generating"));
    }

}