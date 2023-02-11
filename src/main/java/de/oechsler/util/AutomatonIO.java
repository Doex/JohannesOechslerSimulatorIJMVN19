package de.oechsler.util;
import de.oechsler.model.Automaton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Objects;

public class AutomatonIO {

    static final String AUTOMATON_DIR = "automata/";
    static final String JAVA_EXTENSION = ".java";
    static final String DEFAULT_NAME = "DefaultAutomaton";
    static final String GOL_DEFAULT_NAME = "GameOfLifeDefault";
    static final String KRUE_DEFAULT_NAME = "KruemelmonsterDefault";

    public static void createDefaultFolderAndAutomaton() throws IOException {
        File automatonDir = new File("automata");
        if (!automatonDir.exists()) {
            automatonDir.mkdirs();
        }
        createFileWithGivenNameInDir(DEFAULT_NAME);
        createAllAutomatonsForStart();
    }

    public static void createFileWithGivenNameInDir(String automatonName) throws IOException {
        File automatonFile = new File(AUTOMATON_DIR + File.separator + automatonName + JAVA_EXTENSION);
        if (!automatonFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(automatonFile.getAbsoluteFile())) {
                String dummyClassTemplate = AutomatonTemplate.DEFAULT_TEMPLATE.replace("{0}", automatonName);
                fileWriter.write(dummyClassTemplate);
            }
            compile(automatonName);

        } else {
            if (!automatonFile.getName().equals("DefaultAutomaton.java")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, dieser Name existiert bereits! ", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    public static void createAllAutomatonsForStart() throws IOException {
        File golFile = new File(AUTOMATON_DIR + File.separator + GOL_DEFAULT_NAME + JAVA_EXTENSION);
        File krueFile = new File(AUTOMATON_DIR + File.separator + KRUE_DEFAULT_NAME + JAVA_EXTENSION);
        if (!golFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(golFile.getAbsoluteFile())) {
                String dummyClassGOLTemplate = AutomatonTemplate.GOL_TEMPLATE.replace("{0}", GOL_DEFAULT_NAME);
                fileWriter.write(dummyClassGOLTemplate);
            }
        } else {
            if (!golFile.getName().equals(GOL_DEFAULT_NAME + JAVA_EXTENSION)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, dieser Name existiert bereits - GOLDefaultName! ", ButtonType.OK);
                alert.showAndWait();
            }
        }
        if (!krueFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(krueFile.getAbsoluteFile())) {
                String dummyClassGOLTemplate = AutomatonTemplate.KRUEMELMONSTER_TEMPLATE.replace("{0}", KRUE_DEFAULT_NAME);
                fileWriter.write(dummyClassGOLTemplate);
            }
        } else {
            if (!krueFile.getName().equals(KRUE_DEFAULT_NAME + JAVA_EXTENSION)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, dieser Name existiert bereits - KruemelmosterDefaultName ", ButtonType.OK);
                alert.showAndWait();
            }
        }
        compile(GOL_DEFAULT_NAME);
        compile(KRUE_DEFAULT_NAME);
    }

    public static void clearAutomataDirectory() throws IOException {
        File automatonDirHandle = new File("automata");
        if (automatonDirHandle.exists() && automatonDirHandle.isDirectory()) {
            for (File f : Objects.requireNonNull(automatonDirHandle.listFiles())) {
                Files.delete(f.toPath());
            }
        }
    }

    private static void compile(String automatonClassName) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        String pathToAutomatonFile = AUTOMATON_DIR + File.separator + automatonClassName + JAVA_EXTENSION;
        boolean succ = javaCompiler.run(null, null, err, pathToAutomatonFile) == 0;
        if (!succ) {
            System.out.println(err);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, das compilieren ist schief gelaufen\n" + err, ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static Automaton loadProgram(String automataClassName) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(AUTOMATON_DIR).toURI().toURL()})) {
            return (Automaton) classLoader.loadClass(automataClassName).getConstructor().newInstance();
        } catch (IOException | InstantiationException | IllegalArgumentException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
