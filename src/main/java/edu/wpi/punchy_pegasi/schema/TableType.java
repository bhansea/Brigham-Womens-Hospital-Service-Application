package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import org.intellij.lang.annotations.Language;

public enum TableType {
    NODES(edu.wpi.punchy_pegasi.schema.Node.class, """
DO $$
BEGIN
  IF to_regclass('nodes') IS NULL THEN
    CREATE SEQUENCE nodes_id_seq;
    CREATE TABLE nodes
    (
      nodeID bigint DEFAULT nextval('nodes_id_seq') PRIMARY KEY,
      xcoord int,
      ycoord int,
      floor varchar,
      building varchar
    );
    ALTER SEQUENCE nodes_id_seq OWNED BY nodes.nodeID;
  END IF;
END $$;
"""),
    EDGES(edu.wpi.punchy_pegasi.schema.Edge.class, """
DO $$
BEGIN
  IF to_regclass('edges') IS NULL THEN
    CREATE SEQUENCE edges_id_seq;
    CREATE TABLE edges
    (
      uuid bigint DEFAULT nextval('edges_id_seq') PRIMARY KEY,
      startNode bigint,
      endNode bigint
    );
    ALTER SEQUENCE edges_id_seq OWNED BY edges.uuid;
  END IF;
END $$;
"""),
    MOVES(edu.wpi.punchy_pegasi.schema.Move.class, """
DO $$
BEGIN
  IF to_regclass('moves') IS NULL THEN
    CREATE SEQUENCE moves_id_seq;
    CREATE TABLE moves
    (
      uuid bigint DEFAULT nextval('moves_id_seq') PRIMARY KEY,
      nodeID bigint,
      longName varchar,
      date varchar
    );
    ALTER SEQUENCE moves_id_seq OWNED BY moves.uuid;
  END IF;
END $$;
"""),
    LOCATIONNAMES(edu.wpi.punchy_pegasi.schema.LocationName.class, """
DO $$
BEGIN
  IF to_regclass('locationnames') IS NULL THEN
    CREATE SEQUENCE locationnames_id_seq;
    CREATE TABLE locationnames
    (
      uuid bigint DEFAULT nextval('locationnames_id_seq') PRIMARY KEY,
      longName varchar,
      shortName varchar,
      nodeType varchar NOT NULL
    );
    ALTER SEQUENCE locationnames_id_seq OWNED BY locationnames.uuid;
  END IF;
END $$;
"""),
    REQUESTS(edu.wpi.punchy_pegasi.schema.RequestEntry.class, """
CREATE TABLE IF NOT EXISTS requests
(
  serviceID uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  locationName bigint,
  staffAssignment bigint,
  additionalNotes varchar,
  status varchar NOT NULL
);
""")
,
    GENERIC(edu.wpi.punchy_pegasi.schema.GenericRequestEntry.class, """
CREATE TABLE IF NOT EXISTS generic
(
  
) INHERITS (requests);
""")
,
    FOODREQUESTS(edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry.class, """
CREATE TABLE IF NOT EXISTS foodrequests
(
  foodSelection varchar,
  tempType varchar,
  additionalItems varchar ARRAY,
  dietaryRestrictions varchar,
  patientName varchar,
  beverage varchar
) INHERITS (requests);
""")
,
    FLOWERREQUESTS(edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry.class, """
CREATE TABLE IF NOT EXISTS flowerrequests
(
  flowerSize varchar,
  flowerType varchar,
  flowerAmount varchar,
  patientName varchar
) INHERITS (requests);
""")
,
    CONFERENCEREQUESTS(edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry.class, """
CREATE TABLE IF NOT EXISTS conferencerequests
(
  beginningTime varchar,
  endTime varchar,
  date date NOT NULL
) INHERITS (requests);
""")
,
    FURNITUREREQUESTS(edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry.class, """
CREATE TABLE IF NOT EXISTS furniturerequests
(
  selectFurniture varchar ARRAY
) INHERITS (requests);
""")
,
    OFFICEREQUESTS(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry.class, """
CREATE TABLE IF NOT EXISTS officerequests
(
  officeRequest varchar,
  employeeName varchar
) INHERITS (requests);
""")
,
    EMPLOYEES(edu.wpi.punchy_pegasi.schema.Employee.class, """
DO $$
BEGIN
  IF to_regclass('employees') IS NULL THEN
    CREATE SEQUENCE employees_id_seq;
    CREATE TABLE employees
    (
      employeeID bigint DEFAULT nextval('employees_id_seq') PRIMARY KEY,
      firstName varchar,
      lastName varchar
    );
    ALTER SEQUENCE employees_id_seq OWNED BY employees.employeeID;
  END IF;
END $$;
"""),
    ACCOUNTS(edu.wpi.punchy_pegasi.schema.Account.class, """
CREATE TABLE IF NOT EXISTS accounts
(
  username varchar PRIMARY KEY,
  password varchar,
  employeeID bigint,
  accountType varchar NOT NULL
);
""")
;
    @Getter
    private final Class<?> clazz;
    @Getter
    private final String tableSQL;

    TableType(Class<?> clazz, @Language(value = "SQL") String tableSQL) {
        this.clazz = clazz;
        this.tableSQL = tableSQL;
    }
}
