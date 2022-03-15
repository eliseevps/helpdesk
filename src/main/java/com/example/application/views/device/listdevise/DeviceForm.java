package com.example.application.views.device.listdevise;

import com.example.application.data.entity.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class DeviceForm extends FormLayout {
    private Device device;

    ComboBox<CategoryDevice> categoryDevice = new ComboBox<>("Категория оборудования");
    ComboBox<Users> users = new ComboBox<>("Ответственный");
    ComboBox<Location> location = new ComboBox<>("Местоположение");
    Checkbox status = new Checkbox("Закреплено");
    TextArea description = new TextArea("Описание");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Device> binder = new BeanValidationBinder<>(Device.class);

    public DeviceForm(List<CategoryDevice> categoryDevices, List<Users> usersDevice, List<Location> locations) {
        H2 headline = new H2("Добавить оборудование");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);
        categoryDevice.setItems(categoryDevices);
        categoryDevice.setItemLabelGenerator(CategoryDevice::getName);
        users.setItems(usersDevice);
        users.setItemLabelGenerator(Users::getName);
        location.setItems(locations);
        location.setItemLabelGenerator(Location::getFullName);

        add(headline, categoryDevice,
                users,
                location,
                description,
                status,
                createButtonsLayout());
    }

    public void setDevice(Device device) {
        this.device = device;
        binder.readBean(device);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeviceForm.DeleteEvent(this, device)));
        close.addClickListener(event -> fireEvent(new DeviceForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    // Events
    public static abstract class DeviceFormEvent extends ComponentEvent<DeviceForm> {
        private Device device;

        protected DeviceFormEvent(DeviceForm source, Device device) {
            super(source, false);
            this.device = device;
        }

        public Device getDevice() {
            return device;
        }
    }

    public static class SaveEvent extends DeviceForm.DeviceFormEvent {
        SaveEvent(DeviceForm source, Device device) {
            super(source, device);
        }
    }

    public static class DeleteEvent extends DeviceForm.DeviceFormEvent {
        DeleteEvent(DeviceForm source, Device device) {
            super(source, device);
        }

    }

    public static class CloseEvent extends DeviceForm.DeviceFormEvent {
        CloseEvent(DeviceForm source) {
            super(source, null);
        }
    }


    private void validateAndSave() {
        try {
            binder.writeBean(device);
            fireEvent(new DeviceForm.SaveEvent(this, device));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
