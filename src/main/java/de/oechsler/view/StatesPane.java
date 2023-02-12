package de.oechsler.view;
import de.oechsler.Controller.ReferenceHandler;
import de.oechsler.util.Observer;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class StatesPane implements Observer {

    private AutomatonStage stage;

    public StatesPane(ReferenceHandler referenceHandler, AutomatonStage automatonStage){
        this.stage = automatonStage;
        referenceHandler.add(this);
        for (int i = 0; i < referenceHandler.getAutomaton().getNumberOfStates(); i++) {

            ColorPicker cpick = new ColorPicker(new Color(Math.random(), Math.random(), Math.random(), 1.0));
            RadioButton btn = new RadioButton(String.valueOf(i));
            if(i == 1){
                btn.fire();
            }

            HBox box = new HBox(30, btn, cpick);

            automatonStage.getStateBoxes().getChildren().add(box);
            automatonStage.getRadioButtonList().add(btn);
            automatonStage.getColorPickerList().add(cpick);
            btn.setToggleGroup(automatonStage.getToggleGroup());
            btn.setStyle("-fx-padding: 8px");
            btn.selectedProperty().addListener((observableValue, aBoolean, t1) -> cpick.setDisable(aBoolean));
        }
    }

    public void setCPickers(){
       for(int i = 0; i < this.stage.getColorPickerList().size(); i++){
           this.stage.getColorPickerList().set(i, new ColorPicker(this.stage.getColorPickerList().get(i).getValue()));
       }
    }

    @Override
    public void update() {
    }

}
