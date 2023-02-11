package de.oechsler.Controller;

import de.oechsler.model.Cell;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.*;
public class SerializationController {

    private static FileChooser fileChooser;

    static {
        fileChooser = new FileChooser();
        File dir = new File(".");
        fileChooser.setInitialDirectory(dir);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.ser", "*.ser");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public SerializationController(ReferenceHandler referenceHandler) {
        referenceHandler.getAutomatonStage().getSerialisierenMenuItem().setOnAction(e -> savePopulationPane(referenceHandler));
        referenceHandler.getAutomatonStage().getDeserialisierenMenuItem().setOnAction(e -> loadPopulationPane(referenceHandler));
    }


    public void savePopulationPane(ReferenceHandler referenceHandler) {
        fileChooser.setTitle("PopulationPane serialisiert speichern");
        File file = fileChooser.showSaveDialog(referenceHandler.getAutomatonStage());
        if (file == null) {
            return;
        }
        if (!file.getName().endsWith(".ser")) {
            file = new File(file.getAbsolutePath().concat(".ser"));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {

            synchronized (referenceHandler.getAutomaton()) {
                oos.writeObject(referenceHandler.getAutomaton().getNumberOfStates());
                oos.writeObject(referenceHandler.getAutomaton().getCells());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Das speichern der Population-Datei war erfolgreich", ButtonType.OK);
                alert.showAndWait();
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Ups, das Serialisieren ist schiefgelaufen", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void loadPopulationPane(ReferenceHandler referenceHandler) {
        fileChooser.setTitle("Serialisierte PopulationPanes laden");
        File file = fileChooser.showOpenDialog(referenceHandler.getAutomatonStage());

        if (file != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

                int noOfStates = (int) ois.readObject();
                Cell[][] cells = (Cell[][]) ois.readObject();

                if (!referenceHandler.getAutomaton().setCellsAndReturnBool(cells)
                        && referenceHandler.getAutomaton().checkNoOfStates(noOfStates)) {
                    throw new IOException();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, die Populationsdatei ist ungültig!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }


}
