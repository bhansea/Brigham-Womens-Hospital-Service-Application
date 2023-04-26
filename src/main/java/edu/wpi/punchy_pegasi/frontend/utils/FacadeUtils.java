package edu.wpi.punchy_pegasi.frontend.utils;

import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import javafx.beans.value.ObservableObjectValue;
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
import java.util.stream.Stream;

public class FacadeUtils {
    public static final Predicate<LocationName> isDestination = location -> location.getNodeType() != LocationName.NodeType.HALL && location.getNodeType() != LocationName.NodeType.STAI && location.getNodeType() != LocationName.NodeType.ELEV;

    public static Stream<Move> validMoves(Map<Long, Node> nodes, Map<Long, LocationName> locations, Map<Long, Move> moves, LocalDate date) {
        return moves.values().stream()
                // filter by moves which reference valid locations and nodes
                .filter(m -> locations.containsKey(m.getLocationID()))
                .filter(m -> nodes.containsKey(m.getNodeID()))
                // first filter out all future dates
                .filter(m -> m.getDate().isBefore(date) || m.getDate().isEqual(date))
                // group by location
                .collect(Collectors.groupingBy(Move::getLocationID)).values().stream()
                // get max date for each location
                .flatMap(m -> m.stream().max(Comparator.comparing(Move::getDate)).stream());
    }

    public static Map<Node, List<Move>> calculateMoves(Map<Long, Node> nodes, Map<Long, LocationName> locations, Map<Long, Move> moves, LocalDate date) {
        return validMoves(nodes, locations, moves, date)
                // group by node id
                .collect(Collectors.groupingBy(Move::getNodeID)).entrySet().stream()
                // map to node and move
                .collect(Collectors.toMap(e -> nodes.get(e.getKey()), Map.Entry::getValue));
    }

    public static Map<LocationName, Node> calculateNodes(Map<Long, Node> nodes, Map<Long, LocationName> locations, Map<Long, Move> moves, LocalDate date) {
        return validMoves(nodes, locations, moves, date)
                // get map of location to node
                .collect(Collectors.toMap(e -> locations.get(e.getLocationID()), e -> nodes.get(e.getNodeID())));
    }

    public static ObservableMap<Node, ObservableList<LocationName>> getNodeLocations(ObservableMap<Long, Node> nodes, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, ObservableObjectValue<LocalDate> date) {
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
        calculateMoves(nodes, locations, moves, date.get()).forEach((n, ms) -> {
            clear.accept(n);
            ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
        });
        // add event filters to all the maps to update the MultiValuedMap
        nodes.addListener((MapChangeListener<Long, Node>) change -> {
            if (change.wasRemoved())
                nodeLocations.remove(change.getValueRemoved());
//            if (change.wasAdded()) {
//                clear.accept(change.getValueRemoved());
//                var node = change.getValueAdded();
//                var map = new HashMap<Long, Node>();
//                map.put(node.getNodeID(), node);
//                calculateMoves(map, locations, moves, date.get()).forEach(m -> {
//                    add.accept(change.getValueAdded(), locations.get(m.getLocationID()));
//                });
//            }
            calculateMoves(nodes, locations, moves, date.get()).forEach((n, ms) -> {
                clear.accept(n);
                ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
            });
        });
        locations.addListener((MapChangeListener<Long, LocationName>) change -> {
            calculateMoves(nodes, locations, moves, date.get()).forEach((n, ms) -> {
                clear.accept(n);
                ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
            });
        });
        moves.addListener((MapChangeListener<Long, Move>) change -> {
            calculateMoves(nodes, locations, moves, date.get()).forEach((n, ms) -> {
                clear.accept(n);
                ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
            });
        });
        date.addListener((observable, oldValue, newValue) -> {
            calculateMoves(nodes, locations, moves, date.get()).forEach((n, ms) -> {
                clear.accept(n);
                ms.forEach(m -> add.accept(n, locations.get(m.getLocationID())));
            });
        });

        // return the list of locations
        return nodeLocations;
    }

    public static ObservableMap<LocationName, Node> getLocationNode(ObservableMap<Long, Node> nodes, ObservableMap<Long, LocationName> locations, ObservableMap<Long, Move> moves, ObservableObjectValue<LocalDate> date) {
        ObservableMap<LocationName, Node> locationNode = FXCollections.observableMap(calculateNodes(nodes, locations, moves, date.get()));

        // add event filters to all the maps to update the MultiValuedMap
        nodes.addListener((MapChangeListener<Long, Node>) change -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date.get()));
        });
        locations.addListener((MapChangeListener<Long, LocationName>) change -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date.get()));
        });
        moves.addListener((MapChangeListener<Long, Move>) change -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date.get()));
        });
        date.addListener((observable, oldValue, newValue) -> {
            locationNode.clear();
            locationNode.putAll(calculateNodes(nodes, locations, moves, date.get()));
        });
        return locationNode;
    }
}
