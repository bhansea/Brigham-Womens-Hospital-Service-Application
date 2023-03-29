package edu.wpi.punchy_pegasi;

import java.util.List;

public class FoodServiceRequestEntry {
  private String foodSelection;
  private String tempType;
  private List<String> additionalItems;
  private String dietaryRestrictions;
  private String patientName;
  private String roomName;
  private String additionalNotes;

  public FoodServiceRequestEntry(String patientName, String roomName, String additionalNotes, String foodSelection, String tempType, List<String> additionalItems, String dietaryRestrictions) {
    this.foodSelection = foodSelection;
    this.tempType = tempType;
    this.additionalItems = additionalItems;
    this.dietaryRestrictions = dietaryRestrictions;
    this.patientName = patientName;
    this.roomName = roomName;
    this.additionalNotes = additionalNotes;
  }
}
