package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TableType {
    NODES(Node.class, """
            
"""),
    EDGES(Edge.class, """
sdf
"""),
    MOVES(Move.class, """
            """),
    LOCATIONNAMES(LocationName.class, """
            """),
    GENERIC(GenericRequestEntry.class, """
            """),
    FOODREQUESTS(FoodServiceRequestEntry.class, """
            """),
    FLOWERREQUESTS(FlowerDeliveryRequestEntry.class, """
            """),
    CONFERENCEREQUESTS(ConferenceRoomEntry.class, """
            """),
    FURNITUREREQUESTS(FurnitureRequestEntry.class, """
            """),
    OFFICEREQUESTS(OfficeServiceRequestEntry.class, """
            """);
    @Getter
    private final Class<?> clazz;
    @Getter
    private final String generateTableQuery;
}
