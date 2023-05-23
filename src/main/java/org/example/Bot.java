package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class Bot {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "F:\\items\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\SHOWTER\\AppData\\Local\\Google\\Chrome\\User Data");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://hostednovel.com/novel/little-tyrant-doesnt-want-to-meet-with-a-bad-end/chapter-311");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Thread.sleep(1000);


        long pageHeight1 = (long) js.executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
        double scrollPercentage1 = 0.20;
        long scrollOffset1 = (long) (pageHeight1 * scrollPercentage1);
        long scrollPosition1 = pageHeight1 - scrollOffset1;
        js.executeScript("window.scrollTo(0, arguments[0]);", scrollPosition1);
        Thread.sleep(1000); // Add a delay before clicking the button


        int tokenCount = 0;
        while (tokenCount <75) {
            Thread.sleep(120000);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            WebElement button1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Next Chapter')]")));
            button1.click();
            tokenCount++;

            Thread.sleep(1500);

            long pageHeight = (long) js.executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
            double scrollPercentage = 0.15;
            long scrollOffset = (long) (pageHeight * scrollPercentage);
            long scrollPosition = pageHeight - scrollOffset;
            js.executeScript("window.scrollTo(0, arguments[0]);", scrollPosition);
            System.out.println(tokenCount);
        }

        Thread.sleep(120000);
        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }
}