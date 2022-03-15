package com.example.application.views.users;

import com.example.application.data.entity.Users;
import com.example.application.data.service.DepartmentService;
import com.example.application.data.service.LocationService;
import com.example.application.data.service.UsersService;
import com.example.application.views.MainLayout;
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

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Пользователи")
public class UsersView extends VerticalLayout {
    Grid<Users> grid = new Grid<>();
    TextField filterText = new TextField();
    UsersForm form;
    DepartmentService departmentService;
    LocationService locationService;
    UsersService usersService;
    Button addUsersButton = new Button("Добавить пользователя");
    Dialog dialog = new Dialog();

    public UsersView(UsersService usersService, DepartmentService departmentService, LocationService locationService) {
        this.usersService = usersService;
        this.departmentService = departmentService;
        this.locationService = locationService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        dialog.add(form);
        addUsersButton.addClickListener(e -> dialog.open());
        updateList();
        closeEditor();
    }


    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new UsersForm(departmentService.findAllDepartment(""), locationService.findAllLocation(""));
        form.setWidth("50em");
        form.addListener(UsersForm.SaveEvent.class, this::saveUsers);
        form.addListener(UsersForm.DeleteEvent.class, this::deleteUsers);
        form.addListener(UsersForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setAllRowsVisible(true);
        //grid.addColumn(users -> users.getId()).setHeader("ID").setSortable(true);
        grid.addColumn(Users::getLastName).setHeader("Фамилия").setSortable(true);
        grid.addColumn(Users::getFirstName).setHeader("Имя").setSortable(true);
        grid.addColumn(Users::getRoleSystem).setHeader("Роль в системе").setSortable(true);
        grid.addColumn(Users::getLoginUser).setHeader("Логин").setSortable(true);
        grid.addColumn(Users::getEmail).setHeader("Почта").setSortable(true);
        grid.addColumn(Users::getPhone).setHeader("Телефон").setSortable(true);
        grid.addColumn(users -> users.getDepartment().getName()).setHeader("Отдел").setSortable(true);
        grid.addColumn(Users::getPost).setHeader("Должность").setSortable(true);
        grid.addColumn(users -> users.getLocation().getFullName()).setHeader("Местоположение").setSortable(true);
        TemplateRenderer<Users> statusRenderer = TemplateRenderer.<Users>of(
                        "<iron-icon hidden='[[!item.status]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.status]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("status", Users::isStatus);
        grid.addColumn(statusRenderer).setHeader("Работает").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event ->
                editUsers(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addUsersButton.addClickListener(click -> addUsers());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addUsersButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editUsers(Users users) {
        if (users == null) {
            closeEditor();
        } else {
            form.setUsers(users);
            dialog.open();
            addUsersButton.setEnabled(false);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUsers(null);
        dialog.close();
        addUsersButton.setEnabled(true);
        removeClassName("editing");
    }

    private void addUsers() {
        grid.asSingleSelect().clear();
        editUsers(new Users());
    }

    private void updateList() {grid.setItems(usersService.findAllUsers(filterText.getValue())); }

    private void saveUsers(UsersForm.SaveEvent event) {
        usersService.saveUsers(event.getUsers());
        updateList();
        closeEditor();
    }

    private void deleteUsers(UsersForm.DeleteEvent event) {
        usersService.deleteUsers(event.getUsers());
        updateList();
        closeEditor();
    }
}
