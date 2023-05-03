package edu.wpi.punchy_pegasi.schema;

public interface IField<T, B> {
    Object getValue(T ref);

    String getColName();

    String getValueAsString(T ref);

    void setValueFromString(B builder, String value);

    int ordinal();

    boolean isUnique();

    boolean isPrimaryKey();
}
