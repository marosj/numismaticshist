package com.mjurik.web.forms;

import com.mjurik.CrawlerResultsUI;
import com.mjurik.web.services.ResultsService;
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
    Button ignore = new Button("Ignore", this::ignore);
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

    ResultsService service = ResultsService.getInstance();

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

        HorizontalLayout actions = new HorizontalLayout(save, ignore, cancel);
        actions.setSpacing(true);

        addComponents(actions, id, name, price, variant, ean, path, source);
    }

    public void ignore(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();

            service.ignore(crawlerResult.getSource(), crawlerResult.getId(), crawlerResult.getPath());
            Notification.show(String.format("Path '%s' added as ignored", crawlerResult.getPath()),
                    Notification.Type.TRAY_NOTIFICATION);
            getUI().unSelectResult();
            getUI().refreshResults();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }

    }

    public void save(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous service API
//            getUI().service.save(contact);

            String msg = String.format("Saved '%s'.",
                    crawlerResult.getName());
            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            getUI().refreshResults();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here

            e.printStackTrace();
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
