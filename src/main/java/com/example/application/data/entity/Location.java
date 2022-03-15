package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
public class Location extends AbstractEntity {

    @NotEmpty
    @NotBlank
    private String office;

    @NotEmpty
    @NotBlank
    private String floor;

    @NotEmpty
    @NotBlank
    private String cabinet;

    private String description;

    public String getOffice() {
        return office;
    }
    public void setOffice(String office) {
        this.office = office;
    }

    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getCabinet() {
        return cabinet;
    }
    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullName() {
        return office + ", " + floor + " эт., каб." + cabinet;
    }
}
