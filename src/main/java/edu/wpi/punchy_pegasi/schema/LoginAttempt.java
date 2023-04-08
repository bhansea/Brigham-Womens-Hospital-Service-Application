package edu.wpi.punchy_pegasi.schema;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

public class LoginAttempt {
    private String username;
    private String password;
    private boolean loginSuccess;
    private final PdbController pdb = App.getSingleton().getPdb();

    public LoginAttempt(String username, String password) {
        this.username = username;
        this.password = password;
        try {
            this.loginSuccess = pdb.loginCheck(username, password);
        } catch (SQLException e) {

        }
    }



}
