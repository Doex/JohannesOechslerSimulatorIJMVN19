package de.oechsler.view;

import de.oechsler.Controller.ReferenceHandler;
import de.oechsler.model.Automaton;
import de.oechsler.util.Observer;
import de.oechsler.util.Pair;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.List;

public class PopulationPane extends Region implements Observer {


    private Canvas canvas;
    private List<ColorPicker> colorPickerList;
    private AutomatonStage automatonStage;
    private ReferenceHandler referenceHandler;


    private double cellWidth = 25;
    private double cellHeight = 25;
    private double borderWidth = 4;
    private double borderHeight = 4;


    public PopulationPane(ReferenceHandler referenceHandler, List<ColorPicker> colorPickerList, AutomatonStage automatonStage) {
        this.referenceHandler = referenceHandler;
        this.referenceHandler.add(this);
        this.colorPickerList = colorPickerList;
        this.automatonStage = automatonStage;
        this.canvas = new Canvas(calcWidth(), calcHeight());
        paintCanvas();
        this.getChildren().add(this.canvas);
    }

    public void paintCanvas() {
        setWidth(calcWidth());
        setHeight(calcHeight());
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        for (int r = 0; r < this.referenceHandler.getAutomaton().getNumberOfRows(); r++) {
            for (int c = 0; c < this.referenceHandler.getAutomaton().getNumberOfColumns(); c++) {
                gc.setFill(this.colorPickerList.get(this.referenceHandler.getAutomaton().getCell(r, c).getState()).getValue());
                fillRect(gc, r, c);
            }
        }
    }

    private void fillRect(GraphicsContext gc, int row, int column) {
        gc.fillRect(this.borderWidth + column * this.cellWidth,
                this.borderHeight + row * this.cellHeight,
                this.cellWidth, this.cellHeight);

        gc.strokeRect(this.borderWidth + column * this.cellWidth,
                this.borderHeight + row * this.cellHeight,
                this.cellWidth, this.cellHeight);

    }

    private double calcHeight() {
        return 2 * this.borderHeight + this.cellHeight * this.referenceHandler.getAutomaton().getNumberOfColumns();
    }

    private double calcWidth() {
        return 2 * this.borderWidth + this.cellWidth * this.referenceHandler.getAutomaton().getNumberOfRows();
    }

    public void center(Bounds viewPortBounds) {
        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();
        if (width > this.calcWidth()) {
            this.canvas.setTranslateX((width - this.calcWidth()) / 2);
        } else {
            this.canvas.setTranslateX(0);
        }
        if (height > this.calcHeight()) {
            this.canvas.setTranslateY((height - this.calcHeight()) / 2);
        } else {
            this.canvas.setTranslateY(0);
        }
    }


    public Pair<Integer> getRowAndCol(double x, double y) {
        if (x < this.borderWidth || y < this.borderHeight
                || x > this.borderWidth + this.referenceHandler.getAutomaton().getNumberOfColumns() * this.cellWidth
                || y > this.borderHeight + this.referenceHandler.getAutomaton().getNumberOfRows() * this.cellHeight) {
            return null;
        }
        int row = (int) ((y - this.borderHeight) / this.cellHeight);
        int column = (int) ((x - this.borderWidth) / this.cellWidth);
        return new Pair<>(row, column);
    }

    public boolean setCellWidthAndHeightAndReturnBool(double width, double height){
        if(height < 0 || width < 0 || height != width){
            return false;
        }
        this.cellHeight = height;
        this.cellWidth = width;
        return true;
    }

    @Override
    public void update() {
        if(Platform.isFxApplicationThread()){
            this.paintCanvas();
        } else {
            Platform.runLater(this::paintCanvas);
        }
    }


    //Getter&Setter


    public Canvas getCanvas() {
        return canvas;
    }

    public List<ColorPicker> getColorPickerList() {
        return colorPickerList;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public double getBorderHeight() {
        return borderHeight;
    }

    public AutomatonStage getAutomatonStage() {
        return automatonStage;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }

    public void setCellHeight(double cellHeight) {
        this.cellHeight = cellHeight;
    }

    public void setBorderWidth(double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setBorderHeight(double borderHeight) {
        this.borderHeight = borderHeight;
    }

    public ReferenceHandler getReferenceHandler() {
        return referenceHandler;
    }
}
