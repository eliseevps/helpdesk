package com.example.application.views.device.categorydevice;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.entity.PropertyCategoryDevice;
import com.example.application.data.service.PropertyCategoryDeviceService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PropertyCategoryDeviceViewForm extends VerticalLayout {
    private PropertyCategoryDeviceService propertyCategoryDeviceService;

    Grid<PropertyCategoryDevice> grid = new Grid<>();

    Button close = new Button("Закрыть");
    private PropertyCategoryDevice propertyCategoryDevice;

    public PropertyCategoryDeviceViewForm(CategoryDevice value, PropertyCategoryDeviceService propertyCategoryDeviceService) {
        H2 headline = new H2("Категории оборудования " + value.getName());
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        this.propertyCategoryDeviceService = propertyCategoryDeviceService;
        addClassName("contact-form");
        grid.setItems(propertyCategoryDeviceService.findAllPropertyCategoryDevice(value));

        configureGrid();
        add(headline, grid,
                createButtonsLayout());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(pcd -> pcd.getProperty().getName()).setHeader("Наименование").setSortable(true);
        grid.addColumn(pcd -> pcd.getProperty().getDescription()).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    public void setPropertyCategoryDevice(PropertyCategoryDevice propertyCategoryDevice) {
        this.propertyCategoryDevice = propertyCategoryDevice;
    }

    private HorizontalLayout createButtonsLayout() {
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(e -> remove(grid));

        return new HorizontalLayout(close);
    }

}

