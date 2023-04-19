package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Value;

import java.util.*;

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
    PFXCardHolder cardHolder;
    @FXML
    VBox container = new VBox();

    public static BorderPane create(String path) {
        return RequestController.create(new FurnitureRequestController(), path);
    }

    @FXML
    public void init() {
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

        PFXCardVertical card1 = new PFXCardVertical("Bed", "Sturdy mattress", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/furniture/bed.jpg"));
        PFXCardVertical card2 = new PFXCardVertical("Office Chair", "Great comfort", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/furniture/officechair.jpg"));
        PFXCardVertical card3 = new PFXCardVertical("Blanket", "Thick wool", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/furniture/blanket.png"));
        PFXCardVertical card4 = new PFXCardVertical("Frame", "Pure wood", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/furniture/frame.jpg"));
        PFXCardVertical card5 = new PFXCardVertical("Pillow", "Nice feathers", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/furniture/pillow.jpg"));
        PFXCardVertical card6 = new PFXCardVertical("Rug", "Very comfy!", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/furniture/rug.jpg"));
        cardHolder = new PFXCardHolder(new ArrayList<>(Arrays.asList(card1, card2, card3, card4, card5, card6)));
        container.getChildren().add(cardHolder);
        container.setAlignment(Pos.CENTER);
    }

    @Override
    protected void clearEntry() {
        clearGeneric();
        furniture.clear();
        amountOfFurniture.clear();
        cardHolder.clear();
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
                furniture.getItems(),
                // TODO: need a way to get the employeeID of the person making the request entry
                1L);
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
