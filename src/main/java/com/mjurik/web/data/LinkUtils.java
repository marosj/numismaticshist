package com.mjurik.web.data;

/**
 * Created by Marian Jurik on 20.11.2015.
 */
public class LinkUtils {

    public static String getUrl(CrawlerResult result) {
        return getSourceUrl(result) + result.getPath();
    }

    public static String getSourceUrl(CrawlerResult result) {
        switch (result.getSource()) {
            case NUMIZMATIK:
                return "http://www.numizmatik.eu";
            case EURONUMIS:
                return "http://www.euronumis.eu";
        }
        return "";
    }
}
