/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.filewriter;

import java.awt.Color;

/**
 *
 * @author opus
 */
public class FileWriterUIUpdater {
    
    private String filename;
    private Color ledcolor;
    private Integer overwrite;
    private Integer multiline;
    private String payload;

    public FileWriterUIUpdater() {
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
    public FileWriterUIUpdater setFilename(String filename) {
        this.filename = filename;
        return this;
    }


    /**
     * @param ledcolor the ledcolor to set
     */
    public FileWriterUIUpdater setLedcolor(Color ledcolor) {
        this.ledcolor = ledcolor;
        return this;
    }

    /**
     * @param payload the payload to set
     */
    public FileWriterUIUpdater setPayload(String payload) {
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
    public FileWriterUIUpdater setAppend(int append) {
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
    public FileWriterUIUpdater setMultiline(int multiline) {
        this.multiline = multiline;
        return this;
    }
    
}
