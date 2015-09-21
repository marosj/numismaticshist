package com.mjurik;

import com.mjurik.web.views.CoinsTable;
import com.mjurik.web.views.CrawlerResultsTable;
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

    Button crawler = new Button("Crawler", this::displayCrawler);
    Button coins = new Button("Coins", this::displayCoins);

    Panel content = new Panel();
    CrawlerResultsTable resultsTable = new CrawlerResultsTable();
    CoinsTable coinsTable = new CoinsTable();


    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

    }

    public void displayCrawler(Button.ClickEvent event) {
        content.setContent(resultsTable);
        resultsTable.refreshResults();
    }

    public void displayCoins(Button.ClickEvent event) {
        content.setContent(coinsTable);
        coinsTable.refreshCoins();
    }

    private void buildLayout() {

        HorizontalLayout actions = new HorizontalLayout(crawler, coins);
        actions.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(actions, content);
        mainLayout.setSizeFull();
        content.setSizeFull();
        mainLayout.setExpandRatio(content, 1);

        displayCrawler(null);

        setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CrawlerResultsUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
