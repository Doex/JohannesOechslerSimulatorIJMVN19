package de.oechsler.Controller;

import de.oechsler.AutomatonMain;
import de.oechsler.util.AutomatonIO;
import de.oechsler.view.Editor;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AutomatonStageController {

    private final ReferenceHandler referenceHandler;
    private int scaleCounterIn;
    private int scaleCounterOut;

    public AutomatonStageController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        menuItemButtonActions();
        toolbarButtonActions();
        referenceHandler.getAutomatonStage().show();
    }

    /**
     * MenuItems Buttons action
     */
    private void menuItemButtonActions() {
        this.referenceHandler.getAutomatonStage().getNewMenuItem().setOnAction(e -> createNewAutomatonWithGivenName());
        this.referenceHandler.getAutomatonStage().getLoadMenuItem().setOnAction(e -> loadExistingAutomatonFromDir());


        this.referenceHandler.getAutomatonStage().getQuitMenuItem().setOnAction(e -> quitApplication());
        this.referenceHandler.getAutomatonStage().getChangeSizeMenuItem().setOnAction(e -> changePopulationSize());

        this.referenceHandler.getAutomatonStage().getTorusMenuItem().setOnAction(e -> this.referenceHandler.getAutomaton().setTorus(true));
        this.referenceHandler.getAutomatonStage().getDeleteMenuItem().setOnAction(e -> this.referenceHandler.getAutomaton().clearPopulation());
        this.referenceHandler.getAutomatonStage().getCreateMenuItem().setOnAction(e -> this.referenceHandler.getAutomaton().randomPopulation());
        this.referenceHandler.getAutomatonStage().getZoomInMenuItem().setOnAction(e -> zoomIn());
        this.referenceHandler.getAutomatonStage().getZoomOutMenuItem().setOnAction(e -> zoomOut());
        this.referenceHandler.getAutomatonStage().getEditorMenuItem().setOnAction(e -> openEditor(this.referenceHandler.getAutomatonStage().getEditor()));
    }

    /**
     * ToolBar Button actions
     */
    private void toolbarButtonActions() {
        this.referenceHandler.getAutomatonStage().getCreateNewAutomatButton().setOnAction(e -> createNewAutomatonWithGivenName());
        this.referenceHandler.getAutomatonStage().getLoadExistingAutomatButton().setOnAction(e -> loadExistingAutomatonFromDir());
        this.referenceHandler.getAutomatonStage().getChangeSizeButton().setOnAction(e -> changePopulationSize());

        this.referenceHandler.getAutomatonStage().getSetAllCellsToZeroButton().setOnAction(e -> this.referenceHandler.getAutomaton().clearPopulation());
        this.referenceHandler.getAutomatonStage().getRandomPopulationButton().setOnAction(e -> this.referenceHandler.getAutomaton().randomPopulation());
        this.referenceHandler.getAutomatonStage().getSeeAsTorusButton().setOnAction(e -> this.referenceHandler.getAutomaton().setTorus(true));
        this.referenceHandler.getAutomatonStage().getZoomInButton().setOnAction(e -> zoomIn());
        this.referenceHandler.getAutomatonStage().getZoomOutButton().setOnAction(e -> zoomOut());
        this.referenceHandler.getAutomatonStage().getPopulationPaneScrollPane().viewportBoundsProperty()
                .addListener((observable, oldValue, newValue) -> this.referenceHandler.getAutomatonStage().getPopulationPane().center(newValue));
    }


    /**
     * Property
     */
    /*
    private void newAutomatonNameDialogProperty(){
        this.referenceHandler.getAutomatonStage().getDialogNewAutomatonName().getDialogPane().lookupButton(ButtonType.OK).disableProperty().
                bind(Bindings.createBooleanBinding( () -> !this.referenceHandler.getAutomatonStage().getDialogNewAutomatonName().getContentText().matches("^([A-Z])([A-Za-z]*)$"),
                        this.referenceHandler.getAutomatonStage().getDialogNewAutomatonName().getContentText().textProperty()));

    }

     */
    private void changeSizeDialogPaneProperty() {
        this.referenceHandler.getAutomatonStage().getDialogChangeSize().getDialogPane().setContent(this.referenceHandler.getAutomatonStage().getDialogGridChangeSize());
        this.referenceHandler.getAutomatonStage().getDialogChangeSize().getDialogPane().lookupButton(ButtonType.OK).disableProperty().
                bind(Bindings.createBooleanBinding(
                        () -> !this.referenceHandler.getAutomatonStage().getTextField1ChangeSize().getText().matches("^(10|[1-9][0-9]|[1-2][0-9][0-9])$") &&
                                !this.referenceHandler.getAutomatonStage().getTextField2ChangeSize().getText().matches("^(10|[1-9][0-9]|[1-2][0-9][0-9])$"),
                        this.referenceHandler.getAutomatonStage().getTextField1ChangeSize().textProperty(),
                        this.referenceHandler.getAutomatonStage().getTextField2ChangeSize().textProperty()));
    }

    /**
     * create Methods
     */
    private void changePopulationSize() {
        changeSizeDialogPaneProperty();
        Optional res = this.referenceHandler.getAutomatonStage().getDialogChangeSize().showAndWait();
        if (res.isPresent()) {
            this.referenceHandler.getAutomaton().changeSize(Integer.parseInt(this.referenceHandler.getAutomatonStage().getTextField1ChangeSize().getText()),
                    Integer.parseInt(this.referenceHandler.getAutomatonStage().getTextField2ChangeSize().getText()));
        }
    }

    private void createNewAutomatonWithGivenName() {

        //newAutomatonNameDialogProperty();
        Optional res = this.referenceHandler.getAutomatonStage().getDialogNewAutomatonName().showAndWait();
        if (res.isPresent()) {

            try {
                AutomatonIO.createFileWithGivenNameInDir(res.get().toString());
                AutomatonMain.startAutomaton(res.get().toString());
                this.referenceHandler.setAutomatonName(res.get().toString());
            } catch (IOException e) {
                Alert alrt = new Alert(Alert.AlertType.ERROR, "Ups, Automat konnte nicht erstellt werden, createNewAutomaton() - Controller", ButtonType.OK);
                alrt.showAndWait();
            }

        }
    }

    private void loadExistingAutomatonFromDir() {

        File chosenFile = this.referenceHandler.getAutomatonStage().getFileChooser().showOpenDialog(this.referenceHandler.getAutomatonStage());
        if (chosenFile != null) {
            AutomatonMain.startAutomaton(chosenFile.getName().replace(".java", ""));
        }
    }

    public void zoomIn() {

        if (scaleCounterIn < 4) {

            this.referenceHandler.getAutomatonStage().getZoomOutButton().setDisable(false);
            this.referenceHandler.getAutomatonStage().getZoomOutMenuItem().setDisable(false);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setCellHeight(this.referenceHandler.getAutomatonStage().getPopulationPane().getCellHeight() * 1.2);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setCellWidth(this.referenceHandler.getAutomatonStage().getPopulationPane().getCellWidth() * 1.2);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setBorderHeight(this.referenceHandler.getAutomatonStage().getPopulationPane().getBorderHeight() * 1.2);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setBorderWidth(this.referenceHandler.getAutomatonStage().getPopulationPane().getBorderWidth() * 1.2);

            this.scaleCounterIn++;
            this.scaleCounterOut--;

            this.referenceHandler.getAutomatonStage().getPopulationPane().paintCanvas();

        } else {
            this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getZoomInButton().setDisable(true);
            this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getZoomInMenuItem().setDisable(true);
        }
    }

    public void zoomOut() {

        if (scaleCounterOut < 10) {

            this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getZoomInButton().setDisable(false);
            this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getZoomInMenuItem().setDisable(false);
            this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().getGraphicsContext2D().clearRect(0, 0,

                    this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().getWidth(),
                    this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().getHeight());

            this.referenceHandler.getAutomatonStage().getPopulationPane().setCellHeight(this.referenceHandler.getAutomatonStage().getPopulationPane().getCellHeight() / 1.2);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setCellWidth(this.referenceHandler.getAutomatonStage().getPopulationPane().getCellWidth() / 1.2);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setBorderHeight(this.referenceHandler.getAutomatonStage().getPopulationPane().getBorderHeight() / 1.2);
            this.referenceHandler.getAutomatonStage().getPopulationPane().setBorderWidth(this.referenceHandler.getAutomatonStage().getPopulationPane().getBorderWidth() / 1.2);

            this.scaleCounterIn--;
            this.scaleCounterOut++;

            this.referenceHandler.getAutomatonStage().getPopulationPane().paintCanvas();
        } else {

            this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getZoomOutButton().setDisable(true);
            this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getZoomOutMenuItem().setDisable(true);
        }
    }

    public boolean setPosXYAndReturnBool(double x, double y) {
        if (x < 0 || y < 0) {
            return false;
        }
        referenceHandler.getAutomatonStage().setX(x);
        referenceHandler.getAutomatonStage().setY(y);
        return true;
    }

    public boolean setSliderSpeedAndReturnBoolean(double speed) {
        if (speed < 0) {
            return false;
        }
        referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().setValue(speed);
        return true;
    }

    private void quitApplication() {
        this.referenceHandler.getAutomatonStage().close();
        if (this.referenceHandler.getAutomatonStage().getEditor().isShowing()) {
            this.referenceHandler.getAutomatonStage().getEditor().close();
        }
        try {
            AutomatonIO.clearAutomataDirectory();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Das Verzeichnis konnte nicht gelöscht werden!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void openEditor(Editor editor) {
        if (!editor.isShowing()) {
            editor.show();
        } else {
            editor.toFront();
        }
    }
}


