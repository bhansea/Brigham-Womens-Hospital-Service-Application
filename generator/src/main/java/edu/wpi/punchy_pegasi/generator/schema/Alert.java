package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Builder(toBuilder=true)
public class Alert {
    @SchemaID
    private UUID uuid;
    private AlertType alertType;
    private String alertTitle;
    private String description;
    private Instant startDate;
    private Instant endDate; // For map alerts
    private ReadStatus readStatus;
    private Long employeeID; // For employee(who will see the alert) only applies for service/employee alert type
    private Long nodeID; // For map alert type

    public enum ReadStatus {
        READ,
        UNREAD
    }

    public enum AlertType {
        NONE,
        MAP,
        MAP_DISABLED,
        EMPLOYEE,
        ADMIN,
        SERVICE_REQUEST
    }
}