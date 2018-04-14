/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;



/**
 *
 * @author opus
 */
public class FileObserverDescriptor extends BaseDescriptor{
   
    
    private String nodetype = "FileObserverDescriptor";
    private String version = "1.0.0";
    
    
    
    private long timespot;
    private long lastseen;
    
    private long interval;
    private long lag;
    private boolean triggered;
    private long timeout;
    
    private String inputfile;
    private String outputfile;
    

    public FileObserverDescriptor() {
        
        super();
        dependencies = new String[] { "com.virna5.fileobserver.FileObserverService" };
    
        
        this.triggered = false;
        this.lag = 250;
        this.timeout = 60000;
        
        this.timespot = 0l;
        this.interval = 2000;
        
             
    }

    /**
     * @return the lastseen
     */
    public long getTimeSpot() {
        return timespot;
    }

    /**
     * @param lastseen the lastseen to set
     */
    public void setTimespot(long timespot) {
        this.timespot = timespot;
    }

    /**
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * @return the lag
     */
    public long getLag() {
        return lag;
    }

    /**
     * @param lag the lag to set
     */
    public void setLag(long lag) {
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
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the inputfile_path
     */
    public String getInputfile_path() {
        return inputfile;
    }

    /**
     * @param inputfile_path the inputfile_path to set
     */
    public void setInputfile_path(String inputfile_path) {
        this.inputfile = inputfile_path;
    }

    /**
     * @return the outputfile_path
     */
    public String getOutputfile_path() {
        return outputfile;
    }

    /**
     * @param outputfile_path the outputfile_path to set
     */
    public void setOutputfile_path(String outputfile_path) {
        this.outputfile = outputfile_path;
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

    
}
