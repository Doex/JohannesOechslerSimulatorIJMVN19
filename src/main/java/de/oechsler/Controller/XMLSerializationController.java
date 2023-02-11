package de.oechsler.Controller;

import de.oechsler.model.Automaton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import javax.xml.stream.*;
import java.io.*;


/**
 *Tabelle: Nanme pk, zooomzahl, pos x, pos y, breite (col), höhe (row), speed
 *
 * Probleme: Speichern mit Namen -> existiert der Name bereits (Überschreiben), wenn nicht (Neu erstellen) -> muss aber alles locked sein
 *          Löschen -> als transaktion
 *          Wiederherstellen -> Abfragen der Werte, nochmal prüfen ob der Eintrag vielleicht doch in der zwischenzeit gelöscht worden ist.
 *
 */
public class XMLSerializationController {

    private static FileChooser fileChooser;

    static {

        fileChooser = new FileChooser();
        File dir = new File(".");
        fileChooser.setInitialDirectory(dir);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.xml", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public XMLSerializationController(ReferenceHandler referenceHandler) {
        referenceHandler.getAutomatonStage().getXmlSafeMenuItem().setOnAction(e -> savePopulationPane(referenceHandler));
        referenceHandler.getAutomatonStage().getXmlLoadMenuItem().setOnAction(e -> loadPopulationPane(referenceHandler));
    }

    public void savePopulationPane(ReferenceHandler referenceHandler) {
        fileChooser.setTitle("PopulationPane im XML-Format speichern");
        File file = fileChooser.showSaveDialog(referenceHandler.getAutomatonStage());

        if (file == null) {
            return;
        }
        if (!file.getName().endsWith(".xml")) {
            file = new File(file.getAbsolutePath().concat(".xml"));
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            writeXML(referenceHandler, fos);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Das speichern der XML-Datei war erfolgreich", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception exc) {
            exc.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, Fehler beim speichern der XML-Datei!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void loadPopulationPane(ReferenceHandler referenceHandler) {
        fileChooser.setTitle("XML-serialisierte PopulationsPanes laden");
        File file = fileChooser.showOpenDialog(referenceHandler.getAutomatonStage());

        if(file != null){
            try (FileInputStream fis = new FileInputStream(file)){
                loadXML(referenceHandler, fis);
            } catch (Exception exc) {
                exc.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, das laden des XML-PopulationPane ist schiefgelaufen! loadPopPane()", ButtonType.OK);
                alert.showAndWait();
            }
        }

    }

    /**
     *  XML-Format
     * 	 <?xml version="1.0" ?>
     * 	 <automaton numberOfRows="3" numberOfColumns="2" numberOfStates="2">
     * 	 <cell row="0" column="0" state="1"></cell>
     * 	 <cell row="0" column="1" state="0"></cell> ...
     * 	 </automaton>
     */
    private void writeXML(ReferenceHandler referenceHandler, OutputStream os) throws XMLStreamException {
        Automaton automaton = referenceHandler.getAutomaton();
        XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = outFactory.createXMLStreamWriter(os);

        try {
            final String NEW_LINE = System.lineSeparator();
            synchronized (automaton) {

                writer.writeStartDocument();
                writer.writeCharacters(NEW_LINE);
                writer.writeStartElement("automatonMenu");
                writer.writeAttribute("numberOfRows", String.valueOf(automaton.getNumberOfRows()));
                writer.writeAttribute("numberOfColumns", String.valueOf(automaton.getNumberOfColumns()));
                writer.writeAttribute("numberOfStates", String.valueOf(automaton.getNumberOfStates()));
                writer.writeCharacters(NEW_LINE);

                for(int r = 0; r < automaton.getNumberOfColumns(); r++){
                    for(int c = 0; c < automaton.getNumberOfColumns(); c++){
                        writer.writeStartElement("cell");
                        writer.writeAttribute("row", String.valueOf(r));
                        writer.writeAttribute("column", String.valueOf(c));
                        writer.writeAttribute("state", String.valueOf(automaton.getState(r,c)));
                        writer.writeEndElement(); // cell
                        writer.writeCharacters(NEW_LINE);
                    }
                }

                writer.writeEndElement(); //automaton
                writer.writeCharacters(NEW_LINE);
                writer.writeEndDocument();
            }
        } finally {
            writer.close();
        }

    }


    private  void loadXML(ReferenceHandler referenceHandler, InputStream is) throws Exception {
        XMLStreamReader parser = null;

        try {

            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            parser = factory.createXMLStreamReader(is);
            int noOfRow = -1;
            int noOfCols = -1;
            int noOfStates = -1;

            while (parser.hasNext()) {

                switch (parser.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:
                        String element = parser.getLocalName();
                        if ("automatonMenu".equals(element)) {

                            int numberOfRows = Integer.parseInt(parser.getAttributeValue(null, "numberOfRows"));
                            int numberOfColumns = Integer.parseInt(parser.getAttributeValue(null, "numberOfColumns"));
                            int numberOfStates = Integer.parseInt(parser.getAttributeValue(null, "numberOfStates"));
                            noOfRow = numberOfRows;
                            noOfCols = numberOfColumns;
                            noOfStates = numberOfStates;

                        } else if("cell".equals(element)){

                            int row = Integer.parseInt(parser.getAttributeValue(null, "row"));
                            int column = Integer.parseInt(parser.getAttributeValue(null, "column"));
                            int state = Integer.parseInt(parser.getAttributeValue(null, "state"));
                            referenceHandler.getAutomaton().setState(row, column, state);
                        }
                        break;
                }
                parser.next();
            }

            if (!referenceHandler.getAutomaton().setNumberOfRowsAndReturnBool(noOfRow) || !referenceHandler.getAutomaton().setNumberOfColumnsAndReturnBool(noOfCols)
                    || !referenceHandler.getAutomaton().checkNoOfStates(noOfStates)) {
               Alert alrt = new Alert(Alert.AlertType.ERROR, "Ups, da stimmt etwas nicht mit der Anzahl der Rows/Cols oder den Number of States", ButtonType.OK);
               alrt.showAndWait();
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

}
