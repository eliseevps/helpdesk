package com.example.application.views.reports.users;

import com.example.application.data.service.UsersService;
import com.example.application.views.reports.MainReportsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "reports/users", layout = MainReportsView.class)
@PageTitle("Отчеты | Пользователи")
public class ReportsUsersView extends VerticalLayout {
    UsersService usersService;

    public ReportsUsersView(UsersService usersService) {
        this.usersService = usersService;
        addClassName("reports-view");
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getVariantReport(), getGiagram());
    }

    private VerticalLayout getVariantReport() {
        ComboBox<String> itBox = new ComboBox<>("Выберите отдел");
        itBox.setAllowCustomValue(true);
        itBox.setItems("ИТ", "АХС", "ФРО");
        itBox.setHelperText("По умолчанию все отделы");
        HorizontalLayout variantBox = new HorizontalLayout(itBox);

        Button reportButton = new Button("Сформировать");
        reportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button downloadButton = new Button("Выгрузить в Excel");
        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout buttons = new HorizontalLayout(reportButton, downloadButton);
        return new VerticalLayout(variantBox, buttons);
    }


    private Chart getGiagram() {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Уволены: 5", 5, 0));
        series.add(new DataSeriesItem("Работают: 40", 40, 1 ));
        conf.addSeries(series);
        chart.setWidth("600px");
        return chart;
    }

}