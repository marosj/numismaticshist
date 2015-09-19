package com.mjurik.web.forms;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class CrawlerResult implements Serializable, Cloneable {

    private String id;

    private String firstName;
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public CrawlerResult clone() throws CloneNotSupportedException {
        try {
            return (CrawlerResult) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "CrawlerResult{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
