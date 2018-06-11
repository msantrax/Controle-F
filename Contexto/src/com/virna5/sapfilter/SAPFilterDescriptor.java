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
    
    protected SAPFieldsWrapper sapfields;
    
    
    public SAPFilterDescriptor() {
        
        sapfields = new SAPFieldsWrapper();
        
        dependencies = new String[] { "com.virna5.sapfilter.SAPFilterService" };
        
        interfaces = new UIInterface[] { new UIInterface("Filtro SAP", "com.virna5.sapfilter.MonitorIFrame", "iframe") };
        
        
        name="Filtro SAP";
        desc = "Processador de dados em arquivos tipo SAP";
        
        nodetype = "sapfilter.SAPFilterDescriptor";
        version = "1.0.0";
        
        sapfields.add(SAPField.SAPFieldFactory(0));
        
    }

    @Override
    public DescriptorConnector buildConnector(){
        
        SAPFilterConnector foc = new SAPFilterConnector();
        try {
            foc.setNode(new SAPFilterNode(this));
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
     * @return the sapfields
     */
    public SAPFieldsWrapper getSapfields() {
        return sapfields;
    }

    /**
     * @param sapfields the sapfields to set
     */
    public void setSapfields(SAPFieldsWrapper sapfields) {
        this.sapfields = sapfields;
    }

    
    

    
    
    
}
