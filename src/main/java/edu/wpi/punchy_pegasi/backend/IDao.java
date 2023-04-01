package edu.wpi.punchy_pegasi.backend;

import java.util.Optional;
import java.util.List;

public interface IDao<T, K> {
    Optional<T> get(K k);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
