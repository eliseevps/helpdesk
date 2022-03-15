package com.example.application.views.requests;

import com.example.application.data.entity.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class RequestsForm extends VerticalLayout {
    private Requests requests;

    ComboBox<Priority> priority = new ComboBox<>("Приоритет");
    ComboBox<Category> category = new ComboBox<>("Категория");
    ComboBox<Status> status = new ComboBox<>("Статус");
    DateTimePicker dateStart = new DateTimePicker("Дата обращения");
    DateTimePicker dateDue = new DateTimePicker("Срок выполнения");
    DateTimePicker dateEnd = new DateTimePicker("Дата закрытия");
    ComboBox<Users> requester = new ComboBox<>("Инициатор");
    ComboBox<Users> executor = new ComboBox<>("Исполнитель");
    TextField nameRequest = new TextField("Наименование обращения");
    TextArea descriptionRequest = new TextArea("Описание");
    TextArea comment = new TextArea("Комментарий исполнителя");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Requests> binder = new BeanValidationBinder<>(Requests.class);

    public RequestsForm(List<Priority> priorities, List<Category> categories, List<Status> statuses, List<Users> requesters, List<Users> executors) {
        H2 headline = new H2("Просмотр обращения");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);
        priority.setItems(priorities);
        priority.setItemLabelGenerator(Priority::getName);
        category.setItems(categories);
        category.setItemLabelGenerator(Category::getName);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);
        requester.setItems(requesters);
        requester.setItemLabelGenerator(Users::getName);
        executor.setItems(executors);
        executor.setItemLabelGenerator(Users::getName);
        FormLayout head = new FormLayout();
        head.add(headline);
        FormLayout content = new FormLayout();
        content.add(priority,
                category,
                status,
                dateStart,
                dateDue,
                dateEnd,
                requester,
                executor,
                nameRequest,
                descriptionRequest,
                comment);
        add(head, content, createButtonsLayout());
    }

    public void setRequests(Requests requests) {
        this.requests = requests;
        binder.readBean(requests);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, requests)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    // Events
    public static abstract class RequestsFormEvent extends ComponentEvent<RequestsForm> {
        private Requests requests;

        protected RequestsFormEvent(RequestsForm source, Requests requests) {
            super(source, false);
            this.requests = requests;
        }

        public Requests getRequests() {
            return requests;
        }
    }

    public static class SaveEvent extends RequestsFormEvent {
        SaveEvent(RequestsForm source, Requests requests) {
            super(source, requests);
        }
    }

    public static class DeleteEvent extends RequestsFormEvent {
        DeleteEvent(RequestsForm source, Requests requests) {
            super(source, requests);
        }

    }

    public static class CloseEvent extends RequestsFormEvent {
        CloseEvent(RequestsForm source) {
            super(source, null);
        }
    }


    private void validateAndSave() {
        try {
            binder.writeBean(requests);
            fireEvent(new SaveEvent(this, requests));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
