package com.example.application.views.dashboard;

import com.example.application.data.entity.Archive;
import com.example.application.data.entity.Shift;
import com.example.application.data.service.ArchiveService;
import com.example.application.data.service.ShiftService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
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
    private Grid<Archive> shiftPatients = new Grid<>(Archive.class,false);
    private Set<Archive> covidArchives = new HashSet<>();
    private Set<Archive> shiftArchives =new HashSet<>();
    DatePicker labelDatePicker = new DatePicker();

    public DashboardView(@Autowired ArchiveService archiveService, @Autowired ShiftService shiftService) {
        setId("dashboard-view");
        Board board = new Board();
        int n = shiftService.getAll().size();
        labelDatePicker.setLabel("Shift Date");
        Shift[] shiftToArray =new Shift[n];
        shiftToArray = shiftService.getAll().toArray(shiftToArray);
        for (Archive newArchive : archiveService.getAll()) {
            if (newArchive.getDiseases() != null) {
                if (newArchive.getDiseases().getName().equals("COVID"))
                    covidArchives.add(newArchive);
            }
            if (newArchive.getIn_date().equals(shiftToArray[shiftToArray.length-1].getDate())){
                shiftArchives.add(newArchive);
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

        shiftPatients.addColumn("patient.id").setAutoWidth(true).setHeader("Patient ID");
        shiftPatients.addColumn("symptoms").setAutoWidth(true);
        shiftPatients.addColumn(archive->(archive.getExams()!=null)?archive.getExams().getName():"").setAutoWidth(true).setHeader("Exam");
        shiftPatients.addColumn(archive->(archive.getDiseases()!=null)?archive.getDiseases().getName():"").setAutoWidth(true).setHeader("Disease");
        shiftPatients.addColumn(archive->(archive.getDrugs()!=null)?archive.getDrugsNames():"").setAutoWidth(true).setHeader("Drugs");
        shiftPatients.addColumn("long_disease").setAutoWidth(true).setHeader("Past Diseases");
        shiftPatients.addColumn("in_date").setAutoWidth(true);
        shiftPatients.addColumn("out_date").setAutoWidth(true);
        shiftPatients.setItems(shiftArchives);
        shiftPatients.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        WrapperCard ShiftGridWrapper = new WrapperCard("wrapper",new Component[]{new H3("Shift Visits : "+shiftToArray[shiftToArray.length-1].getDate()),shiftPatients},"card");
        WrapperCard CovidgridWrapper = new WrapperCard("wrapper", new Component[]{new H3("Covid Report"), covidGrid}, "card");

        add(board);
        board.addRow(CovidgridWrapper);
        board.add(covidGrid);
        board.addRow(ShiftGridWrapper);
        board.add(shiftPatients);
    }
}


