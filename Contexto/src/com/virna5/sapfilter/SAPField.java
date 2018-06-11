/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.sapfilter;

import com.virna5.csvfilter.*;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.ResultField;

/**
 *
 * @author opus
 */
public class SAPField {

    private String ids; 
    private String fieldname;
    private String template;
   
    
    public static SAPField SAPFieldFactory(Integer seq){
        SAPField instance = new SAPField();
        String ids = String.valueOf(ContextUtils.getUID());
        instance.setIds(ids);
        instance.setFieldname(ids.substring(12));
        instance.setTemplate(ids.substring(10));
        
        return instance;
    }
    
    public SAPField() {    
        
    }

    public SAPField (String _fieldname, String _template){
        this.fieldname = _fieldname;
        this.template = _template;
    }
    
    
    public SAPField clone(){
        SAPField cl = new SAPField();
        cl.setIds(String.valueOf(ContextUtils.getUID()));
        cl.setFieldname(new String(this.getFieldname()));
        cl.setTemplate(new String(this.getTemplate()));
        
        return cl;
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
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }
  
}

