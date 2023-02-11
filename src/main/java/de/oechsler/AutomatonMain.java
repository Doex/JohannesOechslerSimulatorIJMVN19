package de.oechsler;

import de.oechsler.Controller.*;
import de.oechsler.model.Automaton;
import de.oechsler.util.AutomatonIO;
import de.oechsler.view.AutomatonStage;
import de.oechsler.view.DatabaseStage;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class AutomatonMain extends Application {

    public static void main(String[] args) {
        launch(args);
        shutdownHook();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File("automata");
        if (!file.exists()) {
            file.mkdir();
        }
        AutomatonIO.createDefaultFolderAndAutomaton();
        new File("./automaton/");
        AutomatonMain.startAutomaton("DefaultAutomaton");
    }

    public static void startAutomaton(String automatonName) {
        ReferenceHandler referenceHandler = new ReferenceHandler();
        Automaton automaton = AutomatonIO.loadProgram(automatonName);
        referenceHandler.setAutomaton(automaton);
        referenceHandler.setAutomatonName(automatonName);
        referenceHandler.setAutomatonStage(new AutomatonStage(referenceHandler));
        referenceHandler.setAutomatonStageController(new AutomatonStageController(referenceHandler));
        referenceHandler.setPopulationPaneController(new PopulationPaneController(referenceHandler));
        referenceHandler.setStatesPanelController(new StatesPanelController(referenceHandler));
        automaton.add(referenceHandler.getAutomatonStage().getPopulationPane());
        referenceHandler.setSimulationController(new SimulationController(referenceHandler));
        referenceHandler.setEditorController(new EditorController(referenceHandler));
        referenceHandler.setSerializationController(new SerializationController(referenceHandler));
        referenceHandler.setXmlSerializationController(new XMLSerializationController(referenceHandler));
        referenceHandler.setDatabaseStage(new DatabaseStage());
        referenceHandler.setDatabaseController(new DatabaseController(referenceHandler));
        referenceHandler.setLanguageController(new LanguageController(referenceHandler));
        referenceHandler.setPrintController(new PrintController(referenceHandler));
    }

    private static void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                AutomatonIO.clearAutomataDirectory();
                DatabaseController.shutdown();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "shutdownHook ist schief gelaufen" +
                        "\n" + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }));
    }

}
