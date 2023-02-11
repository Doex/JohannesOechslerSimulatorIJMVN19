package de.oechsler.Controller;

import de.oechsler.model.Automaton;
import javafx.scene.control.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseController {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DB_NAME = "AutomatonDB";
    private static final String DB_URL_PREFIX = "jdbc:derby:" + DB_NAME;
    private static final String DB_URL = DB_URL_PREFIX + ";create=false";
    private static final String DB_URL_CREATE = DB_URL_PREFIX + ";create=true";

    private static final String TABLENAME = "SETTINGS";
    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + TABLENAME + " "
            + "( sysName VARCHAR(255) NOT NULL PRIMARY KEY, posX INTEGER NOT NULL, posY INTEGER NOT NULL, cellWidth INTEGER NOT NULL, cellHeight INTEGER NOT NULL, sliderSpeed INTEGER NOT NULL ) ";
    private static final String SELECT_STATEMENT = " SELECT sysName, posX, posY, cellWidth, cellHeight, sliderSpeed FROM " + TABLENAME + " WHERE sysName =?";
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLENAME + " (sysName, posX, posY, cellWidth, cellHeight, sliderSpeed) values (?, ?, ?, ?, ?, ?) ";
    private static final String UPDATE_STATEMENT = "UPDATE " + TABLENAME + " SET posX=?, posY=?, cellWidth=?, cellHeight=?, SliderSpeed=?" + " WHERE sysName=?";
    private static final String CHECK_SELECT_STATEMENT = "SELECT COUNT(*) FROM " + TABLENAME + " WHERE sysName=?";
    private static final String FILL_CHOICEBOX_STATEMENT = "SELECT sysName FROM " + TABLENAME;
    private static final String DELETE_STATEMENT = "DELETE FROM " + TABLENAME + " WHERE sysName=?";
    private static Connection connection = null;
    private final ReferenceHandler referenceHandler;


    public DatabaseController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        this.referenceHandler.setDatabaseController(this);

        if (init()) {
            this.referenceHandler.getAutomatonStage().getDbSerializeMenuItem().setOnAction(e -> {

                Optional res = referenceHandler.getDatabaseStage().getDBtextInputDialog().showAndWait();
                try {
                    if (res.isPresent()) {
                        if (!nameExists(res.get().toString())) {
                            safeAutomatonStage(res.get().toString());
                            updateDBsysNamesToChoiceBox();
                        } else {
                            updateDataInDatabase(res.get().toString());
                        }
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, das Speichern der Daten, beim bestätigen des Namens, ist schief gelaufen! "
                            , ButtonType.OK);
                    alert.showAndWait();
                }
            });
            this.referenceHandler.getAutomatonStage().getDbDeserializeMenuItem().setOnAction(e -> {
                try {
                    Optional result = this.referenceHandler.getDatabaseStage().getDbChoiceDialog().showAndWait();
                    if(result.isPresent()){
                        loadAutomatonStage(result.get().toString());
                    }
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, das laden einer Spezifikation ist schief gelaufen! ", ButtonType.OK);
                    alert.showAndWait();
                }
            });

            this.referenceHandler.getAutomatonStage().getDbDeleteMenuItem().setOnAction(e -> {
                updateDBsysNamesToChoiceBox();
                try {
                    Optional res = this.referenceHandler.getDatabaseStage().getDbChoiceDialog().showAndWait();
                    if (res.isPresent()) {
                        deleteSysSettingInDB(res.get().toString());
                        updateDBsysNamesToChoiceBox();
                    }

                } catch (Exception ex) {
                   Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, das löschen des Eintrages in der Datenbank ist schief gelaufen", ButtonType.OK);
                   alert.showAndWait();
                }
            });
        }
    }

    private boolean init() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            this.referenceHandler.getAutomatonStage().getDbSerializeMenuItem().setDisable(true);
            this.referenceHandler.getAutomatonStage().getDbDeserializeMenuItem().setDisable(true);
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL_CREATE);
             ResultSet resultSet = conn.getMetaData().getTables(null, null, TABLENAME, null)) {
            if (!resultSet.next()) {
                createAndInitTable(conn);
            }
        } catch (SQLException e) {
            this.referenceHandler.getAutomatonStage().getDbSerializeMenuItem().setDisable(true);
            this.referenceHandler.getAutomatonStage().getDbDeserializeMenuItem().setDisable(true);
            return false;
        }
        return true;
    }

    private void createAndInitTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_STATEMENT);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, das erstellen der Tabelle ist schief gelaufen!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed() && connection.isValid(0)) {
                return connection;
            }
            if (connection != null) {
                connection.close();
                return getConnection();
            }
            connection = DriverManager.getConnection(DB_URL);
            return connection;
        } catch (SQLException exc) {
            return null;
        }
    }

    public static void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
        if (DRIVER.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
            }
        }
    }

    private void safeAutomatonStage(String namePK) throws SQLException {
        Connection conn = getConnection();
        if (conn == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, hier gibt es einen fehler bei der Verbindung zur Datenbank! SafeStage()", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_STATEMENT)) {
            Automaton automaton = this.referenceHandler.getAutomaton();
            synchronized (automaton) {
                conn.setAutoCommit(false);

                insertStmt.setString(1, namePK);
                insertStmt.setDouble(2, this.referenceHandler.getAutomatonStage().getX());
                insertStmt.setDouble(3, this.referenceHandler.getAutomatonStage().getY());
                insertStmt.setDouble(4, this.referenceHandler.getAutomatonStage().getPopulationPane().getCellWidth());
                insertStmt.setDouble(5, this.referenceHandler.getAutomatonStage().getPopulationPane().getCellHeight());
                insertStmt.setDouble(6, this.referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().getValue());
                insertStmt.execute();
                conn.commit();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, Datenbankfehler beim Speichern der Einstellung! SafeAuto() ", ButtonType.OK);
            alert.showAndWait();
            conn.rollback();

        } finally {
            try {

                conn.setAutoCommit(true);

            } catch (SQLException e) {

            }
        }

    }

    /**
     * DATABASE:
     * name PK, posX , posY , cellWidth, cellHeight, sliderspeed
     */
    private void loadAutomatonStage(String namePK) {
        Connection conn = getConnection();

        if (conn == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, Datenbankfehler beim Aufbau einer Verbindung getConnection() - Load()! ", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_STATEMENT)) {
            stmt.setString(1, namePK);
            ResultSet set = stmt.executeQuery();
            //index 0 = posx, 1 = posy, 2 = cellwidth, 3 = cellheight, 4 = sliderspeed
            List<Double> values = new ArrayList<>();

            while (set.next()) {

                values.add(set.getDouble("posX"));
                values.add(set.getDouble("posY"));
                values.add(set.getDouble("cellWidth"));
                values.add(set.getDouble("cellHeight"));
                values.add(set.getDouble("sliderSpeed"));
                conn.commit();

            }

            if (!this.referenceHandler.getAutomatonStageController().setPosXYAndReturnBool(values.get(0), values.get(1))
                    || !this.referenceHandler.getAutomatonStage().getPopulationPane().setCellWidthAndHeightAndReturnBool(values.get(2), values.get(3))
                     || !this.referenceHandler.getAutomatonStageController().setSliderSpeedAndReturnBoolean(values.get(4))
                    ) {
                throw new Exception();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Datenbankfehler beim Lesen der Daten! loadAutomaton()", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private boolean nameExists(String namePK) throws SQLException {
        Connection conn = getConnection();

        if (conn == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, Verbindungsfehler - nameExists()", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(CHECK_SELECT_STATEMENT)) {

            stmt.setString(1, namePK);
            ResultSet set = stmt.executeQuery();

            return !set.next() || set.getInt(1) != 0;
        }
    }

    private void updateDataInDatabase(String namePK) throws SQLException {
        Connection conn = getConnection();
        if (conn == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, hier gibt es einen Fehler bei der Verbindung zur Datenbank! updateData()", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_STATEMENT)) {
            stmt.setString(6, namePK);
            stmt.setDouble(1, referenceHandler.getAutomatonStage().getX());
            stmt.setDouble(2, referenceHandler.getAutomatonStage().getY());
            stmt.setDouble(3, referenceHandler.getAutomatonStage().getPopulationPane().getCellWidth());
            stmt.setDouble(4, referenceHandler.getAutomatonStage().getPopulationPane().getBorderHeight());
            stmt.setDouble(5, referenceHandler.getAutomatonStage().getChangeSimulationSpeedSlider().getValue());
            stmt.execute();
            conn.commit();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, Datenbankfehler beim Speichern! updateData()", ButtonType.OK);
            alert.showAndWait();
            conn.rollback();

        } finally {
            try {
                conn.setAutoCommit(true);

            } catch (SQLException e) {

            }
        }
    }

    private void updateDBsysNamesToChoiceBox() {
        Connection conn = getConnection();
        if (conn == null) {
            new Alert(Alert.AlertType.ERROR, "Verbindungsfehler mit der Datenbank!", ButtonType.OK).showAndWait();
        }
        try (Statement stmt = conn.createStatement(); ResultSet set = stmt.executeQuery(FILL_CHOICEBOX_STATEMENT)) {

            while (set.next()) {
                if (!referenceHandler.getDatabaseStage().getDbChoiceDialog().getItems().contains(set.getString("sysName"))) {
                    referenceHandler.getDatabaseStage().getDbChoiceDialog().getItems().add(set.getString("sysName"));
                }
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Es konnten keine Einträge gefunden werden!", ButtonType.OK).showAndWait();
        }
    }

    private void deleteSysSettingInDB(String namePK) {
        Connection conn = getConnection();
        if (conn == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, Datenbankfehler beim Verbindungsaufbau! Delete()", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_STATEMENT)) {
            stmt.setString(1, namePK);
            stmt.execute();
            conn.commit();
            this.referenceHandler.getDatabaseStage().getDbChoiceDialog().getItems().remove(namePK);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, Datenbankfehler beim löschen einer Einstellung! delete()", ButtonType.OK);
            alert.showAndWait();
        }
    }

}