package com.example.application.views.reports;

import com.example.application.views.MainLayout;
import com.example.application.views.reports.device.ReportsDeviceView;
import com.example.application.views.reports.requests.ReportsRequestsView;
import com.example.application.views.reports.users.ReportsUsersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;

@ParentLayout(MainLayout.class)
@Route(value = "reports", layout = MainLayout.class)
@PageTitle("Отчеты")
public class MainReportsView extends FormLayout implements RouterLayout {

    private final VerticalLayout content;

    public MainReportsView() {
        Tabs tabs = getTabs();
        content = new VerticalLayout();
        //content.setSpacing(false);
        add(tabs, content);
    }


    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.setSizeFull();
        tabs.getStyle().set("margin", "auto");
        tabs.add(
                createTab("Обращения", ReportsRequestsView.class ),
                createTab("Пользователи", ReportsUsersView.class),
                createTab("Оборудование", ReportsDeviceView.class)
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
}