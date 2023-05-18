package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Bot {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "F:\\items\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:\\Users\\SHOWTER\\AppData\\Local\\Google\\Chrome\\User Data");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://hostednovel.com/novel/little-tyrant-doesnt-want-to-meet-with-a-bad-end/chapter-70");

        Thread.sleep(1500);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        long pageHeight1 = (long) js.executeScript("return Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
        double scrollPercentage1 = 0.20;
        long scrollOffset1 = (long) (pageHeight1 * scrollPercentage1);
        long scrollPosition1 = pageHeight1 - scrollOffset1;
        js.executeScript("window.scrollTo(0, arguments[0]);", scrollPosition1);

        int tokenCount = 0;
        while (tokenCount < 29) {
            Thread.sleep(120000);

            WebElement button1 = driver.findElement(By.xpath("//a[contains(text(), 'Next Chapter')]"));
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