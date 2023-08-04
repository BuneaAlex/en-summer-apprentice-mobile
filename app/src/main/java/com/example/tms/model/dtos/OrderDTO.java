package com.example.tms.model.dtos;


import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderDTO implements Serializable {
    private int orderID;
    private int eventID;
    private TicketCategoryDTO ticketCategory;
    private String orderedAt;
    private int numberOfTickets;
    private float totalPrice;

    public OrderDTO(int orderID, int eventID, TicketCategoryDTO ticketCategory,
                    String orderedAt, int numberOfTickets, float totalPrice) {
        this.orderID = orderID;
        this.eventID = eventID;
        this.ticketCategory = ticketCategory;
        this.orderedAt = orderedAt;
        this.numberOfTickets = numberOfTickets;
        this.totalPrice = totalPrice;

    }


    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public TicketCategoryDTO getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategoryDTO ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public String getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(String orderedAt) {
        this.orderedAt = orderedAt;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderID=" + orderID +
                ", eventID=" + eventID +
                ", ticketCategory=" + ticketCategory +
                ", orderedAt=" + orderedAt +
                ", numberOfTickets=" + numberOfTickets +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

