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

    
    private String csvfield;
    protected Integer csvseq;
    private String realm;
    private String type;
    private boolean readcsv = true;
    
    protected String resultfield;
    protected Integer resultseq;
    protected boolean writeresult = true;
   
    private String value;
    private String value_type;
    
    private double number;
    
    
    public static SAPField CSVFieldFactory(Integer seq){
        SAPField instance = new SAPField();
        String ids = String.valueOf(ContextUtils.getUID());
        instance.csvfield = ids.substring(12);
        instance.resultfield= ids.substring(10);
        instance.resultseq=seq;
        instance.realm = "valor";
        instance.type="numero";
        instance.readcsv=true;
        instance.setWriteresult(true);
        
        return instance;
    }
    
    
    public static Object[] getRealmTypes(){
        return new Object[] {"cabecalho", "valor", "indicador"};
    }
    
    public static String getRealmEnum (String realmtype){
        
        if (realmtype.equals("cabecalho")){
            return ResultField.FREALM.HEADER.toString();
        }
        else if (realmtype.equals("valor")){
            return ResultField.FREALM.VALUE.toString();
        }
        else{
            return ResultField.FREALM.FLAG.toString();
        }
    }
    
    
    public static Object[] getTypeTypes(){
        return new Object[] {"texto", "numero", "indicador", "instante"};
    }
    
    public static String getTypeEnum (String type){
        
        if (type.equals("texto")){
            return ResultField.FTYPE.TEXT.toString();
        }
        else if (type.equals("numero")){
            return ResultField.FTYPE.NUMBER.toString();
        }
        else if (type.equals("instante")){
            return ResultField.FTYPE.INSTANT.toString();
        }
        else{
            return ResultField.FTYPE.FLAG.toString();
        }
    }
    
    
    
    
    public SAPField() {    
        
    }

    public SAPField (String name, String realm, String type){
        this.csvfield = name;
        this.realm = realm;
        this.type = type;
    }
    
    
    public SAPField clone(){
        SAPField cl = new SAPField();
        cl.csvfield = new String(csvfield);
        cl.csvseq = new Integer(csvseq);
        cl.readcsv = new Boolean(readcsv);
        cl.realm = new String(realm);
        cl.resultfield = new String(resultfield);
        cl.resultseq = new Integer(resultseq);
        cl.type = new String(type);
        
        return cl;
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
     * @return the readcsv
     */
    public boolean isReadcsv() {
        return readcsv;
    }

    /**
     * @param readcsv the readcsv to set
     */
    public void setReadcsv(boolean readcsv) {
        this.readcsv = readcsv;
    }

    /**
     * @return the csvseq
     */
    public Integer getCsvseq() {
        return csvseq;
    }

    /**
     * @param csvseq the csvseq to set
     */
    public void setCsvseq(Integer csvseq) {
        this.csvseq = csvseq;
    }

    /**
     * @return the resultfield
     */
    public String getResultfield() {
        return resultfield;
    }

    /**
     * @param resultfield the resultfield to set
     */
    public void setResultfield(String resultfield) {
        this.resultfield = resultfield;
    }

    /**
     * @return the resultseq
     */
    public Integer getResultseq() {
        return resultseq;
    }

    /**
     * @param resultseq the resultseq to set
     */
    public void setResultseq(Integer resultseq) {
        this.resultseq = resultseq;
    }

    /**
     * @return the writeresult
     */
    public boolean isWriteresult() {
        return writeresult;
    }

    /**
     * @param writeresult the writeresult to set
     */
    public void setWriteresult(boolean writeresult) {
        this.writeresult = writeresult;
    }

    
}

