package com.example.application.views.settings.status;

import com.example.application.data.entity.Status;
import com.example.application.data.service.StatusService;
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

@Route(value = "settings/status", layout = MainSettingsView.class)
@PageTitle("Статусы обращений")
public class StatusView extends VerticalLayout {
    Grid<Status> grid = new Grid<>();
    TextField filterText = new TextField();
    StatusForm form;
    StatusService statusService;
    Button addStatusButton = new Button("Добавить статус");
    Dialog dialog = new Dialog();

    public StatusView(StatusService statusService) {
        this.statusService = statusService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(form);
        addStatusButton.addClickListener(e -> dialog.open());
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        //content.setFlexGrow(2, grid);
        //content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new StatusForm();
        form.setWidth("25em");
        form.addListener(StatusForm.SaveEvent.class, this::saveStatus);
        form.addListener(StatusForm.DeleteEvent.class, this::deleteStatus);
        form.addListener(StatusForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        //grid.addColumn(status -> status.getId()).setHeader("Номер").setSortable(true);
        grid.addColumn(Status::getName).setHeader("Наименование").setSortable(true);
        grid.addColumn(Status::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editStatus(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addStatusButton.addClickListener(click -> addStatus());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStatusButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editStatus(Status status) {
        if (status == null) {
            closeEditor();
        } else {
            form.setStatus(status);
            dialog.open();
            addStatusButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setStatus(null);
        dialog.close();
        addStatusButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addStatus() {
        grid.asSingleSelect().clear();
        editStatus(new Status());
    }

    private void updateList() {
        grid.setItems(statusService.findAllStatus(filterText.getValue()));
    }

    private void saveStatus(StatusForm.SaveEvent event) {
        statusService.saveStatus(event.getStatus());
        updateList();
        closeEditor();
    }

    private void deleteStatus(StatusForm.DeleteEvent event) {
        statusService.deleteStatus(event.getStatus());
        updateList();
        closeEditor();
    }
}
