package edu.wpi.punchy_pegasi.schema;

public interface IField<T> {
    Object getValue(T ref);

    String getColName();

    String getValueAsString(T ref);

    void setValueFromString(T ref, String value);

    int ordinal();

    boolean isUnique();

    boolean isPrimaryKey();
}
