package com.example.application.views.archive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.application.data.entity.Archive;
import com.example.application.data.entity.Diseases;
import com.example.application.data.entity.Exams;
import com.example.application.data.service.ArchiveService;
import com.example.application.data.service.DiseaseService;
import com.example.application.data.service.ExamsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import com.example.application.views.main.MainView;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.component.textfield.TextField;

@Route(value = "archive", layout = MainView.class)
@PageTitle("Archive")
@CssImport("./styles/views/patients/patients-view.css")
public class ArchiveView extends Div {
    private Grid<Archive> archiveGrid = new Grid<>(Archive.class, false);
    private List<String> name=new ArrayList<>();
    private TextField id;
    private ComboBox<String> exams;
    private ComboBox<String> diseases;
    private TextField symptom;
    private TextField in_date;
    private TextField out_date;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Archive> binder;

    private Archive archive;

    public ArchiveView(@Autowired ArchiveService archiveService, @Autowired ExamsService  examsService, @Autowired DiseaseService diseaseService){
        setId("archive-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout,examsService,diseaseService);
        add(splitLayout);
        // Configure ArchiveGrid
        archiveGrid.addColumn("id").setAutoWidth(true);
        archiveGrid.addColumn("patient.id").setAutoWidth(true).setHeader("Patient ID");
        archiveGrid.addColumn("symptoms").setAutoWidth(true);
       // archiveGrid.addColumn("exams").setAutoWidth(true);
        archiveGrid.addColumn("diseases.id").setAutoWidth(true).setHeader("Disease");
       // archiveGrid.addColumn("drugs").setAutoWidth(true);
        archiveGrid.addColumn("in_date").setAutoWidth(true);
        archiveGrid.addColumn("out_date").setAutoWidth(true);
        archiveGrid.setDataProvider(new CrudServiceDataProvider<>(archiveService));
        archiveGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
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
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
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
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the patient details.");
            }
        });

    }

    private void createEditorLayout(SplitLayout splitLayout,ExamsService examsService,DiseaseService diseaseService) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id = new TextField("Id");
       // symptom = new TextField("Symptom");
        in_date = new TextField("In Date");
        out_date = new TextField("Out Date");
        symptom=new TextField("Symptoms");
        exams=new ComboBox<>("Exams");
        diseases=new ComboBox<>("Diseases");
        for (int i = 0; i <= examsService.count(); i++) {
            if (examsService.get(i).isPresent())
                if (examsService.get(i).get().getName() != null) {
                    name.add(examsService.get(i).get().getName());
                }
        }
        exams.setItems(name.stream());
        name.clear();
        for (int i = 0; i <=diseaseService.count() ; i++) {
            if (diseaseService.get(i).isPresent())
                if (diseaseService.get(i).get().getName() != null) {
                    name.add(diseaseService.get(i).get().getName());
                }
        }
        diseases.setItems(name.stream());
        name.clear();
        Component[] fields = new Component[]{id, symptom,exams,diseases,in_date, out_date};

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
}
