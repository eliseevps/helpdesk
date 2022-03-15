package com.example.application.views.device.listdevise;

import com.example.application.data.entity.Device;
import com.example.application.data.entity.PropertyCategoryDevice;
import com.example.application.data.entity.ValueProperty;
import com.example.application.data.service.ValuePropertyService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DeviceViewForm extends VerticalLayout {

    private ValuePropertyService valuePropertyService;

    GridPro<ValueProperty> grid = new GridPro<>();

    Button close = new Button("Закрыть");
    private PropertyCategoryDevice propertyCategoryDevice;

    public DeviceViewForm(Device value, ValuePropertyService valuePropertyService) {
        H2 headline = new H2("Характеристики оборудования Инв. номер " + value.getId() + " - " + value.getCategoryDevice().getName());
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        this.valuePropertyService = valuePropertyService;
        addClassName("contact-form");
        grid.setItems(valuePropertyService.findAllDevicesValue(value));

        configureGrid();
        add(headline, grid,
                createButtonsLayout());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(valueProperty -> valueProperty.getProperty().getName()).setHeader("Характеристика").setSortable(true);
        grid.addEditColumn(ValueProperty::getValue).text(ValueProperty::setValue).setHeader("Значение").setSortable(true);
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
