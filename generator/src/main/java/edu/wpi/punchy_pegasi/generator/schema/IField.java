package edu.wpi.punchy_pegasi.generator.schema;

public interface IField<T> {
    public Object getValue(T ref);
    public String getColName();
}
