package com.mjurik.web.data;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerResult implements Serializable, Cloneable {

    private String id;

    private CrawlerSource source;

    private String name;

    private String ean;

    private String variant;

    private String price;

    private String path;

    private LocalDate processed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CrawlerSource getSource() {
        return source;
    }

    public void setSource(CrawlerSource source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDate getProcessed() {
        return processed;
    }

    public void setProcessed(LocalDate processed) {
        this.processed = processed;
    }

    public boolean matchFulltextSearch(String searchString) {
        return id.toLowerCase().contains(searchString)
                || StringUtils.containsIgnoreCase(source.toString(), searchString)
                || StringUtils.containsIgnoreCase(name, searchString)
                || StringUtils.containsIgnoreCase(ean, searchString)
                || StringUtils.containsIgnoreCase(variant, searchString)
                || StringUtils.containsIgnoreCase(path, searchString);
    }

    @Override
    public CrawlerResult clone() throws CloneNotSupportedException {
        try {
            return (CrawlerResult) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

}
