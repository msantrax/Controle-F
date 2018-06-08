/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

import com.virna5.filewriter.*;
import java.awt.Color;

/**
 *
 * @author opus
 */
public class CSVFilterUIUpdater {
    
    private String filename;
    private Color ledcolor;
    private Integer overwrite;
    private Integer multiline;
    private String payload;

    public CSVFilterUIUpdater() {
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
    public CSVFilterUIUpdater setFilename(String filename) {
        this.filename = filename;
        return this;
    }


    /**
     * @param ledcolor the ledcolor to set
     */
    public CSVFilterUIUpdater setLedcolor(Color ledcolor) {
        this.ledcolor = ledcolor;
        return this;
    }

    /**
     * @param payload the payload to set
     */
    public CSVFilterUIUpdater setPayload(String payload) {
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
     * @return the overwrite
     */
    public Integer getOverwrite() {
        return overwrite;
    }

    /**
     * @param append the overwrite to set
     */
    public CSVFilterUIUpdater setAppend(int append) {
        this.overwrite = append;
        return this;
    }

    /**
     * @return the multiline
     */
    public Integer getMultiline() {
        return multiline;
    }

    /**
     * @param multiline the multiline to set
     */
    public CSVFilterUIUpdater setMultiline(int multiline) {
        this.multiline = multiline;
        return this;
    }
    
}
