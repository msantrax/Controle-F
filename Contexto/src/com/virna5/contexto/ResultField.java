/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

/**
 *
 * @author opus
 */
public class ResultField {

    public static enum FTYPE {TEXT,NUMBER,INSTANT,FLAG}
    public static enum FREALM {HEADER,VALUE,FLAG}
    
    private Long uid;
    private Long record;
    private String name;
    private String realm;
    private String type;
    private String sequence;
    private String format ;
    private String value;
    private String rangeh = "";
    private String rangel = "";
    
    private transient Double dvalue;
    protected boolean flag = false;
    protected int iflag = 0;

    
    
    
    public ResultField() {
    }

    public ResultField(Long record,  String name, String realm, String type, String value, Integer seq) {
        
        this.uid = ContextUtils.getUID();
        this.record = record;
        this.name = name;
        this.realm = realm;
        if (realm.equals(FREALM.HEADER.toString())){
            this.format = "%s";
        }
        else{
            this.format = "%4.2f";
        }
        
        this.type = type;
        this.value = value;
        
        try{
            this.sequence = String.valueOf(seq);
        }
        catch (Exception ex){
            this.sequence="0";
        }
        
        
        
        
    }
    
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public ResultField setName(String name) {
        this.name = name;
        return this;
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
    public ResultField setRealm(String realm) {
        this.realm = realm;
        return this;
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
    public ResultField setType(String type) {
        this.type = type;
        return this;
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
    public ResultField setFormat(String format) {
        this.format = format;
        return this;
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
    public ResultField setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * @param value the value to set
     */
    public void convertValue(String value) {
        this.value = value;
        try{
            dvalue = Double.parseDouble(name);
        } catch (Exception ex){
            dvalue = -1.0;
        }
    }
    
    
    /**
     * @return the value
     */
    public Double getDValue() {
        return dvalue;
    }

    /**
     * @param value the value to set
     */
    public ResultField setDValue(Double value) {
        this.dvalue = value;
        return this;
    }
    
    
    
    /**
     * @return the rangeh
     */
    public String getRangeh() {
        return rangeh;
    }

    /**
     * @param rangeh the rangeh to set
     */
    public  ResultField setRangeh(String rangeh) {
        this.rangeh = rangeh;
        return this;
    }

    
    /**
     * @return the rangel
     */
    public String getRangel() {
        return rangel;
    }

    /**
     * @param rangel the rangel to set
     */
    public ResultField setRangel(String rangel) {
        this.rangel = rangel;
        return this;
    }

    /**
     * @return the flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public ResultField setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the record
     */
    public Long getRecord() {
        return record;
    }

    /**
     * @param record the record to set
     */
    public void setRecord(Long record) {
        this.record = record;
    }
    
    
}
