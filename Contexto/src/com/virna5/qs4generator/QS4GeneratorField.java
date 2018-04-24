/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.contexto.ContextUtils;

/**
 *
 * @author opus
 */
public class QS4GeneratorField {

   
    protected String fieldname;
    protected String fieldtype;
    protected String format;
    protected String range;
    
    
    public static QS4GeneratorField QS4GeneratorFieldFactory(Integer seq){
        QS4GeneratorField instance = new QS4GeneratorField();
        String ids = String.valueOf(ContextUtils.getUID());
           
        return instance;
    }
    
    
    public static Object[] getTypeTypes(){
        return new Object[] {"texto", "valor", "instante"};
    }
    
    public QS4GeneratorField() {    
        
    }

    
    public QS4GeneratorField clone(){
        QS4GeneratorField cl = new QS4GeneratorField();
//        cl.csvfield = new String(csvfield);
//        cl.csvseq = new Integer(csvseq);
//        cl.readcsv = new Boolean(readcsv);
//        cl.realm = new String(realm);
//        cl.resultfield = new String(resultfield);
//        cl.resultseq = new Integer(resultseq);
//        cl.type = new String(type);        
        return cl;
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
     * @return the fieldtype
     */
    public String getFieldtype() {
        return fieldtype;
    }

    /**
     * @param fieldtype the fieldtype to set
     */
    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(String range) {
        this.range = range;
    }
    
    
    
}

