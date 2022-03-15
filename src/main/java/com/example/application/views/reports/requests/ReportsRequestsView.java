package com.example.application.views.reports.requests;

import com.example.application.data.service.RequestsService;
import com.example.application.views.reports.MainReportsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "reports/requests", layout = MainReportsView.class)
@PageTitle("Отчеты | Обращения")
public class ReportsRequestsView extends VerticalLayout {

    private DatePicker start;
    private DatePicker end;
    RequestsService requestsService;

    public  ReportsRequestsView(RequestsService requestsService) {
        this.requestsService = requestsService;
        addClassName("reports-view");
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getVariantReport(), getGiagram());
    }

    private VerticalLayout getVariantReport() {
        ComboBox<String> departmentBox = new ComboBox<>("Выберите сотрудника");
        departmentBox.setAllowCustomValue(true);
        departmentBox.setItems("Елисеев Павел", "Кирьянов Максим", "Панов Дмитрий");
        departmentBox.setHelperText("По умолчанию весь отдел ИТ");
        HorizontalLayout variantBox = new HorizontalLayout(departmentBox);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Выберите период");
        radioGroup.setItems("1 день", "7 дней", "30 дней");
        HorizontalLayout variant = new HorizontalLayout(radioGroup);
        start = new DatePicker();
        start.setPlaceholder("Дата начала");
        end = new DatePicker();
        end.setPlaceholder("Дата окончания");
        // aria-label for screen readers
        start.getElement()
                .executeJs("const start = this.shadowRoot.querySelector('[part=\"text-field\"]').shadowRoot.querySelector('[part=\"value\"]');" +
                        "start.setAttribute('aria-label', 'Дата начала');" +
                        "start.removeAttribute('aria-labelledby');"
                );
        end.getElement()
                .executeJs("const end = this.shadowRoot.querySelector('[part=\"text-field\"]').shadowRoot.querySelector('[part=\"value\"]');" +
                        "end.setAttribute('aria-label', 'Дата окончания');" +
                        "end.removeAttribute('aria-labelledby');"
                );
        HorizontalLayout dateVariant = new HorizontalLayout(start, end);

        Button reportButton = new Button("Сформировать");
        reportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button downloadButton = new Button("Выгрузить в Excel");
        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout buttons = new HorizontalLayout(reportButton, downloadButton);
        return new VerticalLayout(variantBox, variant, dateVariant, buttons);
    }


    private Chart getGiagram() {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();

        DataSeries series = new DataSeries();
        //series.add(new DataSeriesItem("В работе", 10));
        //series.add(new DataSeriesItem("Выполнено в срок: 50", 50, 1 ));
        series.add(new DataSeriesItem("Просрочены: 10", 10, 0));
        series.add(new DataSeriesItem("Выполнено в срок: 50", 50, 1 ));
        conf.addSeries(series);
        chart.setWidth("600px");
        return chart;
    }

}