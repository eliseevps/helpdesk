package com.example.application.views.settings.property;

import com.example.application.data.entity.Property;
import com.example.application.data.service.PropertyService;
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

@Route(value = "settings/property", layout = MainSettingsView.class)
@PageTitle("Атрибуты оборудования")
public class PropertyView extends VerticalLayout {
    Grid<Property> grid = new Grid<>();
    TextField filterText = new TextField();
    PropertyForm form;
    PropertyService propertyService;
    Button addPropertyButton = new Button("Добавить атрибут");
    Dialog dialog = new Dialog();

    public PropertyView(PropertyService propertyService) {
        this.propertyService = propertyService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(form);
        addPropertyButton.addClickListener(e -> dialog.open());
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
        form = new PropertyForm();
        form.setWidth("25em");
        form.addListener(PropertyForm.SaveEvent.class, this::saveProperty);
        form.addListener(PropertyForm.DeleteEvent.class, this::deleteProperty);
        form.addListener(PropertyForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        //grid.addColumn(property -> property.getId()).setHeader("Номер").setSortable(true);
        grid.addColumn(Property::getName).setHeader("Наименование").setSortable(true);
        grid.addColumn(Property::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editProperty(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addPropertyButton.addClickListener(click -> addProperty());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addPropertyButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editProperty(Property property) {
        if (property == null) {
            closeEditor();
        } else {
            form.setProperty(property);
            dialog.open();
            addPropertyButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProperty(null);
        dialog.close();
        addPropertyButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addProperty() {
        grid.asSingleSelect().clear();
        editProperty(new Property());
    }

    private void updateList() {
        grid.setItems(propertyService.findAllProperty(filterText.getValue()));
    }

    private void saveProperty(PropertyForm.SaveEvent event) {
        propertyService.saveProperty(event.getProperty());
        updateList();
        closeEditor();
    }

    private void deleteProperty(PropertyForm.DeleteEvent event) {
        propertyService.deleteProperty(event.getProperty());
        updateList();
        closeEditor();
    }
}
