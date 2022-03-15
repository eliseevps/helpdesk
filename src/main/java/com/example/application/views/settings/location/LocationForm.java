package com.example.application.views.settings.location;

import com.example.application.data.entity.Location;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class LocationForm extends FormLayout {
    TextField office = new TextField("Здание");
    TextField floor = new TextField("Этаж");
    TextField cabinet = new TextField("Кабинет");
    TextArea description = new TextArea("Описание");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<Location> binder = new BeanValidationBinder<>(Location.class);
    private Location location;

    public LocationForm() {
        H2 headline = new H2("Добавить местоположение");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, office,
                floor,
                cabinet,
                description,
                createButtonsLayout());
    }

    public void setLocation(Location location) {
        this.location = location;
        binder.readBean(location);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, location)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(location);
            fireEvent(new LocationForm.SaveEvent(this, location));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class LocationFormEvent extends ComponentEvent<LocationForm> {
        private Location location;

        protected LocationFormEvent(LocationForm source, Location location) {
            super(source, false);
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }
    }

    public static class SaveEvent extends LocationForm.LocationFormEvent {
        SaveEvent(LocationForm source, Location location) {
            super(source, location);
        }
    }

    public static class DeleteEvent extends LocationForm.LocationFormEvent {
        DeleteEvent(LocationForm source, Location location) {
            super(source, location);
        }

    }

    public static class CloseEvent extends LocationForm.LocationFormEvent {
        CloseEvent(LocationForm source) {
            super(source, null);
        }
    }
}
