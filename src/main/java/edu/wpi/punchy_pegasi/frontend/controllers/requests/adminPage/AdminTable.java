package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.schema.IField;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

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

    public void init() {
        var thread = new Thread(() -> {
            var list2 = getAll.get();
            Platform.runLater(() -> {
                ObservableList<T> tableList = FXCollections.observableList(getAll.get());
                table.setItems(tableList);
                // Create columns
                for (Object field : tableType.getFieldEnum().getEnumConstants()) {
                    var iField = (IField<T>) field;
                    MFXTableColumn<T> col = new MFXTableColumn<>(iField.getColName(), true);
                    col.setRowCellFactory(p -> new MFXTableRowCell<>(iField::getValue));
                    table.getTableColumns().add(col);
                }
            });
        });
        thread.setDaemon(true);
        thread.start();

    }
}

