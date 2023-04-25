package edu.wpi.punchy_pegasi.schema;

public interface IField<T> {
    public Object getValue(T ref);
    public String getColName();
    String getValueAsString(T ref);
    void setValueFromString(T ref, String value);
    int ordinal();
    boolean isUnique();
    boolean isPrimaryKey();
}
