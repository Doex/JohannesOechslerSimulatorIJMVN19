package de.oechsler.view;

import de.oechsler.Controller.ReferenceHandler;
import de.oechsler.Controller.ResourcesController;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class DatabaseStage {
    private TextInputDialog DBtextInputDialog;
    private ChoiceDialog<String> dbChoiceDialog;
    private ChoiceBox dbChoiceBox;
    private TextField DBTextfield;

    private ResourcesController resourcesController = ResourcesController.getResourcesController();

    /**
     * Name ggf. ändern, da es keine alleinstehende Stage ist
     *
     */
    public DatabaseStage(){
        /**
         * TextInputDialog
         */
        this.DBtextInputDialog = new TextInputDialog();
        this.DBtextInputDialog.titleProperty().bind(resourcesController.i18n("dbSettingText"));
        this.DBtextInputDialog.setResizable(true);
        this.DBtextInputDialog.setHeight(250);
        this.DBtextInputDialog.setWidth(450);
        Label dbInputLabel = new Label();
        dbInputLabel.textProperty().bind(resourcesController.i18n("dbSafeAsText"));
        this.DBtextInputDialog.setContentText(dbInputLabel.getText());
        /**
         * ChoiceDialog
         */
        this.dbChoiceDialog = new ChoiceDialog();
        this.dbChoiceDialog.titleProperty().bind(resourcesController.i18n("dbSettingText"));
        this.dbChoiceDialog.setResizable(true);
        this.dbChoiceDialog.setHeight(250);
        this.dbChoiceDialog.setWidth(450);
        this.dbChoiceBox = new ChoiceBox();
        Label dbChoiceLabel = new Label();
        dbChoiceLabel.textProperty().bind(resourcesController.i18n("dbChoiceText"));
        this.dbChoiceDialog.setContentText(dbChoiceLabel.getText());


    }

    public TextInputDialog getDBtextInputDialog() {
        return DBtextInputDialog;
    }

    public ChoiceDialog getDbChoiceDialog() {
        return dbChoiceDialog;
    }

    public TextField getDBTextfield() {
        return DBTextfield;
    }

    public ChoiceBox getDbChoiceBox() {
        return dbChoiceBox;
    }
}
