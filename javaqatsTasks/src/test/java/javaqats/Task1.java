package javaqats;
import org.junit.jupiter.api.AfterEach;
import java.lang.RuntimeException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
class Task1 {
    String url = "https://react-redux.realworld.io/";
    String email = "tech_task@qats.sk";        //credentials should not be hardcoded in repository
    String password = "124lkjAF89as";
    String kec = "test";
    String signInPath = "//*[@id='main']//li[2]/a"; //nav li:nth-child(2) css alternative locator
    String emailPath ="//*[@id='main']//fieldset[1]/input";
    String passwordPath ="//*[@id='main']//fieldset[2]/input";
    String loginButtonPath = "//*[@id='main']//fieldset/button";
    String postMessage = "//nav[@class='navbar navbar-light']//li[2]/a";
    String articlePath = "//*[@id='main']//textarea";
    String titlePath = "//div[@class='banner']//h1";
    String settingsBtnPath = "//nav[@class='navbar navbar-light']//li[3]/a";
    String msgLink = "//*[@id='main']//div[2]/div[1]/a/h1";
    String GlobalFeedPath = "//*[@id='main']//div[1]/div[1]/ul/li[2]/a";
    String delMsg = "//button[@class='btn btn-outline-danger btn-sm']";
    WebDriver driver;

    void login(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleContains("Conduit"));
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(signInPath))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(emailPath))).sendKeys(email);
        driver.findElement(By.xpath(passwordPath)).sendKeys(password);
        driver.findElement(By.xpath(loginButtonPath)).click();
    }
    void logout(WebDriver driver)
    {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(settingsBtnPath))).click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='main']/div/div/div/div/div/button"))).click();
        String element2 = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(postMessage))).getText();
        assertEquals("Sign in", element2, "Logout not successful");
    }
    @Test void TestCase1() {
        try{
            driver.get(url);
            login(driver);
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='main']/div/div//li[2]/a")));
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(postMessage))).click();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(emailPath))).sendKeys(kec); //title
            driver.findElement(By.xpath(passwordPath)).sendKeys(kec); //topic
            driver.findElement(By.xpath(articlePath)).sendKeys(kec); //article
            driver.findElement(By.xpath(loginButtonPath)).click(); //post article
            String title =  new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(titlePath))).getText();
            assertEquals(kec, title, "Post's title (\""+title+"\") doesn't match the title given by instructions (\""+kec+"\")");//asserting post was created with the right title
            logout(driver);
        }
        catch(Exception e)
        {}
    }
    @Test void TestCase2(){
        try{
            driver.get(url);
            login(driver);
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(GlobalFeedPath))).click();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.textToBe(By.xpath(msgLink), kec));//Because another element from previous page has same xpath and css.Waits for the right post to be shown plus asserts the creation of the post from TestCase1
            //post deletion
            driver.findElement(By.xpath(msgLink)).click();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(delMsg))).click();
            //asserting post deletion
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GlobalFeedPath))).click();
            String msg = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(msgLink))).getText();
            assertNotSame(kec, msg, "Post deletion was not successful");
            logout(driver);
        }
        catch (Exception e)
        {}
    }
    @AfterEach
    void DriverKill()
    {
        driver.quit();
        driver = null;
    }
    @BeforeEach
    void DriverSetup()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
}

