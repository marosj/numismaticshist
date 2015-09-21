package com.mjurik.web.views;

import com.mjurik.web.crawler.db.entity.Coin;
import com.mjurik.web.services.CoinsService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CoinsTable extends CustomComponent {

    TextField filter = new TextField();
    Grid coinsGrid = new Grid();

    FormLayout resultForm;

    public CoinsTable() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        resultForm = new FormLayout();

        filter.setInputPrompt("Filter results...");
        filter.addTextChangeListener(e -> refreshCoins(e.getText()));

        coinsGrid.setContainerDataSource(new BeanItemContainer<>(Coin.class));
        coinsGrid.setColumnOrder("name", "nominal", "description");
        coinsGrid.removeColumn("id");
        coinsGrid.removeColumn("variants");
        coinsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        coinsGrid.addSelectionListener(e
//                -> resultForm.edit((CrawlerResult) coinsGrid.getSelectedRow()));
        refreshCoins();
    }

    private void buildLayout() {
        VerticalLayout left = new VerticalLayout(filter, coinsGrid);
        left.setSizeFull();
        coinsGrid.setSizeFull();
        left.setExpandRatio(coinsGrid, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, resultForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(left, 1);

        setCompositionRoot(mainLayout);
    }

    public void refreshCoins() {
        refreshCoins(filter.getValue());
    }

    private void refreshCoins(String stringFilter) {
        coinsGrid.setContainerDataSource(new BeanItemContainer<>(
                Coin.class, CoinsService.INSTANCE.findAll(stringFilter)));
        resultForm.setVisible(false);
    }

}
