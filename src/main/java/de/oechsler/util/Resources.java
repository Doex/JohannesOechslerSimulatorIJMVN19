package de.oechsler.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;

public class Resources {

    public static String path;

    static {
        try {
            URI uri = Resources.class.getResource("/resources").toURI();
            if (uri.getScheme().equals("jar")) {
                path = "/resources/";
            } else {
                path = "/";
            }
        } catch (Exception exc) {
            path = "/";
        }
    }

    public static String readResourcesFile(String resourcesFiles) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(Resources.class.getResourceAsStream(path + resourcesFiles))))) {
            StringBuilder buffer = new StringBuilder();
            String input;
            while ((input = in.readLine()) != null) {
                buffer.append(input);
                buffer.append(System.lineSeparator());
            }
            return buffer.toString();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return "";
    }

}
