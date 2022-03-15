package com.example.application.views.settings.priority;

import com.example.application.data.entity.Priority;
import com.example.application.data.service.PriorityService;
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

@Route(value = "settings/priority", layout = MainSettingsView.class)
@PageTitle("Приоритеты")
public class PriorityView extends VerticalLayout {
    Grid<Priority> grid = new Grid<>();
    TextField filterText = new TextField();
    PriorityForm form;
    PriorityService priorityService;
    Button addPriorityButton = new Button("Добавить приоритет");
    Dialog dialog = new Dialog();

    public PriorityView(PriorityService priorityService) {
        this.priorityService = priorityService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(form);
        addPriorityButton.addClickListener(e-> dialog.open());
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new PriorityForm();
        form.setWidth("25em");
        form.addListener(PriorityForm.SaveEvent.class, this::savePriority);
        form.addListener(PriorityForm.DeleteEvent.class, this::deletePriority);
        form.addListener(PriorityForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(Priority::getName).setHeader("Наименование").setSortable(true);
        grid.addColumn(Priority::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editPriority(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addPriorityButton.addClickListener(click -> addPriority());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addPriorityButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editPriority(Priority priority) {
        if (priority == null) {
            closeEditor();
        } else {
            form.setPriority(priority);
            dialog.open();
            addPriorityButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPriority(null);
        dialog.close();
        addPriorityButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addPriority() {
        grid.asSingleSelect().clear();
        editPriority(new Priority());
    }

    private void updateList() {grid.setItems(priorityService.findAllPriority(filterText.getValue())); }

    private void savePriority(PriorityForm.SaveEvent event) {
        priorityService.savePriority(event.getPriority());
        updateList();
        closeEditor();
    }

    private void deletePriority(PriorityForm.DeleteEvent event) {
        priorityService.deletePriority(event.getPriority());
        updateList();
        closeEditor();
    }
}
