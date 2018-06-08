/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import java.awt.Color;

/**
 *
 * @author opus
 */
public class FileObserverUIUpdater {
    
    private Color ledcolor;
    
    private String filename;
    private String dirbackup;
    
    private Long interval;
    private Long timeout;
    
    private Boolean multiline;
    private Boolean auto;
    
    private String payload;

    
    
    public FileObserverUIUpdater() {
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public FileObserverUIUpdater setFilename(String filename) {
        this.filename = filename;
        return this;
    }

     /**
     * @return the filename
     */
    public String getDirbackup() {
        return this.dirbackup;
    }

    /**
     * @param filename the filename to set
     */
    public FileObserverUIUpdater setDirbakup(String filename) {
        this.dirbackup = filename;
        return this;
    }

    
    
    
    /**
     * @param ledcolor the ledcolor to set
     */
    public FileObserverUIUpdater setLedcolor(Color ledcolor) {
        this.ledcolor = ledcolor;
        return this;
    }

    /**
     * @param payload the payload to set
     */
    public FileObserverUIUpdater setPayload(String payload) {
        this.payload = payload;
         return this;
    }

    /**
     * @return the ledcolor
     */
    public Color getLedcolor() {
        return ledcolor;
    }

    /**
     * @return the payload
     */
    public String getPayload() {
        return payload;
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
    public FileObserverUIUpdater setMultiline(Boolean multiline) {
        this.multiline = multiline;
        return this;
    }
    
     
    /**
     * @return the multiline
     */
    public Boolean getAuto() {
        return auto;
    }
    
    /**
     * @param multiline the multiline to set
     */
    public FileObserverUIUpdater setAuto(Boolean data) {
        this.auto = data;
        return this;
    }
    
    
     /**
     * @return the ledcolor
     */
    public Long getInterval() {
        return interval;
    }

     /**
     * @param multiline the multiline to set
     */
    public FileObserverUIUpdater setInterval(Long interval) {
        this.interval = interval;
        return this;
    }
    
    
     /**
     * @return the ledcolor
     */
    public Long getTimeout() {
        return interval;
    }

     /**
     * @param multiline the multiline to set
     */
    public FileObserverUIUpdater setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }
    
    
    
}
