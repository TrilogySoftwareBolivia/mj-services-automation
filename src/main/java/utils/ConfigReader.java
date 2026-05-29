package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration values from testdata.properties.
 * Environment variables take precedence over file values.
 * Key conversion: lowercase with dots → uppercase with underscores.
 * Example: "login.username" → env var "LOGIN_USERNAME"
 *
 * @author Maribel Aiza
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("testdata.properties")) {
            if (is == null) {
                throw new RuntimeException(
                        "Cannot load testdata.properties: file not found on classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot load testdata.properties: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the value for the given key.
     * Checks the environment variable override first (key uppercased, dots replaced
     * with underscores), then falls back to the value in testdata.properties.
     *
     * @param key property key (e.g. "login.username")
     * @return the resolved value, or {@code null} if neither source has the key
     */
    public static String get(String key) {
        String envVarName = key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envVarName);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        return props.getProperty(key);
    }
}
