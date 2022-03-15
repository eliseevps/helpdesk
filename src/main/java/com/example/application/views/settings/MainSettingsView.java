package com.example.application.views.settings;


import com.example.application.views.MainLayout;
import com.example.application.views.settings.category.CategoryView;
import com.example.application.views.settings.department.DepartmentView;
import com.example.application.views.settings.location.LocationView;
import com.example.application.views.settings.priority.PriorityView;
import com.example.application.views.settings.property.PropertyView;
import com.example.application.views.settings.status.StatusView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;

@ParentLayout(MainLayout.class)
@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Настройки")
public class MainSettingsView extends FormLayout implements RouterLayout {

    private final VerticalLayout content;

    public MainSettingsView() {
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
                createTab("Приоритеты", PriorityView.class ),
                createTab("Категории", CategoryView.class),
                createTab("Статусы", StatusView.class),
                createTab("Отделы", DepartmentView.class),
                createTab("Местоположения", LocationView.class),
                createTab("Атрибуты оборудований", PropertyView.class)
        );
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.setSelectedIndex(-1);
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
