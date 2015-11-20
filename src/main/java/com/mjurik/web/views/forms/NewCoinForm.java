package com.mjurik.web.views.forms;

import com.mjurik.web.crawler.Money;
import com.mjurik.web.crawler.PriceUtils;
import com.mjurik.web.crawler.db.entity.*;
import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.data.NameUtils;
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
    ComboBox nominal = new ComboBox("Nominal");
    TextField description = new TextField("Description");

    ComboBox variantField = new ComboBox("Variant");
    TextField priceField = new TextField("Price");

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
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        variantField.setNullSelectionAllowed(false);
        variantField.setInvalidAllowed(false);
        variantField.addItems(Variant.values());
        nominal.setNullSelectionAllowed(true);
        nominal.setInvalidAllowed(false);
        nominal.addItems(NominalValue.values());
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(actions, name, nominal, description, variantField, priceField);
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
            coin.setNominal(NameUtils.parseNominal(crawlerResult.getName()));

            CoinVariant variant = new CoinVariant();
//        variant.setReleaseDate(LocalDate.now());
            variant.setVariant(NameUtils.parseVariant(crawlerResult.getVariant()));
            if (variant.getVariant() == Variant.UNKNOWN) {
                variant.setVariant(NameUtils.parseVariant(crawlerResult.getName()));
            }
            coin.addVariant(variant);
            variantField.setValue(variant.getVariant());

            CoinVariantHistory history = new CoinVariantHistory();
            history.setDate(crawlerResult.getProcessed());
            history.setSource(crawlerResult.getSource().toString());
            Money price = PriceUtils.parse(crawlerResult.getPrice());
            if (price == null) {
                price = Money.euros(0.0);
            }
            history.setPrice(price);
            variant.addHistory(history);
            priceField.setValue(price.toString());

            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(coin, this);

            name.focus();
        }
        setVisible(crawlerResult != null);
    }

}
