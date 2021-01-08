package com.example.application.views.patients;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Optional;
@Route(value = "archive", layout = MainView.class)
@PageTitle("Archive")
@CssImport("./styles/views/patients/patients-view.css")
public class PatientArchiveView extends Div implements HasUrlParameter<String> {
    private Grid<Archive> archiveGrid = new Grid<>(Archive.class, false);
    private ComboBox<Exams> exams;
    private ComboBox<Diseases> diseases;
    private MultiselectComboBox<Drug> drugs;
    private TextField symptom;
    private TextField in_date;
    private TextField out_date;
    private TextField long_disease;
    private ComboBox<Patient> patientID;
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private PatientService patientService;
    private BeanValidationBinder<Archive> binder;

    private Archive archive;

    public PatientArchiveView(@Autowired ArchiveService archiveService , @Autowired ExamsService  examsService, @Autowired DiseaseService diseaseService, @Autowired PatientService patientService, @Autowired DrugService drugService){
        setId("archive-view");
        // Create UI
        this.patientService=patientService;
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout,examsService,diseaseService,patientService,drugService);
        add(splitLayout);
        // Configure ArchiveGrid
        archiveGrid.addColumn("id").setAutoWidth(true);
        archiveGrid.addColumn("patient.id").setAutoWidth(true).setHeader("Patient ID");
        archiveGrid.addColumn("symptoms").setAutoWidth(true);
        archiveGrid.addColumn("long_disease").setHeader("Past Diseases").setAutoWidth(true);
        archiveGrid.addColumn(archive->(archive.getExams()!=null)?archive.getExams().getName():"").setAutoWidth(true).setHeader("Exam");
        archiveGrid.addColumn(archive->(archive.getDiseases()!=null)?archive.getDiseases().getName():"").setAutoWidth(true).setHeader("Disease");
        archiveGrid.addColumn(archive->(archive.getDrugs()!=null)?archive.getDrugsNames():"").setAutoWidth(true).setHeader("Drugs");
        archiveGrid.addColumn("in_date").setAutoWidth(true);
        archiveGrid.addColumn("out_date").setAutoWidth(true);
        archiveGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        archiveGrid.setHeightFull();


        // when a row is selected or deselected, populate form
        archiveGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Archive> archiveFromBackend = archiveService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (archiveFromBackend.isPresent()) {
                    populateForm(archiveFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Archive.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(diseases).bind("diseases");
        binder.forField(exams).bind("exams");
        binder.forField(patientID).bind("patient");
        binder.forField(symptom).bind("symptoms");
        binder.forField(in_date).bind("in_date");
        binder.forField(out_date).bind("out_date");
        binder.forField(drugs).bind("drugs");
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.archive == null) {
                    this.archive = new Archive();
                }
                binder.writeBean(this.archive);
                archiveService.update(this.archive);
                clearForm();
                refreshGrid();
                Notification.show("Patient details stored.");
                UI.getCurrent().getPage().reload();
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the patient details.");
            }
        });

    }

    private void createEditorLayout(SplitLayout splitLayout,ExamsService examsService,DiseaseService diseaseService,PatientService patientService,DrugService drugService) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        in_date = new TextField("In Date");
        out_date = new TextField("Out Date");
        symptom=new TextField("Symptoms");
        exams=new ComboBox<>("Exams");
        diseases=new ComboBox<>("Diseases");
        drugs=new MultiselectComboBox<>("Drugs");
        long_disease=new TextField("Past Diseases");
        exams.setItemLabelGenerator(Exams::getName);
        exams.setItems(examsService.getAll());
        diseases.setItemLabelGenerator(Diseases::getName);
        diseases.setItems(diseaseService.getAll());
        patientID=new ComboBox<>("Patient ID");
        patientID.setItemLabelGenerator(Patient::getStringID);
        drugs.setItemLabelGenerator(Drug::getName);
        drugs.setItems(drugService.getAll());
        Component[] fields = new Component[]{patientID, symptom,long_disease,exams,diseases,drugs,in_date, out_date};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(archiveGrid);
    }

    private void refreshGrid() {
        archiveGrid.select(null);
        archiveGrid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Archive value) {
        this.archive = value;
        binder.readBean(this.archive);

    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        Integer i = Integer.parseInt(parameter);
        if (this.patientService.get(i).isPresent()) {
            archiveGrid.setItems(this.patientService.get(i).get().getArchives());
            patientID.setItems(patientService.get(i).get());

        }
    }
}
