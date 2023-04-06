package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.RequiredArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class Edge {
        private Long uuid;
    private Long startNode;
    private Long endNode;
    @RequiredArgsConstructor
    public enum Field {
        UUID("uuid"),
        START_NODE("startNode"),
        END_NODE("endNode");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.Edge ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case UUID -> getUuid();
            case START_NODE -> getStartNode();
            case END_NODE -> getEndNode();
        };
    }

}