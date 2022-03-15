package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Device extends AbstractEntity {

    @NotNull
    @ManyToOne
    private CategoryDevice categoryDevice;

    @NotNull
    @ManyToOne
    private Users users;

    @NotNull
    @ManyToOne
    private Location location;

    @NotNull
    private boolean status = false;

    private String description;

    public CategoryDevice getCategoryDevice() {return categoryDevice;}
    public void setCategoryDevice(CategoryDevice categoryDevice) {
        this.categoryDevice = categoryDevice;
    }

    public Users getUsers() {return users;}
    public void setUsers(Users users) {
        this.users = users;
    }

    public Location getLocation() {return location;}
    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isStatus() {return status;}
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {return description;}
    public void setDescription(String description) {
        this.description = description;
    }

}
