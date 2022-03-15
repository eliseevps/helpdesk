package com.example.application.views.settings.status;

import com.example.application.data.entity.Status;
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

public class StatusForm extends FormLayout {
    TextField name = new TextField("Наименование статуса");
    TextArea description = new TextArea("Описание");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<Status> binder = new BeanValidationBinder<>(Status.class);
    private Status status;

    public StatusForm() {
        H2 headline = new H2("Добавить статус");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, name,
                description,
                createButtonsLayout());
    }

    public void setStatus(Status status) {
        this.status = status;
        binder.readBean(status);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new StatusForm.DeleteEvent(this, status)));
        close.addClickListener(event -> fireEvent(new StatusForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(status);
            fireEvent(new StatusForm.SaveEvent(this, status));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class StatusFormEvent extends ComponentEvent<StatusForm> {
        private Status status;

        protected StatusFormEvent(StatusForm source, Status status) {
            super(source, false);
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }
    }

    public static class SaveEvent extends StatusForm.StatusFormEvent {
        SaveEvent(StatusForm source, Status status) {
            super(source, status);
        }
    }

    public static class DeleteEvent extends StatusForm.StatusFormEvent {
        DeleteEvent(StatusForm source, Status status) {
            super(source, status);
        }

    }

    public static class CloseEvent extends StatusForm.StatusFormEvent {
        CloseEvent(StatusForm source) {
            super(source, null);
        }
    }
}
