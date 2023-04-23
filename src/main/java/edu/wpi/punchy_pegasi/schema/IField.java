package edu.wpi.punchy_pegasi.schema;

public interface IField<T> {
    Object getValue(T ref);

    String getColName();
}
