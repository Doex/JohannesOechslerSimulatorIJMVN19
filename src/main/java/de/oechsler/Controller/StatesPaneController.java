package de.oechsler.Controller;
import javafx.scene.control.ColorPicker;
public class StatesPaneController {
    public StatesPaneController(ReferenceHandler referenceHandler){
        for (ColorPicker cpick : referenceHandler.getAutomatonStage().getColorPickerList()) {
            cpick.setOnAction(e -> referenceHandler.getAutomaton().notifyObserver());
        }
    }
}
