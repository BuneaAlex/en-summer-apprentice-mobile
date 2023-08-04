package com.example.tms.model.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class EventDTO implements Serializable {
    private int eventID;
    private VenueDTO venue;
    private String eventType;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<TicketCategoryDTO> ticketCategories;
    private String image;
    public EventDTO() {
    }

    public EventDTO(int eventID, VenueDTO venue, String eventType, String name, String description, String startDate, String endDate, List<TicketCategoryDTO> ticketCategories, String image) {
        this.eventID = eventID;
        this.venue = venue;
        this.eventType = eventType;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketCategories = ticketCategories;
        this.image = image;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public VenueDTO getVenue() {
        return venue;
    }

    public void setVenue(VenueDTO venue) {
        this.venue = venue;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<TicketCategoryDTO> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategoryDTO> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
