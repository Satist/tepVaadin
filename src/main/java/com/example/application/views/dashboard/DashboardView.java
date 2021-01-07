package com.example.application.views.dashboard;

import com.example.application.data.entity.Archive;
import com.example.application.data.service.ArchiveService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboard")
@CssImport(value = "./styles/views/dashboard/dashboard-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class DashboardView extends Div {

    private Grid<Archive> covidGrid = new Grid<>(Archive.class,false);

    private Chart monthlyVisitors = new Chart();
    private Chart responseTimes = new Chart();
    private final H2 usersH2 = new H2();
    private final H2 eventsH2 = new H2();
    private final H2 conversionH2 = new H2();
    private Set<Archive> covidArchives = new HashSet<>();

    public DashboardView(@Autowired ArchiveService archiveService) {
        setId("dashboard-view");
        Board board = new Board();

        for (Archive newArchive : archiveService.getAll()) {
            if (newArchive.getDiseases() != null) {
                if (newArchive.getDiseases().getName().equals("COVID"))
                    covidArchives.add(newArchive);
            }
        }
        covidGrid.addColumn("patient.id").setAutoWidth(true).setHeader("Patient ID");
        covidGrid.addColumn("symptoms").setAutoWidth(true);
        covidGrid.addColumn(archive->(archive.getExams()!=null)?archive.getExams().getName():"").setAutoWidth(true).setHeader("Exam");
        covidGrid.addColumn(archive->(archive.getDiseases()!=null)?archive.getDiseases().getName():"").setAutoWidth(true).setHeader("Disease");
        covidGrid.addColumn(archive->(archive.getDrugs()!=null)?archive.getDrugsNames():"").setAutoWidth(true).setHeader("Drugs");
        covidGrid.addColumn("long_disease").setAutoWidth(true).setHeader("Past Diseases");
        covidGrid.addColumn("in_date").setAutoWidth(true);
        covidGrid.addColumn("out_date").setAutoWidth(true);
        covidGrid.setItems(covidArchives);
        covidGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        responseTimes.getConfiguration().setTitle("Response times");
        WrapperCard gridWrapper = new WrapperCard("wrapper", new Component[]{new H3("Covid Report"), covidGrid}, "card");

        add(board);
        board.addRow(gridWrapper);
        board.add(covidGrid);
    }
}


