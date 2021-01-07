package com.example.application.views.clerks;

import java.util.Optional;

import com.example.application.data.entity.Clerk;
import com.example.application.data.service.ClerkService;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import com.example.application.views.main.MainView;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.component.textfield.TextField;
@Route(value = "clerks", layout = MainView.class)
@PageTitle("Clerks")
@CssImport("./styles/views/clerks/clerks-view.css")
public class ClerksView extends Div {

    private Grid<Clerk> grid = new Grid<>(Clerk.class, false);

    private TextField id;
    private TextField name;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Clerk> binder;

    private Clerk clerk;

    public ClerksView(@Autowired ClerkService clerkService) {
        setId("clerks-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.setDataProvider(new CrudServiceDataProvider<>(clerkService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Clerk> clerkFromBackend = clerkService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (clerkFromBackend.isPresent()) {
                    populateForm(clerkFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Clerk.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(id).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.clerk == null) {
                    this.clerk = new Clerk();
                }
                binder.writeBean(this.clerk);

                clerkService.update(this.clerk);
                clearForm();
                refreshGrid();
                Notification.show("Clerk details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the clerk details.");
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
        Component[] fields = new Component[]{id, name};

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

    private void populateForm(Clerk value) {
        this.clerk = value;
        binder.readBean(this.clerk);

    }
}
