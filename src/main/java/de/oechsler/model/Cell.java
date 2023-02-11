package de.oechsler.model;

import java.io.Serializable;

public class Cell implements Serializable {

    private int state;

    public int getState() {
        return state;
    }

    public Cell(int state) {
        this.state = state;
    }
    public void setState(int state) {
        this.state = state;
    }

}
