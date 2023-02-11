package de.oechsler.view;

import de.oechsler.Controller.ReferenceHandler;
import de.oechsler.model.Automaton;
import de.oechsler.model.Callable;
import de.oechsler.util.Pair;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopulationPaneContextMenu extends ContextMenu {

    private static final String PARAMS = "(int row, int column)";

    public PopulationPaneContextMenu(ReferenceHandler referenceHandler, PopulationPane pane, Pair<Integer> coords) {
        List<Method> methods = getMethods(referenceHandler.getAutomaton());
        for (Method method : methods) {
            MenuItem item = new MenuItem(method.getName() + PARAMS);
            item.setOnAction(e -> {
                try {
                    method.setAccessible(true);
                    method.invoke(referenceHandler.getAutomaton(), coords.value1(), coords.value2());
                    pane.paintCanvas();
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "ups, beim ausführen der invoke-methode ist ein Fehler aufgetreten",
                            ButtonType.OK);
                    alert.showAndWait();
                }
            });
            getItems().add(item);
        }
    }
    private List<Method> getMethods(Automaton automaton) {
        List<Method> res = new ArrayList<>();
        for (Method method : automaton.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() == 2 && method.isAnnotationPresent(Callable.class)
                    && !Modifier.isAbstract(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())
                    && Arrays.stream(method.getParameterTypes()).anyMatch(i -> i == int.class)) {

                res.add(method);
            }
        }
        return res;
    }


}
