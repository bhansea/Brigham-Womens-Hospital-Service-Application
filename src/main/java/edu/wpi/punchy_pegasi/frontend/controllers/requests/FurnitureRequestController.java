package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.ConferenceRoomEntryDaoImpl;
import edu.wpi.punchy_pegasi.generated.FurnitureRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.base.MFXCombo;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import lombok.Data;
import lombok.Value;
import org.controlsfx.control.CheckComboBox;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;

public class FurnitureRequestController extends RequestController<FurnitureRequestEntry> {
    @Value
    class FurnitureCartItem{
        public String furnitureType;;
        public Integer number;
    }

    @FXML
    MFXComboBox<String> furniture;

    @FXML
    MFXComboBox<Integer> amountOfFurniture;

    @FXML
    private MFXButton toCart;

    @FXML
    private MFXTableView<FurnitureCartItem> furnTable;

    private ObservableList<String> furnitureList =
            FXCollections.observableArrayList("Desk","Bed","Lamp","Chair",
                    "Office Chair","Pillow", "Picture Frame", "Rug", "Bench", "Blanket", "Night Stand");

    private ObservableList<Integer> amountList = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);

    private ObservableList<FurnitureCartItem> itemList = FXCollections.observableArrayList(new FurnitureCartItem("", 0));

    public static BorderPane create(String path) {
        return RequestController.create(new FurnitureRequestController(), path);
    }

    @FXML
    public void init(){
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

    public String getStringFurn(){
        return furniture.getValue();
    }

    @FXML
    public void tableEntry(FurnitureCartItem item){
    }

    @FXML
    public void clickToCart(){
        if(furniture.getSelectedItem() == null || amountOfFurniture.getSelectedItem() == null) return;
        itemList.add(new FurnitureCartItem(furniture.getSelectedItem(), amountOfFurniture.getSelectedItem()));
        furnTable.setItems(itemList);
        furnTable.update();
    }

    @FXML
    public void submitEntry(){
        requestEntry = new FurnitureRequestEntry(
                locationName.getValue(),
                staffAssignment.getText(),
                additionalNotes.getText(),
                furniture.getItems());
        FurnitureRequestEntryDaoImpl request = new FurnitureRequestEntryDaoImpl();
        request.save(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @FXML
    public void validateEntry(){
        boolean val = validateGeneric() || furniture.getItems() == null;
        submit.setDisable(val);
    }

}
