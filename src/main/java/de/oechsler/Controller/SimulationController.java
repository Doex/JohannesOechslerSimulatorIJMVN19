package de.oechsler.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SimulationController {

    final static int DEF_SPEED = 200;
    final static int MIN_SPEED = 10;
    final static int MAX_SPEED = 400;

    private final ReferenceHandler referenceHandler;

    private volatile int speed;

    private SimulationThread simulationThread;

    public SimulationController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        this.speed = DEF_SPEED;


        this.referenceHandler.getAutomatonStage().getStopSimulationButton().setDisable(true);
        this.referenceHandler.getAutomatonStage().getStoppMenuItem().setDisable(true);


        this.referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().setMin(this.MIN_SPEED);
        this.referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().setMax(this.MAX_SPEED);
        this.referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().setValue(this.DEF_SPEED);

        this.referenceHandler.getAutomatonStage().getStartSimulationButton().setOnAction(e -> startSimulation());
        this.referenceHandler.getAutomatonStage().getStartMenuItem().setOnAction(e -> startSimulation());
        this.referenceHandler.getAutomatonStage().getOneZyclusButton().setOnAction(e -> startOneZyklus());
        this.referenceHandler.getAutomatonStage().getSchrittMenuItem().setOnAction(e -> startOneZyklus());
        this.referenceHandler.getAutomatonStage().getStopSimulationButton().setOnAction(e -> stopSimulation());
        this.referenceHandler.getAutomatonStage().getStoppMenuItem().setOnAction(e -> stopSimulation());

        this.referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().valueProperty().addListener((obs, o, n) -> this.speed = n.intValue());

        simulationThread = null;
    }

    private void startOneZyklus() {
        this.referenceHandler.getAutomatonStage().getStartSimulationButton().setDisable(false);
        this.referenceHandler.getAutomatonStage().getStartMenuItem().setDisable(false);
        this.referenceHandler.getAutomatonStage().getStopSimulationButton().setDisable(false);
        this.referenceHandler.getAutomatonStage().getStoppMenuItem().setDisable(false);
        try {
            referenceHandler.getAutomaton().nextGeneration();
        } catch (Throwable e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, beim starten eines Zyklus ist etwas schief gelaufen!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    private void startSimulation() {
        this.referenceHandler.getAutomatonStage().getStartMenuItem().setDisable(true);
        this.referenceHandler.getAutomatonStage().getStartSimulationButton().setDisable(true);
        this.referenceHandler.getAutomatonStage().getStoppMenuItem().setDisable(false);
        this.referenceHandler.getAutomatonStage().getStopSimulationButton().setDisable(false);

        if (simulationThread == null) {
            simulationThread = new SimulationThread();
            simulationThread.start();
        } else {
            simulationThread.continueSimulation();
        }
    }

    public void stopSimulation() {
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
        try {
            assert simulationThread != null;
            simulationThread.join();
        } catch (InterruptedException e) {
            SimulationThread.currentThread().interrupt();
        }
        this.referenceHandler.getAutomatonStage().getStopSimulationButton().setDisable(true);
        this.referenceHandler.getAutomatonStage().getStartSimulationButton().setDisable(false);
        simulationThread = null;
    }

    class SimulationThread extends Thread {
        private volatile boolean pauseRequest = false;
        private Object syncObject = new Object();

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    referenceHandler.getAutomaton().nextGeneration();
                } catch (Throwable e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Laufzeitfehler in der transform-Methode:" + e, ButtonType.OK);
                    alert.showAndWait();
                }
                try {
                    while (pauseRequest) {
                        synchronized (syncObject) {
                            syncObject.wait();
                        }
                    }
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
            pauseRequest = false;
        }

        void continueSimulation() {
            pauseRequest = false;
            synchronized (syncObject) {
                syncObject.notify();
            }
        }
    }
}


