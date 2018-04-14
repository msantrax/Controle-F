/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

import com.virna5.contexto.BaseDescriptor;
import java.util.ArrayList;
import java.util.logging.Logger;


public class CSVFilterDescriptor extends BaseDescriptor {

    private static final Logger log = Logger.getLogger(CSVFilterDescriptor.class.getName());

    private String nodetype = "CSVFilterDescriptor";
    private String version = "1.0.0";
    
    private boolean useheader = true;
    private String encoding = "UTF-8";
    private String separator = ",";
    private String locale = "PT-BR";
    
    private String output = "result";
    
    private ArrayList<CSVField> csvfields;
    
    
    public CSVFilterDescriptor() {
        csvfields = new ArrayList();
        dependencies = new String[] { "com.virna5.csvfilter.CSVFilterService" };
    }

    
    public String getNodetype() {
        return nodetype;
    }

  
    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the useheader
     */
    public boolean isUseheader() {
        return useheader;
    }

    /**
     * @param useheader the useheader to set
     */
    public void setUseheader(boolean useheader) {
        this.useheader = useheader;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @return the csvfields
     */
    public ArrayList<CSVField> getCSVfields() {
        return csvfields;
    }

    /**
     * @param csvfields the csvfields to set
     */
    public void setCSVfields(ArrayList<CSVField> csvfields) {
        this.csvfields = csvfields;
    }

    

    
    
    
}
