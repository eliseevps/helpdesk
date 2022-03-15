package com.example.application.views.device.categorydevice;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.entity.Property;
import com.example.application.data.entity.PropertyCategoryDevice;
import com.example.application.data.service.PropertyService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class PropertyCategoryDeviceForm extends FormLayout {
    private final PropertyService propertyService;
    Select<CategoryDevice> categoryDevice = new Select<>();
    ComboBox<Property> property = new ComboBox<>("Атрибут");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<PropertyCategoryDevice> binder = new BeanValidationBinder<>(PropertyCategoryDevice.class);
    private PropertyCategoryDevice propertyCategoryDevice;

    public PropertyCategoryDeviceForm(List<CategoryDevice> categoryDevices,
                                      List<Property> properties,
                                      PropertyService propertyService) {
        this.propertyService = propertyService;
        H2 headline = new H2("Добавить атрибут к категории");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);
        categoryDevice.setLabel("Категория");
        categoryDevice.setItems(categoryDevices);
        categoryDevice.setItemLabelGenerator(CategoryDevice::getName);
        categoryDevice.addValueChangeListener(e -> {
            if (e != null) {
                property.setItems(propertyService.findAllPropertyNotIn(categoryDevice.getValue()));
            }
        });
        property.setItemLabelGenerator(Property::getName);

        add(headline, categoryDevice,
                property,
                createButtonsLayout());
    }


    public void setPropertyCategoryDevice(PropertyCategoryDevice propertyCategoryDevice) {
        this.propertyCategoryDevice = propertyCategoryDevice;
        binder.readBean(propertyCategoryDevice);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, propertyCategoryDevice)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(propertyCategoryDevice);
            fireEvent(new SaveEvent(this, propertyCategoryDevice));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class PropertyCategoryDeviceFormEvent extends ComponentEvent<PropertyCategoryDeviceForm> {
        private PropertyCategoryDevice propertyCategoryDevice;

        protected PropertyCategoryDeviceFormEvent(PropertyCategoryDeviceForm source, PropertyCategoryDevice propertyCategoryDevice) {
            super(source, false);
            this.propertyCategoryDevice = propertyCategoryDevice;
        }

        public PropertyCategoryDevice getPropertyCategoryDevice() {
            return propertyCategoryDevice;
        }
    }

    public static class SaveEvent extends PropertyCategoryDeviceFormEvent {
        SaveEvent(PropertyCategoryDeviceForm source, PropertyCategoryDevice propertyCategoryDevice) {
            super(source, propertyCategoryDevice);
        }
    }

    public static class DeleteEvent extends PropertyCategoryDeviceFormEvent {
        DeleteEvent(PropertyCategoryDeviceForm source, PropertyCategoryDevice propertyCategoryDevice) {
            super(source, propertyCategoryDevice);
        }

    }

    public static class CloseEvent extends PropertyCategoryDeviceFormEvent {
        public CloseEvent(PropertyCategoryDeviceForm source) {
            super(source, null);
        }
    }
}
