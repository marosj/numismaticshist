package com.mjurik.web.views.forms;

import com.mjurik.web.crawler.Money;
import com.mjurik.web.crawler.PriceUtils;
import com.mjurik.web.crawler.db.entity.*;
import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.services.ResultsService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Marian Jurik on 21.9.2015.
 */
public class NewCoinForm extends FormLayout {

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);

    TextField name = new TextField("Name");
    TextField nominal = new TextField("Nominal");
    TextField description = new TextField("Description");

    BeanFieldGroup<Coin> formFieldBindings;
    Coin coin;
    CrawlerResult crawlerResult;

    CrawlerResultForm.ParentTable parentTable;
    ResultsService service = ResultsService.getInstance();

    public NewCoinForm(CrawlerResultForm.ParentTable parentTable) {
        this.parentTable = parentTable;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(actions, name, nominal, description);
    }

    public void save(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();

            service.saveAsNewCoin(coin, crawlerResult);

            String msg = String.format("Saved '%s'.",
                    coin.getName());
            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            parentTable.refreshResults();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
    }

    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
        removeAllComponents();
        parentTable.unSelectResult();
    }

    public void edit(CrawlerResult crawlerResult) {
        this.crawlerResult = crawlerResult;
        if(crawlerResult != null) {
            coin = new Coin();
            coin.setName(crawlerResult.getName());
            coin.setNominal(NominalValue.E_100);

            CoinVariant variant = new CoinVariant();
//        variant.setReleaseDate(LocalDate.now());
            variant.setVariant(Variant.PROOF);
            coin.addVariant(variant);

            CoinVariantHistory history = new CoinVariantHistory();
            history.setDate(crawlerResult.getProcessed());
            history.setSource(crawlerResult.getSource().toString());
            Money price = PriceUtils.parse(crawlerResult.getPrice());
            if (price == null) {
                history.setPrice(Money.euros(0.0));
            } else {
                history.setPrice(price);
            }
            variant.addHistory(history);

            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(coin, this);
            name.focus();
        }
        setVisible(crawlerResult != null);
    }

}
