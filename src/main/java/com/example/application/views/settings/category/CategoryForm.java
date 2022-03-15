package com.example.application.views.settings.category;

import com.example.application.data.entity.Category;
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

public class CategoryForm extends FormLayout {
    TextField name = new TextField("Наименование категории");
    TextArea description = new TextArea("Описание");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");
    Binder<Category> binder = new BeanValidationBinder<>(Category.class);
    private Category category;

    public CategoryForm() {
        H2 headline = new H2("Добавить категорию");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(headline, name,
                description,
                createButtonsLayout());
    }

    public void setCategory(Category category) {
        this.category = category;
        binder.readBean(category);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CategoryForm.DeleteEvent(this, category)));
        close.addClickListener(event -> fireEvent(new CategoryForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(category);
            fireEvent(new CategoryForm.SaveEvent(this, category));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class CategoryFormEvent extends ComponentEvent<CategoryForm> {
        private Category category;

        protected CategoryFormEvent(CategoryForm source, Category category) {
            super(source, false);
            this.category = category;
        }

        public Category getCategory() {
            return category;
        }
    }

    public static class SaveEvent extends CategoryForm.CategoryFormEvent {
        SaveEvent(CategoryForm source, Category category) {
            super(source, category);
        }
    }

    public static class DeleteEvent extends CategoryForm.CategoryFormEvent {
        DeleteEvent(CategoryForm source, Category category) {
            super(source, category);
        }

    }

    public static class CloseEvent extends CategoryForm.CategoryFormEvent {
        CloseEvent(CategoryForm source) {
            super(source, null);
        }
    }
}
