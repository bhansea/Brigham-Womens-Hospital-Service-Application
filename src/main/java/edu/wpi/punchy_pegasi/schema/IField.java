package edu.wpi.punchy_pegasi.schema;

public interface IField<T> {
    //add a set field
    public Object getValue(T ref);
    public String getColName();
}
