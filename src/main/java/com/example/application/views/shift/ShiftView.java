package com.example.application.views.shift;

import com.example.application.data.entity.Clerk;
import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Nurse;
import com.example.application.data.entity.Shift;
import com.example.application.data.service.ClerkService;
import com.example.application.data.service.DoctorService;
import com.example.application.data.service.NurseService;
import com.example.application.data.service.ShiftService;
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
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Optional;

@Route(value = "shift", layout = MainView.class)
@PageTitle("Shifts")
@CssImport("./styles/views/nurses/nurses-view.css")
public class ShiftView extends Div{
    private Grid<Shift> grid = new Grid<>(Shift.class,false);

    private TextField id;
    private TextField date;
    private MultiselectComboBox<Doctor> doctors;
    private MultiselectComboBox<Nurse> nurses;
    private MultiselectComboBox<Clerk> clerks;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Shift> binder;

    private Shift shift;

    public ShiftView(@Autowired ShiftService shiftService, @Autowired DoctorService doctorService, @Autowired NurseService nurseService, @Autowired ClerkService clerkService){
        setId("shift-view");
        //Create UI
        SplitLayout splitLayout =new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout,doctorService,nurseService,clerkService);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("date").setAutoWidth(true);
        grid.addColumn(shift->(shift.getDoctors()!=null)?shift.getDoctorsID():"").setAutoWidth(true).setHeader("Doctor ID");
        grid.addColumn(shift->(shift.getNursesID()!=null)?shift.getNursesID():"").setAutoWidth(true).setHeader("Nurse ID");
        grid.addColumn(shift->(shift.getNurses()!=null)?shift.getNursesID():"").setAutoWidth(true).setHeader("Clerk ID");
        grid.setDataProvider(new CrudServiceDataProvider<>(shiftService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Shift> shiftFromBackend = shiftService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (shiftFromBackend.isPresent()) {
                    populateForm(shiftFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });


        // Configure Form
        binder = new BeanValidationBinder<>(Shift.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.shift == null) {
                    this.shift = new Shift();
                }
                binder.writeBean(this.shift);

                shiftService.update(this.shift);
                clearForm();
                refreshGrid();
                Notification.show("Shift details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the shift details.");
            }
        });
    }

    private void createEditorLayout(SplitLayout splitLayout,DoctorService doctorService,NurseService nurseService,ClerkService clerkService) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id = new TextField("Id");
        date = new TextField("Date");
        doctors = new MultiselectComboBox<>("Doctor ID");
        nurses = new MultiselectComboBox<>("Nurse ID");
        clerks = new MultiselectComboBox<>("Clerk ID");
        doctors.setItemLabelGenerator(Doctor::getStringID);
        doctors.setItems(doctorService.getAll());
        nurses.setItemLabelGenerator(Nurse::getStringID);
        nurses.setItems(nurseService.getAll());
        clerks.setItemLabelGenerator(Clerk::getStringID);
        clerks.setItems(clerkService.getAll());
        Component[] fields = new Component[]{id, date,doctors,nurses,clerks};

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
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Shift value) {
        this.shift = value;
        binder.readBean(this.shift);

    }

}
