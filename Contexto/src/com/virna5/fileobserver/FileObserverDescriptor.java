/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.UIInterface;
import java.beans.IntrospectionException;
import org.openide.util.Exceptions;



/**
 *
 * @author opus
 */
public class FileObserverDescriptor extends BaseDescriptor{
 
    
    private long timespot;
    private long lastseen;
    
    private long interval;
    private long lag;
    private boolean triggered;
    private long timeout;
    
    private Boolean multiline;
    
    private String inputfile_path;
    private String outputfile;
    
    private transient Boolean automatic = false;
    private transient String payload;
    

    public FileObserverDescriptor() {
        
        super();
        dependencies = new String[] { "com.virna5.fileobserver.FileObserverService" };
        interfaces = new UIInterface[] { new UIInterface("Observador de Arquivos", "com.virna5.fileobserver.MonitorIFrame", "iframe") };
        
        name="Observador de Arquivos";
        desc = "Dispositivo para verificação e leitura de novos arquivos em um diratorio.";
        
        nodetype = "fileobserver.FileObserverDescriptor";
        version = "1.0.0";
        
        this.inputfile_path = "/Bascon/BSW1/Testbench/area1/qs4a.csv";
        this.outputfile = "/Bascon/BSW1/Testbench/area2";
        this.triggered = false;
        this.lag = 250;
        this.timeout = 60000;
        
        this.timespot = 0l;
        this.interval = 2000;
        
        this.multiline = false;
        
    }

    
    @Override
    public DescriptorConnector buildConnector(){
        
        FileObserverConnector foc = new FileObserverConnector();
        try {
            foc.setNode(new FileObserverNode(this));
            foc.setID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
        
    }
    
    
    /**
     * @return the lastseen
     */
    public Long getTimeSpot() {
        return timespot;
    }

    /**
     * @param lastseen the lastseen to set
     */
    public void setTimespot(Long timespot) {
        this.timespot = timespot;
    }

    /**
     * @return the interval
     */
    public Long getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(Long interval) {
        this.interval = interval;
    }

    /**
     * @return the lag
     */
    public Long getLag() {
        return lag;
    }

    /**
     * @param lag the lag to set
     */
    public void setLag(Long lag) {
        this.lag = lag;
    }

    /**
     * @return the triggered
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * @param triggered the triggered to set
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * @return the timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the inputfile_path
     */
    public String getInputfile_path() {
        return inputfile_path;
    }

    /**
     * @param inputfile_path the inputfile_path to set
     */
    public void setInputfile_path(String inputfile_path) {
        this.inputfile_path = inputfile_path;
    }

    /**
     * @return the outputfile_path
     */
    public String getOutputfile() {
        return outputfile;
    }

    /**
     * @param outputfile_path the outputfile_path to set
     */
    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    /**
     * @return the lastseen
     */
    public long getLastseen() {
        return lastseen;
    }

    /**
     * @param lastseen the lastseen to set
     */
    public void setLastseen(long lastseen) {
        this.lastseen = lastseen;
    }

    /**
     * @return the multiline
     */
    public Boolean getMultiline() {
        return multiline;
    }

    /**
     * @param multiline the multiline to set
     */
    public void setMultiline(Boolean multiline) {
        this.multiline = multiline;
    }

    /**
     * @return the automatic
     */
    public Boolean getAutomatic() {
        return automatic;
    }

    /**
     * @param automatic the automatic to set
     */
    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    /**
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    
}
