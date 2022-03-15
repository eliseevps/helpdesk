package com.example.application.views.reports.device;

import com.example.application.data.service.DeviceService;
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

@Route(value = "reports/device", layout = MainReportsView.class)
@PageTitle("Отчеты | Оборудование")
public class ReportsDeviceView extends VerticalLayout {
    DeviceService deviceService;


    public ReportsDeviceView(DeviceService deviceService) {
        this.deviceService = deviceService;
        addClassName("reports-view");
        add(getVariantReport(), getGiagram());
    }

    private VerticalLayout getVariantReport() {
        ComboBox<String> usersBox = new ComboBox<>("Выберите сотрудника");
        usersBox.setAllowCustomValue(true);
        usersBox.setItems("Елисеев Павел", "Кирьянов Максим", "Панов Дмитрий");
        usersBox.setHelperText("По умолчанию все оборудование");
        HorizontalLayout variantBox = new HorizontalLayout(usersBox);

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
        series.add(new DataSeriesItem("Монитор: 50", 50, 0));
        series.add(new DataSeriesItem("Компьютер: 40", 40 , 1));
        series.add(new DataSeriesItem("Принтер: 10", 10, 2));
        series.add(new DataSeriesItem("Сервер: 5", 5, 3));
        conf.addSeries(series);
        chart.setWidth("600px");
        return chart;
    }

}