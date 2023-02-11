package de.oechsler.AutomatonImplementations;

import de.oechsler.model.Automaton;
import de.oechsler.model.Callable;
import de.oechsler.model.Cell;

public class GameOfLifeAutomaton extends Automaton {

    public GameOfLifeAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, isTorus);
    }

    public GameOfLifeAutomaton() {
        this(20, 20, true);
    }

    @Override
    protected synchronized Cell transform(Cell cell, Cell[] neighbors) {
        int count = 0;
        for (Cell tmpCell : neighbors) {
            if(tmpCell == null){
                continue;
            }
            if (tmpCell.getState() != 0) {
                count++;
            }
        }
        if ((count == 3) && cell.getState() == 0) {
            return new Cell(1);
        }
        if (count < 2) {
            return new Cell(0);
        }
        if ((count == 2 || count == 3) && (cell.getState() == 1)) {
            return new Cell(1);
        }
        if (count > 3 && cell.getState() == 1) {
            return new Cell(0);
        }

        return new Cell(0);
    }

    @Callable
    public void setGlider(int row, int col){
        setState(row, col, 1);
        setState(row + 1, col + 1, 1);
        setState(row + 2, col - 1, 1);
        setState(row + 2, col, 1);
        setState(row + 2, col + 1, 1);
        notifyObserver();
    }

    @Callable
    public void setOscillator(int row, int col){
        setState(row, col, 1);
        setState(row + 1, col, 1);
        setState(row + 2, col, 1);
        notifyObserver();
    }
}
