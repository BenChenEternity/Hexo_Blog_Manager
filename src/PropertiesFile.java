import java.io.*;
import java.util.Properties;

public class PropertiesFile {
    public static void saveToSettingsFile(String key, String value) {
        Properties props = new Properties();

        try (InputStream in = new FileInputStream("settings.properties")) {
            props.load(in);
        } catch (IOException e) {
            NewFile();
        }

        if (value == null) {
            value = "";
        }

        props.setProperty(key, value);

        try (OutputStream out = new FileOutputStream("settings.properties")) {
            props.store(out, " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String loadFromSettingsFile(String key) {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("settings.properties")) {
            props.load(in);
        } catch (IOException e) {
            saveToSettingsFile(key, null);
        }
        String value = props.getProperty(key);
        if (value != null) {
            return props.getProperty(key);
        } else {
            return "";
        }
    }

    private static void NewFile() {
        File file = new File("settings.properties");
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
