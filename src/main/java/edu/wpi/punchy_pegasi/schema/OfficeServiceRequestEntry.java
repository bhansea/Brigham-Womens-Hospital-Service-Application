package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OfficeServiceRequestEntry extends RequestEntry {
    private final Boolean pencils;
    private final Boolean pens;
    private final Boolean paper;
    private final Boolean stapler;
    private final Boolean staples;
    private final Boolean paperclips;
    private final Boolean other;
    private final String pencilAmount;
    private final String penAmount;
    private final String paperAmount;
    private final String staplerAmount;
    private final String stapleAmount;
    private final String paperclipAmount;
    private final String otherItems;

    public OfficeServiceRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status,
                                     Boolean pencils, String pencilAmount, Boolean pens, String penAmount, Boolean paper, String paperAmount,
                                     Boolean stapler, String staplerAmount, Boolean staples, String stapleAmount, Boolean paperclips, String paperclipAmount,
                                     Boolean other, String otherItems) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
        this.pencils = pencils;
        this.pencilAmount = pencilAmount;
        this.pens = pens;
        this.penAmount = penAmount;
        this.paper = paper;
        this.paperAmount = paperAmount;
        this.stapler = stapler;
        this.staplerAmount = staplerAmount;
        this.staples = staples;
        this.stapleAmount = stapleAmount;
        this.paperclips = paperclips;
        this.paperclipAmount = paperclipAmount;
        this.other = other;
        this.otherItems = otherItems;
    }

    public OfficeServiceRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, Boolean pencils, String pencilAmount, Boolean pens, String penAmount, Boolean paper, String paperAmount,
                                     Boolean stapler, String staplerAmount, Boolean staples, String stapleAmount, Boolean paperclips, String paperclipAmount,
                                     Boolean other, String otherItems) {
        this(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, pencils, pencilAmount, pens, penAmount, paper, paperAmount,
                stapler, staplerAmount, staples, stapleAmount, paperclips, paperclipAmount,
                other, otherItems);
    }
    @lombok.RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        PENCILS("pencils"),
        PENCIL_AMOUNT("pencilAmount"),
        PENS("pens"),
        PEN_AMOUNT("pensAmount"),
        PAPER("paper"),
        PAPER_AMOUNT("paperAmount"),
        STAPLER("stapler"),
        STAPLER_AMOUNT("staplerAmount"),
        STAPLES("staples"),
        STAPLE_AMOUNT("stapleAmount"),
        PAPERCLIPS("paperclips"),
        PAPERCLIP_AMOUNT("paperclipAmount"),
        OTHER("other"),
        OTHER_ITEMS("otherItems");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case PENCILS -> getPencils();
            case PENCIL_AMOUNT -> getPencilAmount();
            case PENS -> getPens();
            case PEN_AMOUNT -> getPenAmount();
            case PAPER -> getPaper();
            case PAPER_AMOUNT -> getPaperAmount();
            case STAPLER -> getStapler();
            case STAPLER_AMOUNT -> getStaplerAmount();
            case STAPLES -> getStaples();
            case STAPLE_AMOUNT -> getStapleAmount();
            case PAPERCLIPS -> getPaperclips();
            case PAPERCLIP_AMOUNT -> getPaperclipAmount();
            case OTHER -> getOther();
            case OTHER_ITEMS -> getOtherItems();
        };
    }

}