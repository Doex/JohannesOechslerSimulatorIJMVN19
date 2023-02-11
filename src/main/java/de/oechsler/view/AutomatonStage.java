package de.oechsler.view;

import de.oechsler.Controller.ReferenceHandler;
import de.oechsler.Controller.ResourcesController;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class AutomatonStage extends Stage {

    private final PopulationPane populationPane;
    private final MessageLabel msgLabel;
    private final ReferenceHandler referenceHandler;
    private final StatesPanel statesPanel;
    private final Editor editor;


    private final List<ColorPicker> colorPickerList = new ArrayList<>();
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final List<RadioButton> radioButtonList = new ArrayList<>();
    private final Dialog<String> dialogChangeSize = new TextInputDialog();
    private final TextField textField1ChangeSize = new TextField();
    private final TextField textField2ChangeSize = new TextField();
    private final GridPane dialogGridChangeSize = new GridPane();
    private final TextInputDialog dialogNewAutomatonName;
    private final TextField textFieldNewAutomatonName = new TextField();
    private final VBox stateBoxes;
    private final ScrollPane PopulationPaneScrollPane;
    private final FileChooser fileChooser;
    private final MenuItem newMenuItem;
    private final MenuItem loadMenuItem;
    private final MenuItem editorMenuItem;
    private final MenuItem quitMenuItem;
    private final MenuItem changeSizeMenuItem;
    private final MenuItem deleteMenuItem;
    private final MenuItem createMenuItem;
    private final CheckMenuItem torusMenuItem;
    private final MenuItem zoomInMenuItem;
    private final MenuItem zoomOutMenuItem;
    private final MenuItem xmlSafeMenuItem;
    private final MenuItem serialisierenMenuItem;
    private final MenuItem xmlLoadMenuItem;
    private final MenuItem deserialisierenMenuItem;
    private final MenuItem printMenuItem;
    private final MenuItem schrittMenuItem;
    private final MenuItem startMenuItem;
    private final MenuItem stoppMenuItem;
    private MenuItem dbSerializeMenuItem;
    private MenuItem dbDeserializeMenuItem;
    private MenuItem dbDeleteMenuItem;
    private Menu languageMenu;
    private RadioMenuItem languageEnglishMenuItem;
    private RadioMenuItem languageGermanMenuItem;

    private final Button createNewAutomatButton;
    private final Button loadExistingAutomatButton;
    private final Button changeSizeButton;
    private final Button setAllCellsToZeroButton;
    private final Button randomPopulationButton;
    private final ToggleButton seeAsTorusButton;
    private ToggleGroup languageGroup;
    private final Button printPopulationButton;
    private final Button zoomInButton;
    private final Button zoomOutButton;
    private final Button oneZyclusButton;
    private final Button startSimulationButton;
    private final Button stopSimulationButton;
    private final Slider changeSimulationSpeedSlider;
    private static final String PATH_TO_AUTOMATA = "./automata";
    private ResourcesController resourcesController = ResourcesController.getResourcesController();


    public AutomatonStage(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;

        setTitle(referenceHandler.getAutomatonName());

        this.editor = new Editor();

        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File(PATH_TO_AUTOMATA));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.java", "*.java");
        fileChooser.getExtensionFilters().add(extFilter);

        /**
         * Menubar
         */
        MenuBar menuBar = new MenuBar();
        Menu automatMenu = new Menu();
        automatMenu.textProperty().bind(this.resourcesController.i18n("automatonMenu"));
        this.newMenuItem = new MenuItem("_Neu");
        this.newMenuItem.textProperty().bind(this.resourcesController.i18n("newAutomaton"));
        newMenuItem.setGraphic(new ImageView("Icons/New16.gif"));
        newMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + N"));
        this.loadMenuItem = new MenuItem("_Laden");
        this.loadMenuItem.textProperty().bind(this.resourcesController.i18n("loadMenuItem"));
        loadMenuItem.setGraphic(new ImageView("Icons/Load16.gif"));
        loadMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + L"));
        this.editorMenuItem = new MenuItem("_Editor");
        editorMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + E"));
        this.quitMenuItem = new MenuItem("_Beenden");
        this.quitMenuItem.textProperty().bind(this.resourcesController.i18n("quitMenuItem"));
        quitMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + Q"));
        Menu populationMenu = new Menu("_Population");
        this.changeSizeMenuItem = new MenuItem("_Größe ändern");
        this.changeSizeMenuItem.textProperty().bind(this.resourcesController.i18n("changeSizeMenuItem"));
        changeSizeMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + G"));
        this.deleteMenuItem = new MenuItem("_Löschen");
        this.deleteMenuItem.textProperty().bind(this.resourcesController.i18n("deleteMenuItem"));
        deleteMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + L"));
        this.createMenuItem = new MenuItem("_Erzeugen");
        this.createMenuItem.textProperty().bind(this.resourcesController.i18n("createMenuItem"));
        createMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + Z"));
        this.torusMenuItem = new CheckMenuItem("_Torus");
        torusMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + T"));
        this.zoomInMenuItem = new MenuItem("Vergrößern");
        this.zoomInMenuItem.textProperty().bind(this.resourcesController.i18n("extendMenuItem"));
        zoomInMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + I"));
        this.zoomOutMenuItem = new MenuItem("_Verkleinern");
        this.zoomOutMenuItem.textProperty().bind(this.resourcesController.i18n("reduceMenuItem"));
        zoomOutMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + O"));
        Menu safeMenu = new Menu("_Speichern");
        safeMenu.textProperty().bind(this.resourcesController.i18n("safeMenu"));
        this.xmlSafeMenuItem = new MenuItem("XML");
        xmlSafeMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + X"));
        this.serialisierenMenuItem = new MenuItem("Serialisieren");
        this.serialisierenMenuItem.textProperty().bind(this.resourcesController.i18n("serializeMenuItem"));
        serialisierenMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + S"));
        Menu loadMenu = new Menu("_Laden");
        loadMenu.textProperty().bind(this.resourcesController.i18n("loadMenuItem"));
        this.xmlLoadMenuItem = new MenuItem("XML");
        xmlLoadMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + M"));
        this.deserialisierenMenuItem = new MenuItem("Deserialisieren");
        this.deserialisierenMenuItem.textProperty().bind(this.resourcesController.i18n("deserializeMenuItem"));
        deserialisierenMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + CTRL + D"));
        this.printMenuItem = new MenuItem("_Drucken");
        this.printMenuItem.textProperty().bind(this.resourcesController.i18n("printMenuItem"));
        printMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + P"));
        Menu simulationMenu = new Menu("SimulationMenu");
        this.schrittMenuItem = new MenuItem("Schritt");
        this.schrittMenuItem.textProperty().bind(this.resourcesController.i18n("stepMenuItem"));
        schrittMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + ALT + S"));
        this.startMenuItem = new MenuItem("Start");
        startMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + ALT + A"));
        startMenuItem.setGraphic(new ImageView("Icons/Start16.gif"));
        this.stoppMenuItem = new MenuItem("Stopp");
        this.stoppMenuItem.textProperty().bind(this.resourcesController.i18n("stopMenuItem"));
        stoppMenuItem.setGraphic(new ImageView("Icons/Stop16.gif"));
        stoppMenuItem.setAccelerator(KeyCombination.valueOf("SHORTCUT + ALT + O"));
        Menu settings = new Menu("Einstellungen");
        settings.textProperty().bind(this.resourcesController.i18n("settingsMenu"));
        this.dbSerializeMenuItem = new MenuItem("_Speichern");
        this.dbSerializeMenuItem.textProperty().bind(this.resourcesController.i18n("dbSafeMenuItem"));
        this.dbDeserializeMenuItem = new MenuItem("_Wiederherstellen");
        this.dbDeserializeMenuItem.textProperty().bind(this.resourcesController.i18n("dbRestoreMenuItem"));
        this.dbDeleteMenuItem = new MenuItem("_Löschen");
        this.dbDeleteMenuItem.textProperty().bind(this.resourcesController.i18n("dbDeleteMenuItem"));
        this.languageMenu = new Menu("_Language");
        createLanguageMenu();
        automatMenu.getItems().addAll(this.newMenuItem, this.loadMenuItem, this.editorMenuItem, this.quitMenuItem);
        safeMenu.getItems().addAll(this.xmlSafeMenuItem, this.serialisierenMenuItem);
        loadMenu.getItems().addAll(this.xmlLoadMenuItem, this.deserialisierenMenuItem);
        populationMenu.getItems().addAll(this.changeSizeMenuItem, this.deleteMenuItem, this.createMenuItem,
                this.torusMenuItem, this.zoomInMenuItem, this.zoomOutMenuItem,
                safeMenu, loadMenu, this.printMenuItem);
        settings.getItems().addAll(this.dbSerializeMenuItem, this.dbDeserializeMenuItem, this.dbDeleteMenuItem);
        simulationMenu.getItems().addAll(this.schrittMenuItem, this.startMenuItem, stoppMenuItem);
        languageMenu.getItems().addAll(this.languageEnglishMenuItem, this.languageGermanMenuItem);
        menuBar.getMenus().addAll(automatMenu, populationMenu, simulationMenu, settings, this.languageMenu);

        /**
         * Toolbar
         */
        ToolBar toolBar = new ToolBar();
        this.createNewAutomatButton = new Button();
        this.createNewAutomatButton.setGraphic(new ImageView("Icons/New24.gif"));
        this.createNewAutomatButton.setTooltip(new Tooltip("Neus Spiel"));
        this.loadExistingAutomatButton = new Button();
        this.loadExistingAutomatButton.setGraphic(new ImageView("Icons/Open24.gif"));
        this.loadExistingAutomatButton.setTooltip(new Tooltip(" Existierenden Automat laden"));
        this.changeSizeButton = new Button();
        this.changeSizeButton.setGraphic(new ImageView("Icons/Size24.gif"));
        this.changeSizeButton.setTooltip(new Tooltip("Größe der Population ändern"));
        this.setAllCellsToZeroButton = new Button();
        this.setAllCellsToZeroButton.setGraphic(new ImageView("Icons/Delete24.gif"));
        this.setAllCellsToZeroButton.setTooltip(new Tooltip("Alle Zellen in Zustand 0 setzen"));
        this.randomPopulationButton = new Button();
        this.randomPopulationButton.setGraphic(new ImageView("Icons/Random24.gif"));
        this.randomPopulationButton.setTooltip(new Tooltip("Zufällige Population"));
        this.seeAsTorusButton = new ToggleButton();
        this.seeAsTorusButton.setGraphic(new ImageView("Icons/Torus24.gif"));
        this.seeAsTorusButton.setTooltip(new Tooltip("Als Torus betrachten"));
        this.printPopulationButton = new Button();
        this.printPopulationButton.setGraphic(new ImageView("Icons/Print24.gif"));
        this.printPopulationButton.setTooltip(new Tooltip("Drucken"));
        this.zoomInButton = new Button();
        this.zoomInButton.setGraphic(new ImageView("Icons/ZoomIn24.gif"));
        this.zoomInButton.setTooltip(new Tooltip("Vergrößern"));
        this.zoomOutButton = new Button();
        this.zoomOutButton.setGraphic(new ImageView("Icons/ZoomOut24.gif"));
        this.zoomOutButton.setTooltip(new Tooltip("Verkleinern"));
        this.oneZyclusButton = new Button();
        this.oneZyclusButton.setGraphic(new ImageView("Icons/Step24.gif"));
        this.oneZyclusButton.setTooltip(new Tooltip("Einen Simulationszyklus ausführen"));
        this.startSimulationButton = new Button();
        this.startSimulationButton.setGraphic(new ImageView("Icons/Play24.gif"));
        this.startSimulationButton.setTooltip(new Tooltip("Simulation starten"));
        this.stopSimulationButton = new Button();
        this.stopSimulationButton.setGraphic(new ImageView("Icons/Stop24.gif"));
        this.stopSimulationButton.setTooltip(new Tooltip("Simulation stoppen"));
        this.changeSimulationSpeedSlider = new Slider();
        this.changeSimulationSpeedSlider.setShowTickLabels(true);
        this.changeSimulationSpeedSlider.setShowTickMarks(true);
        this.changeSimulationSpeedSlider.setTooltip(new Tooltip("Simulationsgeschwindigkeit"));
        toolBar.getItems().addAll(this.createNewAutomatButton, this.loadExistingAutomatButton,
                this.changeSizeButton, this.setAllCellsToZeroButton, this.randomPopulationButton,
                this.seeAsTorusButton, this.printPopulationButton, new Separator(Orientation.VERTICAL), this.zoomInButton,
                this.zoomOutButton, new Separator(Orientation.VERTICAL), this.oneZyclusButton, this.startSimulationButton,
                this.stopSimulationButton, new Separator(Orientation.VERTICAL), this.changeSimulationSpeedSlider);

        //RadioButtonAndColorPickerBox
        this.stateBoxes = new VBox(10);
        this.stateBoxes.setMinHeight(500);

        this.statesPanel = new StatesPanel(this.referenceHandler, this);

        //Region
        Region populationView = new Region();

        //LabelPane
        this.msgLabel = new MessageLabel(referenceHandler.getAutomaton());
        this.msgLabel.setLayoutX(740);

        //Split the middle part
        this.populationPane = new PopulationPane(referenceHandler, this.colorPickerList, this);
        this.PopulationPaneScrollPane = new ScrollPane(this.populationPane);

        //SplitPane
        SplitPane mainContentSplitPane = new SplitPane();
        mainContentSplitPane.setDividerPositions(0.25);
        mainContentSplitPane.setMinHeight(650);
        mainContentSplitPane.getItems().addAll(this.stateBoxes, this.PopulationPaneScrollPane);
        SplitPane.setResizableWithParent(this.stateBoxes, false);

        //Dialogs....
        //ChangeSize
        this.dialogChangeSize.setTitle("Change size");
        this.dialogChangeSize.setResizable(true);
        this.dialogChangeSize.getDialogPane().setHeaderText("Wähle eine neue Breite und Höhe aus!");
        this.dialogChangeSize.setHeight(150);
        this.dialogChangeSize.setWidth(300);
        Label newWidthLabel = new Label("Row:  ");
        this.textField1ChangeSize.setPromptText(String.valueOf(referenceHandler.getAutomaton().getNumberOfRows()));
        Label newHeightLabel = new Label("column:  ");
        this.textField2ChangeSize.setPromptText(String.valueOf(referenceHandler.getAutomaton().getNumberOfColumns()));
        this.dialogGridChangeSize.add(newWidthLabel, 1, 1);
        this.dialogGridChangeSize.add(textField1ChangeSize, 2, 1);
        this.dialogGridChangeSize.add(newHeightLabel, 1, 2);
        this.dialogGridChangeSize.add(textField2ChangeSize, 2, 2);
        this.dialogChangeSize.getDialogPane().setContent(this.dialogGridChangeSize);


        //New Automaton
        this.dialogNewAutomatonName = new TextInputDialog();
        this.dialogNewAutomatonName.titleProperty().bind(resourcesController.i18n("newAutomaton"));
        this.dialogNewAutomatonName.setResizable(true);
        this.dialogNewAutomatonName.setHeight(250);
        this.dialogNewAutomatonName.setWidth(450);
        Label newNameLabel = new Label("Name: ");
        this.textFieldNewAutomatonName.setPromptText("z.B. GOL...");
        this.dialogNewAutomatonName.setContentText(newNameLabel.getText());



        //Main Scene view...
        VBox root = new VBox();
        VBox.setVgrow(populationView, Priority.ALWAYS);
        root.getChildren().addAll(menuBar, toolBar, mainContentSplitPane, this.msgLabel);
        setScene(new Scene(root, 900, 740));
    }


    private void createLanguageMenu() {
        this.languageMenu = new Menu();
        this.languageMenu.textProperty().bind(this.resourcesController.i18n("languageMenu"));

        this.languageGroup = new ToggleGroup();

        this.languageEnglishMenuItem = new RadioMenuItem();
        this.languageEnglishMenuItem.textProperty().bind(this.resourcesController.i18n("languageEnglishMenuItem"));
        this.languageEnglishMenuItem.setToggleGroup(this.languageGroup);
        this.languageEnglishMenuItem
                .setSelected(ResourcesController.getResourcesController().getLocale().getLanguage().equals("en"));

        this.languageGermanMenuItem = new RadioMenuItem();
        this.languageGermanMenuItem.textProperty().bind(this.resourcesController.i18n("languageGermanMenuItem"));
        this.languageGermanMenuItem.setToggleGroup(this.languageGroup);
        this.languageGermanMenuItem
                .setSelected(ResourcesController.getResourcesController().getLocale().getLanguage().equals("de"));

    }

    //Getter & Setter
    public PopulationPane getPopulationPane() {
        return populationPane;
    }

    public Dialog<String> getDialogChangeSize() {
        return dialogChangeSize;
    }

    public TextField getTextField1ChangeSize() {
        return textField1ChangeSize;
    }

    public TextField getTextField2ChangeSize() {
        return textField2ChangeSize;
    }

    public MenuItem getNewMenuItem() {
        return newMenuItem;
    }

    public MenuItem getLoadMenuItem() {
        return loadMenuItem;
    }

    public MenuItem getEditorMenuItem() {
        return editorMenuItem;
    }

    public MenuItem getQuitMenuItem() {
        return quitMenuItem;
    }

    public MenuItem getChangeSizeMenuItem() {
        return changeSizeMenuItem;
    }

    public MenuItem getDeleteMenuItem() {
        return deleteMenuItem;
    }

    public MenuItem getCreateMenuItem() {
        return createMenuItem;
    }

    public CheckMenuItem getTorusMenuItem() {
        return torusMenuItem;
    }

    public MenuItem getXmlSafeMenuItem() {
        return xmlSafeMenuItem;
    }

    public MenuItem getSerialisierenMenuItem() {
        return serialisierenMenuItem;
    }

    public MenuItem getXmlLoadMenuItem() {
        return xmlLoadMenuItem;
    }

    public MenuItem getDeserialisierenMenuItem() {
        return deserialisierenMenuItem;
    }

    public MenuItem getPrintMenuItem() {
        return printMenuItem;
    }

    public MenuItem getSchrittMenuItem() {
        return schrittMenuItem;
    }

    public VBox getStateBoxes() {
        return stateBoxes;
    }

    public MenuItem getStartMenuItem() {
        return startMenuItem;
    }

    public MenuItem getStoppMenuItem() {
        return stoppMenuItem;
    }

    public Button getZoomInButton() {
        return zoomInButton;
    }

    public Button getZoomOutButton() {
        return zoomOutButton;
    }

    public MenuItem getZoomInMenuItem() {
        return zoomInMenuItem;
    }

    public MenuItem getZoomOutMenuItem() {
        return zoomOutMenuItem;
    }

    public List<RadioButton> getRadioButtonList() {
        return radioButtonList;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public Button getCreateNewAutomatButton() {
        return createNewAutomatButton;
    }

    public Button getLoadExistingAutomatButton() {
        return loadExistingAutomatButton;
    }

    public Button getChangeSizeButton() {
        return changeSizeButton;
    }

    public Button getSetAllCellsToZeroButton() {
        return setAllCellsToZeroButton;
    }

    public Button getRandomPopulationButton() {
        return randomPopulationButton;
    }

    public ToggleButton getSeeAsTorusButton() {
        return seeAsTorusButton;
    }

    public Button getPrintPopulationButton() {
        return printPopulationButton;
    }

    public List<ColorPicker> getColorPickerList() {
        return colorPickerList;
    }

    public Button getOneZyclusButton() {
        return oneZyclusButton;
    }

    public Button getStartSimulationButton() {
        return startSimulationButton;
    }

    public Button getStopSimulationButton() {
        return stopSimulationButton;
    }

    public Slider getChangeSimulationSpeedSlider() {
        return changeSimulationSpeedSlider;
    }

    public ScrollPane getPopulationPaneScrollPane() {
        return PopulationPaneScrollPane;
    }

    public GridPane getDialogGridChangeSize() {
        return dialogGridChangeSize;
    }

    public TextInputDialog getDialogNewAutomatonName() {
        return dialogNewAutomatonName;
    }

    public TextField getTextFieldNewAutomatonName() {
        return textFieldNewAutomatonName;
    }

    public Editor getEditor() {
        return editor;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public MenuItem getDbDeserializeMenuItem() {
        return dbDeserializeMenuItem;
    }

    public MenuItem getDbSerializeMenuItem() {
        return dbSerializeMenuItem;
    }

    public MenuItem getDbDeleteMenuItem() {
        return dbDeleteMenuItem;
    }

    public MenuItem getLanguageEnglishMenuItem() {
        return languageEnglishMenuItem;
    }

    public MenuItem getLanguageGermanMenuItem() {
        return languageGermanMenuItem;
    }

    public ToggleGroup getLanguageGroup() {
        return languageGroup;
    }


}