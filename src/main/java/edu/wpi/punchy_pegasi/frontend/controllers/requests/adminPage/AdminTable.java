package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.generator.schema.Node;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class AdminTable<T> {
    final String humanReadableName;
    @Getter
    final MFXTableView<T> table;
    @Getter
    final TableType tableType;
    @Getter
    final Supplier<List<T>> getAll;

    public AdminTable(String humanReadableName, TableType tableType, Supplier<List<T>> getAll) {
        this.humanReadableName = humanReadableName;
        table = new MFXTableView<>();
        this.tableType = tableType;
        this.getAll = getAll;
    }

    public void init(){
        ObservableList<T> tableList = FXCollections.observableList(getAll.get());

        // Create columns
        for (var field : Arrays.stream(tableType.getClazz().getFields()).filter(fld->fld.getName() == "Field").findFirst().get().getClass().getEnumConstants()) {
//                MFXTableColumn<FlowerDeliveryRequestEntry> col = new MFXTableColumn<>(field.getColName(), true);
//                col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
//                tables.get(tableType.humanReadableName).getTable().getTableColumns().add(col);
        }

    }
}

