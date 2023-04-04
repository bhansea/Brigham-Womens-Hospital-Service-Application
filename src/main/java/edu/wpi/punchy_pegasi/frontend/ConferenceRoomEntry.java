package edu.wpi.punchy_pegasi.frontend;

public class ConferenceRoomEntry extends RequestEntry {

    private final String beginningTime;
    private final String endTime;

    //String patientName, String roomNumber, String additionalNotes
    public ConferenceRoomEntry(String beginningTime,
                               String endTime,
                               String patientName,
                               String roomNumber,
                               String additionalNotes,
                               String staffAssignment){
        super(patientName,roomNumber,additionalNotes,staffAssignment);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
    }

}
