package edu.wpi.punchy_pegasi.backend;


import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

@Slf4j
public class PdbController {
    public class DatabaseException extends Exception {
        public DatabaseException(String e) {
            super(e);
        }
    }

    private static Connection connection;
    private static String url;
    private static String username;
    private static String password;


    private PdbController(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.error("Failed to connect to db :", e);
        }
    }

    static private PdbController singleton;

    static public PdbController getSingleton() {
        if (singleton != null) {
            return singleton;
        } else {
            singleton = new PdbController(url, username, password);
            return singleton;
        }
    }

//    public Node getNode(String nodeID) throws DatabaseException{
//        Node node = NodeMap.get(nodeID);
//        if(node == null) {
//            throw new DatabaseException("nodeID does not exist");
//        } else {
//            return node;
//        }
//    }

//    public Edge getEdge(String edgeID) throws DatabaseException{
//        Edge edge = EdgeMap.get(edgeID);
//        if(edge == null) {
//            throw new DatabaseException("edgeID does not exist");
//        } else {
//            return edge;
//        }
//    }

    public void initTableByType(TableType tableType, String tableName) throws DatabaseException {
        try {
            initTable(tableType, tableName);
            System.out.println("Table initialized");
        } catch (SQLException e) {
            log.error("Failed to Initialize table", e);
            throw new DatabaseException("SQL error");
        }
    }

    private void initTable(TableType tableType, String tableName) throws SQLException {
        var statement = connection.createStatement();
        switch (tableType) {
            case NODES -> {
                var ret = statement.execute("CREATE TABLE IF NOT EXISTS " +
                        tableName +
                        "(" +
                        "nodeID varchar PRIMARY KEY, " +
                        "xcoord int, " +
                        "ycoord int, " +
                        "floor varchar, " +
                        "building varchar);");
            }
            case EDGES -> {
                var ret2 = statement.execute("CREATE TABLE IF NOT EXISTS " +
                        tableName +
                        "(" +
                        "startNode varchar, " +
                        "endNode varchar);");
            }
            case MOVES -> {
                var ret2 = statement.execute("CREATE TABLE IF NOT EXISTS " +
                        tableName +
                        "(" +
                        "nodeID int, " +
                        "longName varchar" +
                        "date varchar" +
                        ");");
            }
            case LOCATIONNAMES -> {
                var ret2 = statement.execute("CREATE TABLE IF NOT EXISTS " +
                        tableName +
                        "(" +
                        "longName varchar " +
                        "shortName varchar" +
                        "nodeType varchar" +
                        ");");
            }
        }
    }

    public void exportTable(String path, String tableName) throws DatabaseException {
        try {
            exportToCSV(path, tableName);
            log.info("Exported table successfully");
        } catch (SQLException | IOException e) {
            log.error("Failed to export table:", e);
            if (e instanceof IOException)
                throw new DatabaseException("Failed to open selected file");
            else if (e instanceof SQLException)
                throw new DatabaseException("Table does not exist");
        }
    }

    public void importTable(String path, String tableName) throws DatabaseException {
//        try {
//            insertfromCSV(path, tableName);
//            log.info("Imported table successfully");
//            try {
//                syncNodes();
//                syncEdges();
//            } catch (DatabaseException e) {
////                log.error("Failed to sync nodes/edges:", e);
//                throw new DatabaseException("Failed to sync nodes/edges");
//            }
//        } catch (SQLException | IOException e) {
//            log.error("Failed to import table:", e);
//            if(e instanceof  IOException) {
//                throw new DatabaseException("Failed to open selected file");
//            }
//            else if(e instanceof SQLException){
//                if(e.getMessage().contains("violates unique constraint")) {
//                    throw new DatabaseException("One or more nodes in csv already exist");
//                }
//                else if(Pattern.matches("[\\S\\s]*column.+does not exist[\\S\\s]*", e.getMessage())) {
//                    throw new DatabaseException("CSV headers do not match table headers");
//                }
//            }
//            throw new DatabaseException("Table does not exist, CSV has incorrect headers");
//        }
    }

    private int insertfromCSV(String path, String tableName) throws IOException, SQLException {
        // read the file one line at a time
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(path));

        var sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName)
                .append("(").append(reader.readLine()).append(") VALUES ");

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append("(");
            Arrays.stream(line.split(",")).forEach(val -> {
                sb.append("'");
                sb.append(val);
                sb.append("',");
            });
            sb.setLength(sb.length() - 1);
            sb.append("),");
        }
        sb.setLength(sb.length() - 1);
        sb.append(";");
        var statement = connection.createStatement();
        return statement.executeUpdate(sb.toString());
    }

    private void exportToCSV(String path, String tableName) throws SQLException, IOException {
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM " + tableName + ";");
        var metaData = result.getMetaData();
        var headerCount = metaData.getColumnCount();
        @Cleanup var fw = new FileWriter(path);
        for (int i = 1; i <= headerCount; i++) {
            fw.append(metaData.getColumnName(i));
            fw.append(i < headerCount ? ',' : '\n');
        }
        while (result.next()) {
            for (int i = 1; i <= headerCount; i++) {
                fw.append(result.getString(i));
                fw.append(i <= headerCount - 1 ? ',' : '\n');
            }
        }
    }

    // read csv file and insert into database
    public void insertNode(Node node) throws SQLException {
//        try {
//            var statement = connection.createStatement();
//            var ret = statement.execute("INSERT INTO teamp.Nodes VALUES (" +
//                    "'" + node.getNodeID() + "', " +
//                    node.getXcoord() + ", " +
//                    node.getYcoord() + ", " +
//                    "'" + node.getFloor() + "', " +
//                    "'" + node.getBuilding() + "', " +
//                    "'" + node.nodeType + "', " +
//                    "'" + node.longName + "', " +
//                    "'" + node.shortName + "');");
//            NodeMap.put(node.nodeID,node);
//        } catch (SQLException e) {
//            log.error("Failed to insert node", e);
//            throw (e);
//        }
    }

    // parse csv file and insert into database
    public void insertEdge(Edge edge) throws SQLException {
//        try {
//            var statement = connection.createStatement();
//            var ret = statement.execute("INSERT INTO teamp.Edges VALUES (" +
//                    "'" + edge.edgeID + "', " +
//                    "'" + edge.startNode + "', " +
//                    "'" + edge.endNode + "');");
//            EdgeMap.put(edge.edgeID, edge);
//        } catch (SQLException e) {
//            log.error("Failed to insert edge", e);
//            throw (e);
//        }
    }

    //
    // delete node from database
    public void deleteNode(String nodeID) throws SQLException {
//        try {
//            var statement = connection.createStatement();
//            var ret = statement.execute("DELETE FROM teamp.Nodes WHERE nodeID = '" + nodeID + "';");
//            NodeMap.remove(nodeID);
//        } catch (SQLException e) {
//            log.error("Failed to delete node", e);
//            throw (e);
//        }
    }

    //
    // delete edge from database
    public void deleteEdge(String edgeID) throws SQLException {
//        try {
//            var statement = connection.createStatement();
//            var ret = statement.execute("DELETE FROM teamp.Edges WHERE edgeID = '" + edgeID + "';");
//            EdgeMap.remove(edgeID);
//        } catch (SQLException e) {
//            log.error("Failed to delete edge", e);
//            throw (e);
//        }
    }

    //
    private void updateNode(String nodeID, String column, String value) throws SQLException {
//        try{
//            var statement = connection.createStatement();
//            statement.executeUpdate("UPDATE teamp.Nodes SET " + column + " = " + value + " WHERE nodeID = '" + nodeID + "';");
//        } catch (SQLException e) {
//            log.error("Failed to update node", e);
//            throw (e);
//        }
    }

    private void updateEdge(String edgeID, String column, String value) throws SQLException {
//        try{
//            var statement = connection.createStatement();
//            var ret = statement.execute("UPDATE teamp.Edges SET " + column + " = " + value + " WHERE edgeID = '" + edgeID + "';");
//        } catch (SQLException e) {
//            log.error("Failed to update edge");
//            throw (e);
//        }
    }

    //
    public void updateNodeName(String nodeID, String longName, String shortName) throws DatabaseException {
//        Node newNode = NodeMap.get(nodeID);
//        try {
//            updateNode(nodeID, "longname", longName);
//            newNode.longName = longName.substring(1,longName.length()-1);
//            updateNode(nodeID, "shortname", shortName);
//            newNode.shortName = shortName.substring(1,shortName.length()-1);
//            NodeMap.put(nodeID, newNode);
//        } catch (SQLException e) {
//            log.error("Unable to update node", e);
//            throw new DatabaseException("Unable to update node");
//        }
    }

    public void updateNodeCoordinate(String nodeID, String xcoord, String ycoord) throws DatabaseException {
//        Node newNode = NodeMap.get(nodeID);
//        try {
//            updateNode(nodeID, "xcoord", xcoord);
//            newNode.xcoord = Integer.parseInt(xcoord);
//            updateNode(nodeID, "ycoord", ycoord);
//            newNode.ycoord = Integer.parseInt(ycoord);
//            NodeMap.put(nodeID, newNode);
//        } catch (SQLException e) {
//            log.error("Unable to update node", e);
//            throw new DatabaseException("Unable to update node");
//        }
    }

//    public List<Node> syncNodes() throws DatabaseException {
//        try {
//            var statement = connection.createStatement();
//            var result = statement.executeQuery("SELECT * FROM nodes");
//            var nodes = new ArrayList<Node>();
//            while (result.next()) {
//                var nodeID = result.getString("nodeID");
//                var xcoord = result.getInt("xcoord");
//                var ycoord = result.getInt("ycoord");
//                var floor = result.getString("floor");
//                var nodeType = Node.NodeType.CONF;// Node.NodeType.valueOf(result.getString("nodeType"));
//                var building = result.getString("building");
//                var longName = result.getString("longName");
//                var shortName = result.getString("shortName");
//                var node = new Node(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
//                nodes.add(node);
//                NodeMap.put(nodeID, node);
//            }
//            return nodes;
//        } catch (SQLException e) {
//            log.error("Failed to get node", e);
//            throw new DatabaseException("Failed to get nodes");
//        }
//    }

//    public List<Edge> syncEdges() throws DatabaseException {
//        try {
//            var statement = connection.createStatement();
//            var result = statement.executeQuery("SELECT * FROM edges");
//            var edges = new ArrayList<Edge>();
//            while (result.next()) {
//                var edgeID = result.getString("edgeID");
//                var startNode = result.getString("startNode");
//                var endNode = result.getString("endNode");
//                var edge = new Edge(edgeID, startNode, endNode);
//                EdgeMap.put(edgeID, edge);
//            }
//            return edges;
//        } catch (SQLException e) {
//            log.error("Failed to get node", e);
//            throw new DatabaseException("Failed to get edges");
//        }
//    }

    public enum TableType {
        NODES, EDGES, MOVES, LOCATIONNAMES
    }
}
