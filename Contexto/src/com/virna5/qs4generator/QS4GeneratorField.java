/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.contexto.ContextUtils;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author opus
 */
public class QS4GeneratorField {

    public static enum FTYPE {texto,numero,instante,indicador}
   
   
    protected String fieldname;
    protected String fieldtype;
    protected String format;
    protected String range;
    
    private transient Double dvalue;
    private transient String svalue;
    
    
    
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

    
    
    public String calculateField(int sequence, Locale lc){
    
        String out;
       
        if (fieldtype.equals(FTYPE.texto.name())){
           try {
                if (format.equals("%s")){
                    out = String.format(format, range);
                }
                else{
                    out = String.format(format, range, sequence);
                }
             } catch (Exception ex){
                out = "Falha na conv.";
            }
        }
        else if (fieldtype.equals(FTYPE.instante.name())){
            out = String.format(format, System.currentTimeMillis());
        }
        else if (fieldtype.equals(FTYPE.numero.name())){
            try {
                
                if (range.toUpperCase().equals("NULL") || range.equals("")){
                    out = "Null";
                }
                else{
                    String[] ranges = range.split(":");
                    if (ranges.length == 2){
                        Double dl = Double.parseDouble(ranges[0]);
                        Double dh = Double.parseDouble(ranges[1]);
                        Double drange = dh - dl;
                        if (drange > 0.0001){
                            double random = ThreadLocalRandom.current().nextDouble(dl, dh);
                            out = String.format(lc, format, random);
                        }
                        else{
                            out = ranges[0];
                        }
                    }
                    else {
                        out = ranges[0];
                    } 
                }    
            } catch (Exception ex){
                out = "0.0";
            }
        }
        else {
            out = "0";
        }
        return out;
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

