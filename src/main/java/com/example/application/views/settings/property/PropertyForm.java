package com.example.application.views.settings.property;

import com.example.application.data.entity.Property;
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

public class PropertyForm extends FormLayout {
    TextField name = new TextField("Наименование атрибута");
    TextArea description = new TextArea("Описание");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<Property> binder = new BeanValidationBinder<>(Property.class);
    private Property property;

    public PropertyForm() {
        H2 headline = new H2("Добавить атрибут");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, name,
                description,
                createButtonsLayout());
    }

    public void setProperty(Property property) {
        this.property = property;
        binder.readBean(property);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new PropertyForm.DeleteEvent(this, property)));
        close.addClickListener(event -> fireEvent(new PropertyForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(property);
            fireEvent(new PropertyForm.SaveEvent(this, property));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class PropertyFormEvent extends ComponentEvent<PropertyForm> {
        private Property property;

        protected PropertyFormEvent(PropertyForm source, Property property) {
            super(source, false);
            this.property = property;
        }

        public Property getProperty() {
            return property;
        }
    }

    public static class SaveEvent extends PropertyForm.PropertyFormEvent {
        SaveEvent(PropertyForm source, Property property) {
            super(source, property);
        }
    }

    public static class DeleteEvent extends PropertyForm.PropertyFormEvent {
        DeleteEvent(PropertyForm source, Property property) {
            super(source, property);
        }

    }

    public static class CloseEvent extends PropertyForm.PropertyFormEvent {
        CloseEvent(PropertyForm source) {
            super(source, null);
        }
    }
}
