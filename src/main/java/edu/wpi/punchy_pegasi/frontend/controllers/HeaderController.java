package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.AppSearch;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXListView;
import edu.wpi.punchy_pegasi.schema.Account;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;


public class HeaderController extends HBox implements PropertyChangeListener {

    @FXML
    MFXButton troll;

    @FXML
    HBox headerSearch;



    public ObservableList<VBox> vboxPopulator(FilteredList<AppSearch.SearchableItem> filtered){
        ObservableMap<String, VBox> items = FXCollections.observableHashMap();
        for (AppSearch.SearchableItem s: filtered) {
            var name = s.getScreen().name();
            VBox item = items.get(name);
            if (item == null) {
                item = new VBox();
                item.setId(name);
                var separator = new Separator();
                var nameLabel = new Label(name);
                nameLabel.setStyle("-fx-cursor: hand; -fx-fill: -pfx-accent; -fx-underline: true");
                nameLabel.setOnMouseClicked(e -> s.getNavigate().run());
                items.put(name, item);
                var description = new Label(s.getDescription());
                item.getChildren().addAll(nameLabel, separator, description);
            } else {
                var description = new Label(s.getDescription());
                description.setOnMouseClicked(e -> s.getNavigate().run());
                if (!item.getChildren().contains(description)) {
                    item.getChildren().add(description);
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
        list.add(new AppSearch.SearchableItem("Pathfind between two different locations on the hospital map", Screen.MAP_PAGE, () -> App.getSingleton().navigate(Screen.MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("View the hospital map", Screen.MAP_PAGE, () -> App.getSingleton().navigate(Screen.MAP_PAGE)));
        list.add(new AppSearch.SearchableItem("View the about page", Screen.INFO, () -> App.getSingleton().navigate(Screen.INFO)));
        list.add(new AppSearch.SearchableItem( "View the credits page", Screen.INFO, () -> App.getSingleton().navigate(Screen.INFO)));
        var filtered = list.filtered(s -> true);

        var container = new VBox();
        container.setStyle("-fx-background-color: -pfx-secondary-transparent");
        var containerV2 = new VBox();
        

        ObservableList<VBox> VBoxlist = vboxPopulator(filtered);
        var listView = new PFXListView<VBox>(VBoxlist, s-> s, s -> s.getId());


        var searchField = new TextField("Testing");
        containerV2.getStyleClass().add("search-popup");
        containerV2.prefWidthProperty().bind(headerSearch.widthProperty());
        containerV2.maxWidthProperty().bind(headerSearch.widthProperty());
        containerV2.minWidthProperty().bind(headerSearch.widthProperty());
        container.getChildren().add(containerV2);
        containerV2.setTranslateX(headerSearch.getLayoutX());
        containerV2.getChildren().addAll(searchField, listView);
        headerSearch.setOnMouseClicked(e -> {
            App.getSingleton().getLayout().showOverlay(container, true);
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
