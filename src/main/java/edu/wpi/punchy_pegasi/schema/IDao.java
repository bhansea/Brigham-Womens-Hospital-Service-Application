package edu.wpi.punchy_pegasi.schema;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Map;
import java.util.Optional;

public interface IDao<K, T, C> {
    Optional<T> get(K k);

    Map<K, T> get(C column, Object value);

    Map<K, T> get(C[] column, Object[] value);

    ObservableMap<K, T> getAll();

    ObservableList<T> getAllAsList();

    void save(T t);

    void update(T t, C[] params);

    void delete(T t);
}
