package de.oechsler.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Editor extends Stage {

    private MenuItem saveMenuItem;
    private MenuItem editorCompileMenuItem;
    private MenuItem editorQuitMenuItem;
    private Button editorSafeButton;
    private Button editorCompileButton;
    private TextArea textArea;

    private Label editorLabel;

    public Editor(){
        super();
        this.setTitle("Editor");

        MenuBar menuBar = new MenuBar();
        Menu editMenu = new Menu("_Editor");
        this.saveMenuItem = new MenuItem("_Speichern");
        this.saveMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + S"));
        this.editorCompileMenuItem = new MenuItem("_Kompilieren");
        this.editorCompileMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + K"));
        this.editorQuitMenuItem = new MenuItem("_Schließen");
        this.editorQuitMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + Q"));

        editMenu.getItems().addAll(this.saveMenuItem, this.editorCompileMenuItem,this.editorQuitMenuItem);
        menuBar.getMenus().add(editMenu);

        ToolBar editorToolbar = new ToolBar();
        this.editorSafeButton = new Button();
        this.editorSafeButton.setGraphic(new ImageView("Icons/Save24.gif"));
        this.editorCompileButton = new Button();
        this.editorCompileButton.setGraphic(new ImageView("Icons/Compile24.gif"));
        editorToolbar.getItems().addAll(this.editorSafeButton, this.editorCompileButton);


        this.textArea = new TextArea();

        this.editorLabel = new Label("Willkommen");

        VBox root = new VBox();
        VBox.setVgrow(this.textArea, Priority.ALWAYS);
        root.getChildren().addAll(menuBar, editorToolbar, this.textArea, this.editorLabel);
        Scene scene = new Scene(root, 500, 600);
        this.setScene(scene);
    }

    public MenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public MenuItem getEditorCompileMenuItem() {
        return editorCompileMenuItem;
    }

    public MenuItem getEditorQuitMenuItem() {
        return editorQuitMenuItem;
    }

    public Button getEditorSafeButton() {
        return editorSafeButton;
    }

    public Button getEditorCompileButton() {
        return editorCompileButton;
    }

    public Label getEditorLabel() {
        return editorLabel;
    }
}
