package de.oechsler.model;

import de.oechsler.util.Observable;
import de.oechsler.util.Observer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.*;

public abstract class   Automaton extends Observable {
    private int rows;
    private int columns;
    private int numberOfStates;
    private boolean isMooreNeighborHood;
    private boolean isTorus;
    private String name;

    private Cell[][] cells;

    /**
     * Konstruktor
     *
     * @param rows                Anzahl an Reihen
     * @param columns             Anzahl an Spalten
     * @param numberOfStates      Anzahl an Zuständen; die Zustände
     *                            des Automaten
     *                            sind dann die Werte 0 bis
     *                            numberOfStates-1
     * @param isMooreNeighborHood true, falls der Automat die
     *                            Moore-Nachbarschaft
     *                            benutzt; false, falls der Automat die
     *                            von-Neumann-Nachbarschaft benutzt
     * @param isTorus             true, falls die Zellen als
     *                            Torus betrachtet werden
     *                            d
     */
    protected Automaton(int rows, int columns, int numberOfStates, boolean isMooreNeighborHood, boolean isTorus) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfStates = numberOfStates;
        this.isMooreNeighborHood = isMooreNeighborHood;
        this.isTorus = isTorus;
        this.cells = new Cell[rows][columns];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(0);
            }
        }
    }

    protected Automaton(int rows, int columns, boolean isTorus) {
        this.rows = rows;
        this.columns = columns;
        this.isTorus = isTorus;
        numberOfStates = 2;
        this.cells = new Cell[rows][columns];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(0);
            }
        }
    }

    /**
     * Implementierung der Transformationsregel
     *
     * @param cell      die betroffene Zelle (darf nicht verändert
     *                  werden!!!)
     * @param neighbors die Nachbarn der betroffenen Zelle (dürfen nicht
     *                  verändert werden!!!)
     * @return eine neu erzeugte Zelle, die gemäß der
     * Transformationsregel aus der
     * betroffenen Zelle hervorgeht
     * @throws Throwable moeglicherweise wirft die Methode eine Exception
     */
    protected abstract Cell transform(Cell cell, Cell[] neighbors) throws Throwable;

    /**
     * Liefert die Anzahl an Zuständen des Automaten; gültige Zustände sind
     * int-Werte zwischen 0 und Anzahl-1
     *
     * @return die Anzahl an Zuständen des Automaten
     */
    public synchronized int getNumberOfStates() {
        return this.numberOfStates;
    }

    /**
     * Liefert die Anzahl an Reihen
     *
     * @return die Anzahl an Reihen
     */
    public synchronized int getNumberOfRows() {
        return this.rows;
    }

    /**
     * Liefert die Anzahl an Spalten
     *
     * @return die Anzahl an Spalten
     */
    public synchronized int getNumberOfColumns() {
        return this.columns;
    }

    public synchronized void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }

    public synchronized String getName() {
        return this.name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }


    /**
     * Ändert die Größe des Automaten; Achtung: aktuelle Belegungen nicht
     * gelöschter Zellen sollen beibehalten werden; neue Zellen sollen im
     * Zustand 0 erzeugt werden
     *
     * @param rows    die neue Anzahl an Reihen
     * @param columns die neue Anzahl an Spalten
     */
    public synchronized void changeSize(int rows, int columns) {
        Cell[][] newCells = new Cell[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i < this.rows && j < this.columns) {
                    newCells[i][j] = cells[i][j];
                } else {
                    newCells[i][j] = new Cell(0);
                }
            }
        }
        this.rows = rows;
        this.columns = columns;
        this.cells = newCells.clone();

        notifyObserver();
    }

    /**
     * Liefert Informationen, ob der Automat als Torus betrachtet wird
     *
     * @return true, falls der Automat als Torus betrachtet wird; false
     * sonst
     */
    public synchronized boolean isTorus() {
        return this.isTorus;
    }

    /**
     * Ändert die Torus-Eigenschaft des Automaten
     *
     * @param isTorus true, falls der Automat als Torus betrachtet wird;
     *                false sonst
     */
    public synchronized void setTorus(boolean isTorus) {
        this.isTorus = isTorus;
        notifyObserver();
    }

    /**
     * Liefert Informationen über die Nachbarschaft-Eigenschaft des
     * Automaten
     * (Hinweis: Die Nachbarschaftseigenschaft kann nicht verändert werden)
     *
     * @return true, falls der Automat die Moore-Nachbarschaft berücksicht;
     * false, falls er die von-Neumann-Nachbarschaft berücksichtigt
     */
    public synchronized boolean isMooreNeighborHood() {
        return this.isMooreNeighborHood;
    }

    /**
     * setzt alle Zellen in den Zustand 0
     */
    public synchronized void clearPopulation() {
        Arrays.stream(cells).forEach(row -> Arrays.fill(row, new Cell(0)));
        notifyObserver();
    }

    /**
     * setzt für jede Zelle einen zufällig erzeugten Zustand
     */
    public synchronized void randomPopulation() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell(new Random().nextInt(this.numberOfStates));
            }
            notifyObserver();
        }
    }

    /**
     * Liefert eine Zelle des Automaten
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @return Cell-Objekt a Position row/column
     */
    public synchronized Cell getCell(int row, int column) {
        return this.cells[row][column];
    }

    /**
     * Aendert den Zustand einer Zelle
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @param state  neuer Zustand der Zelle
     */
    public synchronized void setState(int row, int column, int state) {
        cells[row][column] = new Cell(state);
        notifyObserver();
    }

    public synchronized int getState(int row, int column){
        return this.cells[row][column].getState();
    }

    /**
     * Aendert den Zustand eines ganzen Bereichs von Zellen
     *
     * @param fromRow    Reihe der obersten Zelle
     * @param fromColumn Spalte der obersten Zelle
     * @param toRow      Reihe der untersten Zelle
     * @param toColumn   Spalte der untersten Zelle
     * @param state      neuer Zustand der Zellen
     */
    public synchronized void setStateFromTo(int fromRow, int fromColumn, int toRow, int toColumn, int state) {
        for (int i = fromRow; i <= toRow; i++) {
            for (int j = fromColumn; j <= toColumn; j++) {
                cells[i][j].setState(state);
            }
        }
        notifyObserver();
    }

    /**
     * überführt den Automaten in die nächste Generation; ruft dabei die
     * abstrakte Methode "transform" für alle Zellen auf; Hinweis: zu
     * berücksichtigen sind die Nachbarschaftseigenschaft und die
     * Torus-Eigenschaft des Automaten
     *
     * @throws Throwable Exceptions der transform-Methode werden
     *                   weitergeleitet
     */
    public synchronized void nextGeneration() throws Throwable {
        Cell[][] newCells = new Cell[this.rows][this.columns];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                newCells[i][j] = transform(this.cells[i][j], getNeighborsAt(i, j));
            }
        }

        this.cells = newCells;
        notifyObserver();
    }

    private synchronized Cell[] getNeighborsAt(int row, int column) {

        Cell[] neighbors = new Cell[8];

        int check = 0;

        //Iterate
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    int x = row + i;
                    int y = column + j;

                    if (checkConstraints(x, y)) {
                        neighbors[check] = cells[x][y];
                    } else {
                        if (isTorus) {
                            x = x < 0 ? rows - 1 : x > rows - 1 ? 0 : x;
                            y = y < 0 ? columns - 1 : y > columns - 1 ? 0 : y;

                            if (checkConstraints(x, y)) {
                                neighbors[check] = cells[x][y];
                            } else {
                                throw new IllegalArgumentException("Fehler beim suchen der Nachbarn! ");
                            }
                        }
                    }
                    check++;
                }
            }
        }

        if (!isMooreNeighborHood) {
            neighbors[0] = null;
            neighbors[2] = null;

            neighbors[5] = null;
            neighbors[7] = null;
        }
        return neighbors;

    }

    private boolean checkConstraints(int row, int column) {
        return row >= 0 && column >= 0 && row < rows && column < columns;
    }


    public synchronized boolean setCellsAndReturnBool(Cell[][] cells) {
        if (cells.length != this.cells.length || cells[0].length != this.cells[0].length ) {
            try {
                changeSize(cells.length, cells[0].length);
            } catch (Exception e) {
                Alert alrt = new Alert(Alert.AlertType.ERROR, "Größe des Arrays konnte nicht angepasst werden\n" + e, ButtonType.OK);
                alrt.showAndWait();
            }
        } else {
            this.cells = cells;
        }
        notifyObserver();
        return true;
    }

    public synchronized boolean checkNoOfStates(int state) {
        if (this.numberOfStates != state) {
            return false;
        }
        this.numberOfStates = state;
        notifyObserver();
        return true;
    }

    public synchronized boolean setNumberOfRowsAndReturnBool(int rows) {
        if (rows <= 0 || rows >= 250) {
            return false;
        }
        this.rows = rows;
        notifyObserver();
        return true;
    }

    public synchronized boolean setNumberOfColumnsAndReturnBool(int columns) {
        if (columns <= 0 || columns >= 250) {
            return false;
        }
        this.columns = columns;
        notifyObserver();
        return true;
    }


    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Automaton observers = (Automaton) o;
        return rows == observers.rows && columns == observers.columns && numberOfStates == observers.numberOfStates
                && isMooreNeighborHood == observers.isMooreNeighborHood && isTorus == observers.isTorus
                && Arrays.deepEquals(cells, observers.cells);
    }

    @Override
    public synchronized int hashCode() {
        int result = Objects.hash(super.hashCode(), rows, columns, numberOfStates, isMooreNeighborHood, isTorus);
        result = 31 * result + Arrays.deepHashCode(cells);
        return result;
    }

    public void deleteObserver() {
        getObserver().removeAll(this);
    }

    public List<Observer> getObserver() {
        return new ArrayList<>(this);
    }

    public Cell[][] getCells() {
        return cells;
    }
}


