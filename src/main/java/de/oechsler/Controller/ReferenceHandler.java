package de.oechsler.Controller;

import de.oechsler.model.Automaton;
import de.oechsler.util.Observable;
import de.oechsler.view.AutomatonStage;
import de.oechsler.view.DatabaseStage;
import de.oechsler.view.StatesPane;



public class ReferenceHandler extends Observable {

    private Automaton automaton;
    private StatesPaneController statesPaneController;
    private SimulationController simulationController;
    private AutomatonStage automatonStage;
    private AutomatonStageController automatonStageController;
    private PopulationPaneController populationPaneController;
    private EditorController editorController;
    private SerializationController serializationController;
    private XMLSerializationController xmlSerializationController;
    private DatabaseController databaseController;
    private DatabaseStage databaseStage;
    private LanguageController languageController;
    private PrintController printController;
    private String automatonName;

    public Automaton getAutomaton(){return this.automaton;}


    public void setAutomaton(Automaton newAutomaton){
        if(newAutomaton == null){
            return;
        }
        if(this.automaton == null){
            this.automaton = newAutomaton;
            return;
        }

        newAutomaton.addAll(this.automaton.getObserver());
        this.automaton.deleteObserver();

        this.automatonStage.getSeeAsTorusButton().setSelected(newAutomaton.isTorus());
        this.automatonStage.getTorusMenuItem().setSelected(newAutomaton.isTorus());
        this.simulationController.stopSimulation();
        this.automaton.setNumberOfStates(newAutomaton.getNumberOfStates());
        this.automatonName = newAutomaton.getName();

        this.automaton = newAutomaton;
        this.notifyObserver();
    }



    public AutomatonStage getAutomatonStage() {
        return automatonStage;
    }
    public void setAutomatonStage(AutomatonStage automatonStage) {
        this.automatonStage = automatonStage;
    }

    public void setSimulationController(SimulationController simulationController) {
        this.simulationController = simulationController;
    }
    public void setLanguageController(LanguageController languageController) {
        this.languageController = languageController;
    }
    public void setAutomatonStageController(AutomatonStageController automatonStageController) {
        this.automatonStageController = automatonStageController;
    }
    public void setAutomatonName(String automatonName) {
        this.automatonName = automatonName;
    }
    public void setPopulationPaneController(PopulationPaneController populationPaneController) {
        this.populationPaneController = populationPaneController;
    }
    public void setStatesPanelController(StatesPaneController statesPaneController) {
        this.statesPaneController = statesPaneController;
    }
    public void setEditorController(EditorController editorController) {
        this.editorController = editorController;
    }


    public void setSerializationController(SerializationController serializationController) {
        this.serializationController = serializationController;
    }
    public void setXmlSerializationController(XMLSerializationController xmlSerializationController) {
        this.xmlSerializationController = xmlSerializationController;
    }
    public String getAutomatonName() {
        return automatonName;
    }

    public void setDatabaseController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }
    public DatabaseStage getDatabaseStage() {
        return databaseStage;
    }

    public void setDatabaseStage(DatabaseStage databaseStage) {
        this.databaseStage = databaseStage;
    }

    public AutomatonStageController getAutomatonStageController() {
        return automatonStageController;
    }

    public void setPrintController(PrintController printController) {
        this.printController = printController;
    }

}
