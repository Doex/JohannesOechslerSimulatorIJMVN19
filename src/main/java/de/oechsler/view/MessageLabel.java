package de.oechsler.view;

import de.oechsler.model.Automaton;
import de.oechsler.util.Observer;
import javafx.scene.control.Label;

public class MessageLabel extends Label implements Observer {

    private final Automaton automaton;

    public MessageLabel(Automaton automaton) {
        super("Willkommen");
        this.automaton = automaton;
        automaton.add(this);
    }

    @Override
    public void update() {

    }
}
