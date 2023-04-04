package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class OfficeServiceRequestController {
    @FXML
    private MFXButton submit;
    @FXML
    private TextField officeRequest;
    @FXML
    private TextField employeeName;
    @FXML
    private TextField roomNum;
    @FXML
    private TextField addNotes;
    private Connection connection = null;

    public void initialize() {
        // if connection is successful
        if (this.connectToDB()) {
            this.createTable();
        }
    }



    /**
     * When the button is clicked, the method will log the data in the terminal and database
     *
     * @param actionEvent event that triggered method
     * @throws IOException
     */
    public void buttonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("Button was clicked");
        System.out.println(this.logData() ? "Data logged" : "Data NOT logged");
    }

    public void submitButtonClick(ActionEvent actionEvent) throws IOException {
        System.out.println("Button was clicked");
        System.out.println(this.logData() ? "Data logged" : "Data NOT logged");
        Navigation.navigate(Screen.HOME);
    }

    /**
     * Generates connection to server on localhost at default port (1521) be aware of the username and
     * password when testing
     *
     * @return True when connection is successful, False when failed
     */
    private boolean connectToDB() {

        try {
            Class.forName(
                    "org.apache.derby.jdbc.ClientDriver"); // Check that proper driver is packaged for Apache
            // Derby
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NO DRIVER");
            return false;
        }
        try {
            // create Connection at specified URL
            this.connection =
                    DriverManager.getConnection(
                            "jdbc:derby://localhost:1527/testDB;create=true",
                            "app",
                            "derbypass"); // This will change for each team as their DB is developed
            if (this.connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // connection successful, return true
        return true;
    }

    /**
     * generates a table to store button click information
     *
     * @return true when table is successfully created or already exists, false otherwise
     */
    private boolean createTable() {

        boolean table_exists = false;

        if (this.connection != null) {
            String createQuery =
                    "CREATE TABLE APP.buttonClicks("
                            + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                            + "btn_name VARCHAR(50), "
                            + "time_stamp TIMESTAMP NOT NULL, "
                            + "PRIMARY KEY(id) )";
            try {
                Statement statement = this.connection.createStatement();
                statement.execute(createQuery);

                table_exists = true;
            } catch (SQLException e) {
                // Error code 955 is "name is already used by an existing object", so this table name
                // already exists
                if (e.getErrorCode() == 955 || e.getMessage().contains("already exists"))
                    table_exists = true;
                else e.printStackTrace();
            }
        }
        return table_exists;
    }

    /**
     * Stores button click data to database
     *
     * @return true if data is stored successfully, false otherwise
     */
    private boolean logData() {
        if (connection != null) {
            String writeQuery =
                    "INSERT INTO APP.buttonClicks(btn_name, time_stamp) VALUES ( 'ClickButton', CURRENT_TIMESTAMP ) ";
            try {
                Statement statement = this.connection.createStatement();
                statement.execute(writeQuery);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}