package edu.wpi.punchy_pegasi.backend;

import java.util.Map;
import java.util.Optional;
import java.util.List;

public interface IDao<T, K> {
    Optional<T> get(K k);

    Map<K, T> getAll();

    void save(T t);

    void update(K k, Object[] params);

    void delete(K k);
}
