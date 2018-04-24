/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.filewriter;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import java.beans.IntrospectionException;
import org.openide.util.Exceptions;


public class FileWriterDescriptor extends BaseDescriptor{
 
    
    protected String outputfile;
    protected Boolean append;
    protected Boolean overwrite;

    public FileWriterDescriptor() {
        
        super();
        dependencies = new String[] { "com.virna5.fileobserver.FileWriterService" };
        
        name="Gravador de Arquivos";
        desc = "Dispositivo para gravaçãode novos arquivos em um diratorio.";
        
        nodetype = "filewriter.FileWriterDescriptor";
        version = "1.0.0";
       
    }

    @Override
    public DescriptorConnector buildConnector(){
        
        FileWriterConnector foc = new FileWriterConnector();
        try {
            foc.setNode(new FileWriterNode(this));
            foc.setID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
        
    }

    /**
     * @return the outputfile
     */
    public String getOutputfile() {
        return outputfile;
    }

    /**
     * @param outputfile the outputfile to set
     */
    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    /**
     * @return the append
     */
    public Boolean getAppend() {
        return append;
    }

    /**
     * @param append the append to set
     */
    public void setAppend(Boolean append) {
        this.append = append;
    }

    /**
     * @return the overwrite
     */
    public Boolean getOverwrite() {
        return overwrite;
    }

    /**
     * @param overwrite the overwrite to set
     */
    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    
    
}
