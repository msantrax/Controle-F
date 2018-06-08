/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.sapfilter;

import com.virna5.csvfilter.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.UIInterface;
import java.beans.IntrospectionException;
import java.util.logging.Logger;
import org.openide.util.Exceptions;


public class SAPFilterDescriptor extends BaseDescriptor {

    private static final Logger log = Logger.getLogger(SAPFilterDescriptor.class.getName());

    private boolean useheader = true;
    private String encoding = "UTF-8";
    private String separator = ",";
    private String locale = "PT-BR";
    
    private String output = "result";
    
    protected CSVFieldsWrapper csvfields;
    
    
    public SAPFilterDescriptor() {
        
        csvfields = new CSVFieldsWrapper();
        
        dependencies = new String[] { "com.virna5.csvfilter.CSVFilterService" };
        
        interfaces = new UIInterface[] { new UIInterface("Filtro CSV", "com.virna5.csvfilter.MonitorIFrame", "iframe") };
        
        
        name="Filtro CSV";
        desc = "Processador de dados em arquivos tipo CSV";
        
        nodetype = "csvfilter.CSVFilterDescriptor";
        version = "1.0.0";
        
        csvfields.add(CSVField.CSVFieldFactory(0));
        
    }

    @Override
    public DescriptorConnector buildConnector(){
        
        CSVFilterConnector foc = new CSVFilterConnector();
        try {
            foc.setNode(new CSVFilterNode(this));
            foc.setID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
        
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
    public Boolean isUseheader() {
        return useheader;
    }

    
    
    /**
     * @param useheader the useheader to set
     */
    public void setUseheader(Boolean useheader) {
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
    public CSVFieldsWrapper getCsvfields() {
        return csvfields;
    }

    /**
     * @param csvfields the csvfields to set
     */
    public void setCsvfields(CSVFieldsWrapper csvfields) {
        this.csvfields = csvfields;
    }

    
    

    
    
    
}
