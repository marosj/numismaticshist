package com.mjurik.web.views;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CoinsTable extends CustomComponent {
    public CoinsTable() {
        Panel panel = new Panel("My Custom Component");
        panel.setSizeUndefined();
        setSizeUndefined();
        setCompositionRoot(panel);
    }
}
