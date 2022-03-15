package com.example.application.views.device.categorydevice;

import com.example.application.data.entity.CategoryDevice;
import com.example.application.data.entity.PropertyCategoryDevice;
import com.example.application.data.service.CategoryDeviceService;
import com.example.application.data.service.PropertyCategoryDeviceService;
import com.example.application.data.service.PropertyService;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "device/categorydevice", layout = MainDeviceView.class)
@PageTitle("Категории оборудования")
public class CategoryDeviceView extends VerticalLayout {
    Grid<CategoryDevice> grid = new Grid<>();
    TextField filterText = new TextField();
    CategoryDeviceForm deviceForm;
    PropertyCategoryDeviceForm propertyCategoryDeviceForm;
    PropertyCategoryDeviceViewForm propertyCategoryDeviceViewForm;
    CategoryDeviceService categoryDeviceService;
    PropertyService propertyService;
    PropertyCategoryDeviceService propertyCategoryDeviceService;
    Button addCategoryDeviceButton = new Button("Добавить категорию");
    Button addPropertyCategoryDeviceButton = new Button("Добавить характеристику");
    Dialog dialog = new Dialog();
    Dialog dialogProperty = new Dialog();
    Dialog dialogPropertyDevice = new Dialog();

    public CategoryDeviceView(CategoryDeviceService service,
                              PropertyService propertyService,
                              PropertyCategoryDeviceService propertyCategoryDeviceService) {
        this.categoryDeviceService = service;
        this.propertyService = propertyService;
        this.propertyCategoryDeviceService = propertyCategoryDeviceService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        dialog.add(deviceForm);
        dialogProperty.add(propertyCategoryDeviceForm);
        grid.addItemClickListener(e -> {
                if (e!=null) {
                    propertyCategoryDeviceViewForm = new PropertyCategoryDeviceViewForm(grid.asSingleSelect().getValue(), propertyCategoryDeviceService);
                    propertyCategoryDeviceViewForm.setWidth("50em");
                    dialogPropertyDevice.add(propertyCategoryDeviceViewForm);
                    dialogPropertyDevice.open();
                }
        });
        addCategoryDeviceButton.addClickListener(e -> dialog.open());
        addPropertyCategoryDeviceButton.addClickListener(e -> dialogProperty.open());
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
        deviceForm = new CategoryDeviceForm();
        deviceForm.setWidth("25em");
        deviceForm.addListener(CategoryDeviceForm.SaveEvent.class, this::saveCategoryDevice);
        deviceForm.addListener(CategoryDeviceForm.DeleteEvent.class, this::deleteCategoryDevice);
        deviceForm.addListener(CategoryDeviceForm.CloseEvent.class, e -> closeEditor());
        propertyCategoryDeviceForm = new PropertyCategoryDeviceForm(categoryDeviceService.findAllCategoryDevices(), propertyService.findAllPropertys(), propertyService);
        propertyCategoryDeviceForm.setWidth("25em");
        propertyCategoryDeviceForm.addListener(PropertyCategoryDeviceForm.SaveEvent.class, this::savePropertyCategoryDevice);
        propertyCategoryDeviceForm.addListener(PropertyCategoryDeviceForm.DeleteEvent.class, this::deletePropertyCategoryDevice);
        propertyCategoryDeviceForm.addListener(PropertyCategoryDeviceForm.CloseEvent.class, e -> closeEditor());
    }


    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        grid.addColumn(CategoryDevice::getName).setHeader("Наименование").setSortable(true);
        grid.addColumn(CategoryDevice::getDescription).setHeader("Описание").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        //grid.asSingleSelect().addValueChangeListener(event ->
                //editCategoryDevice(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addCategoryDeviceButton.addClickListener(click -> addCategoryDevice());
        addPropertyCategoryDeviceButton.addClickListener(click -> addPropertyCategoryDevice());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCategoryDeviceButton, addPropertyCategoryDeviceButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editCategoryDevice(CategoryDevice categoryDevice) {
        if (categoryDevice == null) {
            closeEditor();
        } else {
            deviceForm.setCategoryDevice(categoryDevice);
            dialog.open();
            addClassName("editing");
        }
    }

    public void editPropertyCategoryDevice(PropertyCategoryDevice propertyCategoryDevice) {
        if (propertyCategoryDevice == null) {
            closeEditor();
        } else {
            propertyCategoryDeviceForm.setPropertyCategoryDevice(propertyCategoryDevice);
            dialogProperty.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        deviceForm.setCategoryDevice(null);
        propertyCategoryDeviceForm.setPropertyCategoryDevice(null);
        dialog.close();
        dialogProperty.close();
        updateList();
        removeClassName("editing");
    }

    private void closeEditorProperty() {
        dialog.close();
        dialogProperty.close();
        updateList();
        removeClassName("editing");
    }

    private void addCategoryDevice() {
        grid.asSingleSelect().clear();
        editCategoryDevice(new CategoryDevice());
    }

    private void addPropertyCategoryDevice() {
        grid.asSingleSelect().clear();
        editPropertyCategoryDevice(new PropertyCategoryDevice());
    }

    private void updateList() {
        grid.setItems(categoryDeviceService.findAllCategoryDevice(filterText.getValue()));
    }

    private void saveCategoryDevice(CategoryDeviceForm.SaveEvent event) {
        categoryDeviceService.saveCategoryDevice(event.getCategoryDevice());
        updateList();
        closeEditor();
    }

    private void deleteCategoryDevice(CategoryDeviceForm.DeleteEvent event) {
        categoryDeviceService.deleteCategoryDevice(event.getCategoryDevice());
        updateList();
        closeEditor();
    }

    private void savePropertyCategoryDevice(PropertyCategoryDeviceForm.SaveEvent event) {
        propertyCategoryDeviceService.savePropertyCategoryDevice(event.getPropertyCategoryDevice());
        updateList();
        closeEditor();
    }

    private void deletePropertyCategoryDevice(PropertyCategoryDeviceForm.DeleteEvent event) {
        propertyCategoryDeviceService.deletePropertyCategoryDevice(event.getPropertyCategoryDevice());
        updateList();
        closeEditor();
    }
}
