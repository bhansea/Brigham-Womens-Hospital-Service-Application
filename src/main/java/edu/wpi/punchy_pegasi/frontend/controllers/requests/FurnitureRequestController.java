package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.Value;

import java.util.Comparator;

public class FurnitureRequestController extends RequestController<FurnitureRequestEntry> {
    private final ObservableList<String> furnitureList =
            FXCollections.observableArrayList("Desk", "Bed", "Lamp", "Chair",
                    "Office Chair", "Pillow", "Picture Frame", "Rug", "Bench", "Blanket", "Night Stand");
    private final ObservableList<Integer> amountList = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private final ObservableList<FurnitureCartItem> itemList = FXCollections.observableArrayList(new FurnitureCartItem("", 0));
    @FXML
    MFXComboBox<String> furniture = new MFXComboBox<>();
    @FXML
    MFXComboBox<Integer> amountOfFurniture = new MFXComboBox<>();
    @FXML
    private MFXButton toCart;
    @FXML
    private MFXTableView<FurnitureCartItem> furnTable = new MFXTableView<>();
    @FXML
    Label invalidText;

    public static BorderPane create(String path) {
        return RequestController.create(new FurnitureRequestController(), path);
    }

    @FXML
    public void init() {
        invalidText.setVisible(false);
        invalidText.setManaged(false);
        furniture.setItems(furnitureList);
        amountOfFurniture.setItems(amountList);
        submit.setDisable(true);

        MFXTableColumn<FurnitureCartItem> typeCol = new MFXTableColumn<>("Furniture Type", true, Comparator.comparing(FurnitureCartItem::getFurnitureType));
        MFXTableColumn<FurnitureCartItem> numCol = new MFXTableColumn<>("Number", true, Comparator.comparing(FurnitureCartItem::getNumber));
        typeCol.setRowCellFactory(f -> new MFXTableRowCell<>(FurnitureCartItem::getFurnitureType));
        numCol.setRowCellFactory(f -> new MFXTableRowCell<>(FurnitureCartItem::getNumber));

        furnTable.getTableColumns().addAll(typeCol, numCol);
        furnTable.setItems(itemList);
        itemList.remove(0, 1);
        setHeaderText("Furniture Request");
    }

    @Override
    protected void clearEntry() {
        clearGeneric();
        furniture.clear();
        amountOfFurniture.clear();
    }

    public MFXComboBox<String> getFurniture() {
        return furniture;
    }

    public String getStringFurn() {
        return furniture.getValue();
    }

    @FXML
    public void tableEntry(FurnitureCartItem item) {
    }

    @FXML
    public void clickToCart() {
        if (furniture.getSelectedItem() == null || amountOfFurniture.getSelectedItem() == null) return;
        itemList.add(new FurnitureCartItem(furniture.getSelectedItem(), amountOfFurniture.getSelectedItem()));
        furnTable.setItems(itemList);
        furnTable.update();
    }

    @FXML
    public void submitEntry() {
        requestEntry = new FurnitureRequestEntry(
                locationName.getSelectedItem().getUuid(),
                staffAssignment.getSelectedItem().getEmployeeID(),
                additionalNotes.getText(),
                invalidText.getText(),
                furniture.getItems());
        facade.saveFurnitureRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @FXML
    public void validateEntry() {
        boolean val = validateGeneric() || furniture.getItems() == null;
        submit.setDisable(val);
    }

    @Value
    class FurnitureCartItem {
        public String furnitureType;
        public Integer number;
    }

}
