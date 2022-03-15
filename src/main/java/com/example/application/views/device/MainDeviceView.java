package com.example.application.views.device;

import com.example.application.views.MainLayout;
import com.example.application.views.device.categorydevice.CategoryDeviceView;
import com.example.application.views.device.listdevise.DeviceView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;

@ParentLayout(MainLayout.class)
@Route(value = "device", layout = MainLayout.class)
@PageTitle("Оборудование")
public class MainDeviceView extends FormLayout implements RouterLayout {

    private final VerticalLayout content;

    public MainDeviceView() {
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
                createTab("Оборудование", DeviceView.class ),
                createTab("Категории оборудования", CategoryDeviceView.class)
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
