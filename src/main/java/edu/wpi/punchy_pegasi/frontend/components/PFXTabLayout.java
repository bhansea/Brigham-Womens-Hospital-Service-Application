package edu.wpi.punchy_pegasi.frontend.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;
import lombok.Getter;


public class PFXTabLayout extends BorderPane {
    @Getter
    protected StackPane stackPane = new StackPane();
    protected final HBox container = new HBox();
    @Getter
    protected ObservableList<PFXTab> tabGroup = FXCollections.observableArrayList();


    public PFXTabLayout() {
        super();
//        HBox.setHgrow(container, Priority.ALWAYS);
//        VBox.setVgrow(container, Priority.ALWAYS);
//        HBox.setHgrow(stackPane, Priority.ALWAYS);
//        VBox.setVgrow(stackPane, Priority.ALWAYS);
//        HBox.setHgrow(this, Priority.ALWAYS);
//        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("pfx-tab-layout");
        container.getStyleClass().add("pfx-tab-container");
        stackPane.getStyleClass().add("pfx-tab-stackpane");

        this.setTop(container);
        this.setCenter(stackPane);
    }

    public void setSelected(PFXTab pfxTab) {
        tabGroup.forEach(t -> {
            var is = t == pfxTab;
            t.getNode().setManaged(is);
            t.getNode().setVisible(is);
            t.setSelected(is);
        });
    }

    public PFXTabLayout(ObservableList<PFXTab> tabGroup) {
        this();
        this.tabGroup = FXCollections.observableList(tabGroup);

        for (PFXTab tab : tabGroup) {
            container.getChildren().add(tab);
        }
        container.getStyleClass().add("pfx-tab-container");
    }

    public void addTab(PFXTab tab) {
        this.tabGroup.add(tab);
        container.getChildren().add(tab);
        stackPane.getChildren().add(tab.getNode());
    }
}
