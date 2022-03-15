package com.example.application.views.device.listdevise;

import com.example.application.data.entity.Device;
import com.example.application.data.service.*;
import com.example.application.views.device.MainDeviceView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "device/devices", layout = MainDeviceView.class)
@PageTitle("Оборудование")
public class DeviceView extends VerticalLayout {
    Grid<Device> grid = new Grid<>();
    TextField filterText = new TextField();
    DeviceForm form;
    DeviceViewForm formValue;
    DeviceService deviceService;
    ValuePropertyService valuePropertyService;
    LocationService locationService;
    CategoryDeviceService categoryDeviceService;
    UsersService usersService;
    Button addDeviceButton = new Button("Добавить оборудование");
    Dialog dialog = new Dialog();
    Dialog dialogValue = new Dialog();

    public DeviceView(DeviceService deviceService,
                      ValuePropertyService valuePropertyService,
                      LocationService locationService,
                      CategoryDeviceService categoryDeviceService,
                      UsersService usersService) {
        this.deviceService = deviceService;
        this.valuePropertyService = valuePropertyService;
        this.locationService = locationService;
        this.categoryDeviceService = categoryDeviceService;
        this.usersService = usersService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        dialog.add(form);

        grid.addItemClickListener(e -> {
            if (e!=null) {
                formValue = new DeviceViewForm(grid.asSingleSelect().getValue(), valuePropertyService);
                formValue.setWidth("50em");
                dialogValue.add(formValue);
                dialogValue.open();
            }
        });
        addDeviceButton.addClickListener(e -> dialog.open());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new DeviceForm(categoryDeviceService.findAllCategoryDevices(), usersService.findAllUsers(""), locationService.findAllLocations());
        form.setWidth("25em");
        form.addListener(DeviceForm.SaveEvent.class, this::saveDevice);
        form.addListener(DeviceForm.DeleteEvent.class, this::deleteDevice);
        form.addListener(DeviceForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(device -> device.getId()).setHeader("Инв.номер").setSortable(true);
        grid.addColumn(device -> device.getCategoryDevice().getName()).setHeader("Категория").setSortable(true);
        grid.addColumn(device -> device.getUsers().getName()).setHeader("Пользователь").setSortable(true);
        grid.addColumn(device -> device.getLocation().getFullName()).setHeader("Местоположение").setSortable(true);
        TemplateRenderer<Device> statusRenderer = TemplateRenderer.<Device>of(
                        "<iron-icon hidden='[[!item.status]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.status]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("status", Device::isStatus);
        grid.addColumn(statusRenderer).setHeader("Закреплен").setSortable(true);
        //grid.addColumn(device -> device.getDescription()).setHeader("Почта").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        //grid.asSingleSelect().addValueChangeListener(event ->
                //editDevice(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addDeviceButton.addClickListener(click -> addDevice());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addDeviceButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editDevice(Device device) {
        if (device == null) {
            closeEditor();
        } else {
            form.setDevice(device);
            dialog.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setDevice(null);
        dialog.close();
        removeClassName("editing");
    }

    private void addDevice() {
        grid.asSingleSelect().clear();
        editDevice(new Device());
    }

    private void updateList() {
        grid.setItems(deviceService.findAllDevice(filterText.getValue()));
    }

    private void saveDevice(DeviceForm.SaveEvent event) {
        deviceService.saveDevice(event.getDevice());
        updateList();
        closeEditor();
    }

    private void deleteDevice(DeviceForm.DeleteEvent event) {
        deviceService.deleteDevice(event.getDevice());
        updateList();
        closeEditor();
    }
}
