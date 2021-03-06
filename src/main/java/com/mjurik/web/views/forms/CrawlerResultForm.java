package com.mjurik.web.views.forms;

import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.data.LinkUtils;
import com.mjurik.web.services.ResultsService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerResultForm extends FormLayout {

    Button save = new Button("Save as new", this::save);
    Button ignore = new Button("Ignore path", this::ignore);
    Button cancel = new Button("Cancel", this::cancel);

    TextField name = new TextField("Name");
    TextField ean = new TextField("Ean");
    TextField id = new TextField("id");
    TextField price = new TextField("Price");
    TextField variant = new TextField("Variant");
    Link sourceLink = new Link();

    CrawlerResult crawlerResult;

    BeanFieldGroup<CrawlerResult> formFieldBindings;

    ResultsService service = ResultsService.getInstance();

    ParentTable parentTable;

    public interface ParentTable {
        void unSelectResult();

        void refreshResults();

        void displaySaveNewForm(CrawlerResult result);
    }

    public CrawlerResultForm(ParentTable parentTable) {
        this.parentTable = parentTable;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        id.setEnabled(false);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, ignore, cancel);
        actions.setSpacing(true);

        addComponents(actions, id, name, price, variant, ean, sourceLink);
    }

    public void ignore(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();

            service.ignore(crawlerResult.getSource(), crawlerResult.getId(), crawlerResult.getPath());
            Notification.show(String.format("Path '%s' added as ignored", crawlerResult.getPath()),
                    Notification.Type.TRAY_NOTIFICATION);
            parentTable.unSelectResult();
            parentTable.refreshResults();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
    }

    public void save(Button.ClickEvent event) {
        parentTable.displaySaveNewForm(crawlerResult);
    }


    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
        parentTable.unSelectResult();
    }

    public void edit(CrawlerResult crawlerResult) {
        this.crawlerResult = crawlerResult;
        if(crawlerResult != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(crawlerResult, this);
            sourceLink.setCaption(LinkUtils.getSourceUrl(crawlerResult));
            sourceLink.setResource(new ExternalResource(LinkUtils.getUrl(crawlerResult)));
            sourceLink.setTargetName("_blank");
            name.focus();
        }
        setVisible(crawlerResult != null);
    }

}
