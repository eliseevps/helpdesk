package com.example.application.views.settings.category;

import com.example.application.data.entity.Category;
import com.example.application.data.service.CategoryService;
import com.example.application.views.settings.MainSettingsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "settings/category", layout = MainSettingsView.class)
@PageTitle("Категории")
public class CategoryView extends VerticalLayout {
    Grid<Category> grid = new Grid<>();
    TextField filterText = new TextField();
    CategoryForm form;
    CategoryService categoryService;
    Button addCategoryButton = new Button("Добавить категорию");
    Dialog dialog = new Dialog();

    public CategoryView(CategoryService service) {
        this.categoryService = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(form);
        addCategoryButton.addClickListener(e -> dialog.open());
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new CategoryForm();
        form.setWidth("25em");
        form.addListener(CategoryForm.SaveEvent.class, this::saveCategory);
        form.addListener(CategoryForm.DeleteEvent.class, this::deleteCategory);
        form.addListener(CategoryForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(Category::getName).setHeader("Наименование").setSortable(true);
        grid.addColumn(Category::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editCategory(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addCategoryButton.addClickListener(click -> addCategory());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCategoryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editCategory(Category category) {
        if (category == null) {
            closeEditor();
        } else {
            form.setCategory(category);
            dialog.open();
            addCategoryButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCategory(null);
        dialog.close();
        addCategoryButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Category());
    }

    private void updateList() {
        grid.setItems(categoryService.findAllCategory(filterText.getValue()));
    }

    private void saveCategory(CategoryForm.SaveEvent event) {
        categoryService.saveCategory(event.getCategory());
        updateList();
        closeEditor();
    }

    private void deleteCategory(CategoryForm.DeleteEvent event) {
        categoryService.deleteCategory(event.getCategory());
        updateList();
        closeEditor();
    }
}
