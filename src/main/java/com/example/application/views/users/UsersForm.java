package com.example.application.views.users;

import com.example.application.data.entity.Department;
import com.example.application.data.entity.Location;
import com.example.application.data.entity.Users;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class UsersForm extends VerticalLayout {
    private Users users;

    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    ComboBox<String> roleSystem = new ComboBox<>("Роль в системе");
    TextField loginUser = new TextField("Логин");
    PasswordField passwordUser = new PasswordField("Пароль");
    EmailField email = new EmailField("Почта");
    TextField phone = new TextField("Телефон");
    ComboBox<Department> department = new ComboBox<>("Отдел");
    TextField post = new TextField("Должность");
    Checkbox status = new Checkbox("Работает");
    ComboBox<Location> location = new ComboBox<>("Местоположение");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Users> binder = new BeanValidationBinder<>(Users.class);

    public UsersForm(List<Department> departments, List<Location> locations) {
        H2 headline = new H2("Добавить пользователя");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        addClassName("contact-form");
        binder.bindInstanceFields(this);
        roleSystem.setItems("ИТ", "Пользователь");
        department.setItems(departments);
        department.setItemLabelGenerator(Department::getName);
        location.setItems(locations);
        location.setItemLabelGenerator(Location::getFullName);
        FormLayout head = new FormLayout();
        head.add(headline);
        FormLayout content = new FormLayout();
        content.add(lastName,
                firstName,
                roleSystem,
                loginUser,
                passwordUser,
                email,
                phone,
                department,
                post,
                location,
                status);
        add (head, content, createButtonsLayout());
    }

    public void setUsers(Users users) {
        this.users = users;
        binder.readBean(users);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, users)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    // Events
    public static abstract class UsersFormEvent extends ComponentEvent<UsersForm> {
        private Users users;

        protected UsersFormEvent(UsersForm source, Users users) {
            super(source, false);
            this.users = users;
        }

        public Users getUsers() {
            return users;
        }
    }

    public static class SaveEvent extends UsersFormEvent {
        SaveEvent(UsersForm source, Users users) {
            super(source, users);
        }
    }

    public static class DeleteEvent extends UsersFormEvent {
        DeleteEvent(UsersForm source, Users users) {
            super(source, users);
        }

    }

    public static class CloseEvent extends UsersFormEvent {
        CloseEvent(UsersForm source) {
            super(source, null);
        }
    }


    private void validateAndSave() {
        try {
            binder.writeBean(users);
            fireEvent(new SaveEvent(this, users));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

