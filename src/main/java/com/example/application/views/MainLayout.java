package com.example.application.views;

import com.example.application.views.device.listdevise.DeviceView;
import com.example.application.views.reports.MainReportsView;
import com.example.application.views.requests.RequestsView;
import com.example.application.views.settings.MainSettingsView;
import com.example.application.views.users.UsersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Help Desk", shortName = "Help Desk", enableInstallPrompt = false)
@Theme(themeFolder = "myapp", variant = Lumo.LIGHT)
public class MainLayout extends AppLayout implements RouterLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private static RouterLink createLink(MenuItemInfo menuItemInfo) {
        RouterLink link = new RouterLink();
        link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
        link.setRoute(menuItemInfo.getView());

        Span text = new Span(menuItemInfo.getText());
        text.addClassNames("font-medium", "text-m");

        link.add(text);
        return link;
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        return header;
    }

    private Component createDrawerContent() {
        Image img = new Image("images/solid.png", "placeholder plant");
        img.setWidth("60px");
        img.addClassNames("h-x1", "m-10", "px-m", "text-xl");
        H1 appName = new H1("ИС СП ИТ");
        appName.addComponentAsFirst(img);
        appName.addClassNames("flex", "items-center", "h-x1", "m-3", "px-m", "text-xl");
        //Div header = new Div(img, appName);
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        for (RouterLink link : createLinks()) {
            ListItem item = new ListItem(link);
            list.add(item);
        }
        return nav;
    }

    private List<RouterLink> createLinks() {
        MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
                new MenuItemInfo("Обращения", RequestsView.class), //

                new MenuItemInfo("Пользователи", UsersView.class), //

                new MenuItemInfo("Оборудование", DeviceView.class), //

                new MenuItemInfo("Отчеты", MainReportsView.class), //

                new MenuItemInfo("Настройки", MainSettingsView.class), //

                new MenuItemInfo("Выйти", DeviceView.class),  //

        };
        List<RouterLink> links = new ArrayList<>();
        for (MenuItemInfo menuItemInfo : menuItems) {
            links.add(createLink(menuItemInfo));

        }
        return links;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    public static class MenuItemInfo {

        private String text;
        private Class<? extends Component> view;

        public MenuItemInfo(String text, Class<? extends Component> view) {
            this.text = text;
            this.view = view;
        }

        public String getText() {
            return text;
        }


        public Class<? extends Component> getView() {
            return view;
        }

    }
}
