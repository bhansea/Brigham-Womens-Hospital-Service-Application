package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import org.intellij.lang.annotations.Language;

public enum TableType {
    NODES(edu.wpi.punchy_pegasi.generator.schema.Node.class, """
DO $$
BEGIN
  IF to_regclass('teamp.nodes') IS NULL THEN
    CREATE SEQUENCE nodes_id_seq;
    CREATE TABLE teamp.nodes
    (
    nodeID bigint DEFAULT nextval('nodes_id_seq') PRIMARY KEY,
    xcoord int,
    ycoord int,
    floor varchar,
    building varchar
    );
    ALTER SEQUENCE nodes_id_seq OWNED BY teamp.nodes;
  END IF;
END $$;
"""),
    EDGES(edu.wpi.punchy_pegasi.generator.schema.Edge.class, """
DO $$
BEGIN
  IF to_regclass('teamp.edges') IS NULL THEN
    CREATE SEQUENCE edges_id_seq;
    CREATE TABLE teamp.edges
    (
    uuid bigint DEFAULT nextval('edges_id_seq') PRIMARY KEY,
    startNode bigint,
    endNode bigint
    );
    ALTER SEQUENCE edges_id_seq OWNED BY teamp.edges;
  END IF;
END $$;
"""),
    MOVES(edu.wpi.punchy_pegasi.generator.schema.Move.class, """
DO $$
BEGIN
  IF to_regclass('teamp.moves') IS NULL THEN
    CREATE SEQUENCE moves_id_seq;
    CREATE TABLE teamp.moves
    (
    uuid bigint DEFAULT nextval('moves_id_seq') PRIMARY KEY,
    nodeID bigint,
    longName varchar,
    date varchar
    );
    ALTER SEQUENCE moves_id_seq OWNED BY teamp.moves;
  END IF;
END $$;
"""),
    LOCATIONNAMES(edu.wpi.punchy_pegasi.generator.schema.LocationName.class, """
DO $$
BEGIN
  IF to_regclass('teamp.locationnames') IS NULL THEN
    CREATE SEQUENCE locationnames_id_seq;
    CREATE TABLE teamp.locationnames
    (
    uuid bigint DEFAULT nextval('locationnames_id_seq') PRIMARY KEY,
    longName varchar,
    shortName varchar,
    nodeType varchar
    );
    ALTER SEQUENCE locationnames_id_seq OWNED BY teamp.locationnames;
  END IF;
END $$;
"""),
    GENERIC(edu.wpi.punchy_pegasi.generator.schema.GenericRequestEntry.class, """
DO $$
BEGIN
  IF to_regclass('teamp.generic') IS NULL THEN
    CREATE SEQUENCE generic_id_seq;
    CREATE TABLE teamp.generic
    (
    serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    roomNumber varchar,
    staffAssignment varchar,
    additionalNotes varchar,
    status varchar
    );
    ALTER SEQUENCE generic_id_seq OWNED BY teamp.generic;
  END IF;
END $$;
"""),
    FOODREQUESTS(edu.wpi.punchy_pegasi.generator.schema.FoodServiceRequestEntry.class, """
DO $$
BEGIN
  IF to_regclass('teamp.foodrequests') IS NULL THEN
    CREATE SEQUENCE foodrequests_id_seq;
    CREATE TABLE teamp.foodrequests
    (
    serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    roomNumber varchar,
    staffAssignment varchar,
    additionalNotes varchar,
    status varchar,
    foodSelection varchar,
    tempType varchar,
    additionalItems varchar ARRAY,
    dietaryRestrictions varchar,
    patientName varchar
    );
    ALTER SEQUENCE foodrequests_id_seq OWNED BY teamp.foodrequests;
  END IF;
END $$;
"""),
    FLOWERREQUESTS(edu.wpi.punchy_pegasi.generator.schema.FlowerDeliveryRequestEntry.class, """
DO $$
BEGIN
  IF to_regclass('teamp.flowerrequests') IS NULL THEN
    CREATE SEQUENCE flowerrequests_id_seq;
    CREATE TABLE teamp.flowerrequests
    (
    serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    roomNumber varchar,
    staffAssignment varchar,
    additionalNotes varchar,
    status varchar,
    flowerSize varchar,
    flowerType varchar,
    flowerAmount varchar,
    patientName varchar
    );
    ALTER SEQUENCE flowerrequests_id_seq OWNED BY teamp.flowerrequests;
  END IF;
END $$;
"""),
    CONFERENCEREQUESTS(edu.wpi.punchy_pegasi.generator.schema.ConferenceRoomEntry.class, """
DO $$
BEGIN
  IF to_regclass('teamp.conferencerequests') IS NULL THEN
    CREATE SEQUENCE conferencerequests_id_seq;
    CREATE TABLE teamp.conferencerequests
    (
    serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    roomNumber varchar,
    staffAssignment varchar,
    additionalNotes varchar,
    status varchar,
    beginningTime varchar,
    endTime varchar
    );
    ALTER SEQUENCE conferencerequests_id_seq OWNED BY teamp.conferencerequests;
  END IF;
END $$;
"""),
    FURNITUREREQUESTS(edu.wpi.punchy_pegasi.generator.schema.FurnitureRequestEntry.class, """
DO $$
BEGIN
  IF to_regclass('teamp.furniturerequests') IS NULL THEN
    CREATE SEQUENCE furniturerequests_id_seq;
    CREATE TABLE teamp.furniturerequests
    (
    serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    roomNumber varchar,
    staffAssignment varchar,
    additionalNotes varchar,
    status varchar,
    selectFurniture varchar ARRAY
    );
    ALTER SEQUENCE furniturerequests_id_seq OWNED BY teamp.furniturerequests;
  END IF;
END $$;
"""),

    OFFICEREQUESTS(edu.wpi.punchy_pegasi.generator.schema.OfficeServiceRequestEntry.class, """
DO $$
BEGIN
  IF to_regclass('teamp.officerequests') IS NULL THEN
    CREATE SEQUENCE officerequests_id_seq;
    CREATE TABLE teamp.officerequests
    (
    serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    roomNumber varchar,
    staffAssignment varchar,
    additionalNotes varchar,
    status varchar,
    officeRequest varchar,
    employeeName varchar
    );
    ALTER SEQUENCE officerequests_id_seq OWNED BY teamp.officerequests;
  END IF;
END $$;
""");
    @Getter
    private final Class<?> clazz;
    @Getter
    private final String tableSQL;

    TableType(Class<?> clazz, @Language(value = "SQL") String tableSQL) {
        this.clazz = clazz;
        this.tableSQL = tableSQL;
    }
}
