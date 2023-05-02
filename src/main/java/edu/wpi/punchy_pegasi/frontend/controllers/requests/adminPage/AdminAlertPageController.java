package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXAlertCard;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.controllers.LayoutController;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Alert;
import edu.wpi.punchy_pegasi.schema.TableType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminAlertPageController {
@FXML
    public TextField alertDescription;
@FXML
    public TextField alertTitle;
@FXML
    public VBox alertsContainer;
@FXML
    public PFXButton sendButton;
    public VBox activeAlertsContainer;
    public BorderPane container;
    public PFXButton removeButton;
    Facade facade = App.getSingleton().getFacade();
    LayoutController layout = new LayoutController();

    public void initialize() {
        App.getSingleton().getExecutorService().execute( () -> {
            AdminTable alertTable = new AdminTable("Alert", TableType.ALERT, facade::getAllAsListAlert);
            alertTable.init();

            List<Alert> allAlerts = App.getSingleton().getFacade().getAllAsListAlert();
            List<Alert> alerts = new ArrayList<>();
            ArrayList<PFXAlertCard> alertCards = new ArrayList<>();
            ArrayList<Integer> indexes = new ArrayList<>();


            Platform.runLater( () -> {
//                activeAlertsContainer.getChildren().add(alertTable.getTable());
                activeAlertsContainer.setStyle("-fx-spacing: 15");

                for(Alert alert: allAlerts) {
                    if(App.getSingleton().getAccount().getEmployeeID().equals(alert.getEmployeeID())) {
                        PFXAlertCard alertCard = new PFXAlertCard(alert);
                        alertCard.changeToDeleteIcon();
                        activeAlertsContainer.getChildren().add(alertCard);
                        alertCards.add(alertCard);
;                       alerts.add(alert);
                    }
                }

                // this is a terrible way to do it i know please dont hurt me
                removeButton.setOnAction(e -> {
                    int i = 0;
                    for (PFXAlertCard card: alertCards) {

                        if (card.getIsRead()) {
                            activeAlertsContainer.getChildren().remove(card);
                            indexes.add(i);
                        }


                        for (Integer num : indexes) {
                            allAlerts.remove(alertCards.get(i).getAlert());
                            facade.deleteAlert(alertCards.get(i).getAlert());
                            alertCards.remove(num);
                            alerts.remove(num);
                        }
                        i++;
                    }

                });

                sendButton.setOnAction(e -> {
                    Instant dateTime = Instant.now();
                    Alert.AlertType alertType = Alert.AlertType.NONE;
                    if (alertTypeComboBox.getSelectedItem().equals("Map")) {
                        alertType = Alert.AlertType.MAP;
                    } else if (alertTypeComboBox.getSelectedItem().equals("Map Disabled")) {
                        alertType = Alert.AlertType.MAP_DISABLED;
                    } else if (alertTypeComboBox.getSelectedItem().equals("Employee")) {
                        alertType = Alert.AlertType.EMPLOYEE;
                    } else if (alertTypeComboBox.getSelectedItem().equals("Admin")) {
                        alertType = Alert.AlertType.ADMIN;
                    } else if (alertTypeComboBox.getSelectedItem().equals("Service Request")) {
                        alertType = Alert.AlertType.SERVICE_REQUEST;
                    }


                    Alert alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.READ).build();
                    facade.saveAlert(alert);
                    alerts.add(alert);
                    PFXAlertCard alertCard = new PFXAlertCard(alert);
                    activeAlertsContainer.getChildren().add(alertCard);
                    alertCards.add(alertCard);
                    alertCards.get(alertCards.size()-1).changeToDeleteIcon();
                });
            });
        });


    }
}
