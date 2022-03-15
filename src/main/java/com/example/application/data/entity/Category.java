package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Category extends AbstractEntity {

    private String name;

    private String description;

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

}
