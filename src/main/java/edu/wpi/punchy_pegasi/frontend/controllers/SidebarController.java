package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SidebarController implements PropertyChangeListener {
    @FXML
    HBox loginLogout;
    @FXML
    private VBox sidebar;
    @FXML
    HBox editMapPage;
    @FXML
    HBox home;
    @FXML
    VBox serviceRequestDropDown;
    @FXML
    Label conferenceRoomText;
    @FXML
    Label officeSuppliesText;
    @FXML
    Label furnitureText;
    @FXML
    Label flowersText;
    @FXML
    Label mealText;
    @FXML
    HBox serviceRequests;
    @FXML
    HBox map;
    @FXML
    HBox adminPage;
    @FXML
    HBox signagePage;
    @FXML
    private Label loginLogoutText;
    @FXML
    ImageView profileImage;
    @FXML
    Label nameText;
    @FXML
    Label roleText;
    @FXML
    private VBox accountInfo;
    private ObservableList<SidebarItem> sidebarItems = FXCollections.observableArrayList(
            new SidebarItem(Screen.HOME, "M19.469 12.594l3.625 3.313c0.438 0.406 0.313 0.719-0.281 0.719h-2.719v8.656c0 0.594-0.5 1.125-1.094 1.125h-4.719v-6.063c0-0.594-0.531-1.125-1.125-1.125h-2.969c-0.594 0-1.125 0.531-1.125 1.125v6.063h-4.719c-0.594 0-1.125-0.531-1.125-1.125v-8.656h-2.688c-0.594 0-0.719-0.313-0.281-0.719l10.594-9.625c0.438-0.406 1.188-0.406 1.656 0l2.406 2.156v-1.719c0-0.594 0.531-1.125 1.125-1.125h2.344c0.594 0 1.094 0.531 1.094 1.125v5.875z"),
            new SidebarItem("Service Requests", "M27 29h-4v-21h4c1.104 0 2 0.896 2 2v17c0 1.104-0.896 2-2 2zM10 8v0-2c0-1.105 0.896-2 2-2h7c1.104 0 2 0.895 2 2v23h-11v-21zM12 8h7c0 0 0-0.448 0-1 0-0.553-0.448-1-1-1h-5c-0.553 0-1 0.447-1 1 0 0.552 0 1 0 1zM2 27v-17c0-1.104 0.896-2 2-2h4v21h-4c-1.104 0-2-0.896-2-2z", Arrays.stream(Screen.values()).filter(v -> v.name().toLowerCase().contains("request")).toList()),
            new SidebarItem(Screen.MAP_PAGE, "M390.54,55.719C353.383,18.578,304.696,0,255.993,0c-48.688,0-97.391,18.578-134.547,55.719 c-59.219,59.219-74.641,149.563-36.094,218.875C129.586,354.109,255.993,512,255.993,512s126.422-157.891,170.656-237.406 C465.195,205.281,449.773,114.938,390.54,55.719z M255.993,305.844c-63.813,0-115.563-51.75-115.563-115.547 c0-63.859,51.75-115.609,115.563-115.609c63.828,0,115.578,51.75,115.578,115.609C371.571,254.094,319.821,305.844,255.993,305.844 z"),
            new SidebarItem(Screen.ADMIN_PAGE, "M11,6H5v2h6V6z M11,2c0-0.6-0.4-1-1-1H9.4C9.2,0.4,8.7,0,8,0S6.8,0.4,6.6,1H6C5.4,1,5,1.4,5,2v2h6V2z M11,10H5v2h6V10z M13,2h-1v2h1v10H3V4h1V2H3C1.9,2,1,2.9,1,4v10c0,1.1,0.9,2,2,2h10c1.1,0,2-0.9,2-2V4C15,2.9,14.1,2,13,2z"),
            new SidebarItem(Screen.EDIT_MAP_PAGE, "M390.54,55.719C353.383,18.578,304.696,0,255.993,0c-48.688,0-97.391,18.578-134.547,55.719 c-59.219,59.219-74.641,149.563-36.094,218.875C129.586,354.109,255.993,512,255.993,512s126.422-157.891,170.656-237.406 C465.195,205.281,449.773,114.938,390.54,55.719z M255.993,305.844c-63.813,0-115.563-51.75-115.563-115.547 c0-63.859,51.75-115.609,115.563-115.609c63.828,0,115.578,51.75,115.578,115.609C371.571,254.094,319.821,305.844,255.993,305.844 z"),
            new SidebarItem(Screen.SIGNAGE, "M10,4843 L14,4843 L14,4841 L10,4841 L10,4843 Z M18,4850 C18,4850.552 17.552,4851 17,4851 L7,4851 C6.448,4851 6,4850.552 6,4850 L6,4846 C6,4845.448 6.448,4845 7,4845 L17,4845 C17.552,4845 18,4845.448 18,4846 L18,4850 Z M22,4839 L5,4839 C4.448,4839 4,4839.448 4,4840 C4,4840.552 4.448,4841 5,4841 L8,4841 L8,4843 L6,4843 L6,4843 C4.895,4843 4,4843.895 4,4845 L4,4845 L4,4851 L4,4851 C4,4852.105 4.895,4853 6,4853 L18,4853 C19.105,4853 20,4852.105 20,4851 L20,4851 L20,4845 L20,4845 C20,4843.895 19.105,4843 18,4843 L18,4843 L16,4843 L16,4841 L20.997,4841 C21.551,4841 22,4841.449 22,4842.003 L22,4858 C22,4858.552 22.448,4859 23,4859 L23,4859 C23.552,4859 24,4858.552 24,4858 L24,4841 L24,4841 C24,4839.895 23.105,4839 22,4839 L22,4839 Z")
    );

    private void setAccount(Account account) {
        sidebarItems.forEach(s -> {
            if (s.dropdown) {
                var min = s.dropdownItems.stream().mapToInt(s2 -> s2.getShield().getShieldLevel()).min();
                if (min.isEmpty()) return;
                if (min.getAsInt() > account.getAccountType().getShieldLevel()) {
                    s.node.setVisible(false);
                    s.node.setManaged(false);
                } else {
                    s.node.setVisible(true);
                    s.node.setManaged(true);
                    s.dropdownItems.forEach(dI -> {
                        if (account.getAccountType().getShieldLevel() < dI.getShield().getShieldLevel()) {
                            //hide
                        } else {
                            //show
                        }
                    });
                }
            } else {
                var screen = s.screen;
                if (screen == null) return;
                if (account.getAccountType().getShieldLevel() >= screen.getShield().getShieldLevel()) {
                    s.node.setVisible(true);
                    s.node.setManaged(true);
                } else {
                    s.node.setVisible(false);
                    s.node.setManaged(false);
                }
            }
        });

        if (account.getAccountType() == Account.AccountType.NONE) {
            accountInfo.setManaged(false);
            accountInfo.setVisible(false);
            loginLogoutText.setText("Login");
            return;
        }

        accountInfo.setManaged(true);
        accountInfo.setVisible(true);
        loginLogoutText.setText("Logout");
        nameText.setText(account.getUsername());
        roleText.setText(account.getAccountType().name());
    }

    public void initialize() {
        App.getSingleton().addPropertyChangeListener(this);
        setAccount(App.getSingleton().getAccount());

        Image defaultImage = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/bwhlogo.png")));
        profileImage.imageProperty().set(defaultImage);

        sidebar.getChildren().addAll(1, sidebarItems.stream().map(v -> v.node).toList());

        loginLogout.setOnMouseClicked(e -> {
            if (App.getSingleton().getAccount().getAccountType() == Account.AccountType.NONE)
                App.getSingleton().navigate(Screen.LOGIN);
            else
                App.getSingleton().setAccount(null);
        });
    }

    private class SidebarItem {
        final boolean dropdown;
        final String dropdownText;
        final String svg;
        final Screen screen;
        List<Screen> dropdownItems;
        Node node;

        SidebarItem(Screen screen, String svg) {
            this.dropdown = false;
            this.svg = svg;
            this.screen = screen;
            this.dropdownText = "";
            node = dropdown ? dropdown() : normal();
        }

        SidebarItem(String text, String svg, List<Screen> dropdownItems) {
            this.dropdown = true;
            this.svg = svg;
            this.screen = null;
            this.dropdownText = text;
            this.dropdownItems = dropdownItems;
            node = dropdown ? dropdown() : normal();
        }

        private Node normal() {
            var container = new HBox();
            container.setAlignment(Pos.CENTER_LEFT);
            container.setSpacing(10);
            container.getStyleClass().add("button");
            container.setPadding(new Insets(0, 0, 0, 25));
            var label = new Label(screen != null ? screen.getReadable() : dropdownText);
            label.setTextFill(Color.valueOf("#FFFFFF"));
            label.setFont(new Font(20));
            var region = new Region();
            region.getStyleClass().add("icon");
            var svgPath = new SVGPath();
            svgPath.setContent(svg);
            region.setShape(svgPath);
            container.getChildren().addAll(region, label);
            container.setOnMouseClicked(e -> App.getSingleton().navigate(screen));
            return container;
        }

        private Node dropdown() {
            var container = new HBox();
            container.setAlignment(Pos.CENTER_LEFT);
            var vbox = new VBox();
            container.getChildren().add(vbox);

            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setSpacing(10);
            HBox.setHgrow(vbox, Priority.ALWAYS);
            var serviceRequests = normal();
            var serviceRequestDropDown = new VBox();
            serviceRequestDropDown.setAlignment(Pos.CENTER_LEFT);
            serviceRequestDropDown.setSpacing(5);
            serviceRequestDropDown.setPadding(new Insets(0, 0, 0, 65));
            for (var item : dropdownItems) {
                var label2 = new Label(item.getReadable().replaceAll("[Rr]equest", "").trim());
                label2.setTextFill(Color.valueOf("#FFFFFF"));
                label2.setOpacity(0.5);
                label2.setOnMouseEntered(e -> label2.setOpacity(1.0));
                label2.setOnMouseExited(e -> label2.setOpacity(.5));
                label2.setOnMouseClicked(e -> App.getSingleton().navigate(item));
                serviceRequestDropDown.getChildren().add(label2);
            }
            vbox.getChildren().addAll(serviceRequests, serviceRequestDropDown);
            serviceRequestDropDown.setVisible(false);
            serviceRequestDropDown.setManaged(false);

            serviceRequests.onMouseClickedProperty().set(e -> {
                if (serviceRequestDropDown.isVisible()) {
                    serviceRequestDropDown.setVisible(false);
                    serviceRequestDropDown.setManaged(false);
                } else {
                    serviceRequestDropDown.setVisible(true);
                    serviceRequestDropDown.setManaged(true);
                }
            });
            return container;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }
}

