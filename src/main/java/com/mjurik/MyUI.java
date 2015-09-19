package com.mjurik;

import javax.servlet.annotation.WebServlet;

import com.mjurik.web.forms.CrawlerResult;
import com.mjurik.web.forms.CrawlerResultForm;
import com.mjurik.web.services.ResultsService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.mjurik.MyAppWidgetset")
public class MyUI extends UI {

    TextField filter = new TextField();
    Grid contactList = new Grid();

    CrawlerResultForm resultForm = new CrawlerResultForm();

    ResultsService service = ResultsService.getInstance();

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        filter.setInputPrompt("Filter results...");
        filter.addTextChangeListener(e -> refreshResults(e.getText()));

        contactList.setContainerDataSource(new BeanItemContainer<>(CrawlerResult.class));
        contactList.setColumnOrder("firstName", "lastName");
//        contactList.removeColumn("id");
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.addSelectionListener(e
                -> resultForm.edit((CrawlerResult) contactList.getSelectedRow()));
        refreshResults();
    }

    private void buildLayout() {
        VerticalLayout left = new VerticalLayout(filter, contactList);
        left.setSizeFull();
        contactList.setSizeFull();
        left.setExpandRatio(contactList, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, resultForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        // Split and allow resizing
        setContent(mainLayout);
    }

    void refreshResults() {
        refreshResults(filter.getValue());
    }

    private void refreshResults(String stringFilter) {
        contactList.setContainerDataSource(new BeanItemContainer<>(
                CrawlerResult.class, service.findAll(stringFilter)));
        resultForm.setVisible(false);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
