package com.example.tms.model.dtos;

public class OrderPatchRequest {
    private int numberOfTickets;
    private String ticketType;

    public OrderPatchRequest(int numberOfTickets, String ticketType) {
        this.numberOfTickets = numberOfTickets;
        this.ticketType = ticketType;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public String toString() {
        return "{" +
                "numberOfTickets=" + numberOfTickets +
                ", ticketType='" + ticketType + '\'' +
                '}';
    }
}

