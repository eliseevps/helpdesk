package com.example.application.views.settings.department;

import com.example.application.data.entity.Department;
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

public class DepartmentForm extends FormLayout {
    TextField name = new TextField("Наименование отдела");
    TextArea description = new TextArea("Описание");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<Department> binder = new BeanValidationBinder<>(Department.class);
    private Department department;

    public DepartmentForm() {
        H2 headline = new H2("Добавить отдел");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, name,
                description,
                createButtonsLayout());
    }

    public void setDepartment(Department department) {
        this.department = department;
        binder.readBean(department);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, department)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(department);
            fireEvent(new DepartmentForm.SaveEvent(this, department));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class DepartmentFormEvent extends ComponentEvent<DepartmentForm> {
        private Department department;

        protected DepartmentFormEvent(DepartmentForm source, Department department) {
            super(source, false);
            this.department = department;
        }

        public Department getDepartment() {
            return department;
        }
    }

    public static class SaveEvent extends DepartmentForm.DepartmentFormEvent {
        SaveEvent(DepartmentForm source, Department department) {
            super(source, department);
        }
    }

    public static class DeleteEvent extends DepartmentForm.DepartmentFormEvent {
        DeleteEvent(DepartmentForm source, Department department) {
            super(source, department);
        }

    }

    public static class CloseEvent extends DepartmentForm.DepartmentFormEvent {
        CloseEvent(DepartmentForm source) {
            super(source, null);
        }
    }
}
