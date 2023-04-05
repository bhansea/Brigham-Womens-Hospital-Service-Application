package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

public enum TableType {
    NODES(Node.class),
    EDGES(Edge.class),
    MOVES(Move.class),
    LOCATIONNAMES(LocationName.class),
    GENERIC(GenericRequestEntry.class),
    FOODREQUESTS(FoodServiceRequestEntry.class),

    FLOWERREQUESTS(FlowerDeliveryRequestEntry.class),
    CONFERENCEREQUESTS(ConferenceRoomEntry.class),
    // TODO: Change GenericRequestEntry to FurnitureRequestEntry once implemented
    FURNITUREREQUESTS(GenericRequestEntry.class),
    OFFICEREQUESTS(OfficeServiceRequestEntry.class);


    @Getter
    private final Class<?> clazz;

    TableType(Class clazz) {
        this.clazz = clazz;
    }
}
