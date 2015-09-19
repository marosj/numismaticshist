package com.mjurik;

import javax.servlet.annotation.WebServlet;

import com.mjurik.web.forms.CrawlerResult;
import com.mjurik.web.forms.CrawlerResultForm;
import com.mjurik.web.services.ResultsService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Title("Numismatics History")
public class CrawlerResultsUI extends UI {

    TextField filter = new TextField();
    Grid crawlerResults = new Grid();

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

        crawlerResults.setContainerDataSource(new BeanItemContainer<>(CrawlerResult.class));
        crawlerResults.setColumnOrder("name", "price", "variant", "source");
        crawlerResults.removeColumn("id");
        crawlerResults.removeColumn("ean");
        crawlerResults.removeColumn("path");
        crawlerResults.setSelectionMode(Grid.SelectionMode.SINGLE);
        crawlerResults.addSelectionListener(e
                -> resultForm.edit((CrawlerResult) crawlerResults.getSelectedRow()));
        refreshResults();
    }

    private void buildLayout() {
        VerticalLayout left = new VerticalLayout(filter, crawlerResults);
        left.setSizeFull();
        crawlerResults.setSizeFull();
        left.setExpandRatio(crawlerResults, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, resultForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        // Split and allow resizing
        setContent(mainLayout);
    }

    public void unSelectResult() {
        crawlerResults.select(null);
    }

    public void refreshResults() {
        refreshResults(filter.getValue());
    }

    private void refreshResults(String stringFilter) {
        crawlerResults.setContainerDataSource(new BeanItemContainer<>(
                CrawlerResult.class, service.findAll(stringFilter)));
        resultForm.setVisible(false);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CrawlerResultsUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
