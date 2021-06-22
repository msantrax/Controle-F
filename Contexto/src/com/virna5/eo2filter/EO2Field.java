/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.eo2filter;

import com.virna5.contexto.ContextUtils;
import java.util.Locale;

/**
 *
 * @author opus
 */
public class EO2Field {

    private String ids; 
    private String fieldname;
    private Integer fieldindex;
    private String record;
    private String value;
    
    
    public static EO2Field  EO2FieldFactory(Integer seq){
        EO2Field instance = new EO2Field();
        String ids = String.valueOf(ContextUtils.getUID());
        instance.setIds(ids);
        instance.setFieldname(ids.substring(12));
        instance.setFieldindex(0);
        instance.setRecord("None");
        instance.setValue("None");
        
        return instance;
    }
    
    public EO2Field() {    
        
    }

    public EO2Field (String _fieldname){
        this.fieldname = _fieldname;
    }
    
    public EO2Field clone(){
        EO2Field cl = new EO2Field();
        cl.setIds(String.valueOf(ContextUtils.getUID()));
        cl.setFieldname(new String(this.getFieldname()));
        cl.setFieldindex(new Integer(this.getFieldindex()));
        cl.setValue(new String(this.getValue()));
        cl.setRecord(new String(this.getRecord()));
        
        return cl;
    }

    public static String convertField (Double field){
        try{
            
            return String.format(Locale.ENGLISH, "%10.5f", field).trim();
        } catch (Exception ex){
            return "0.0";
        }
    }
    
    public static Double encodeField (String field){
        try{
            return Double.valueOf(field);
        } catch (Exception ex){
            return 0.0;
        }
    }
    
    

    /**
     * @return the ids
     */
    public String getIds() {
        return ids;
    }

    /**
     * @param ids the ids to set
     */
    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * @return the fieldname
     */
    public String getFieldname() {
        return fieldname;
    }

    /**
     * @param fieldname the fieldname to set
     */
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    /**
     * @return the fieldindex
     */
    public Integer getFieldindex() {
        return fieldindex;
    }

    /**
     * @param fieldindex the fieldindex to set
     */
    public void setFieldindex(Integer fieldindex) {
        this.fieldindex = fieldindex;
    }

    /**
     * @return the record
     */
    public String getRecord() {
        return record;
    }

    /**
     * @param record the record to set
     */
    public void setRecord(String record) {
        this.record = record;
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
  
    
    
    
    
  
}

