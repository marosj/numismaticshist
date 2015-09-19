package com.mjurik.web.views;

import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.views.forms.CrawlerResultForm;
import com.mjurik.web.services.ResultsService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerResultsTable extends CustomComponent implements CrawlerResultForm.ParentTable {

    TextField filter = new TextField();
    Grid crawlerResults = new Grid();

    CrawlerResultForm resultForm;

    ResultsService service = ResultsService.getInstance();

    public CrawlerResultsTable() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        resultForm = new CrawlerResultForm(this);

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

        setCompositionRoot(mainLayout);
    }

    @Override
    public void unSelectResult() {
        crawlerResults.select(null);
    }

    @Override
    public void refreshResults() {
        refreshResults(filter.getValue());
    }

    private void refreshResults(String stringFilter) {
        crawlerResults.setContainerDataSource(new BeanItemContainer<>(
                CrawlerResult.class, service.findAll(stringFilter)));
        resultForm.setVisible(false);
    }

}
