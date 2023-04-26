package edu.wpi.punchy_pegasi.frontend.utils;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FacadeUtils {
    public static final Predicate<LocationName> isDestination = location -> location.getNodeType() != LocationName.NodeType.HALL && location.getNodeType() != LocationName.NodeType.STAI && location.getNodeType() != LocationName.NodeType.ELEV;

    public static Map<Node, List<Move>> calculateMoves(ObservableMap<Long, Node> nodes, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, LocalDate date) {
        return moves.values().stream()
                // filter by moves which reference valid locations and nodes
                .filter(m -> locations.containsKey(m.getLocationID()))
                .filter(m -> nodes.containsKey(m.getNodeID()))
                // first filter out all future dates
                .filter(m -> m.getDate().isBefore(date) || m.getDate().isEqual(date))
                // group by location
                .collect(Collectors.groupingBy(Move::getLocationID)).values().stream()
                // get max date for each location
                .map(m -> m.stream().max(Comparator.comparing(Move::getDate)).get())
                // group by node id
                .collect(Collectors.groupingBy(Move::getNodeID)).entrySet().stream()
                // map to node and move
                .collect(Collectors.toMap(e -> nodes.get(e.getKey()), Map.Entry::getValue));
    }

    public static Map<LocationName, Node> calculateNodes(ObservableMap<Long, Node> nodes, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, LocalDate date) {
        return moves.values().stream()
                // filter by moves which reference valid locations and nodes
                .filter(m -> locations.containsKey(m.getLocationID()))
                .filter(m -> nodes.containsKey(m.getNodeID()))
                // first filter out all future dates
                .filter(m -> m.getDate().isBefore(date) || m.getDate().isEqual(date))
                // group by location
                .collect(Collectors.groupingBy(Move::getLocationID)).values().stream()
                // get max date for each location
                .map(m -> m.stream().max(Comparator.comparing(Move::getDate)).get())
                // get map of location to node
                .collect(Collectors.toMap(e -> locations.get(e.getLocationID()), e -> nodes.get(e.getNodeID())));
    }

    public static List<Move> calculateMoves(Node node, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, LocalDate date) {
        return moves.values().stream()
                // first filter out all future dates
                .filter(m -> m.getDate().isBefore(date) || m.getDate().isEqual(date))
                // group by location
                .collect(Collectors.groupingBy(Move::getLocationID)).values().stream()
                // get max date for each location
                .map(m -> m.stream().max(Comparator.comparing(Move::getDate)).get())
                // select relevant node
                .filter(m -> m.getNodeID().equals(node.getNodeID())).toList();
    }

    public static ObservableMap<Node, ObservableList<LocationName>> getNodeLocations(ObservableMap<Long, Node> nodes, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, LocalDate date) {
        ObservableMap<Node, ObservableList<LocationName>> nodeLocations = FXCollections.observableMap(new LinkedHashMap<>());
        BiConsumer<Node, LocationName> add = (n, l) -> {
            if (!nodeLocations.containsKey(n))
                nodeLocations.put(n, FXCollections.observableArrayList());
            nodeLocations.get(n).add(l);
        };
        Consumer<Node> clear = n -> {
            if (nodeLocations.containsKey(n))
                nodeLocations.get(n).clear();
        };
        calculateMoves(nodes, locations, moves, date).forEach((n, ms) -> {
            clear.accept(n);
            ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
        });
        // add event filters to all the maps to update the MultiValuedMap
        nodes.addListener((MapChangeListener<Long, Node>) change -> {
            if (change.wasRemoved())
                nodeLocations.remove(change.getValueRemoved());
            if (change.wasAdded()) {
                clear.accept(change.getValueRemoved());
                calculateMoves(change.getValueAdded(), locations, moves, date).forEach(m -> {
                    add.accept(change.getValueAdded(), locations.get(m.getLocationID()));
                });
            }
        });
        locations.addListener((MapChangeListener<Long, LocationName>) change -> {
            calculateMoves(nodes, locations, moves, date).forEach((n, ms) -> {
                clear.accept(n);
                ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
            });
        });
        moves.addListener((MapChangeListener<Long, Move>) change -> {
            calculateMoves(nodes, locations, moves, date).forEach((n, ms) -> {
                clear.accept(n);
                ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
            });
        });

        // return the list of locations
        return nodeLocations;
    }

    public static ObservableMap<LocationName, Node> getLocationNode(ObservableMap<Long, Node> nodes, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, LocalDate date) {
        ObservableMap<LocationName, Node> locationNode = FXCollections.observableMap(calculateNodes(nodes, locations, moves, date));

        // add event filters to all the maps to update the MultiValuedMap
        nodes.addListener((MapChangeListener<Long, Node>) change -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date));
        });
        locations.addListener((MapChangeListener<Long, LocationName>) change -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date));
        });
        moves.addListener((MapChangeListener<Long, Move>) change -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date));
        });
        return locationNode;
    }
}
