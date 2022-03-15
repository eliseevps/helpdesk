package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PropertyCategoryDevice extends AbstractEntity {

    @ManyToOne
    private CategoryDevice categoryDevice;

    @ManyToOne
    private Property property;

    public CategoryDevice getCategoryDevice() {
        return categoryDevice;
    }
    public void setCategoryDevice(CategoryDevice categoryDevice) {
        this.categoryDevice = categoryDevice;
    }

    public Property getProperty() {
        return property;
    }
    public void setProperty(Property property) {
        this.property = property;
    }

}
