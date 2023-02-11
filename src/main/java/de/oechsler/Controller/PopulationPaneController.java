package de.oechsler.Controller;

import de.oechsler.util.Pair;
import de.oechsler.view.PopulationPaneContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PopulationPaneController {

    private final ReferenceHandler referenceHandler;

    public PopulationPaneController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                Pair<Integer> rowCol = this.referenceHandler.getAutomatonStage().getPopulationPane().getRowAndCol(event.getX(), event.getY());
                if(rowCol != null){
                    this.referenceHandler.getAutomaton().setState(rowCol.value1(), rowCol.value2(), this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage()
                            .getRadioButtonList().indexOf(this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getToggleGroup().getSelectedToggle()));
                    this.referenceHandler.getAutomatonStage().getPopulationPane().update();
                }
            }
        });

        this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                Pair<Integer> rowCol = this.referenceHandler.getAutomatonStage().getPopulationPane().getRowAndCol(event.getX(), event.getY());
                if(rowCol != null){
                    this.referenceHandler.getAutomaton().setState(rowCol.value1(), rowCol.value2(), this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage()
                            .getRadioButtonList().indexOf(this.referenceHandler.getAutomatonStage().getPopulationPane().getAutomatonStage().getToggleGroup().getSelectedToggle()));
                    this.referenceHandler.getAutomatonStage().getPopulationPane().update();
                }
            }
        });

        this.referenceHandler.getAutomatonStage().getPopulationPane().setOnContextMenuRequested(ev -> this.referenceHandler.getAutomatonStage().getPopulationPane().getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                if(event.getButton() == MouseButton.SECONDARY) {
                Pair<Integer> rowCol = this.referenceHandler.getAutomatonStage().getPopulationPane().getRowAndCol(event.getX(), event.getY());
                if (rowCol != null) {
                    PopulationPaneContextMenu contextMenu = new PopulationPaneContextMenu(this.referenceHandler, this.referenceHandler.getAutomatonStage().getPopulationPane(), rowCol);
                    contextMenu.show(this.referenceHandler.getAutomatonStage().getPopulationPane().getScene().getWindow(), ev.getScreenX(), ev.getScreenY());
                    this.referenceHandler.getAutomatonStage().getPopulationPane().update();
                }
            }
        }));
    }
}



