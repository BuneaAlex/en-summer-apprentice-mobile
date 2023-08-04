package com.example.tms.model.dtos;

import java.io.Serializable;

public class VenueDTO implements Serializable {
    private String type;
    private int capacity;
    private String location;

    public VenueDTO(String type, int capacity, String location) {
        this.type = type;
        this.capacity = capacity;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
