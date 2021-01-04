package com.example.application.views.drugs;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.application.data.entity.Doctor;
import com.example.application.data.entity.Drug;
import com.example.application.data.service.DoctorService;
import com.example.application.data.service.DrugService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import com.example.application.views.main.MainView;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.component.textfield.TextField;

@Route(value = "drugs", layout = MainView.class)
@PageTitle("Drugs")
@CssImport("./styles/views/doctors/doctors-view.css")
public class DrugsView extends Div {

    private Grid<Drug> grid = new Grid<>(Drug.class, false);

    private TextField id;
    private TextField name;
    private TextField mg;
    private TextField disease;
    private ComboBox<String> type;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Drug> binder;

    private Drug drug;

    public DrugsView(@Autowired DrugService drugService) {
        setId("drugs-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout,drugService);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("mg").setAutoWidth(true);
        grid.addColumn("disease").setAutoWidth(true);
        grid.setDataProvider(new CrudServiceDataProvider<>(drugService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Drug> drugFromBackend = drugService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (drugFromBackend.isPresent()) {
                    populateForm(drugFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Drug.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");
        binder.forField(type).bind("type");
        binder.forField(mg).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("mg");
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.drug == null) {
                    this.drug = new Drug();
                }
                binder.writeBean(this.drug);

                drugService.update(this.drug);
                clearForm();
                refreshGrid();
                Notification.show("Drug details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the drug details.");
            }
        });

    }

    private void createEditorLayout(SplitLayout splitLayout,DrugService drugService) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id = new TextField("Id");
        name = new TextField("Name");
        type = new ComboBox<>("Type");
        disease=new TextField("Disease");
        mg=new TextField("Mg");
        List<String> spec= Arrays.asList("Pill", "Capsule", "Injection", "Inhaler", "Liquid", "Topical");
        type.setItems(spec);
        Component[] fields = new Component[]{id, name, type,mg,disease};

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

    private void populateForm(Drug value) {
        this.drug = value;
        binder.readBean(this.drug);

    }
}
