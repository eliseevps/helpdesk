package com.example.application.views.settings.location;

import com.example.application.data.entity.Location;
import com.example.application.data.service.LocationService;
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

@Route(value = "settings/location", layout = MainSettingsView.class)
@PageTitle("Отделы")
public class LocationView extends VerticalLayout {
    Grid<Location> grid = new Grid<>();
    TextField filterText = new TextField();
    LocationForm form;
    LocationService locationService;
    Button addLocationButton = new Button("Добавить местоположение");
    Dialog dialog = new Dialog();

    public LocationView(LocationService locationService) {
        this.locationService = locationService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(form);
        addLocationButton.addClickListener(e -> dialog.open());
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
        form = new LocationForm();
        form.setWidth("25em");
        form.addListener(LocationForm.SaveEvent.class, this::saveLocation);
        form.addListener(LocationForm.DeleteEvent.class, this::deleteLocation);
        form.addListener(LocationForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        //grid.addColumn(location -> location.getId()).setHeader("Номер").setSortable(true);
        grid.addColumn(Location::getOffice).setHeader("Здание").setSortable(true);
        grid.addColumn(Location::getFloor).setHeader("Этаж").setSortable(true);
        grid.addColumn(Location::getCabinet).setHeader("Кабинет").setSortable(true);
        grid.addColumn(Location::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editLocation(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addLocationButton.addClickListener(click -> addLocation());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addLocationButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editLocation(Location location) {
        if (location == null) {
            closeEditor();
        } else {
            form.setLocation(location);
            dialog.open();
            addLocationButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setLocation(null);
        dialog.close();
        addLocationButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addLocation() {
        grid.asSingleSelect().clear();
        editLocation(new Location());
    }

    private void updateList() {
        grid.setItems(locationService.findAllLocation(filterText.getValue()));
    }

    private void saveLocation(LocationForm.SaveEvent event) {
        locationService.saveLocation(event.getLocation());
        updateList();
        closeEditor();
    }

    private void deleteLocation(LocationForm.DeleteEvent event) {
        locationService.deleteLocation(event.getLocation());
        updateList();
        closeEditor();
    }
}
