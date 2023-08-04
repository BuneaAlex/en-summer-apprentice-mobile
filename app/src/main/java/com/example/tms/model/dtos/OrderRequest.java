package com.example.tms.model.dtos;

import com.google.gson.annotations.SerializedName;

public class OrderRequest {

    @SerializedName("customerID")
    private int customerID;
    @SerializedName("eventID")
    private int eventID;
    @SerializedName("ticketCategoryID")
    private int ticketCategoryID;
    @SerializedName("numberOfTickets")
    private int numberOfTickets;


    public OrderRequest(int customerID, int eventID, int ticketCategoryID, int numberOfTickets) {
        this.customerID = customerID;
        this.eventID = eventID;
        this.ticketCategoryID = ticketCategoryID;
        this.numberOfTickets = numberOfTickets;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getTicketCategoryID() {
        return ticketCategoryID;
    }

    public void setTicketCategoryID(int ticketCategoryID) {
        this.ticketCategoryID = ticketCategoryID;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "customerID=" + customerID +
                ", eventID=" + eventID +
                ", ticketCategoryID=" + ticketCategoryID +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }
}

