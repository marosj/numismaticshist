package com.mjurik;

import com.mjurik.web.views.CoinsTable;
import com.mjurik.web.views.CrawlerUnprocessedTable;
import com.mjurik.web.views.CrawlerUnprocessedWithMatchTable;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Theme("valo")
@Title("Numismatics History")
public class CrawlerResultsUI extends UI {

    Button allUnprocessedBtn = new Button("All unprocessed", this::displayAllUnprocessed);
    Button unprocessedWithMatchBtn = new Button("Unprocessed with match", this::displayUnprocessedWithMatch);
    Button coinsBtn = new Button("Coins", this::displayCoins);

    Panel content = new Panel();
    CrawlerUnprocessedTable unprocessedTable = new CrawlerUnprocessedTable();
    CrawlerUnprocessedWithMatchTable unprocessedWithMatchTable = new CrawlerUnprocessedWithMatchTable();
    CoinsTable coinsTable = new CoinsTable();


    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

    }

    public void displayAllUnprocessed(Button.ClickEvent event) {
        content.setContent(unprocessedTable);
        unprocessedTable.refreshResults();
    }

    public void displayUnprocessedWithMatch(Button.ClickEvent event) {
        content.setContent(unprocessedWithMatchTable);
        unprocessedWithMatchTable.refreshResults();
    }

    public void displayCoins(Button.ClickEvent event) {
        content.setContent(coinsTable);
        coinsTable.refreshCoins();
    }

    private void buildLayout() {

        HorizontalLayout actions = new HorizontalLayout(unprocessedWithMatchBtn, allUnprocessedBtn, coinsBtn);
        actions.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(actions, content);
        mainLayout.setSizeFull();
        content.setSizeFull();
        mainLayout.setExpandRatio(content, 1);

        displayUnprocessedWithMatch(null);

        setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CrawlerResultsUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
