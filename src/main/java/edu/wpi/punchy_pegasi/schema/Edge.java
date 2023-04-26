package edu.wpi.punchy_pegasi.schema;

import edu.wpi.punchy_pegasi.backend.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {
    @SchemaID
    @com.jsoniter.annotation.JsonProperty("uuid")
    private Long uuid;
    @com.jsoniter.annotation.JsonProperty("startnode")
    private Long startNode;
    @com.jsoniter.annotation.JsonProperty("endnode")
    private Long endNode;

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Edge> {
        UUID("uuid", true, false),
        START_NODE("startNode", false, false),
        END_NODE("endNode", false, false);
        @lombok.Getter
        private final String colName;
        @lombok.Getter
        private final boolean primaryKey;
        @lombok.Getter
        private final boolean unique;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Edge ref) {
            return ref.getFromField(this);
        }

        public String getValueAsString(edu.wpi.punchy_pegasi.schema.Edge ref) {
            return ref.getFromFieldAsString(this);
        }

        public void setValueFromString(edu.wpi.punchy_pegasi.schema.Edge ref, String value) {
            ref.setFieldFromString(this, value);
        }

        public int oridinal() {
            return ordinal();
        }
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case UUID -> getUuid();
            case START_NODE -> getStartNode();
            case END_NODE -> getEndNode();
        };
    }

    public void setFieldFromString(Field field, String value) {
        switch (field) {
            case UUID -> setUuid(Long.parseLong(value));
            case START_NODE -> setStartNode(Long.parseLong(value));
            case END_NODE -> setEndNode(Long.parseLong(value));
        }
    }

    public String getFromFieldAsString(Field field) {
        return switch (field) {
            case UUID -> Long.toString(getUuid());
            case START_NODE -> Long.toString(getStartNode());
            case END_NODE -> Long.toString(getEndNode());
        };
    }

}