package de.oechsler.Controller;

import java.util.Locale;

public class LanguageController {

    public LanguageController(ReferenceHandler referencesHandler) {
        referencesHandler.getAutomatonStage().getLanguageGroup().selectedToggleProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        return;
                    }
                    if (newValue == referencesHandler.getAutomatonStage().getLanguageEnglishMenuItem()) {
                        ResourcesController.getResourcesController().setLocale(Locale.ENGLISH);
                    } else if (newValue == referencesHandler.getAutomatonStage().getLanguageGermanMenuItem()) {
                        ResourcesController.getResourcesController().setLocale(Locale.GERMAN);
                    }
                    referencesHandler.getAutomatonStage().getStatesPane().setCPickers();
                });
    }

}
