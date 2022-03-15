package com.example.application.views.settings.department;

import com.example.application.data.entity.Department;
import com.example.application.data.service.DepartmentService;
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

@Route(value = "settings/department", layout = MainSettingsView.class)
@PageTitle("Отделы")
public class DepartmentView extends VerticalLayout {
    Grid<Department> grid = new Grid<>();
    TextField filterText = new TextField();
    DepartmentForm form;
    DepartmentService departmentService;
    Button addDepartmentButton = new Button("Добавить отдел");
    Dialog dialog = new Dialog();

    public DepartmentView(DepartmentService departmentService) {
        this.departmentService = departmentService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(form);
        addDepartmentButton.addClickListener(e -> dialog.open());
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);

        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new DepartmentForm();
        form.setWidth("25em");
        form.addListener(DepartmentForm.SaveEvent.class, this::saveDepartment);
        form.addListener(DepartmentForm.DeleteEvent.class, this::deleteDepartment);
        form.addListener(DepartmentForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(Department::getName).setHeader("Наименование").setSortable(true);
        grid.addColumn(Department::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editDepartment(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addDepartmentButton.addClickListener(click -> addDepartment());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addDepartmentButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editDepartment(Department department) {
        if (department == null) {
            closeEditor();
        } else {
            form.setDepartment(department);
            dialog.open();
            addDepartmentButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setDepartment(null);
        dialog.close();
        addDepartmentButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addDepartment() {
        grid.asSingleSelect().clear();
        editDepartment(new Department());
    }

    private void updateList() {
        grid.setItems(departmentService.findAllDepartment(filterText.getValue()));
    }

    private void saveDepartment(DepartmentForm.SaveEvent event) {
        departmentService.saveDepartment(event.getDepartment());
        updateList();
        closeEditor();
    }

    private void deleteDepartment(DepartmentForm.DeleteEvent event) {
        departmentService.deleteDepartment(event.getDepartment());
        updateList();
        closeEditor();
    }
}
