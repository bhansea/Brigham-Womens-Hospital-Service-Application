package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.AppSearch;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXListView;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.schema.Account;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.tools.Platform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


public class HeaderController extends HBox implements PropertyChangeListener {

//    @FXML
//    MFXButton troll;

    @FXML
    HBox headerSearch;



    public ObservableList<VBox> vboxPopulator(FilteredList<AppSearch.SearchableItem> filtered){
        ObservableMap<String, VBox> items = FXCollections.observableHashMap();
        for (AppSearch.SearchableItem s: filtered) {
            var name = s.getScreen().name();
            VBox item = items.get(name);
            if(s.getScreen().getShield().getShieldLevel() <= App.getSingleton().getAccount().getAccountType().getShieldLevel()){
                if (item == null) {
                    item = new VBox();
                    item.setId(name);
                    var separator = new Separator();
                    var nameLabel = new Label(name);
                    nameLabel.setStyle("-fx-cursor: hand; -fx-fill: -pfx-accent; -fx-underline: true");
                    nameLabel.setOnMouseClicked(e -> s.getNavigate().run());
                    items.put(name, item);
                    var description = new Label(s.getDescription());
                    description.setStyle("-fx-cursor: hand; -fx-fill: -pfx-accent;");
                    description.setOnMouseClicked(e -> s.getNavigate().run());
                    item.getChildren().addAll(nameLabel, separator, description);
                } else {
                    var description = new Label(s.getDescription());
                    description.setStyle("-fx-cursor: hand; -fx-fill: -pfx-accent;");
                    description.setOnMouseClicked(e -> s.getNavigate().run());
                    if (!item.getChildren().contains(description)) {
                        item.getChildren().add(description);
                    }
                }
            }
        }

        ObservableList<VBox> VBoxlist = FXCollections.observableList(new ArrayList<>(items.values()));
        return VBoxlist;
    }
    public void initialize() {
        App.getSingleton().addPropertyChangeListener(this);
        setAccount(App.getSingleton().getAccount());

        //for each page, list its functions??
        ObservableList<AppSearch.SearchableItem> list = FXCollections.observableArrayList();
        list.add(new AppSearch.SearchableItem("View the home page", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("Check your outstanding service requests", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("View service request pie chart", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("View service request table", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("View alerts", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("Change status of service requests", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("Mark service request as done", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));
        list.add(new AppSearch.SearchableItem("Mark service request as processing", Screen.HOME, () -> App.getSingleton().navigate(Screen.HOME)));

        list.add(new AppSearch.SearchableItem("Pathfind between two locations on the hospital map", Screen.MAP_PAGE, () -> App.getSingleton().navigate(Screen.MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("View the hospital map", Screen.MAP_PAGE, () -> App.getSingleton().navigate(Screen.MAP_PAGE)));

        list.add(new AppSearch.SearchableItem("Edit the hospital map", Screen.EDIT_MAP_PAGE, () -> App.getSingleton().navigate(Screen.EDIT_MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("Edit moves on the map", Screen.EDIT_MAP_PAGE, () -> App.getSingleton().navigate(Screen.EDIT_MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("Edit locations on the hospital map", Screen.EDIT_MAP_PAGE, () -> App.getSingleton().navigate(Screen.EDIT_MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("Add nodes to the hospital map", Screen.EDIT_MAP_PAGE, () -> App.getSingleton().navigate(Screen.EDIT_MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("Add edges to the hospital map", Screen.EDIT_MAP_PAGE, () -> App.getSingleton().navigate(Screen.EDIT_MAP_PAGE)));

        list.add(new AppSearch.SearchableItem("Create a service request", Screen.SERVICE_REQUEST, () -> App.getSingleton().navigate(Screen.SERVICE_REQUEST)));
        list.add(new AppSearch.SearchableItem("Create a flower delivery service request", Screen.SERVICE_REQUEST, () -> App.getSingleton().navigate(Screen.SERVICE_REQUEST)));
        list.add(new AppSearch.SearchableItem("Create a conference room service request", Screen.SERVICE_REQUEST, () -> App.getSingleton().navigate(Screen.SERVICE_REQUEST)));
        list.add(new AppSearch.SearchableItem("Create a office supplies service request", Screen.SERVICE_REQUEST, () -> App.getSingleton().navigate(Screen.SERVICE_REQUEST)));
        list.add(new AppSearch.SearchableItem("Create a food delivery service request", Screen.SERVICE_REQUEST, () -> App.getSingleton().navigate(Screen.SERVICE_REQUEST)));
        list.add(new AppSearch.SearchableItem("Create a furniture service request", Screen.SERVICE_REQUEST, () -> App.getSingleton().navigate(Screen.SERVICE_REQUEST)));

        list.add(new AppSearch.SearchableItem("View the about page", Screen.INFO, () -> App.getSingleton().navigate(Screen.INFO)));
        list.add(new AppSearch.SearchableItem( "View the credits page", Screen.INFO, () -> App.getSingleton().navigate(Screen.INFO)));
        var filtered = list.filtered(s -> true);

        var container = new VBox();
        var containerV2 = new VBox();



        ObservableList<VBox> VBoxlist = vboxPopulator(filtered);
        var listView = new PFXListView<VBox>(VBoxlist, s-> s, s -> s.getId());


        var scrollPane = new MFXScrollPane();
        var searchField = new TextField("");
        var searchBox = new HBox();
        searchBox.setPadding(new Insets(0, 0, 0, 18));
        searchBox.setStyle("-fx-background-radius: 40; -fx-background-color: -pfx-background; -fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 40");
        searchBox.setPrefHeight(35);
        searchBox.setMaxHeight(35);
        searchBox.setMinHeight(35);
        searchField.setStyle("-fx-background-color: -pfx-background;");
        searchField.setPrefHeight(30);
        searchField.setMaxHeight(30);
        searchField.setMinHeight(30);
        var icon = new PFXIcon();
        icon.setTranslateY(-2);
        icon.setIcon(MaterialSymbols.SEARCH);
        icon.setSize(30.0);
        searchBox.getChildren().addAll(icon, searchField);
        containerV2.getStyleClass().add("search-popup");
        containerV2.prefWidthProperty().bind(headerSearch.widthProperty());
        containerV2.maxWidthProperty().bind(headerSearch.widthProperty());
        containerV2.minWidthProperty().bind(headerSearch.widthProperty());
        containerV2.setMaxHeight(500);
        container.getChildren().addAll(searchBox, containerV2);
        containerV2.setTranslateX(headerSearch.getLayoutX());
        scrollPane.setContent(listView);
        containerV2.getChildren().addAll(scrollPane);


        headerSearch.setOnMouseClicked(e -> {
            searchField.requestFocus();
            searchField.positionCaret(searchField.getText().length());
            VBoxlist.clear();
            VBoxlist.addAll(vboxPopulator(filtered));
            var spacer = new HBox();
            spacer.setPrefWidth(App.getSingleton().getLayout().getLeftLayout().getWidth());
            var containerH = new HBox();
            var anotherVBox = new VBox();
            anotherVBox.setStyle("-fx-background-color: -pfx-secondary-transparent");
            containerH.getChildren().add(spacer);
            containerH.getChildren().add(container);
            containerH.setPadding(new Insets(10,0,0,10));
            anotherVBox.getChildren().add(containerH);
            App.getSingleton().getLayout().showOverlay(anotherVBox, true);
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtered.setPredicate(s -> true);
            if (newValue.isBlank()) {
                VBoxlist.clear();
                VBoxlist.addAll(vboxPopulator(filtered));
                return;
            }
            var preppedWords = Arrays.stream(newValue.trim().split("\s+")).distinct().map(w -> String.join(".?",
                    w.chars().mapToObj(c -> String.valueOf((char) c))
                            .toList())
            ).map(w -> Pattern.compile(w, Pattern.CASE_INSENSITIVE)).toList();
            list.forEach(si -> si.setCurrentWeight(( // weight 5
                    preppedWords.stream().mapToInt(regex ->
                            regex.matcher(si.getScreen().name()).find() ? 1 : 0
                    ).sum()) * 5
                    + ( // weight 1
                    preppedWords.stream().mapToInt(regex ->
                            regex.matcher(si.getDescription()).find() ? 1 : 0
                    ).sum())));
            FXCollections.sort(list, Comparator.comparing(AppSearch.SearchableItem::getCurrentWeight).reversed());
            filtered.setPredicate(si -> si.getCurrentWeight() > 0);
            VBoxlist.clear();
            VBoxlist.addAll(vboxPopulator(filtered));
            if(filtered.isEmpty()){
                var emptyVBox = new VBox();
                var emptyLabel = new Label("No results");
                emptyVBox.getChildren().add(emptyLabel);
                VBoxlist.add(emptyVBox);
            }
        });
    }

    public HeaderController() {
        super();
        try {
            App.getSingleton().loadWithCache("frontend/components/Header.fxml", this, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAccount(Account account) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }

    private List<String> performSearchQuery(String query) {

        ArrayList<String> list1 = new ArrayList<>();
        list1.add("hey");
        return list1;
    }
}
