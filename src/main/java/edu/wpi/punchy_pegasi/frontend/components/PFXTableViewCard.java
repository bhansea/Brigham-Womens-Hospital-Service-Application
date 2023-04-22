package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.generator.schema.RequestEntry;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import static com.sun.javafx.css.StyleClassSet.getStyleClass;

public class PFXTableViewCard {
    private VBox tableBox = new VBox();
    private TableView<RequestEntry> tableView = new TableView<>();

    public PFXTableViewCard() {
        super();

        tableView = new TableView<>();
        getStyleClass("-pfx-table-card-vertical-container");

        tableBox.getStyleClass().add("pfx-table-box");
        tableBox.getChildren().add(tableView);
    }
//        getStyleClass().add("pfx-card-vertical-container");
//        getChildren().addAll(imageBox, stats);
//        imageBox.getStyleClass().add("pfx-card-vertical-imageBox");
//        imageBox.getChildren().add(image);
//        HBox.setHgrow(stats, Priority.ALWAYS);
//
//        stats.getStyleClass().add("pfx-card-vertical-stats");
//        HBox.setHgrow(info, Priority.ALWAYS);
//        HBox.setHgrow(selection, Priority.ALWAYS);
//        stats.getChildren().addAll(info, selection);
//
//        info.getStyleClass().add("pfx-card-vertical-info");
//        selection.getStyleClass().add("pfx-card-vertical-selection");
//
//
//        info.getChildren().addAll(titleLabel, subtitleLabel);
//        selection.getChildren().addAll(quantity, picker);
    }

//    public PFXTableViewCard(String title, String subtitle, int quantity, Image image) {
//        super();
//        this.tableBox = new VBox();
//        this.tableView = new TableView<>();

//        getStyleClass().add("pfx-card-vertical-container");
//        getChildren().addAll(imageBox, stats);
//        imageBox.getStyleClass().add("pfx-card-vertical-imageBox");
//        imageBox.getChildren().add(this.image);
//        HBox.setHgrow(stats, Priority.ALWAYS);
//
//        stats.getStyleClass().add("pfx-card-vertical-stats");
//        HBox.setHgrow(info, Priority.ALWAYS);
//        HBox.setHgrow(selection, Priority.ALWAYS);
//        stats.getChildren().addAll(info, selection);
//
//        info.getStyleClass().add("pfx-card-vertical-info");
//        selection.getStyleClass().add("pfx-card-vertical-selection");
//
//        info.getChildren().addAll(titleLabel, subtitleLabel);
//        selection.getChildren().addAll(this.quantity, picker);




