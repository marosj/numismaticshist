package com.mjurik.web.forms;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerResultForm extends FormLayout {

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    CrawlerResult crawlerResult;

    BeanFieldGroup<CrawlerResult> formFieldBindings;

    public CrawlerResultForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

        addComponents(actions, firstName, lastName);
    }

    public void save(Button.ClickEvent event) {
        Notification.show("Saved", Notification.Type.TRAY_NOTIFICATION);
//        getUI().contactList.select(null);
    }


    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
//        getUI().contactList.select(null);
    }

    public void edit(CrawlerResult crawlerResult) {
        this.crawlerResult = crawlerResult;
        if(crawlerResult != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(crawlerResult, this);
            firstName.focus();
        }
        setVisible(crawlerResult != null);
    }
}
