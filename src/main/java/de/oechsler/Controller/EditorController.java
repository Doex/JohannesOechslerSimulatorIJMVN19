package de.oechsler.Controller;
import de.oechsler.model.Automaton;
import de.oechsler.util.AutomatonIO;
import de.oechsler.util.AutomatonTemplate;
import de.oechsler.util.Resources;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EditorController {

    private static final String PATH_AUTOMATA = "./automata/";
    private static final String JAVA_ENDING = ".java";
    private ReferenceHandler referenceHandler;

    public EditorController(ReferenceHandler referenceHandler){
        this.referenceHandler = referenceHandler;
        this.referenceHandler.getAutomatonStage().getEditor().getSaveMenuItem()
                .setOnAction(e -> this.saveCode(referenceHandler.getAutomatonStage().getEditor().getTextArea().getText()));
        this.referenceHandler.getAutomatonStage().getEditor().getEditorCompileButton().setOnAction(e -> safeAndCompileCodeAndSetNewAutomaton(referenceHandler, referenceHandler.getAutomatonStage().getEditor().getTextArea().getText()));
        this.referenceHandler.getAutomatonStage().getEditor().getTextArea().setText(getCode());
        this.referenceHandler.getAutomatonStage().getEditor().getEditorSafeButton().setOnAction(e -> this.saveCode(referenceHandler.getAutomatonStage().getEditor().getTextArea().getText()));
        this.referenceHandler.getAutomatonStage().getEditor().getEditorCompileMenuItem().setOnAction(e -> safeAndCompileCodeAndSetNewAutomaton(referenceHandler, referenceHandler.getAutomatonStage().getEditor().getTextArea().getText()));
        this.referenceHandler.getAutomatonStage().getEditor().getEditorQuitMenuItem().setOnAction(e -> referenceHandler.getAutomatonStage().getEditor().close());
    }

    private void safeAndCompileCodeAndSetNewAutomaton(ReferenceHandler referencehandler, String code){
        saveCode(code);
        if(compile(referencehandler.getAutomatonName())){
            //Automaton automaton = loadProgram(referenceHandler.getAutomatonName());
            Automaton automaton = AutomatonIO.loadProgram(referenceHandler.getAutomatonName());
            referencehandler.setAutomaton(automaton);
        }
    }

    private void saveCode(String code){
        if(this.referenceHandler.getAutomatonStage().getEditor().getTextArea().getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Das Textfeld darf nicht leer sein!", ButtonType.OK);
            alert.showAndWait();
        } else {
            code = code.replace("\n", System.lineSeparator());
            ArrayList<String> lines = new ArrayList<>();
            lines.add(code);
            try {
                Path pathTo = Paths.get(EditorController.PATH_AUTOMATA.concat(this.referenceHandler.getAutomatonName()).concat(JAVA_ENDING));
                Files.write(pathTo, lines, StandardCharsets.UTF_8);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, beim speichern des Codes ist etwas schief gelaufen!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    private String getCode(){
        try {
            Path file = Paths.get(EditorController.PATH_AUTOMATA.concat(this.referenceHandler.getAutomatonName()).concat(JAVA_ENDING));
            if (!Files.exists(file)) {
                return Resources.readResourcesFile(AutomatonTemplate.DEFAULT_TEMPLATE);
            }
            StringBuilder text = new StringBuilder();
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (int l = 0; l < lines.size(); l++) {
                text.append(lines.get(l));
                if (l < lines.size() - 1) {
                    text.append(System.lineSeparator());
                }
            }
            return text.toString();
        } catch (Exception exc) {
            return "";
        }
    }

    private boolean compile(String fileName) {
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        boolean success = javac.run(null, null, err, PATH_AUTOMATA.concat(File.separator).concat(fileName).concat(JAVA_ENDING)) == 0;
        if (!success) {
            String msg = err.toString();
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.setTitle("Compilierergebnis");
            alert.showAndWait();
            return false;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compilieren erfolgreich!", ButtonType.OK);
            alert.setTitle("Compilierergebnis");
            alert.showAndWait();
            return true;
        }
    }

    private Automaton loadProgram(String fileName) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(".").toURI().toURL() })) {
            return (Automaton) classLoader.loadClass(fileName).getConstructor().newInstance();
        } catch (IOException | InstantiationException | IllegalArgumentException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
