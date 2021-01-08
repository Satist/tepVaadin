package com.example.application.views.patients;

import com.example.application.data.entity.Patient;
import com.example.application.data.service.PatientService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.util.Optional;


@Route(value = "patients", layout = MainView.class)
@PageTitle("Patients")
@CssImport("./styles/views/patients/patients-view.css")
public class PatientsView extends Div {

    private Grid<Patient> grid = new Grid<>(Patient.class, false);
    private TextField id;
    private TextField name;
    private TextField address;
    private TextField insurance;
    private TextField amka;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button archive=new Button("Archive");

    private BeanValidationBinder<Patient> binder;

    private Patient patient;
    public PatientsView(@Autowired PatientService patientService) {
        setId("patients-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);
        grid.addColumn("insurance").setAutoWidth(true);
        grid.addColumn("amka").setAutoWidth(true);
        grid.setDataProvider(new CrudServiceDataProvider<>(patientService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Patient> patientFromBackend = patientService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (patientFromBackend.isPresent()) {
                    populateForm(patientFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Patient.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
        binder.forField(amka).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("amka");

        binder.bindInstanceFields(this);

        archive.addClickListener(e->{
            archive.getUI().ifPresent(ui -> ui.navigate(PatientArchiveView.class,id.getValue()));
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.patient == null) {
                    this.patient = new Patient();
                }
                binder.writeBean(this.patient);
                patientService.update(this.patient);
                clearForm();
                refreshGrid();
                Notification.show("Patient details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the patient details.");
            }
        });

    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id = new TextField("Id");
        name = new TextField("Name");
        address = new TextField("Address");
        insurance = new TextField("Insurance");
        amka = new TextField("Amka");
        Component[] fields = new Component[]{id, name, address, insurance, amka};

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
        buttonLayout.add(save, cancel,archive);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Patient value) {
        this.patient = value;
        binder.readBean(this.patient);

    }
}
