package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;

public class Bot {
    private static final String CHROME_USER_DATA_DIR = "C:\\Users\\Александр\\AppData\\Local\\Google\\Chrome\\User Data";
    private static String NOVEL_URL;
    private static final double SCROLL_PERCENTAGE = 0.15;
    private static final int TOKEN_COUNT_LIMIT = 45;
    private static final long PAGE_LOAD_TIMEOUT_SECONDS = 30;
    private static final long SCROLL_WAIT_MILLISECONDS = 2000;
    private static final String PAGE_HEIGHT_FETCH_SCRIPT = "return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);";
    private static final String PAGE_SCROLL_SCRIPT = "window.scrollTo(0, arguments[0]);";
    private static final String DOCUMENT_READY_STATE_CHECK_SCRIPT = "return document.readyState";
    private static final String DOCUMENT_READY_STATE_COMPLETE = "complete";
    private static Thread adBlockerHandlerThread;
    private static void handleAdblockerModal(WebDriver driver) {
        while (true) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("x0qsjk")));
                WebElement closeButton = modal.findElement(By.className("es7erd"));
                closeButton.click();
            } catch (TimeoutException e) {
//                System.out.println("No adblocker modal found");
            }

            try {
                Thread.sleep(500); // Wait 1 second before the next check
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NOVEL_URL = loadURLFromTextFile();
        WebDriver driver = initializeBrowser();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Start a new thread to handle the adblocker modal
        adBlockerHandlerThread = new Thread(() -> handleAdblockerModal(driver));
        adBlockerHandlerThread.setName("AdBlockerHandlerThread");
        adBlockerHandlerThread.start();

        automateBrowser(driver, js);
    }
    private static String loadURLFromTextFile() {
        String url = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))){
            url = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static void updateURLInTextFile(String url) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"))) {
            writer.write(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static WebDriver initializeBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + CHROME_USER_DATA_DIR);
        return new ChromeDriver(options);
    }

    private static void automateBrowser(WebDriver driver, JavascriptExecutor js) throws InterruptedException {
        driver.get(NOVEL_URL);
        waitForPageLoad(driver);
        int tokenCount = 0;
        while (tokenCount < TOKEN_COUNT_LIMIT) {
            tokenCount++;
            performReading(driver, js);
            NOVEL_URL = driver.getCurrentUrl();  //getting current URL
            updateURLInTextFile(NOVEL_URL); //updating the URL in properties file
            System.out.println(tokenCount);
        }
        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }

    private static void performReading(WebDriver driver, JavascriptExecutor js) throws InterruptedException {
        long pageHeight = getPageHeight(js);
        long scrollOffset = (long) (pageHeight * SCROLL_PERCENTAGE);
        long scrollPosition = pageHeight - scrollOffset;
        Thread.sleep(SCROLL_WAIT_MILLISECONDS);
        scrollPage(js, scrollPosition);
        Thread.sleep(90000);

        WebElement nextChapterButton = waitForElementToBeClickable(driver, By.xpath("//a[contains(text(), 'Next Chapter ›')]"));
        js.executeScript("arguments[0].click();", nextChapterButton);
    }

    private static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript(DOCUMENT_READY_STATE_CHECK_SCRIPT).equals(DOCUMENT_READY_STATE_COMPLETE));
    }

    private static long getPageHeight(JavascriptExecutor js) {
        return (long) js.executeScript(PAGE_HEIGHT_FETCH_SCRIPT);
    }

    private static void scrollPage(JavascriptExecutor js, long scrollPosition) {
        js.executeScript(PAGE_SCROLL_SCRIPT, scrollPosition);
    }

    private static WebElement waitForElementToBeClickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}