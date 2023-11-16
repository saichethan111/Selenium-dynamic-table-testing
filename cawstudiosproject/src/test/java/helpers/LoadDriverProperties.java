package helpers;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class LoadDriverProperties {

    public Properties loadProperties() {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("C:\\Users\\saich\\Downloads\\digitalassetmanagement\\cawstudiosproject\\src\\test\\application.properties")) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
