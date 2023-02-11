package de.oechsler.Controller;
import javafx.scene.control.ColorPicker;
public class StatesPanelController {
    public StatesPanelController(ReferenceHandler referenceHandler){
        for (ColorPicker cpick : referenceHandler.getAutomatonStage().getColorPickerList()) {
            cpick.setOnAction(e -> referenceHandler.getAutomaton().notifyObserver());
        }
    }
}
