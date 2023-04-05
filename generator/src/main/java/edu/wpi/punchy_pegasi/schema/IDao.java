package edu.wpi.punchy_pegasi.schema;

import java.util.Map;
import java.util.Optional;

public interface IDao<K, T, C> {
    Optional<T> get(K k);

    Optional<T> get(C column, Object value);

    Map<K, T> getAll();

    void save(T t);

    void update(T t, C[] params);

    void delete(T t);
}
