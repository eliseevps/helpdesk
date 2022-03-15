package com.example.application.views.requests;

import com.example.application.data.AbstractEntity;
import com.example.application.data.entity.Requests;
import com.example.application.data.service.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "requests", layout = MainLayout.class)
@PageTitle("Обращения")
public class RequestsView extends VerticalLayout {
    Grid<Requests> grid = new Grid<>(Requests.class, false);
    TextField filterText = new TextField();
    RequestsForm form;
    RequestsService requestsService;
    CategoryService categoryService;
    PriorityService priorityService;
    StatusService statusService;
    UsersService usersService;
    Button addRequestsButton = new Button("Создать обращение");
    Dialog dialog = new Dialog();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm");

    public RequestsView(RequestsService requestsService,
                        CategoryService categoryService,
                        PriorityService priorityService,
                        StatusService statusService,
                        UsersService usersService) {
        this.requestsService = requestsService;
        this.categoryService = categoryService;
        this.priorityService = priorityService;
        this.statusService = statusService;
        this.usersService = usersService;
        Tabs tabs = getTabs();
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(tabs, getToolbar(), getContent());
        dialog.add(form);
        addRequestsButton.addClickListener(e-> dialog.open());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.setWidth("600px");
        //tabs.setSizeFull();
        tabs.getStyle().set("margin", "0");
        tabs.add(
                createTab("Общие", RequestsView.class ),
                createTab("Создано", RequestsView.class),
                createTab("В работе", RequestsView.class),
                createTab("Выполненные", RequestsView.class)
        );
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.setSelectedIndex(0);
        return tabs;
    }

    private Tab createTab(String viewName, Class<? extends Component> view) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        // Demo has no routes
        link.setRoute(view);
        link.setTabIndex(-1);
        return new Tab(link);
    }

    private void configureForm() {
        form = new RequestsForm(priorityService.findAllPrioritys(), categoryService.findAllCategorys(), statusService.findAllStatuses(), usersService.findAllRequesters(), usersService.findAllExecutors());
        form.setWidth("50em");
        form.addListener(RequestsForm.SaveEvent.class, this::saveRequests);
        form.addListener(RequestsForm.DeleteEvent.class, this::deleteRequests);
        form.addListener(RequestsForm.CloseEvent.class, e -> closeEditor());
    }

    private Span createStatusBadge(String status) {
        String theme;
        switch (status) {
            case "В работе":
                theme = "badge";
                break;
            case "Выполнено":
                theme = "badge success";
                break;
            case "Закрыто":
                theme = "badge error";
                break;
            default:
                theme = "badge contrast";
                break;
        }
        Span badge = new Span(status);
        badge.getElement().getThemeList().add(theme);
        return badge;
    }

    private Span createDateDue(LocalDateTime dateDue, LocalDateTime dateEnd) {
        String theme;
        LocalDateTime dateToday = LocalDateTime.now();

        if (dateEnd == null) {
            dateEnd = dateDue;
        }
        if (dateDue.isAfter(dateEnd) || dateToday.isBefore(dateDue)) {
            theme = "badge success";
        }
        else {
            theme = "badge error";
        }

        String date = dateDue.format(dtf);
        Span badge = new Span(date);
        badge.getElement().getThemeList().add(theme);
        return badge;
    }

    private void configureGrid() {

        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumnReorderingAllowed(true);
        grid.addColumn(AbstractEntity::getId).setHeader("ID").setSortable(true).setResizable(true);
        grid.addColumn(requests -> requests.getPriority().getName()).setHeader("Приоритет").setSortable(true).setResizable(true);
        grid.addColumn(requests -> requests.getCategory().getName()).setHeader("Категория").setSortable(true).setResizable(true);
        grid.addComponentColumn(requests -> createStatusBadge(requests.getStatus().getName())).setHeader("Статус").setSortable(true).setResizable(true);
        grid.addColumn(Requests::getNameRequest).setHeader("Наименование").setSortable(true).setResizable(true);
        grid.addColumn(requests -> requests.getDateStart().format(dtf)).setHeader("Дата обращения").setSortable(true).setResizable(true);
        grid.addComponentColumn(requests -> createDateDue(requests.getDateDue(), requests.getDateEnd())).setHeader("Срок выполнения").setSortable(true).setResizable(true);
        grid.addColumn(Requests::getDateEnd).setHeader("Дата закрытия").setSortable(true).setResizable(true);
        grid.addColumn(requests -> requests.getRequester().getName()).setHeader("Инициатор").setSortable(true).setResizable(true);
        grid.addColumn(requests -> requests.getExecutor().getName()).setHeader("Исполнитель").setSortable(true).setResizable(true);
        //grid.addColumn(requests -> requests.getDescriptionRequest()).setHeader("Описание").setFlexGrow(2).setResizable(true).setWidth("25em");
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editRequests(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Поиск");
        filterText.getElement().setAttribute("aria-label", "search");
        filterText.setClearButtonVisible(true);
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        addRequestsButton.addClickListener(click -> addRequests());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addRequestsButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editRequests(Requests requests) {
        if (requests == null) {
            closeEditor();
        } else {
            form.setRequests(requests);
            dialog.open();
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setRequests(null);
        dialog.close();
        removeClassName("editing");
    }

    private void addRequests() {
        grid.asSingleSelect().clear();
        editRequests(new Requests());
    }

    private void updateList() {grid.setItems(requestsService.findAllRequests(filterText.getValue())); }

    private void saveRequests(RequestsForm.SaveEvent event) {
        requestsService.saveRequests(event.getRequests());
        updateList();
        closeEditor();
    }

    private void deleteRequests(RequestsForm.DeleteEvent event) {
        requestsService.deleteRequests(event.getRequests());
        updateList();
        closeEditor();
    }
}
