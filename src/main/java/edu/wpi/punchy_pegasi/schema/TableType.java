package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import org.intellij.lang.annotations.Language;

public enum TableType {
    NODES(Node.class, """
            (
            nodeID bigint PRIMARY KEY,
            xcoord int,
            ycoord int,
            floor varchar,
            building varchar
            )"""),
    EDGES(Edge.class, """
            (
            uuid bigint PRIMARY KEY,
            startNode bigint,
            endNode bigint
            )"""),
    MOVES(Move.class, """
            (
            uuid bigint PRIMARY KEY,
            nodeID bigint,
            longName varchar,
            date varchar
            )"""),
    LOCATIONNAMES(LocationName.class, """
            (
            uuid bigint PRIMARY KEY,
            longName varchar,
            shortName varchar,
            nodeType varchar
            )"""),
    GENERIC(GenericRequestEntry.class, """
            (
            serviceID uuid DEFAULT uuid_generate_v4(),
            patientName varchar(100),
            roomNumber varchar(100),
            staffAssignment varchar(100),
            additionalNotes varchar(1000),
            status varchar(50),
            PRIMARY KEY (serviceID)
            )"""),
    FOODREQUESTS(FoodServiceRequestEntry.class, """
            (
            serviceID uuid DEFAULT uuid_generate_v4(),
            patientName varchar(100),
            roomNumber varchar(100),
            staffAssignment varchar(100),
            additionalNotes varchar(1000),
            status varchar(50),
            foodSelection varchar(100),
            tempType varchar(50),
            additionalItems varchar(100) ARRAY,
            dietaryRestrictions varchar(1000),
            PRIMARY KEY (serviceID)
            )"""),
    FLOWERREQUESTS(FlowerDeliveryRequestEntry.class, """
            (
            serviceID uuid DEFAULT uuid_generate_v4(),
            patientName varchar(100),
            roomNumber varchar(100),
            staffAssignment varchar(100),
            additionalNotes varchar(1000),
            status varchar(50),
            flowerSize varchar(100),
            flowerAmount varchar(100),
            flowerType varchar(100),
            PRIMARY KEY (serviceID)
            )"""),
    CONFERENCEREQUESTS(ConferenceRoomEntry.class, """
            (
            serviceID uuid DEFAULT uuid_generate_v4(),
            patientName varchar(100),
            roomNumber varchar(100),
            additionalNotes varchar(1000),
            staffAssignment varchar(100),
            status varchar(50),
            beginningTime varchar(100),
            endTime varchar(100),
            PRIMARY KEY (serviceID)
            )"""),
    FURNITUREREQUESTS(FurnitureRequestEntry.class, """
            (
            serviceID uuid DEFAULT uuid_generate_v4(),
            patientName varchar(100),
            roomNumber varchar(100),
            staffAssignment varchar(100),
            additionalNotes varchar(1000),
            status varchar(50),
            selectFurniture varchar(100) ARRAY,
            PRIMARY KEY (serviceID)
            )"""),

    OFFICEREQUESTS(OfficeServiceRequestEntry.class, """
            (
            serviceID uuid DEFAULT uuid_generate_v4(),
            patientName varchar(100),
            roomNumber varchar(100),
            staffAssignment varchar(100),
            additionalNotes varchar(1000),
            status varchar(50),
            officeRequest varchar(100),
            employeeName varchar(100),
            PRIMARY KEY (serviceID)
            )""");
    @Getter
    private final Class<?> clazz;
    @Getter
    private final String tableSQL;

    TableType(Class<?> clazz, @Language(value = "SQL", prefix = "CREATE TABLE IF NOT EXISTS teamp.table") String tableSQL) {
        this.clazz = clazz;
        this.tableSQL = tableSQL;
    }
}
