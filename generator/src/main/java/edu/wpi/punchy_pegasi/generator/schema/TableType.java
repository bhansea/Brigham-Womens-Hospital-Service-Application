package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;
import org.intellij.lang.annotations.Language;

public enum TableType {
    NODES(Node.class, "", Object.class),
    EDGES(Edge.class, "", Object.class),
    MOVES(Move.class, "", Object.class),
    LOCATIONNAMES(LocationName.class, "", Object.class),
    REQUESTS(RequestEntry.class, "", Object.class),
    GENERIC(GenericRequestEntry.class, "", Object.class),
    FOODREQUESTS(FoodServiceRequestEntry.class, "", Object.class),
    FLOWERREQUESTS(FlowerDeliveryRequestEntry.class, "", Object.class),
    CONFERENCEREQUESTS(ConferenceRoomEntry.class, "", Object.class),
    FURNITUREREQUESTS(FurnitureRequestEntry.class, "", Object.class),
    OFFICEREQUESTS(OfficeServiceRequestEntry.class, "", Object.class),
    EMPLOYEES(Employee.class, "", Object.class),
    ACCOUNTS(Account.class, "", Object.class),
    SIGNAGE(Signage.class, "", Object.class),
    ALERT(Alert.class, "", Object.class);
    @Getter
    private final Class<?> clazz;
    @Getter
    private final String tableSQL;
    @Getter
    private final Class<?> fieldEnum;

    TableType(Class<?> clazz, @Language(value = "SQL") String tableSQL, Class<?>fieldEnum) {
        this.clazz = clazz;
        this.tableSQL = tableSQL;
        this.fieldEnum = fieldEnum;
    }
}
