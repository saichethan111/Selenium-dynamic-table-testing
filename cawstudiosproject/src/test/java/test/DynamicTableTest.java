package test;
import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonReader;
import helpers.LoadDriverProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import requests.Person;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class DynamicTableTest {

    private WebDriver driver;
    private LoadDriverProperties loadDriverProperties = new LoadDriverProperties();
    private JsonReader jsonReader = new JsonReader();
    private Logger logger;
    String dataToBeInserted;
    List<Map<String, Object>> resultMap;

    /**
     * A before method, executes right before every test method in the class
     * Setting up the web driver, reading json data is done here
     * @throws InterruptedException
     * @throws IOException
     */
    @BeforeMethod
    public void setUp() throws InterruptedException, IOException {
        String filePath = "C:\\Users\\saich\\Downloads\\digitalassetmanagement\\cawstudiosproject\\src\\test\\resources\\add_data.json";
        List<Person> testData = jsonReader.readTestData(filePath);

        dataToBeInserted = testData.toString();
        JsonNode jsonNode = jsonReader.convertStringToJson(dataToBeInserted);
        resultMap = jsonReader.convertJsonToHashMap(jsonNode);

        //Read data from application.properties file
        Properties properties = loadDriverProperties.loadProperties();

        // Set the path to driver executable
        System.setProperty(properties.getProperty("drivername"), properties.getProperty("drivervalue"));

        // Create a new instance of the Chrome driver
        driver = new ChromeDriver();

        try {
            // Maximize the browser window
            driver.manage().window().maximize();

            // Open your website
            driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");

            // Wait for the page to load
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));


        }
        catch (Exception e){
            logger.error("Error while loading the browser");
        }
    }

    /**
     * After method executes after every test method
     * Quitting the driver once we are done testing
     */
    @AfterMethod
    public void tearDown() {
        // Close the browser after the test
        driver.quit();
    }

    /**
     * Test method to test insertion, validation of json
     * @throws InterruptedException
     */
    @Test
    public void testTable() throws InterruptedException {
        WebElement clickable = driver.findElement(By.xpath("/html/body/div/div[3]/details/summary"));
        clickable.click();
        WebElement input = driver.findElement(By.xpath("//*[@id=\"jsondata\"]"));
        input.clear();
        Thread.sleep(500);
        input.sendKeys(dataToBeInserted);
        WebElement refreshClick = driver.findElement(By.xpath("//*[@id=\"refreshtable\"]"));
        refreshClick.click();
        Thread.sleep(5000);

        WebElement table = driver.findElement(By.id("dynamictable"));
        for (Map<String, Object> resultMap : resultMap) {
            String expectedName = resultMap.get("name").toString();
            String expectedAge = resultMap.get("age").toString();
            String expectedGender = resultMap.get("gender").toString();

            WebElement row = driver.findElement(By.xpath("//tr[td[text()='" + expectedName + "']]"));

            String actualName = row.findElement(By.xpath("./td[1]")).getText();
            String actualAge = row.findElement(By.xpath("./td[2]")).getText();
            String actualGender = row.findElement(By.xpath("./td[3]")).getText();

            // Perform assertions
            assertEqual("Name", expectedName, actualName);
            assertEqual("Age", expectedAge, actualAge);
            assertEqual("Gender", expectedGender, actualGender);
        }
    }

    /**
     * If the assertion fails, the message is displayed.
     * @param fieldName
     * @param expected
     * @param actual
     */
    private static void assertEqual(String fieldName, String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(fieldName + " assertion failed. Expected: " + expected + ", Actual: " + actual);
        }
    }
}
