package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Bot {
    private static final String CHROME_USER_DATA_DIR = "C:\\Users\\SHOWTER\\AppData\\Local\\Google\\Chrome\\User Data";
    private static final String NOVEL_URL = "https://hostednovel.com/novel/chaotic-sword-god/chapter-528";
    private static final double SCROLL_PERCENTAGE = 0.15;
    private static final int TOKEN_COUNT_LIMIT = 25;
    private static final long PAGE_LOAD_TIMEOUT_SECONDS = 30;
    private static final long CHAPTER_WAIT_TIMEOUT_MILLISECONDS = 120000;
    private static final long SCROLL_WAIT_MILLISECONDS = 1000;

    public static void main(String[] args) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + CHROME_USER_DATA_DIR);
        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(NOVEL_URL);
        waitForPageLoad(driver);

        long pageHeight = getPageHeight(js);
        long scrollOffset = (long) (pageHeight * SCROLL_PERCENTAGE);
        long scrollPosition = pageHeight - scrollOffset;
        scrollPage(js, scrollPosition);

        int tokenCount = 0;
        while (tokenCount < TOKEN_COUNT_LIMIT) {
            Thread.sleep(CHAPTER_WAIT_TIMEOUT_MILLISECONDS);
            WebElement nextChapterButton = waitForElementToBeClickable(driver, By.xpath("//a[contains(text(), 'Next Chapter')]"));
            nextChapterButton.click();
            tokenCount++;

            pageHeight = getPageHeight(js);
            scrollOffset = (long) (pageHeight * SCROLL_PERCENTAGE);
            scrollPosition = pageHeight - scrollOffset;
            Thread.sleep(SCROLL_WAIT_MILLISECONDS);
            scrollPage(js, scrollPosition);
            System.out.println(tokenCount);
        }

        Thread.sleep(CHAPTER_WAIT_TIMEOUT_MILLISECONDS);
        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }

    private static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    private static long getPageHeight(JavascriptExecutor js) {
        return (long) js.executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
    }

    private static void scrollPage(JavascriptExecutor js, long scrollPosition) {
        js.executeScript("window.scrollTo(0, arguments[0]);", scrollPosition);
    }

    private static WebElement waitForElementToBeClickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}