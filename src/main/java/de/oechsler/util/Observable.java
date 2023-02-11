package de.oechsler.util;

import java.util.ArrayList;

public class Observable extends ArrayList<Observer> {

    public void notifyObserver() {
        for (Observer obs : this) {
            obs.update();
        }
    }




}
