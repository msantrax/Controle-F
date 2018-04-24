/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.csvfilter.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import java.beans.IntrospectionException;
import java.util.logging.Logger;
import org.openide.util.Exceptions;


public class QS4GeneratorDescriptor extends BaseDescriptor {

    private static final Logger log = Logger.getLogger(QS4GeneratorDescriptor.class.getName());

    private boolean useheader = true;
    private String encoding = "UTF-8";
    private String separator = ",";
    private String locale = "PT-BR";
    
    private String output = "csv";
    
    protected QS4GeneratorFieldsWrapper generatorfields;
    
    
    public QS4GeneratorDescriptor() {
        
        generatorfields = new QS4GeneratorFieldsWrapper();
        
        dependencies = new String[] { "com.virna5.qs4generator.QS4GeneratorService" };
        
        name="Geradorn QS4";
        desc = "Cria arquivo CSV de resultados randomicos para teste";
        
        nodetype = "qs4generator.QS4GeneratorDescriptor";
        version = "1.0.0";
        
        generatorfields.add(QS4GeneratorField.QS4GeneratorFieldFactory(0));
        
    }

    @Override
    public DescriptorConnector buildConnector(){
        
        QS4GeneratorConnector foc = new QS4GeneratorConnector();
        try {
            foc.setNode(new QS4GeneratorNode(this));
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
     * @return the generatorfields
     */
    public QS4GeneratorFieldsWrapper getGeneratorfields() {
        return generatorfields;
    }

    /**
     * @param generatorfields the generatorfields to set
     */
    public void setGeneratorfields(QS4GeneratorFieldsWrapper generatorfields) {
        this.generatorfields = generatorfields;
    }

    
    

    
    
    
}
