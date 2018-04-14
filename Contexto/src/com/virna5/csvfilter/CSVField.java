/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

/**
 *
 * @author opus
 */
public class CSVField {

    private String csvfield;
    private String id;
    private String realm;
    private String type;
    private boolean exclude = false;
    
    
    private String value;
    private String value_type;
    
    private double number;
    
    
    public CSVField() {
    
    }

    /**
     * @return the csvfield
     */
    public String getCSVfield() {
        return csvfield;
    }

    /**
     * @param csvfield the csvfield to set
     */
    public void setCSVfield(String csvfield) {
        this.csvfield = csvfield;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     * @param realm the realm to set
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the value_type
     */
    public String getValue_type() {
        return value_type;
    }

    /**
     * @param value_type the value_type to set
     */
    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    /**
     * @return the number
     */
    public double getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(double number) {
        this.number = number;
    }

    /**
     * @return the exclude
     */
    public boolean isExclude() {
        return exclude;
    }

    /**
     * @param exclude the exclude to set
     */
    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    
    
}

