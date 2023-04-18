package edu.wpi.punchy_pegasi.frontend.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.*;
import lombok.Getter;

import java.util.Objects;


public class PFXTabLayout extends BorderPane {
    @Getter
    protected StackPane stackPane = new StackPane();
    protected final HBox container = new HBox();
    @Getter
    protected ObservableList<PFXTab> tabGroup = FXCollections.observableArrayList();
    protected ObservableList<Node> nodes = FXCollections.observableArrayList();

    public PFXTabLayout() {
        super();
        HBox.setHgrow(container, Priority.ALWAYS);
        VBox.setVgrow(container, Priority.ALWAYS);
        HBox.setHgrow(stackPane, Priority.ALWAYS);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("pfx-tab-layout");
        stackPane.getStyleClass().add("pfx-tab-stackpane");

        this.setTop(container);
        this.setCenter(stackPane);
    }

    public void setSelected(Node node) {
        node.setVisible(true);
        node.setManaged(true);

        nodes.stream().filter(f -> !Objects.equals(f, node)).forEach(f -> {
            f.setVisible(false);
            f.setManaged(false);
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
        nodes.add(tab.getNode());
        stackPane.getChildren().add(tab.getNode());
    }
}
