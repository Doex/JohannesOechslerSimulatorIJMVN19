package de.oechsler.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesController {

    private static final String PROPFILE = "de.oechsler.properties";

    private static PropertiesController propertiesController = null;

    public static PropertiesController getPropertiesController() {
        if (propertiesController.propertiesController == null) {
            propertiesController.propertiesController = new PropertiesController();
        }
        return propertiesController.propertiesController;
    }

    private static final String PROP_LANGUAGE = "language";
    private static final String DEF_LANGUAGE = null;

    private static final String PROP_SPEED = "speed";
    private static final int DEF_SPEED = SimulationController.DEF_SPEED;

    private Properties prop = new Properties();

    private PropertiesController() {
        this.prop = new Properties();
        try (FileInputStream propfile = new FileInputStream(propertiesController.PROPFILE)) {
            this.prop.load(propfile);
        } catch (IOException e) {
        }
    }

    public String getLanguage() {
        String lang = this.prop.getProperty(propertiesController.PROP_LANGUAGE);
        return lang == null ? DEF_LANGUAGE : lang;
    }

    public int getSpeed() {
        String speed = this.prop.getProperty(propertiesController.PROP_SPEED);
        if (speed == null) {
            return DEF_SPEED;
        }
        try {
            return Integer.parseInt(speed);
        } catch (NumberFormatException exc) {
            return DEF_SPEED;
        }
    }
}
