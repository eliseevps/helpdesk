package com.example.application.views.settings.priority;

import com.example.application.data.entity.Priority;
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

public class PriorityForm extends FormLayout {
    private Priority priority;

    TextField name = new TextField("Наименование приоритета");
    TextArea description  = new TextArea("Описание");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Priority> binder = new BeanValidationBinder<>(Priority.class);

    public PriorityForm() {
        H2 headline = new H2("Добавить приоритет");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, name,
                description,
                createButtonsLayout());
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        binder.readBean(priority);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new PriorityForm.DeleteEvent(this, priority)));
        close.addClickListener(event -> fireEvent(new PriorityForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    // Events
    public static abstract class PriorityFormEvent extends ComponentEvent<PriorityForm> {
        private Priority priority;

        protected PriorityFormEvent(PriorityForm source, Priority priority) {
            super(source, false);
            this.priority = priority;
        }

        public Priority getPriority() {
            return priority;
        }
    }

    public static class SaveEvent extends PriorityForm.PriorityFormEvent {
        SaveEvent(PriorityForm source, Priority priority) {
            super(source, priority);
        }
    }

    public static class DeleteEvent extends PriorityForm.PriorityFormEvent {
        DeleteEvent(PriorityForm source, Priority priority) {
            super(source, priority);
        }

    }

    public static class CloseEvent extends PriorityForm.PriorityFormEvent {
        CloseEvent(PriorityForm source) {
            super(source, null);
        }
    }


    private void validateAndSave() {
        try {
            binder.writeBean(priority);
            fireEvent(new PriorityForm.SaveEvent(this, priority));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
