package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class ServiceRequestController {

    @FXML
    MFXTextField servSearchBar;

    @FXML
    MFXButton submit;

    // @FXML MFXButton back;
    @Getter
    @Setter
    private ArrayList<String> requests = new ArrayList<>(); //store requests in list to search through

    @FXML
    String showReq() {
        String temp = "";
        if (submit.isPressed()) {
            temp = servSearchBar.getText();
            for (int i = 0; i < requests.size(); i++) { //does not loop because theres nothing in requests so no size
                //so i++ is never used as well
                if (temp.matches(requests.get(i))) {
                    return requests.get(i);
                } else {
                    throw new RuntimeException("No such request found.");
                }
            }
        }

        //match case
        //throw error if no request found
        //display request
        //display back button/somehow reset the search bar for a new search?

        return temp;


    }
}
