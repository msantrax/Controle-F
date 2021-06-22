/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qeffilter;

import com.virna5.contexto.ContextUtils;
import java.util.Locale;

/**
 *
 * @author opus
 */
public class QEFField {

    private String ids; 
    private String fieldname;
    private Double value;
    private Double window;
    private Boolean percent;
    private Double limitlow;
    private Double limithigh;
    
    private transient Double rangelow;
    private transient Double rangehigh;
    private transient Double alarmlow;
    private transient Double alarmhigh;
   
    
    public static QEFField QEFFieldFactory(Integer seq){
        QEFField instance = new QEFField();
        String ids = String.valueOf(ContextUtils.getUID());
        instance.setIds(ids);
        instance.setFieldname(ids.substring(12));
        instance.percent= true;
        instance.window= 10.0;
        instance.value = 0.1;
        instance.limithigh = 0.0;
        instance.limitlow = 0.0;
        instance.rangehigh = 0.11;
        instance.rangelow = 0.09;
        instance.alarmhigh = 0.105;
        instance.alarmlow = 0.0905;
        
        
        return instance;
    }
    
    public QEFField() {    
        
    }

    public QEFField (String _fieldname){
        this.fieldname = _fieldname;
    }
    
    public QEFField clone(){
        QEFField cl = new QEFField();
        cl.setIds(String.valueOf(ContextUtils.getUID()));
        cl.setFieldname(new String(this.getFieldname()));
        cl.percent = new Boolean(percent);
        cl.window = new Double(window);
        cl.value = new Double(value);
        cl.limithigh = new Double(limithigh);
        cl.limitlow = new Double(limitlow);
        cl.rangehigh = new Double(rangehigh);
        cl.rangelow = new Double(rangelow);
        cl.alarmhigh = new Double(alarmhigh);
        cl.alarmlow = new Double(alarmlow);
        
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
  
    
    
    
    
    // GET/SET Area =======================================================================
    
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

    
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
        if (percent){
            this.rangelow = value - ((value/100)*window);
            this.rangehigh = value + ((value/100)*window);
        }
        else{
            this.rangelow = value - this.limitlow;
            this.rangehigh = value + this.limithigh;
        }
        
        this.alarmhigh = value +((rangehigh-value)/2);
        this.alarmlow = value -((value-rangelow)/2);
    }

    
    public Double getWindow() {
        return window;
    }

    public void setWindow(Double window) {
        this.window = window;
    }

    public Boolean getPercent() {
        return percent;
    }

    public void setPercent(Boolean percent) {
        this.percent = percent;
    }

    public Double getLimitlow() {
        return limitlow;
    }

    public void setLimitlow(Double limitlow) {
        this.limitlow = limitlow;
    }

    public Double getLimithigh() {
        return limithigh;
    }

    public void setLimithigh(Double limithigh) {
        this.limithigh = limithigh;
    }

    public Double getRangelow() {
        return rangelow;
    }

    public Double getRangehigh() {
        return rangehigh;
    }

    public Double getAlarmlow() {
        return alarmlow;
    }

    public void setAlarmlow(Double alarmlow) {
        this.alarmlow = alarmlow;
    }

    public Double getAlarmhigh() {
        return alarmhigh;
    }

    public void setAlarmhigh(Double alarmhigh) {
        this.alarmhigh = alarmhigh;
    }

   
  
}

