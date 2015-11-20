package com.mjurik.web.views;

import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.services.ResultsService;
import com.mjurik.web.views.forms.CrawlerResultForm;
import com.mjurik.web.views.forms.NewCoinForm;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerUnprocessedTable extends CustomComponent implements CrawlerResultForm.ParentTable {

    TextField filter = new TextField();
    Grid crawlerResults = new Grid();

    Panel rightPanelHolder;
    CrawlerResultForm resultForm;

    ResultsService service = ResultsService.getInstance();

    public CrawlerUnprocessedTable() {
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
                -> {
            rightPanelHolder.setVisible(true);
            rightPanelHolder.setSizeUndefined();
            rightPanelHolder.setContent(resultForm);
            resultForm.edit((CrawlerResult) crawlerResults.getSelectedRow());
        });
    }

    private void buildLayout() {
        VerticalLayout left = new VerticalLayout(filter, crawlerResults);
        left.setSizeFull();
        crawlerResults.setSizeFull();
        left.setExpandRatio(crawlerResults, 1);

        rightPanelHolder = new Panel();
        rightPanelHolder.setContent(resultForm);

        HorizontalLayout mainLayout = new HorizontalLayout(left, rightPanelHolder);
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

    @Override
    public void displaySaveNewForm(CrawlerResult result) {
        unSelectResult();
        resultForm.setVisible(false);
        NewCoinForm newCoinForm = new NewCoinForm(this);
        newCoinForm.edit(result);
        rightPanelHolder.setContent(newCoinForm);
    }

    private void refreshResults(String stringFilter) {
        crawlerResults.setContainerDataSource(new BeanItemContainer<>(
                CrawlerResult.class, service.findAll(stringFilter)));
        if (rightPanelHolder.getContent() != resultForm) {
            rightPanelHolder.setContent(null);
        }
        rightPanelHolder.setVisible(false);
        resultForm.setVisible(false);
    }

}
