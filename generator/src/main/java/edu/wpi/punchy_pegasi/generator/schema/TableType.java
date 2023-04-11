package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;
import org.intellij.lang.annotations.Language;

public enum TableType {
    NODES(Node.class, ""),
    EDGES(Edge.class, ""),
    MOVES(Move.class, ""),
    LOCATIONNAMES(LocationName.class, ""),
    GENERIC(GenericRequestEntry.class, ""),
    FOODREQUESTS(FoodServiceRequestEntry.class, ""),
    FLOWERREQUESTS(FlowerDeliveryRequestEntry.class, ""),
    CONFERENCEREQUESTS(ConferenceRoomEntry.class, ""),
    FURNITUREREQUESTS(FurnitureRequestEntry.class, ""),
    OFFICEREQUESTS(OfficeServiceRequestEntry.class, "");
    @Getter
    private final Class<?> clazz;
    @Getter
    private final String tableSQL;

    TableType(Class<?> clazz, @Language(value = "SQL") String tableSQL) {
        this.clazz = clazz;
        this.tableSQL = tableSQL;
    }
}
