package com.example.application.views.about;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@RouteAlias(value = "", layout = MainView.class)
public class AboutView extends Div {

    public AboutView() {
        setId("about-view");
        add(new Text("Ioannis Lamprou/CSD3976"));
    }

}
