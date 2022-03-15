package com.example.application.views.device.categorydevice;

import com.example.application.data.entity.CategoryDevice;
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

public class CategoryDeviceForm extends FormLayout {
    TextField name = new TextField("Наименование категории");
    TextArea description = new TextArea("Описание");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<CategoryDevice> binder = new BeanValidationBinder<>(CategoryDevice.class);
    private CategoryDevice categoryDevice;

    public CategoryDeviceForm() {
        H2 headline = new H2("Добавить категорию оборудования");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, name,
                description,
                createButtonsLayout());
    }

    public void setCategoryDevice(CategoryDevice categoryDevice) {
        this.categoryDevice = categoryDevice;
        binder.readBean(categoryDevice);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, categoryDevice)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(categoryDevice);
            fireEvent(new SaveEvent(this, categoryDevice));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class CategoryDeviceFormEvent extends ComponentEvent<CategoryDeviceForm> {
        private CategoryDevice categoryDevice;

        protected CategoryDeviceFormEvent(CategoryDeviceForm source, CategoryDevice categoryDevice) {
            super(source, false);
            this.categoryDevice = categoryDevice;
        }

        public CategoryDevice getCategoryDevice() {
            return categoryDevice;
        }
    }

    public static class SaveEvent extends CategoryDeviceFormEvent {
        SaveEvent(CategoryDeviceForm source, CategoryDevice categoryDevice) {
            super(source, categoryDevice);
        }
    }

    public static class DeleteEvent extends CategoryDeviceFormEvent {
        DeleteEvent(CategoryDeviceForm source, CategoryDevice categoryDevice) {
            super(source, categoryDevice);
        }

    }

    public static class CloseEvent extends CategoryDeviceFormEvent {
        CloseEvent(CategoryDeviceForm source) {
            super(source, null);
        }
    }
}
