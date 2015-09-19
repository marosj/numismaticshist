package com.mjurik.web.forms;

import com.mjurik.CrawlerResultsUI;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerResultForm extends FormLayout {

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);

    TextField name = new TextField("Name");
    TextField ean = new TextField("Ean");
    TextField id = new TextField("id");
    TextField path = new TextField("Path");
    TextField price = new TextField("Price");
    TextField variant = new TextField("Variant");
    TextField source = new TextField("Source");

    CrawlerResult crawlerResult;

    BeanFieldGroup<CrawlerResult> formFieldBindings;

    public CrawlerResultForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        id.setEnabled(false);
        path.setEnabled(false);
        source.setEnabled(false);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(actions, id, name, price, variant, ean, path, source);
    }

    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous service API
//            getUI().service.save(contact);

            String msg = String.format("Saved '%s'.",
                    crawlerResult.getName());
            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            getUI().refreshResults();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }


    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
        getUI().unSelectResult();
    }

    public void edit(CrawlerResult crawlerResult) {
        this.crawlerResult = crawlerResult;
        if(crawlerResult != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(crawlerResult, this);
            name.focus();
        }
        setVisible(crawlerResult != null);
    }

    @Override
    public CrawlerResultsUI getUI() {
        return (CrawlerResultsUI) super.getUI();
    }
}
