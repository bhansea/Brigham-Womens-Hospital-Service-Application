package edu.wpi.punchy_pegasi.backend;

import java.util.Map;
import java.util.Optional;
import java.util.List;

public interface IDao<T, K> {
    Optional<T> get(K k);

    Map<K, T> getAll();  // TODO: return List or Map?

    void save(T t);

    void update(T t, Object[] params);

    void delete(T t);
}
